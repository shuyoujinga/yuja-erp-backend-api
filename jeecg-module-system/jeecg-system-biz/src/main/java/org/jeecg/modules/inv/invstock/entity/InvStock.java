package org.jeecg.modules.inv.invstock.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 实时库存
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Data
@TableName("inv_stock")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="inv_stock对象", description="实时库存")
public class InvStock implements Serializable {
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
    private String sysOrgCode;
	/**仓库*/
	@Excel(name = "仓库", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "仓库")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String warehouseCode;
	/**库位*/
	@Excel(name = "库位", width = 15)
    @ApiModelProperty(value = "库位")
    private String locationCode;
	/**物料*/
	@Excel(name = "物料", width = 15,dictTable = "yujiakeji_materials",dicText = "material_name",dicCode = "material_code")
    @ApiModelProperty(value = "物料")
    @Dict(dictTable = "yujiakeji_materials",dicText = "material_name",dicCode = "material_code")
    private String materialCode;
    /**规格*/
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    @TableField(exist = false)
    private String specifications;
    /**单位*/
    @Excel(name = "单位", width = 15,dicCode = "dict_materials_unit")
    @ApiModelProperty(value = "单位")
    @Dict(dicCode = "dict_materials_unit")
    @TableField(exist = false)
    private String unit;
	/**总数量*/
	@Excel(name = "总数量", width = 15)
    @ApiModelProperty(value = "总数量")
    private Double totalQty;
	/**锁定数量*/
	@Excel(name = "锁定数量", width = 15)
    @ApiModelProperty(value = "锁定数量")
    private Double lockedQty;
	/**库存数量*/
	@Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    private Double stockQty;
	/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
