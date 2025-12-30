package org.jeecg.modules.sal.salquote.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 销售报价
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@ApiModel(value="sal_quote对象", description="销售报价")
@Data
@TableName("sal_quote")
public class SalQuote implements Serializable {
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
	@Excel(name = "所属部门", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String sysOrgCode;
	/**报价单号*/
	@Excel(name = "报价单号", width = 15)
    @ApiModelProperty(value = "报价单号")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**客户*/
	@Excel(name = "客户", width = 15,dictTable = "yujiakeji_customers",dicText = "name",dicCode = "code")
    @ApiModelProperty(value = "客户")
    @Dict(dictTable = "yujiakeji_customers",dicText = "name",dicCode = "code")
    private String customerCode;
    /**金额合计*/
    @Excel(name = "金额合计", width = 15)
    @ApiModelProperty(value = "金额合计")
    private Double amount;
    /**含税合计*/
    @Excel(name = "含税合计", width = 15)
    @ApiModelProperty(value = "含税合计")
    private Double taxAmount;
    /**状态*/
    @ApiModelProperty(value = "状态")
    private Integer status;
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
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
