package org.jeecg.modules.inv.invmovetype.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.inv.invmovetype.entity.InvMoveType;
import org.jeecg.modules.inv.invmovetype.service.IInvMoveTypeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: inv_move_type
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Api(tags="inv_move_type")
@RestController
@RequestMapping("/invmovetype/invMoveType")
@Slf4j
public class InvMoveTypeController extends JeecgController<InvMoveType, IInvMoveTypeService> {
	@Autowired
	private IInvMoveTypeService invMoveTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invMoveType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "inv_move_type-分页列表查询")
	@ApiOperation(value="inv_move_type-分页列表查询", notes="inv_move_type-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvMoveType>> queryPageList(InvMoveType invMoveType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvMoveType> queryWrapper = QueryGenerator.initQueryWrapper(invMoveType, req.getParameterMap());
		Page<InvMoveType> page = new Page<InvMoveType>(pageNo, pageSize);
		IPage<InvMoveType> pageList = invMoveTypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invMoveType
	 * @return
	 */
	@AutoLog(value = "inv_move_type-添加")
	@ApiOperation(value="inv_move_type-添加", notes="inv_move_type-添加")
	@RequiresPermissions("invmovetype:inv_move_type:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvMoveType invMoveType) {
		invMoveTypeService.save(invMoveType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invMoveType
	 * @return
	 */
	@AutoLog(value = "inv_move_type-编辑")
	@ApiOperation(value="inv_move_type-编辑", notes="inv_move_type-编辑")
	@RequiresPermissions("invmovetype:inv_move_type:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvMoveType invMoveType) {
		invMoveTypeService.updateById(invMoveType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "inv_move_type-通过id删除")
	@ApiOperation(value="inv_move_type-通过id删除", notes="inv_move_type-通过id删除")
	@RequiresPermissions("invmovetype:inv_move_type:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invMoveTypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "inv_move_type-批量删除")
	@ApiOperation(value="inv_move_type-批量删除", notes="inv_move_type-批量删除")
	@RequiresPermissions("invmovetype:inv_move_type:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invMoveTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "inv_move_type-通过id查询")
	@ApiOperation(value="inv_move_type-通过id查询", notes="inv_move_type-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvMoveType> queryById(@RequestParam(name="id",required=true) String id) {
		InvMoveType invMoveType = invMoveTypeService.getById(id);
		if(invMoveType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invMoveType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invMoveType
    */
    @RequiresPermissions("invmovetype:inv_move_type:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvMoveType invMoveType) {
        return super.exportXls(request, invMoveType, InvMoveType.class, "inv_move_type");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("invmovetype:inv_move_type:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, InvMoveType.class);
    }

}
