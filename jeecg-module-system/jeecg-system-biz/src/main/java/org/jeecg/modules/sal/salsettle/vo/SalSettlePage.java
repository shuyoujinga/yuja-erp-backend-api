package org.jeecg.modules.sal.salsettle.vo;

import java.util.List;
import org.jeecg.modules.sal.salsettle.entity.SalSettle;
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
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
 * @Description: 销售结算
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Data
@ApiModel(value="sal_settlePage对象", description="销售结算")
public class SalSettlePage {

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
    private java.lang.String sysOrgCode;
	/**结算单号*/
	@Excel(name = "结算单号", width = 15)
	@ApiModelProperty(value = "结算单号")
    private java.lang.String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private java.util.Date docTime;
	/**客户*/
	@Excel(name = "客户", width = 15)
	@ApiModelProperty(value = "客户")
    private java.lang.String customerCode;
	/**发货单号_IDS*/
	@Excel(name = "发货单号_IDS", width = 15)
	@ApiModelProperty(value = "发货单号_IDS")
    private java.lang.String deliveryIds;
	/**发货单号*/
	@Excel(name = "发货单号", width = 15)
	@ApiModelProperty(value = "发货单号")
    private java.lang.String deliveryCodes;
	/**退货单号_IDS*/
	@Excel(name = "退货单号_IDS", width = 15)
	@ApiModelProperty(value = "退货单号_IDS")
    private java.lang.String returnIds;
	/**退货单号*/
	@Excel(name = "退货单号", width = 15)
	@ApiModelProperty(value = "退货单号")
    private java.lang.String returnCodes;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
	@ApiModelProperty(value = "审核状态")
    private java.lang.Integer audit;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
	@ApiModelProperty(value = "审核人")
    private java.lang.String auditBy;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审核时间")
    private java.util.Date auditTime;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
    private java.lang.Integer status;
	/**结算合计*/
	@Excel(name = "结算合计", width = 15)
	@ApiModelProperty(value = "结算合计")
    private java.lang.Double settleAmount;
	/**退货冲抵*/
	@Excel(name = "退货冲抵", width = 15)
	@ApiModelProperty(value = "退货冲抵")
    private java.lang.Double returnAmount;
	/**金额合计*/
	@Excel(name = "金额合计", width = 15)
	@ApiModelProperty(value = "金额合计")
    private java.lang.Double amount;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private java.lang.Integer delFlag;

	@ExcelCollection(name="销售结算_明细")
	@ApiModelProperty(value = "销售结算_明细")
	private List<SalSettleDetail> salSettleDetailList;

}
