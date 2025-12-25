package org.jeecg.modules.sal.salreceipt.vo;

import java.util.List;
import org.jeecg.modules.sal.salreceipt.entity.SalReceipt;
import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
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
 * @Description: 销售收款
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Data
@ApiModel(value="sal_receiptPage对象", description="销售收款")
public class SalReceiptPage {

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
	/**收款单号*/
	@Excel(name = "收款单号", width = 15)
	@ApiModelProperty(value = "收款单号")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**客户*/
	@Excel(name = "客户", width = 15)
	@ApiModelProperty(value = "客户")
    private String customerCode;
	/**结算IDS*/
	@Excel(name = "结算IDS", width = 15)
	@ApiModelProperty(value = "结算IDS")
    private String settleIds;
	/**结算单号*/
	@Excel(name = "结算单号", width = 15)
	@ApiModelProperty(value = "结算单号")
    private String settleCodes;
	/**预收ID*/
	@Excel(name = "预收ID", width = 15)
	@ApiModelProperty(value = "预收ID")
    private String prepayId;
	/**预收单*/
	@Excel(name = "预收单", width = 15)
	@ApiModelProperty(value = "预收单")
    private String prepayCode;
	/**收款方式*/
	@Excel(name = "收款方式", width = 15)
	@ApiModelProperty(value = "收款方式")
    private Integer receiptType;
	/**结算金额*/
	@Excel(name = "结算金额", width = 15)
	@ApiModelProperty(value = "结算金额")
    private Double settleAmount;
	/**预收抵扣*/
	@Excel(name = "预收抵扣", width = 15)
	@ApiModelProperty(value = "预收抵扣")
    private Double prepay;
	/**实收合计*/
	@Excel(name = "实收合计", width = 15)
	@ApiModelProperty(value = "实收合计")
    private Double amount;
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
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
    private Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="销售收款_明细")
	@ApiModelProperty(value = "销售收款_明细")
	private List<SalReceiptDetail> salReceiptDetailList;

}
