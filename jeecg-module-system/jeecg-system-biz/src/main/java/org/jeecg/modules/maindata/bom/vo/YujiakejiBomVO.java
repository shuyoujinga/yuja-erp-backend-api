package org.jeecg.modules.maindata.bom.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class YujiakejiBomVO {
    // 主表字段
    @Excel(name = "主键")
    private String id;

    @Excel(name = "物料编码")
    private String materialCode;

    @Excel(name = "子物料编码")
    private String materialCodeChild;


}


