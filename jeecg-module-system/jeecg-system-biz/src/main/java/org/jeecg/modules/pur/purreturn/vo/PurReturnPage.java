package org.jeecg.modules.pur.purreturn.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 采购退货
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_returnPage对象", description="采购退货")
public class PurReturnPage {

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
	@Excel(name = "所属部门", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String sysOrgCode;
	/**退货单号 */
	@Excel(name = "退货单号 ", width = 15)
	@ApiModelProperty(value = "退货单号 ")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**供应商*/
	@Excel(name = "供应商", width = 15, dictTable = "yujiakeji_suppliers", dicText = "name", dicCode = "code")
	@Dict(dictTable = "yujiakeji_suppliers", dicText = "name", dicCode = "code")
	@ApiModelProperty(value = "供应商")
    private String supplierCode;
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
	/**退货类型*/
	@Excel(name = "退货类型", width = 15,dicCode = "dict_return_type")
	@Dict(dicCode = "dict_return_type")
	@ApiModelProperty(value = "退货类型")
    private Integer returnType;
	/**退货金额*/
	@Excel(name = "退货金额", width = 15)
	@ApiModelProperty(value = "退货金额")
    private Double amount;
	/**状态*/
	@Excel(name = "状态", width = 15,dicCode = "dict_pur_status")
	@ApiModelProperty(value = "状态")
	@Dict(dicCode = "dict_pur_status")
    private Integer status;
	/**采购订单_ids*/
	@ApiModelProperty(value = "采购订单_ids")
    private String orderIds;
	/**采购订单*/
	@Excel(name = "采购订单", width = 15)
	@ApiModelProperty(value = "采购订单")
    private String orderCodes;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**是否退货*/
	@Excel(name = "是否退货", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否退货")
	private Integer isReturn;
	/**是否有效*/
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="采购退货_明细")
	@ApiModelProperty(value = "采购退货_明细")
	private List<PurReturnDetail> purReturnDetailList;

}
