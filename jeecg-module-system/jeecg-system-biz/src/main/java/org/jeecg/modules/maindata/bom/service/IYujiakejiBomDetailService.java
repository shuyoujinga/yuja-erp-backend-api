package org.jeecg.modules.maindata.bom.service;

import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 材料清单_明细表
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
public interface IYujiakejiBomDetailService extends IService<YujiakejiBomDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<YujiakejiBomDetail>
	 */
	public List<YujiakejiBomDetail> selectByMainId(String mainId);
}
