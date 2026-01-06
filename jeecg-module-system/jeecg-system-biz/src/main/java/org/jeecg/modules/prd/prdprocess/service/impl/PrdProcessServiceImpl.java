package org.jeecg.modules.prd.prdprocess.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import org.jeecg.modules.maindata.bom.service.IYujiakejiBomService;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcess;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import org.jeecg.modules.prd.prdprocess.mapper.PrdProcessDetailMapper;
import org.jeecg.modules.prd.prdprocess.mapper.PrdProcessMapper;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @Description: 生产工序
 * @Author: 舒有敬
 * @Date: 2026-01-06
 * @Version: V1.0
 */
@Service
public class PrdProcessServiceImpl extends ServiceImpl<PrdProcessMapper, PrdProcess> implements IPrdProcessService {

    @Resource
    private PrdProcessMapper prdProcessMapper;
    @Resource
    private PrdProcessDetailMapper prdProcessDetailMapper;
    @Autowired
    private IYujiakejiBomService yujiakejiBomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(PrdProcess prdProcess, List<PrdProcessDetail> prdProcessDetailList) {
        prdProcessMapper.insert(prdProcess);
        if (prdProcessDetailList != null && prdProcessDetailList.size() > 0) {
            List<YujiakejiBomDetail> bomDetailList = yujiakejiBomService.queryBomListByMainCode(prdProcess.getMaterialCode());
			Assert.isTrue(CollectionUtil.isEmpty(bomDetailList),String.format("操作失败,物料[%s]的材料清单不存在明细清单,请联系工程部维护!",prdProcess.getMaterialCode()));
			List<String> materialCodeList = bomDetailList.stream().map(YujiakejiBomDetail::getMaterialCode).collect(Collectors.toList());

			for (PrdProcessDetail entity : prdProcessDetailList) {
				Assert.isTrue(!entity.getMaterialCode().equals(prdProcess.getMaterialCode())&&!materialCodeList.contains(entity.getMaterialCode()),String.format("操作失败,物料[%s]错误,请检查!",entity.getMaterialCode()));
                //外键设置
                entity.setPid(prdProcess.getId());
                prdProcessDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(PrdProcess prdProcess, List<PrdProcessDetail> prdProcessDetailList) {
        prdProcessMapper.updateById(prdProcess);

        //1.先删除子表数据
        prdProcessDetailMapper.deleteByMainId(prdProcess.getId());

        //2.子表数据重新插入
        if (prdProcessDetailList != null && prdProcessDetailList.size() > 0) {
			List<YujiakejiBomDetail> bomDetailList = yujiakejiBomService.queryBomListByMainCode(prdProcess.getMaterialCode());
			Assert.isTrue(CollectionUtil.isEmpty(bomDetailList),String.format("操作失败,物料[%s]的材料清单不存在明细清单,请联系工程部维护!",prdProcess.getMaterialCode()));
			List<String> materialCodeList = bomDetailList.stream().map(YujiakejiBomDetail::getMaterialCode).collect(Collectors.toList());
            for (PrdProcessDetail entity : prdProcessDetailList) {
				Assert.isTrue(!entity.getMaterialCode().equals(prdProcess.getMaterialCode())&&!materialCodeList.contains(entity.getMaterialCode()),String.format("操作失败,物料[%s]错误,请检查!",entity.getMaterialCode()));
				//外键设置
                entity.setPid(prdProcess.getId());
                prdProcessDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        prdProcessDetailMapper.deleteByMainId(id);
        prdProcessMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            prdProcessDetailMapper.deleteByMainId(id.toString());
            prdProcessMapper.deleteById(id);
        }
    }

}
