package org.jeecg.modules.pur.purreceive.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 采购收货_明细
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
@ApiModel(value="pur_receive_detail对象", description="采购收货_明细")
@Data
@TableName("pur_receive_detail")
public class PurReceiveDetail implements Serializable {
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
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private String pid;
	/**物料*/
	@Excel(name = "物料", width = 15, dictTable = "yujiakeji_materials", dicText = "material_name", dicCode = "material_code")
    @ApiModelProperty(value = "物料")
    private String materialCode;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String specifications;
	/**单位*/
	@Excel(name = "单位", width = 15, dicCode = "dict_materials_unit")
    @ApiModelProperty(value = "单位")
    private String unit;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Double receiveQty;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private Double unitPrice;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private Double amount;
	/**仓库*/
	@Excel(name = "仓库", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "仓库")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String warehouseCode;
	/**仓位*/
	@Excel(name = "仓位", width = 15)
    @ApiModelProperty(value = "仓位")
    private Integer locationCode;
	/**引用ID*/
	@Excel(name = "引用ID", width = 15)
    @ApiModelProperty(value = "引用ID")
    private String refId;
	/**引用单号*/
	@Excel(name = "引用单号", width = 15)
    @ApiModelProperty(value = "引用单号")
    private String refCode;
	/**订单明细ID*/
	@Excel(name = "订单明细ID", width = 15)
    @ApiModelProperty(value = "订单明细ID")
    private String orderDetailId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
