package org.jeecg.modules.maindata.materials.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 物料主数据
 * @Author: 舒有敬
 * @Date:   2024-06-08
 * @Version: V1.0
 */
@Data
@TableName("yujiakeji_materials")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="yujiakeji_materials对象", description="物料主数据")
public class YujiakejiMaterials implements Serializable {
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
	/**分类*/
	@Excel(name = "分类", width = 15, dictTable = "sys_category", dicText = "name", dicCode = "code")
	@Dict(dictTable = "sys_category", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "分类")
    private String materialCategory;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String materialName;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String materialCode;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String specifications;
	/**仓位*/
	@Excel(name = "仓位", width = 15)
    @ApiModelProperty(value = "仓位")
    private String stock;
	/**单位*/
	@Excel(name = "单位", width = 15,dicCode = "dict_materials_unit")
    @ApiModelProperty(value = "单位")
    @Dict(dicCode = "dict_materials_unit")
    private String unit;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private Double unitPrice;
    /**库存数量*/
    @Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    @TableField(exist = false)
    private Double stockQty;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer delFlag;
}
