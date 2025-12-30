package org.jeecg.modules.inv.invmaterialvoucher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 物料凭证
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@ApiModel(value="inv_material_voucher对象", description="物料凭证")
@Data
@TableName("inv_material_voucher")
public class InvMaterialVoucher implements Serializable {
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
	/**物料凭证*/
	@Excel(name = "物料凭证", width = 15)
    @ApiModelProperty(value = "物料凭证")
    private String docCode;
	/**记账日期*/
	@Excel(name = "记账日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "记账日期")
    private Date docTime;
	/**业务类型*/
	@Excel(name = "业务类型", width = 15, dicCode = "dict_biz_type")
    @Dict(dicCode = "dict_biz_type")
    @ApiModelProperty(value = "业务类型")
    private Integer bizType;
	/**来源单据类型*/
	@Excel(name = "来源单据类型", width = 15, dicCode = "dict_source_doc_type")
    @Dict(dicCode = "dict_source_doc_type")
    @ApiModelProperty(value = "来源单据类型")
    private Integer sourceDocType;
	/**来源单据号*/
	@Excel(name = "来源单据号", width = 15)
    @ApiModelProperty(value = "来源单据号")
    private String sourceDocCode;
	/**来源单据ID*/
	@Excel(name = "来源单据ID", width = 15)
    @ApiModelProperty(value = "来源单据ID")
    private String sourceDocId;
    /**使用组织*/
    @Excel(name = "使用组织", width = 15,dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "使用组织")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String orgCode;
	/**主移动类型*/
	@Excel(name = "主移动类型", width = 15,dictTable = "inv_move_type",dicText = "move_desc",dicCode = "move_type")
    @ApiModelProperty(value = "主移动类型")
    @Dict(dictTable = "inv_move_type",dicText = "move_desc",dicCode = "move_type")
    private String moveType;
	/**冲销凭证*/
	@Excel(name = "冲销凭证", width = 15,dictTable = "inv_material_voucher",dicText = "doc_code",dicCode = "id")
    @ApiModelProperty(value = "冲销凭证")
    @Dict(dictTable = "inv_material_voucher",dicText = "doc_code",dicCode = "id")
    private String reversalDocId;
	/**是否冲销*/
	@Excel(name = "是否冲销", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否冲销")
    private Integer isReversal;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
/**是否有效*/
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;

    public InvMaterialVoucher() {
    }



    public InvMaterialVoucher(Integer bizType, Integer sourceDocType, String sourceDocCode, String sourceDocId, String moveType, String reversalDocId, Integer isReversal, String remark) {
        this.bizType = bizType;
        this.sourceDocType = sourceDocType;
        this.sourceDocCode = sourceDocCode;
        this.sourceDocId = sourceDocId;
        this.moveType = moveType;
        this.reversalDocId = reversalDocId;
        this.isReversal = isReversal;
        this.remark = remark;
    }


    public InvMaterialVoucher( String moveType, String sourceDocCode, String sourceDocId, String remark) {
        this.moveType = moveType;
        this.sourceDocCode = sourceDocCode;
        this.sourceDocId = sourceDocId;
        this.remark = remark;
    }
}
