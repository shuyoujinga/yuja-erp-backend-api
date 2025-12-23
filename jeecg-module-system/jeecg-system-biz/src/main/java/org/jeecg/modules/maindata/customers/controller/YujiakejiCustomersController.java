package org.jeecg.modules.maindata.customers.controller;

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
import org.jeecg.modules.maindata.customers.entity.YujiakejiCustomers;
import org.jeecg.modules.maindata.customers.service.IYujiakejiCustomersService;

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
 * @Description: 客户管理
 * @Author: 舒有敬
 * @Date:   2024-06-08
 * @Version: V1.0
 */
@Api(tags="客户管理")
@RestController
@RequestMapping("/customers/yujiakejiCustomers")
@Slf4j
public class YujiakejiCustomersController extends JeecgController<YujiakejiCustomers, IYujiakejiCustomersService> {
	@Autowired
	private IYujiakejiCustomersService yujiakejiCustomersService;
	
	/**
	 * 分页列表查询
	 *
	 * @param yujiakejiCustomers
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "客户管理-分页列表查询")
	@ApiOperation(value="客户管理-分页列表查询", notes="客户管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YujiakejiCustomers>> queryPageList(YujiakejiCustomers yujiakejiCustomers,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<YujiakejiCustomers> queryWrapper = QueryGenerator.initQueryWrapper(yujiakejiCustomers, req.getParameterMap());
		Page<YujiakejiCustomers> page = new Page<YujiakejiCustomers>(pageNo, pageSize);
		IPage<YujiakejiCustomers> pageList = yujiakejiCustomersService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yujiakejiCustomers
	 * @return
	 */
	@AutoLog(value = "客户管理-添加")
	@ApiOperation(value="客户管理-添加", notes="客户管理-添加")
	@RequiresPermissions("customers:yujiakeji_customers:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YujiakejiCustomers yujiakejiCustomers) {
		yujiakejiCustomersService.saveEntity(yujiakejiCustomers);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yujiakejiCustomers
	 * @return
	 */
	@AutoLog(value = "客户管理-编辑")
	@ApiOperation(value="客户管理-编辑", notes="客户管理-编辑")
	@RequiresPermissions("customers:yujiakeji_customers:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YujiakejiCustomers yujiakejiCustomers) {
		yujiakejiCustomersService.updateById(yujiakejiCustomers);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户管理-通过id删除")
	@ApiOperation(value="客户管理-通过id删除", notes="客户管理-通过id删除")
	@RequiresPermissions("customers:yujiakeji_customers:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yujiakejiCustomersService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户管理-批量删除")
	@ApiOperation(value="客户管理-批量删除", notes="客户管理-批量删除")
	@RequiresPermissions("customers:yujiakeji_customers:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yujiakejiCustomersService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "客户管理-通过id查询")
	@ApiOperation(value="客户管理-通过id查询", notes="客户管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YujiakejiCustomers> queryById(@RequestParam(name="id",required=true) String id) {
		YujiakejiCustomers yujiakejiCustomers = yujiakejiCustomersService.getById(id);
		if(yujiakejiCustomers==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yujiakejiCustomers);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yujiakejiCustomers
    */
    @RequiresPermissions("customers:yujiakeji_customers:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YujiakejiCustomers yujiakejiCustomers) {
        return super.exportXls(request, yujiakejiCustomers, YujiakejiCustomers.class, "客户管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("customers:yujiakeji_customers:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YujiakejiCustomers.class);
    }

}
