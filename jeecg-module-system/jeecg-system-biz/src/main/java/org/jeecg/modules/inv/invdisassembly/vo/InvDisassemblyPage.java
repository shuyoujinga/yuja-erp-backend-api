package org.jeecg.modules.inv.invdisassembly.vo;

import java.util.List;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassembly;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
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
 * @Description: 拆卸单
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="inv_disassemblyPage对象", description="拆卸单")
public class InvDisassemblyPage {

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
	/**拆卸单号*/
	@Excel(name = "拆卸单号", width = 15)
	@ApiModelProperty(value = "拆卸单号")
    private String docCode;
	/**制单日期*/
	@Excel(name = "制单日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "制单日期")
    private Date docTime;
	/**拆卸入库*/
	@Excel(name = "拆卸入库", width = 15)
	@ApiModelProperty(value = "拆卸入库")
    private String inWarehouseCode;
	/**拆卸出库*/
	@Excel(name = "拆卸出库", width = 15)
	@ApiModelProperty(value = "拆卸出库")
    private String outWarehouseCode;
	/**状态*/
	@Excel(name = "状态", width = 15)
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
    /**是否有效*/
    @ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="拆卸单_明细")
	@ApiModelProperty(value = "拆卸单_明细")
	private List<InvDisassemblyDetail> invDisassemblyDetailList;

}
