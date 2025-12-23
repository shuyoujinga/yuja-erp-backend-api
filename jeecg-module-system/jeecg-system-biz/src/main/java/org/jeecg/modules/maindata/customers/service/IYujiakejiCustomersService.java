package org.jeecg.modules.maindata.customers.service;

import org.jeecg.modules.maindata.customers.entity.YujiakejiCustomers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 客户管理
 * @Author: 舒有敬
 * @Date:   2024-06-08
 * @Version: V1.0
 */
public interface IYujiakejiCustomersService extends IService<YujiakejiCustomers> {

    void saveEntity(YujiakejiCustomers yujiakejiCustomers);
}
