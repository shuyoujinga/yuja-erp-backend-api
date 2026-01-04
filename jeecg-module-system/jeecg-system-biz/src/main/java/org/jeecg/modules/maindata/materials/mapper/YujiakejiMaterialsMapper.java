package org.jeecg.modules.maindata.materials.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.api.vo.MaterialBomInfoVO;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;

import java.util.List;

/**
 * @Description: 物料主数据
 * @Author: 舒有敬
 * @Date:   2024-06-08
 * @Version: V1.0
 */
public interface YujiakejiMaterialsMapper extends BaseMapper<YujiakejiMaterials> {

    YujiakejiMaterials queryByMaterialCode(@Param("materialCode") String materialCode,@Param("warehouseCode") String warehouseCode);

    YujiakejiMaterials queryByMaterialCodeInSale(@Param("materialCode")  String materialCode);

    List<MaterialBomInfoVO> queryBomListByMaterialCode(@Param("materialCode") String materialCode);
}
