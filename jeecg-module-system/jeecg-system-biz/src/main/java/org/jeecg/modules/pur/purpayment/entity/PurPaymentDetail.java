package org.jeecg.modules.pur.purpayment.entity;

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
 * @Description: 采购付款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@ApiModel(value="pur_payment_detail对象", description="采购付款_明细")
@Data
@TableName("pur_payment_detail")
public class PurPaymentDetail implements Serializable {
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
	/**采购结算明细_ID*/
	@Excel(name = "采购结算明细_ID", width = 15)
    @ApiModelProperty(value = "采购结算明细_ID")
    private String settleDetailId;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private String materialCode;
	/**结算数量*/
	@Excel(name = "结算数量", width = 15)
    @ApiModelProperty(value = "结算数量")
    private Double settleNum;
	/**结算单价*/
	@Excel(name = "结算单价", width = 15)
    @ApiModelProperty(value = "结算单价")
    private Double settleUnitPrice;
	/**结算金额*/
	@Excel(name = "结算金额", width = 15)
    @ApiModelProperty(value = "结算金额")
    private Double settleAmount;
	/**付款金额*/
	@Excel(name = "付款金额", width = 15)
    @ApiModelProperty(value = "付款金额")
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
