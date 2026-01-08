package org.jeecg.modules.prd.prdmps.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mchange.lang.DoubleUtils;
import org.jeecg.modules.api.vo.MaterialBomInfoVO;
import org.jeecg.modules.api.vo.MaterialInfoVO;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsBomDetailMapper;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsBomDetailService;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcess;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessDetailService;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessService;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 生产计划_材料清单
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Service
public class PrdMpsBomDetailServiceImpl extends ServiceImpl<PrdMpsBomDetailMapper, PrdMpsBomDetail> implements IPrdMpsBomDetailService {
	
	@Resource
	private PrdMpsBomDetailMapper prdMpsBomDetailMapper;
	@Autowired
	private IYujiakejiMaterialsService yujiakejiMaterialsService;
	@Autowired
	private ISalBizPlanDetailService salBizPlanDetailService;
	@Autowired
	private IPrdProcessService prdProcessService;
	@Autowired
	private IPrdProcessDetailService prdProcessDetailService;
	
	@Override
	public List<PrdMpsBomDetail> selectByMainId(String mainId) {
		return prdMpsBomDetailMapper.selectByMainId(mainId);
	}

	@Override
	public List<PrdMpsBomDetail> selectByTargetId(String ids) {

		Assert.isTrue(StrUtil.isBlank(ids), "操作失败!传入ID为空!");

		List<String> idList = Arrays.asList(ids.split(","));
		Assert.isTrue(CollectionUtil.isEmpty(idList), "操作失败!传入ID为空!");

		List<SalBizPlanDetail> planList = salBizPlanDetailService.listByIds(idList);
		Assert.isTrue(CollectionUtil.isEmpty(planList), "操作失败! 明细列表为空!");

		// 工序主表：key = 主物料编码(C01xxx)
		List<PrdProcess> processList = prdProcessService.list();
		Assert.isTrue(CollectionUtil.isEmpty(processList), "操作失败! 工序表为空!");

		Map<String, String> processMap = processList.stream()
				.collect(Collectors.toMap(
						PrdProcess::getMaterialCode,
						PrdProcess::getId,
						(a, b) -> a
				));

		// 统一合并池
		Map<String, PrdMpsBomDetail> mergeMap = new HashMap<>();

		// ========== 核心逻辑 ==========
		for (SalBizPlanDetail plan : planList) {

			// ① 主物料 BOM
			mergeBom(
					plan.getMaterialCode(),
					plan.getQty(),
					mergeMap
			);

			// ② 如果主物料存在工序 → 拆前工序
			String processId = processMap.get(plan.getMaterialCode());
			if (processId == null) {
				continue;
			}

			List<PrdProcessDetail> processDetails =
					prdProcessDetailService.selectByMainId(processId);

			for (PrdProcessDetail pd : processDetails) {

				// 只处理“前工序物料”（如 Bxxx）
				if (!pd.getMaterialCode().startsWith("B")) {
					continue;
				}

				mergeBom(
						pd.getMaterialCode(),
						plan.getQty(),
						mergeMap
				);
			}
		}

		return mergeMap.values().stream()
				.sorted(Comparator.comparing(
						PrdMpsBomDetail::getProductionMaterialCode,
						Comparator.nullsLast(String::compareTo)
				).reversed())
				.collect(Collectors.toList());

	}
	private void mergeBom(String materialCode,
						  double planQty,
						  Map<String, PrdMpsBomDetail> mergeMap) {

		MaterialInfoVO materialInfoVO =
				yujiakejiMaterialsService.queryByMaterialInfoAndBomList(materialCode);

		List<MaterialBomInfoVO> bomList = materialInfoVO.getBomInfoList();
		Assert.isTrue(CollectionUtil.isEmpty(bomList),
				String.format("操作失败!物料[%s]不存在BOM,请维护!", materialCode));

		for (MaterialBomInfoVO bom : bomList) {

			String key = bom.getMaterialCode();
			Assert.isTrue(ObjectUtils.isEmpty(bom.getStandardQty()),String.format("操作失败![%s]物料没有维护标准用量!", materialCode));
			BigDecimal addQty = BigDecimal.valueOf(bom.getStandardQty())
					.multiply(BigDecimal.valueOf(planQty));

			PrdMpsBomDetail exist = mergeMap.get(key);
			if (exist == null) {
				PrdMpsBomDetail entity = new PrdMpsBomDetail();
				BeanUtils.copyProperties(bom, entity);
				entity.setId(null);
				entity.setStandardQty(bom.getStandardQty());
				entity.setQty(addQty.doubleValue());
				mergeMap.put(key, entity);
			} else {
				exist.setQty(
						AmountUtils.add(exist.getQty(), addQty.doubleValue())
				);
			}
		}
	}


}
