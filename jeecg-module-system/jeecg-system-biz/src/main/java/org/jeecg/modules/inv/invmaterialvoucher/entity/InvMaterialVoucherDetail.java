package org.jeecg.modules.inv.invmaterialvoucher.entity;

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
 * @Description: 物料凭证_明细
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@ApiModel(value="inv_material_voucher_detail对象", description="物料凭证_明细")
@Data
@TableName("inv_material_voucher_detail")
public class InvMaterialVoucherDetail implements Serializable {
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
    private String sysOrgCode;
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private String pid;
	/**来源单据明细ID*/
	@Excel(name = "来源单据明细ID", width = 15)
    @ApiModelProperty(value = "来源单据明细ID")
    private String sourceDocDetailId;
	/**行号*/
	@Excel(name = "行号", width = 15)
    @ApiModelProperty(value = "行号")
    private Integer lineNo;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private String materialCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String materialName;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String specifications;
	/**单位*/
	@Excel(name = "单位", width = 15,dicCode = "dict_materials_unit")
    @ApiModelProperty(value = "单位")
    @Dict(dicCode = "dict_materials_unit")
    private String unit;
	/**仓库*/
	@Excel(name = "仓库", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "仓库")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String warehouseCode;
	/**库位*/
	@Excel(name = "库位", width = 15)
    @ApiModelProperty(value = "库位")
    private String locationCode;
	/**移动数量*/
	@Excel(name = "移动数量", width = 15)
    @ApiModelProperty(value = "移动数量")
    private Double qty;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private Double price;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private Double amount;
	/**移动类型*/
	@Excel(name = "移动类型", width = 15,dictTable = "inv_move_type",dicText = "move_desc",dicCode = "move_type")
    @ApiModelProperty(value = "移动类型")
    @Dict(dictTable = "inv_move_type",dicText = "move_desc",dicCode = "move_type")
    private String moveType;
	/**出入库类型*/
	@Excel(name = "出入库类型", width = 15,dicCode = "dict_in_out_type")
    @ApiModelProperty(value = "出入库类型")
    @Dict(dicCode = "dict_in_out_type")
    private Integer stockType;
	/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
