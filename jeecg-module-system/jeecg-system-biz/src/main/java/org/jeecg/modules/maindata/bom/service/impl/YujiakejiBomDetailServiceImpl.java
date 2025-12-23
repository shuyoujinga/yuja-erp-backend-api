package org.jeecg.modules.maindata.bom.service.impl;

import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import org.jeecg.modules.maindata.bom.mapper.YujiakejiBomDetailMapper;
import org.jeecg.modules.maindata.bom.service.IYujiakejiBomDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Description: 材料清单_明细表
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Service
public class YujiakejiBomDetailServiceImpl extends ServiceImpl<YujiakejiBomDetailMapper, YujiakejiBomDetail> implements IYujiakejiBomDetailService {
	
	@Resource
	private YujiakejiBomDetailMapper yujiakejiBomDetailMapper;
	
	@Override
	public List<YujiakejiBomDetail> selectByMainId(String mainId) {
		return yujiakejiBomDetailMapper.selectByMainId(mainId);
	}
}
