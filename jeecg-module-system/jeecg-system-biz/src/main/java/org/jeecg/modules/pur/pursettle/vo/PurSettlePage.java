package org.jeecg.modules.pur.pursettle.vo;

import java.util.List;
import org.jeecg.modules.pur.pursettle.entity.PurSettle;
import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
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
 * @Description: 采购结算
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_settlePage对象", description="采购结算")
public class PurSettlePage {

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
	/**结算单号*/
	@Excel(name = "结算单号", width = 15)
	@ApiModelProperty(value = "结算单号")
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
    private String suplierCode;
	/**采购收货_IDS*/
	@Excel(name = "采购收货_IDS", width = 15)
	@ApiModelProperty(value = "采购收货_IDS")
    private String receiveIds;
	/**收货单号*/
	@Excel(name = "收货单号", width = 15)
	@ApiModelProperty(value = "收货单号")
    private String receiveCodes;
	/**预计付款日期*/
	@Excel(name = "预计付款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "预计付款日期")
    private Date payTime;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "dict_pur_status")
    @Dict(dicCode = "dict_pur_status")
	@ApiModelProperty(value = "状态")
    private Integer status;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15, dicCode = "dict_audit_status")
    @Dict(dicCode = "dict_audit_status")
	@ApiModelProperty(value = "审核状态")
    private Integer audit;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
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
	/**差异金额合计*/
	@Excel(name = "差异金额合计", width = 15)
	@ApiModelProperty(value = "差异金额合计")
    private Double differAmount;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="采购结算_明细")
	@ApiModelProperty(value = "采购结算_明细")
	private List<PurSettleDetail> purSettleDetailList;

}
