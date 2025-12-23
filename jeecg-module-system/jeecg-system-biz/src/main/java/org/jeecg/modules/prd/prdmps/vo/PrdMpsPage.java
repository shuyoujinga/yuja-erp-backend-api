package org.jeecg.modules.prd.prdmps.vo;

import java.util.List;
import org.jeecg.modules.prd.prdmps.entity.PrdMps;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
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
 * @Description: 生产计划
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Data
@ApiModel(value="prd_mpsPage对象", description="生产计划")
public class PrdMpsPage {

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
	/**计划单号*/
	@Excel(name = "计划单号", width = 15)
	@ApiModelProperty(value = "计划单号")
    private String docCode;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "单据日期")
    private Date docTime;
	/**业务计划*/
	@Excel(name = "业务计划", width = 15)
	@ApiModelProperty(value = "业务计划")
    private String bizPlanCodes;
	/**业务计划_IDS*/
	@Excel(name = "业务计划_IDS", width = 15)
	@ApiModelProperty(value = "业务计划_IDS")
    private String bizPlanIds;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
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
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
    private Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**要求交期*/
	@Excel(name = "要求交期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "要求交期")
    private Date deliveryTime;
	/**回复交期*/
	@Excel(name = "回复交期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "回复交期")
    private Date replyDeliveryTime;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="生产计划_明细")
	@ApiModelProperty(value = "生产计划_明细")
	private List<PrdMpsDetail> prdMpsDetailList;
	@ExcelCollection(name="生产计划_材料清单")
	@ApiModelProperty(value = "生产计划_材料清单")
	private List<PrdMpsBomDetail> prdMpsBomDetailList;

}
