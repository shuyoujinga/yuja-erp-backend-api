package org.jeecg.modules.inv.invtransfer.entity;

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
 * @Description: 物料调拨_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@ApiModel(value="inv_transfer_detail对象", description="物料调拨_明细")
@Data
@TableName("inv_transfer_detail")
public class InvTransferDetail implements Serializable {
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
	/**引用明细ID*/
	@Excel(name = "引用明细ID", width = 15)
    @ApiModelProperty(value = "引用明细ID")
    private String refDetailId;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private String materialCode;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private String unit;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String specifications;
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
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
