package org.jeecg.modules.pur.purpayment.vo;

import java.util.List;
import org.jeecg.modules.pur.purpayment.entity.PurPayment;
import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
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
 * @Description: 采购付款
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_paymentPage对象", description="采购付款")
public class PurPaymentPage {

	/**主键*/
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
	/**付款单号*/
	@Excel(name = "付款单号", width = 15)
	@ApiModelProperty(value = "付款单号")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**结算单号*/
	@Excel(name = "结算单号", width = 15)
	@ApiModelProperty(value = "结算单号")
    private String settleCodes;
	/**结算_IDS*/
	@Excel(name = "结算_IDS", width = 15)
	@ApiModelProperty(value = "结算_IDS")
    private String settleIds;
	/**供应商*/
	@Excel(name = "供应商", width = 15, dictTable = "yujiakeji_suppliers", dicText = "name", dicCode = "code")
    @Dict(dictTable = "yujiakeji_suppliers", dicText = "name", dicCode = "code")
	@ApiModelProperty(value = "供应商")
    private String supplierCode;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	@Excel(name = "审核状态", width = 15, dicCode = "dict_audit_status")
    @Dict(dicCode = "dict_audit_status")
	@ApiModelProperty(value = "审核状态")
    private Integer audit;
	/**审核人*/
	@Excel(name = "审核人", width = 15,dictTable = "sys_user",dicText = "realname",dicCode = "username")
@Dict(dictTable = "sys_user",dicText = "realname",dicCode = "username")
	@ApiModelProperty(value = "审核人")
    private String auditBy;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审核时间")
    private Date auditTime;
	/**金额合计*/
	@Excel(name = "金额合计", width = 15)
	@ApiModelProperty(value = "金额合计")
    private Double amount;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "dict_pur_status")
    @Dict(dicCode = "dict_pur_status")
	@ApiModelProperty(value = "状态")
    private Integer status;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="采购付款_明细")
	@ApiModelProperty(value = "采购付款_明细")
	private List<PurPaymentDetail> purPaymentDetailList;

}
