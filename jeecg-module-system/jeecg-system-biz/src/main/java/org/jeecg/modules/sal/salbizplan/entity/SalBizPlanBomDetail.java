package org.jeecg.modules.sal.salbizplan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 业务计划_材料明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@ApiModel(value="sal_biz_plan_bom_detail对象", description="业务计划_材料明细")
@Data
@TableName("sal_biz_plan_bom_detail")
public class SalBizPlanBomDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
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
	@Excel(name = "所属部门", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String sysOrgCode;
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private String pid;
	/**标准BOM*/
	@Excel(name = "标准BOM", width = 15)
    @ApiModelProperty(value = "标准BOM")
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
	/**需求数*/
	@Excel(name = "需求数", width = 15)
    @ApiModelProperty(value = "需求数")
    private Double requiredQty;
	/**库存数*/
	@Excel(name = "库存数", width = 15)
    @ApiModelProperty(value = "库存数")
    private Double stockQty;
	/**在途数*/
	@Excel(name = "在途数", width = 15)
    @ApiModelProperty(value = "在途数")
    private Double inTransitQty;
	/**可用数*/
	@Excel(name = "可用数", width = 15)
    @ApiModelProperty(value = "可用数")
    private Double availableQty;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
