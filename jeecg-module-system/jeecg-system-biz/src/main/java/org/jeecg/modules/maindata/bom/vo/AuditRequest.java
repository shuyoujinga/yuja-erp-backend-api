package org.jeecg.modules.maindata.bom.vo;

import lombok.Data;

import java.util.List;

/**
 * 审核请求体
 */
@Data
public class AuditRequest {
    private List<String> ids;
    private String type;
}