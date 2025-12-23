package org.jeecg.modules.maindata.bom.vo;

import java.util.List;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
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
 * @Description: 材料清单_主表
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Data
@ApiModel(value="yujiakeji_bomPage对象", description="材料清单_主表")
public class YujiakejiBomPage {

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
	/**BOM编号*/
	@Excel(name = "BOM编号", width = 15)
	@ApiModelProperty(value = "BOM编号")
    private String docCode;
	/**BOM类型*/
	@Excel(name = "BOM类型", width = 15, dicCode = "dict_bom_type")
    @Dict(dicCode = "dict_bom_type")
	@ApiModelProperty(value = "BOM类型")
    private Integer bomType;
	/**物料编码*/
	@Excel(name = "货品名称", width = 15, dictTable = "yujiakeji_materials", dicCode = "material_code",dicText = "material_name")
    @Dict(dictTable = "yujiakeji_materials", dicCode = "material_code",dicText = "material_name")
	@ApiModelProperty(value = "货品名称")
    private String materialCode;
	/**审核状态*/
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
	@Excel(name = "是否有效", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="材料清单_明细表")
	@ApiModelProperty(value = "材料清单_明细表")
	private List<YujiakejiBomDetail> yujiakejiBomDetailList;

}
