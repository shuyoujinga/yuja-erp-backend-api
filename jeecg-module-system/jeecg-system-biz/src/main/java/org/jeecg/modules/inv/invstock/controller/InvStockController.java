package org.jeecg.modules.inv.invstock.controller;

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
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;

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
 * @Description: 实时库存
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Api(tags="实时库存")
@RestController
@RequestMapping("/invstock/invStock")
@Slf4j
public class InvStockController extends JeecgController<InvStock, IInvStockService> {
	@Autowired
	private IInvStockService invStockService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invStock
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "实时库存-分页列表查询")
	@ApiOperation(value="实时库存-分页列表查询", notes="实时库存-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvStock>> queryPageList(InvStock invStock,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<InvStock> page = new Page<InvStock>(pageNo, pageSize);
		// 自己写一个联表分页方法
		IPage<InvStock> pageList = invStockService.queryPageWithMaterial(page, invStock, req.getParameterMap());
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invStock
	 * @return
	 */
	@AutoLog(value = "实时库存-添加")
	@ApiOperation(value="实时库存-添加", notes="实时库存-添加")
	@RequiresPermissions("invstock:inv_stock:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvStock invStock) {
		invStockService.save(invStock);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invStock
	 * @return
	 */
	@AutoLog(value = "实时库存-编辑")
	@ApiOperation(value="实时库存-编辑", notes="实时库存-编辑")
	@RequiresPermissions("invstock:inv_stock:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvStock invStock) {
		invStockService.updateById(invStock);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "实时库存-通过id删除")
	@ApiOperation(value="实时库存-通过id删除", notes="实时库存-通过id删除")
	@RequiresPermissions("invstock:inv_stock:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invStockService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "实时库存-批量删除")
	@ApiOperation(value="实时库存-批量删除", notes="实时库存-批量删除")
	@RequiresPermissions("invstock:inv_stock:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invStockService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "实时库存-通过id查询")
	@ApiOperation(value="实时库存-通过id查询", notes="实时库存-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvStock> queryById(@RequestParam(name="id",required=true) String id) {
		InvStock invStock = invStockService.getById(id);
		if(invStock==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invStock);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invStock
    */
    @RequiresPermissions("invstock:inv_stock:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvStock invStock) {
		// 1. 使用自定义 SQL（不走 QueryWrapper）
		List<InvStock> list = invStockService.queryListWithMaterial(request.getParameterMap());

		// 2. 使用“兼容版父类方法”导出
		return super.exportXlsByCustom(request, list, InvStock.class, "实时库存");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("invstock:inv_stock:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, InvStock.class);
    }

}
