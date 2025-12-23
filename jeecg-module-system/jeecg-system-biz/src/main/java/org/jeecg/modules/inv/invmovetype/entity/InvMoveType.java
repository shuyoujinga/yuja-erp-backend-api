package org.jeecg.modules.inv.invmovetype.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: inv_move_type
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Data
@TableName("inv_move_type")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="inv_move_type对象", description="inv_move_type")
public class InvMoveType implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**移动类型编码*/
	@Excel(name = "移动类型编码", width = 15)
    @ApiModelProperty(value = "移动类型编码")
    private String moveType;
	/**移动类型说明*/
	@Excel(name = "移动类型说明", width = 15)
    @ApiModelProperty(value = "移动类型说明")
    private String moveDesc;
	/**出入库类型*/
	@Excel(name = "出入库类型", width = 15, dicCode = "dict_in_out_type")
	@Dict(dicCode = "dict_in_out_type")
    @ApiModelProperty(value = "出入库类型")
    private Integer direction;
	/**影响成本*/
	@Excel(name = "影响成本", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "影响成本")
    private Integer affectCost;
	/**允许冲销*/
	@Excel(name = "允许冲销", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "允许冲销")
    private Integer isReversal;
	/**业务类型*/
	@Excel(name = "业务类型", width = 15, dicCode = "dict_biz_type")
	@Dict(dicCode = "dict_biz_type")
    @ApiModelProperty(value = "业务类型")
    private String bizType;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    @TableLogic
    private Integer delFlag;
}
