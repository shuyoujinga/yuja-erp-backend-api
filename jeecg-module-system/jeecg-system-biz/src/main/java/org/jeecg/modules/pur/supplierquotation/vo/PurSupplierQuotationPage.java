package org.jeecg.modules.pur.supplierquotation.vo;

import java.util.List;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotation;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
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
 * @Description: 采购报价
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_supplier_quotationPage对象", description="采购报价")
public class PurSupplierQuotationPage {

	/**主键*/
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
	@Excel(name = "所属部门", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private java.lang.String sysOrgCode;
	/**单据*/
	@Excel(name = "单据", width = 15)
	@ApiModelProperty(value = "单据")
    private java.lang.String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private java.util.Date docTime;
	/**供应商*/
	@Excel(name = "供应商", width = 15, dictTable = "yujiakeji_suppliers", dicText = "name", dicCode = "code")
    @Dict(dictTable = "yujiakeji_suppliers", dicText = "name", dicCode = "code")
	@ApiModelProperty(value = "供应商")
    private java.lang.String supplierCode;
	/**金额*/
	@Excel(name = "金额", width = 15)
	@ApiModelProperty(value = "金额")
	private java.lang.Double amount;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15,dicCode = "dict_audit_status")
	@ApiModelProperty(value = "审核状态")
	@Dict(dicCode = "dict_audit_status")
    private java.lang.Integer audit;
	/**审核人*/
	@Excel(name = "审核人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "审核人")
    private java.lang.String auditBy;
	/**审核时间*/
	@Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "审核时间")
    private java.util.Date auditTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private java.lang.Integer delFlag;

	@ExcelCollection(name="采购报价_明细")
	@ApiModelProperty(value = "采购报价_明细")
	private List<PurSupplierQuotationDetail> purSupplierQuotationDetailList;

}
