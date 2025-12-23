package org.jeecg.common.util.security.entity;

import lombok.Data;

/**
 * @Description: SecurityReq
 * @Author: 舒有敬
 */
@Data
public class SecurityReq {
    private String data;
    private String pubKey;
    private String signData;
    private String aesKey;
}
