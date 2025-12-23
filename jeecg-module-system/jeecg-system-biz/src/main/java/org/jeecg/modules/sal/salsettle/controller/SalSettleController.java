package org.jeecg.modules.sal.salsettle.controller;

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
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import org.jeecg.modules.sal.salsettle.entity.SalSettle;
import org.jeecg.modules.sal.salsettle.vo.SalSettlePage;
import org.jeecg.modules.sal.salsettle.service.ISalSettleService;
import org.jeecg.modules.sal.salsettle.service.ISalSettleDetailService;
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
 * @Description: 销售结算
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="销售结算")
@RestController
@RequestMapping("/salsettle/salSettle")
@Slf4j
public class SalSettleController {
	@Autowired
	private ISalSettleService salSettleService;
	@Autowired
	private ISalSettleDetailService salSettleDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salSettle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售结算-分页列表查询")
	@ApiOperation(value="销售结算-分页列表查询", notes="销售结算-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalSettle>> queryPageList(SalSettle salSettle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalSettle> queryWrapper = QueryGenerator.initQueryWrapper(salSettle, req.getParameterMap());
		Page<SalSettle> page = new Page<SalSettle>(pageNo, pageSize);
		IPage<SalSettle> pageList = salSettleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salSettlePage
	 * @return
	 */
	@AutoLog(value = "销售结算-添加")
	@ApiOperation(value="销售结算-添加", notes="销售结算-添加")
    @RequiresPermissions("salsettle:sal_settle:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalSettlePage salSettlePage) {
		SalSettle salSettle = new SalSettle();
		BeanUtils.copyProperties(salSettlePage, salSettle);
		salSettleService.saveMain(salSettle, salSettlePage.getSalSettleDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salSettlePage
	 * @return
	 */
	@AutoLog(value = "销售结算-编辑")
	@ApiOperation(value="销售结算-编辑", notes="销售结算-编辑")
    @RequiresPermissions("salsettle:sal_settle:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalSettlePage salSettlePage) {
		SalSettle salSettle = new SalSettle();
		BeanUtils.copyProperties(salSettlePage, salSettle);
		SalSettle salSettleEntity = salSettleService.getById(salSettle.getId());
		if(salSettleEntity==null) {
			return Result.error("未找到对应数据");
		}
		salSettleService.updateMain(salSettle, salSettlePage.getSalSettleDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售结算-通过id删除")
	@ApiOperation(value="销售结算-通过id删除", notes="销售结算-通过id删除")
    @RequiresPermissions("salsettle:sal_settle:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salSettleService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售结算-批量删除")
	@ApiOperation(value="销售结算-批量删除", notes="销售结算-批量删除")
    @RequiresPermissions("salsettle:sal_settle:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salSettleService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售结算-通过id查询")
	@ApiOperation(value="销售结算-通过id查询", notes="销售结算-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalSettle> queryById(@RequestParam(name="id",required=true) String id) {
		SalSettle salSettle = salSettleService.getById(id);
		if(salSettle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salSettle);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售结算_明细通过主表ID查询")
	@ApiOperation(value="销售结算_明细主表ID查询", notes="销售结算_明细-通主表ID查询")
	@GetMapping(value = "/querySalSettleDetailByMainId")
	public Result<List<SalSettleDetail>> querySalSettleDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalSettleDetail> salSettleDetailList = salSettleDetailService.selectByMainId(id);
		return Result.OK(salSettleDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param salSettle
    */
    @RequiresPermissions("salsettle:sal_settle:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalSettle salSettle) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalSettle> queryWrapper = QueryGenerator.initQueryWrapper(salSettle, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalSettle> salSettleList = salSettleService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalSettlePage> pageList = new ArrayList<SalSettlePage>();
      for (SalSettle main : salSettleList) {
          SalSettlePage vo = new SalSettlePage();
          BeanUtils.copyProperties(main, vo);
          List<SalSettleDetail> salSettleDetailList = salSettleDetailService.selectByMainId(main.getId());
          vo.setSalSettleDetailList(salSettleDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售结算列表");
      mv.addObject(NormalExcelConstants.CLASS, SalSettlePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售结算数据", "导出人:"+sysUser.getRealname(), "销售结算"));
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
    @RequiresPermissions("salsettle:sal_settle:importExcel")
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
              List<SalSettlePage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalSettlePage.class, params);
              for (SalSettlePage page : list) {
                  SalSettle po = new SalSettle();
                  BeanUtils.copyProperties(page, po);
                  salSettleService.saveMain(po, page.getSalSettleDetailList());
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
