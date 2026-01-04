package org.jeecg.modules.maindata.materials.service;

import org.jeecg.modules.api.vo.MaterialInfoVO;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 物料主数据
 * @Author: 舒有敬
 * @Date:   2024-06-08
 * @Version: V1.0
 */
public interface IYujiakejiMaterialsService extends IService<YujiakejiMaterials> {
    /**
     * 物料主数据保存
     * @param yujiakejiMaterials
     */
    void saveEntity(YujiakejiMaterials yujiakejiMaterials);

    YujiakejiMaterials queryByMaterialCode(String materialCode, String warehouseCode);

    YujiakejiMaterials queryByMaterialCodeInSale(String materialCode);

    MaterialInfoVO queryByMaterialInfoAndBomList(String materialCode);
}
