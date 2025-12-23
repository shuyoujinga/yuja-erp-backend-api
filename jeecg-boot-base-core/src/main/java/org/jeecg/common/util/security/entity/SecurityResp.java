package org.jeecg.common.util.security.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Description: SecurityResp
 * @Author: 舒有敬
 */
@Data
public class SecurityResp {
    private Boolean success;
    private JSONObject data;
}
