package org.jeecg.modules.maindata.suppliers.service;

import org.jeecg.modules.maindata.suppliers.entity.YujiakejiSuppliers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 供应商管理
 * @Author: 舒有敬
 * @Date:   2024-06-02
 * @Version: V1.0
 */
public interface IYujiakejiSuppliersService extends IService<YujiakejiSuppliers> {

    void saveEntity(YujiakejiSuppliers yujiakejiSuppliers);
}
