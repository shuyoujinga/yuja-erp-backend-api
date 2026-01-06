package org.jeecg.modules.prd.prdprocess.vo;

import java.util.List;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcess;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
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
 * @Description: 生产工序
 * @Author: 舒有敬
 * @Date:   2026-01-06
 * @Version: V1.0
 */
@Data
@ApiModel(value="prd_processPage对象", description="生产工序")
public class PrdProcessPage {

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
	/**货品*/
	@Excel(name = "货品", width = 15)
	@ApiModelProperty(value = "货品")
    private String materialCode;
	/**单位*/
	@Excel(name = "单位", width = 15,dicCode="dict_materials_unit")
	@ApiModelProperty(value = "单位")
	@Dict(dicCode="dict_materials_unit")
	private String unit;
	/**规格*/
	@Excel(name = "规格", width = 15)
	@ApiModelProperty(value = "规格")
    private String specifications;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
    private Integer delFlag;

	@ExcelCollection(name="生产工序_明细")
	@ApiModelProperty(value = "生产工序_明细")
	private List<PrdProcessDetail> prdProcessDetailList;

}
