package org.jeecg.modules.sal.salsettle.entity;

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
 * @Description: 销售结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@ApiModel(value="sal_settle_detail对象", description="销售结算_明细")
@Data
@TableName("sal_settle_detail")
public class SalSettleDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**主编ID*/
    @ApiModelProperty(value = "主编ID")
    private java.lang.String pid;
	/**结算明细ID*/
	@Excel(name = "结算明细ID", width = 15)
    @ApiModelProperty(value = "结算明细ID")
    private java.lang.String deliveryDetailId;
	/**退货明细ID*/
	@Excel(name = "退货明细ID", width = 15)
    @ApiModelProperty(value = "退货明细ID")
    private java.lang.String returnDetailId;
	/**货品*/
	@Excel(name = "货品", width = 15)
    @ApiModelProperty(value = "货品")
    private java.lang.String materialCode;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String unit;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String specifications;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.lang.Double unitPrice;
	/**发货数*/
	@Excel(name = "发货数", width = 15)
    @ApiModelProperty(value = "发货数")
    private java.lang.Double deliveryQty;
	/**发货金额*/
	@Excel(name = "发货金额", width = 15)
    @ApiModelProperty(value = "发货金额")
    private java.lang.Double deliveryAmount;
	/**结算数*/
	@Excel(name = "结算数", width = 15)
    @ApiModelProperty(value = "结算数")
    private java.lang.Double qty;
	/**结算金额*/
	@Excel(name = "结算金额", width = 15)
    @ApiModelProperty(value = "结算金额")
    private java.lang.Double amount;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private java.lang.Integer delFlag;
}
