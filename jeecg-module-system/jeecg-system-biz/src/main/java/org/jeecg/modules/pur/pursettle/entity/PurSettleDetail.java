package org.jeecg.modules.pur.pursettle.entity;

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
 * @Description: 采购结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@ApiModel(value="pur_settle_detail对象", description="采购结算_明细")
@Data
@TableName("pur_settle_detail")
public class PurSettleDetail implements Serializable {
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
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private String pid;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
	/**收货数量*/
	@Excel(name = "收货数量", width = 15)
    @ApiModelProperty(value = "收货数量")
    private Double receiveNum;
	/**收货单价*/
	@Excel(name = "收货单价", width = 15)
    @ApiModelProperty(value = "收货单价")
    private Double receiveUnitPrice;
	/**收货金额*/
	@Excel(name = "收货金额", width = 15)
    @ApiModelProperty(value = "收货金额")
    private Double receiveAmount;
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
	/**差异金额*/
	@Excel(name = "差异金额", width = 15)
    @ApiModelProperty(value = "差异金额")
    private Double settleDifferAmount;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**采购收货明细ID*/
    @ApiModelProperty(value = "采购收货明细ID")
    private String receiveDetailId;

	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
