package org.jeecg.modules.prd.prdmps.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 生产计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@ApiModel(value="prd_mps_detail对象", description="生产计划_明细")
@Data
@TableName("prd_mps_detail")
public class PrdMpsDetail implements Serializable {
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
    /**业务计划明细ID*/
    @ApiModelProperty(value = "业务计划明细ID")
    private String bizDetailId;
    /**BOM编码*/
    @Excel(name = "BOM编码", width = 15)
    private String bomCode;
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
	/**业务数量*/
	@Excel(name = "业务数量", width = 15)
    @ApiModelProperty(value = "业务数量")
    private Double bizQty;
	/**排产数量*/
	@Excel(name = "排产数量", width = 15)
    @ApiModelProperty(value = "排产数量")
    private Double qty;
    /**工序类型*/
    @Excel(name = "用工类型", width = 15,dicCode = "dict_work_type")
    @ApiModelProperty(value = "用工类型")
    @Dict(dicCode = "dict_work_type")
    private String workType;
    /**工序单价*/
    @Excel(name = "工序单价", width = 15)
    @ApiModelProperty(value = "工序单价")
    private Double workUnitPrice;
    /**工序类型*/
    @Excel(name = "工序类型", width = 15,dicCode = "dict_process_type")
    @ApiModelProperty(value = "工序类型")
    private String processType;
    /**产线*/
    @Excel(name = "产线", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "产线")
    private String prdLine;
	/**工序*/
	@Excel(name = "工序", width = 15,dicCode = "dict_process_code")
    @ApiModelProperty(value = "工序")
    private String processCode;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
