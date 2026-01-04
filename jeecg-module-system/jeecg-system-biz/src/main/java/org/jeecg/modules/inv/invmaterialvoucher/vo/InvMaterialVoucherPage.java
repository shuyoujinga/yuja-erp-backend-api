package org.jeecg.modules.inv.invmaterialvoucher.vo;

import java.util.List;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
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
 * @Description: 物料凭证
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Data
@ApiModel(value="inv_material_voucherPage对象", description="物料凭证")
public class InvMaterialVoucherPage {

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
	@ApiModelProperty(value = "来源单据ID")
    private String sourceDocId;
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
    /**是否有效*/
    @ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="物料凭证_明细")
	@ApiModelProperty(value = "物料凭证_明细")
	private List<InvMaterialVoucherDetail> invMaterialVoucherDetailList;

}
