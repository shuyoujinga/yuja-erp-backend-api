package org.jeecg.modules.prd.prdworkorder.vo;

import java.util.List;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
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
 * @Description: 生产工单
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Data
@ApiModel(value="prd_work_orderPage对象", description="生产工单")
public class PrdWorkOrderPage {

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
	/**生产工单*/
	@Excel(name = "生产工单", width = 15)
	@ApiModelProperty(value = "生产工单")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**排产单号_IDS*/
	@Excel(name = "排产单号_IDS", width = 15)
	@ApiModelProperty(value = "排产单号_IDS")
    private String mpsIds;
	/**排查单号*/
	@Excel(name = "排查单号", width = 15)
	@ApiModelProperty(value = "排查单号")
    private String mpsDocCodes;
	/**产线*/
	@Excel(name = "产线", width = 15)
	@ApiModelProperty(value = "产线")
    private String prdLine;
	/**生产产品*/
	@Excel(name = "生产产品", width = 15)
	@ApiModelProperty(value = "生产产品")
    private String materialCode;
	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
    private String unit;
	/**规格*/
	@Excel(name = "规格", width = 15)
	@ApiModelProperty(value = "规格")
    private String specifications;
	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
	@ApiModelProperty(value = "计划数量")
    private Double planQty;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
    private Double qty;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
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
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
    private Integer status;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="生产工单_物料明细")
	@ApiModelProperty(value = "生产工单_物料明细")
	private List<PrdWorkOrderDetail> prdWorkOrderDetailList;

}
