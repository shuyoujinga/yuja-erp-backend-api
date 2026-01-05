package org.jeecg.modules.inv.invassembly.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 组装单_材料明细
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
@ApiModel(value="inv_assembly_bom_detail对象", description="组装单_材料明细")
@Data
@TableName("inv_assembly_bom_detail")
public class InvAssemblyBomDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private String pid;
	/**BOM编号*/

    @ApiModelProperty(value = "BOM编号")
    private String bomCode;
    /**物料*/
    @Excel(name = "货品", width = 15,dictTable = "yujiakeji_materials",dicText = "material_name",dicCode = "material_code")
    @ApiModelProperty(value = "货品")
    private String productionMaterialCode;
    /**物料*/
    @Excel(name = "物料", width = 15,dictTable = "yujiakeji_materials",dicText = "material_name",dicCode = "material_code")
    @ApiModelProperty(value = "物料")
    private String materialCode;
    /**单位*/
    @Excel(name = "单位", width = 15,dicCode="dict_materials_unit")
    @ApiModelProperty(value = "单位")
    private String unit;
    /**规格*/
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String specifications;
    /**标准用量*/
    @Excel(name = "标准用量", width = 15)
    @ApiModelProperty(value = "标准用量")
    private Double standardQty;
    /**数量*/
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Double qty;
    /**单价*/
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private Double unitPrice;
    /**金额*/
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private Double amount;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
