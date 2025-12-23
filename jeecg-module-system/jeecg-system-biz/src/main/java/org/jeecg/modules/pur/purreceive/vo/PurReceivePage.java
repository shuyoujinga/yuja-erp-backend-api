package org.jeecg.modules.pur.purreceive.vo;

import java.util.List;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购收货
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_receivePage对象", description="采购收货")
public class PurReceivePage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
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
	/**收货单号*/
	@Excel(name = "收货单号", width = 15)
	@ApiModelProperty(value = "收货单号")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**供应商*/
	@Excel(name = "供应商", width = 15,dictTable = "yujiakeji_suppliers",dicText = "name",dicCode = "code")
	@ApiModelProperty(value = "供应商")
	@Dict(dictTable = "yujiakeji_suppliers",dicText = "name",dicCode = "code")
    private String supplierCode;
	/**对方单号*/
	@Excel(name = "对方单号", width = 15)
	@ApiModelProperty(value = "对方单号")
    private String externalOrderNo;
	/**采购订单*/
	@Excel(name = "采购订单", width = 15)
	@ApiModelProperty(value = "采购订单")
    private String orderCode;
	/**订单ID*/
	@ApiModelProperty(value = "订单ID")
    private String orderId;
	/**状态*/
	/**状态*/
	@Excel(name = "状态", width = 15,dicCode = "dict_pur_status")
	@ApiModelProperty(value = "状态")
	@Dict(dicCode = "dict_pur_status")
    private Integer status;
	/**金额合计*/
	@Excel(name = "金额合计", width = 15)
	@ApiModelProperty(value = "金额合计")
    private Double amount;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15, dicCode = "dict_audit_status")
    @Dict(dicCode = "dict_audit_status")
	@ApiModelProperty(value = "审核状态")
    private Integer audit;
	/**审核人*/
	@Excel(name = "审核人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "审核人")
    private String auditBy;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审核时间")
    private Date auditTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="采购收货_明细")
	@ApiModelProperty(value = "采购收货_明细")
	private List<PurReceiveDetail> purReceiveDetailList;

}
