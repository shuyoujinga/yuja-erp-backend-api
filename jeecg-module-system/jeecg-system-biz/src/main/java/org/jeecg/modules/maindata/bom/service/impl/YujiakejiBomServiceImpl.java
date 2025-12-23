package org.jeecg.modules.maindata.bom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import org.jeecg.modules.maindata.bom.mapper.YujiakejiBomDetailMapper;
import org.jeecg.modules.maindata.bom.mapper.YujiakejiBomMapper;
import org.jeecg.modules.maindata.bom.service.IYujiakejiBomService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 材料清单_主表
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class YujiakejiBomServiceImpl extends ServiceImpl<YujiakejiBomMapper, YujiakejiBom> implements IYujiakejiBomService {

	@Resource
	private YujiakejiBomMapper yujiakejiBomMapper;
	@Resource
	private YujiakejiBomDetailMapper yujiakejiBomDetailMapper;

	@Resource
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(YujiakejiBom yujiakejiBom, List<YujiakejiBomDetail> yujiakejiBomDetailList) {
		yujiakejiBom.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.BOM));
		yujiakejiBomMapper.insert(yujiakejiBom);
		if(yujiakejiBomDetailList!=null && yujiakejiBomDetailList.size()>0) {
			for(YujiakejiBomDetail entity:yujiakejiBomDetailList) {
				//外键设置
				entity.setPid(yujiakejiBom.getId());
				yujiakejiBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(YujiakejiBom yujiakejiBom,List<YujiakejiBomDetail> yujiakejiBomDetailList) {
		yujiakejiBomMapper.updateById(yujiakejiBom);
		
		//1.先删除子表数据
		yujiakejiBomDetailMapper.deleteByMainId(yujiakejiBom.getId());
		
		//2.子表数据重新插入
		if(yujiakejiBomDetailList!=null && yujiakejiBomDetailList.size()>0) {
			for(YujiakejiBomDetail entity:yujiakejiBomDetailList) {
				//外键设置
				entity.setPid(yujiakejiBom.getId());
				yujiakejiBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		yujiakejiBomDetailMapper.deleteByMainId(id);
		yujiakejiBomMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			yujiakejiBomDetailMapper.deleteByMainId(id.toString());
			yujiakejiBomMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) {
		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.NO);
	}

	/**
	 * 批量更新审核状态
	 *
	 * @param ids 待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<YujiakejiBom> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (YujiakejiBom record : records) {
			if (!status.equals(record.getAudit())) {
				record.setAudit(status);
				record.setAuditBy(sysUser.getUsername());
				record.setAuditTime(new Date());
				count++;
			}
		}

		if (count > 0) {
			updateBatchById(records);
		}
		return count;
	}


}
