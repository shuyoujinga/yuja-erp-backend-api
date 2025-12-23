package org.jeecg.modules.sal.saldelivery.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.entity.SalDelivery;
import org.jeecg.modules.sal.saldelivery.vo.SalDeliveryPage;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryService;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 销售出货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="销售出货")
@RestController
@RequestMapping("/saldelivery/salDelivery")
@Slf4j
public class SalDeliveryController {
	@Autowired
	private ISalDeliveryService salDeliveryService;
	@Autowired
	private ISalDeliveryDetailService salDeliveryDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salDelivery
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售出货-分页列表查询")
	@ApiOperation(value="销售出货-分页列表查询", notes="销售出货-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalDelivery>> queryPageList(SalDelivery salDelivery,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalDelivery> queryWrapper = QueryGenerator.initQueryWrapper(salDelivery, req.getParameterMap());
		Page<SalDelivery> page = new Page<SalDelivery>(pageNo, pageSize);
		IPage<SalDelivery> pageList = salDeliveryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salDeliveryPage
	 * @return
	 */
	@AutoLog(value = "销售出货-添加")
	@ApiOperation(value="销售出货-添加", notes="销售出货-添加")
    @RequiresPermissions("saldelivery:sal_delivery:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalDeliveryPage salDeliveryPage) {
		SalDelivery salDelivery = new SalDelivery();
		BeanUtils.copyProperties(salDeliveryPage, salDelivery);
		salDeliveryService.saveMain(salDelivery, salDeliveryPage.getSalDeliveryDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salDeliveryPage
	 * @return
	 */
	@AutoLog(value = "销售出货-编辑")
	@ApiOperation(value="销售出货-编辑", notes="销售出货-编辑")
    @RequiresPermissions("saldelivery:sal_delivery:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalDeliveryPage salDeliveryPage) {
		SalDelivery salDelivery = new SalDelivery();
		BeanUtils.copyProperties(salDeliveryPage, salDelivery);
		SalDelivery salDeliveryEntity = salDeliveryService.getById(salDelivery.getId());
		if(salDeliveryEntity==null) {
			return Result.error("未找到对应数据");
		}
		salDeliveryService.updateMain(salDelivery, salDeliveryPage.getSalDeliveryDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售出货-通过id删除")
	@ApiOperation(value="销售出货-通过id删除", notes="销售出货-通过id删除")
    @RequiresPermissions("saldelivery:sal_delivery:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salDeliveryService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售出货-批量删除")
	@ApiOperation(value="销售出货-批量删除", notes="销售出货-批量删除")
    @RequiresPermissions("saldelivery:sal_delivery:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salDeliveryService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售出货-通过id查询")
	@ApiOperation(value="销售出货-通过id查询", notes="销售出货-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalDelivery> queryById(@RequestParam(name="id",required=true) String id) {
		SalDelivery salDelivery = salDeliveryService.getById(id);
		if(salDelivery==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salDelivery);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售发货_明细通过主表ID查询")
	@ApiOperation(value="销售发货_明细主表ID查询", notes="销售发货_明细-通主表ID查询")
	@GetMapping(value = "/querySalDeliveryDetailByMainId")
	public Result<List<SalDeliveryDetail>> querySalDeliveryDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalDeliveryDetail> salDeliveryDetailList = salDeliveryDetailService.selectByMainId(id);
		return Result.OK(salDeliveryDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param salDelivery
    */
    @RequiresPermissions("saldelivery:sal_delivery:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalDelivery salDelivery) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalDelivery> queryWrapper = QueryGenerator.initQueryWrapper(salDelivery, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalDelivery> salDeliveryList = salDeliveryService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalDeliveryPage> pageList = new ArrayList<SalDeliveryPage>();
      for (SalDelivery main : salDeliveryList) {
          SalDeliveryPage vo = new SalDeliveryPage();
          BeanUtils.copyProperties(main, vo);
          List<SalDeliveryDetail> salDeliveryDetailList = salDeliveryDetailService.selectByMainId(main.getId());
          vo.setSalDeliveryDetailList(salDeliveryDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售出货列表");
      mv.addObject(NormalExcelConstants.CLASS, SalDeliveryPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售出货数据", "导出人:"+sysUser.getRealname(), "销售出货"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("saldelivery:sal_delivery:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<SalDeliveryPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalDeliveryPage.class, params);
              for (SalDeliveryPage page : list) {
                  SalDelivery po = new SalDelivery();
                  BeanUtils.copyProperties(page, po);
                  salDeliveryService.saveMain(po, page.getSalDeliveryDetailList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
