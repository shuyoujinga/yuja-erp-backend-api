package org.jeecg.modules.sal.salquote.controller;

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
import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import org.jeecg.modules.sal.salquote.entity.SalQuote;
import org.jeecg.modules.sal.salquote.vo.SalQuotePage;
import org.jeecg.modules.sal.salquote.service.ISalQuoteService;
import org.jeecg.modules.sal.salquote.service.ISalQuoteDetailService;
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
 * @Description: 销售报价
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Api(tags="销售报价")
@RestController
@RequestMapping("/salquote/salQuote")
@Slf4j
public class SalQuoteController {
	@Autowired
	private ISalQuoteService salQuoteService;
	@Autowired
	private ISalQuoteDetailService salQuoteDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salQuote
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售报价-分页列表查询")
	@ApiOperation(value="销售报价-分页列表查询", notes="销售报价-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalQuote>> queryPageList(SalQuote salQuote,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalQuote> queryWrapper = QueryGenerator.initQueryWrapper(salQuote, req.getParameterMap());
		Page<SalQuote> page = new Page<SalQuote>(pageNo, pageSize);
		IPage<SalQuote> pageList = salQuoteService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salQuotePage
	 * @return
	 */
	@AutoLog(value = "销售报价-添加")
	@ApiOperation(value="销售报价-添加", notes="销售报价-添加")
    @RequiresPermissions("salquote:sal_quote:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalQuotePage salQuotePage) {
		SalQuote salQuote = new SalQuote();
		BeanUtils.copyProperties(salQuotePage, salQuote);
		salQuoteService.saveMain(salQuote, salQuotePage.getSalQuoteDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salQuotePage
	 * @return
	 */
	@AutoLog(value = "销售报价-编辑")
	@ApiOperation(value="销售报价-编辑", notes="销售报价-编辑")
    @RequiresPermissions("salquote:sal_quote:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalQuotePage salQuotePage) {
		SalQuote salQuote = new SalQuote();
		BeanUtils.copyProperties(salQuotePage, salQuote);
		SalQuote salQuoteEntity = salQuoteService.getById(salQuote.getId());
		if(salQuoteEntity==null) {
			return Result.error("未找到对应数据");
		}
		salQuoteService.updateMain(salQuote, salQuotePage.getSalQuoteDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售报价-通过id删除")
	@ApiOperation(value="销售报价-通过id删除", notes="销售报价-通过id删除")
    @RequiresPermissions("salquote:sal_quote:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salQuoteService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售报价-批量删除")
	@ApiOperation(value="销售报价-批量删除", notes="销售报价-批量删除")
    @RequiresPermissions("salquote:sal_quote:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salQuoteService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售报价-通过id查询")
	@ApiOperation(value="销售报价-通过id查询", notes="销售报价-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalQuote> queryById(@RequestParam(name="id",required=true) String id) {
		SalQuote salQuote = salQuoteService.getById(id);
		if(salQuote==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salQuote);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售报价_明细通过主表ID查询")
	@ApiOperation(value="销售报价_明细主表ID查询", notes="销售报价_明细-通主表ID查询")
	@GetMapping(value = "/querySalQuoteDetailByMainId")
	public Result<List<SalQuoteDetail>> querySalQuoteDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalQuoteDetail> salQuoteDetailList = salQuoteDetailService.selectByMainId(id);
		return Result.OK(salQuoteDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param salQuote
    */
    @RequiresPermissions("salquote:sal_quote:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalQuote salQuote) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalQuote> queryWrapper = QueryGenerator.initQueryWrapper(salQuote, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalQuote> salQuoteList = salQuoteService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalQuotePage> pageList = new ArrayList<SalQuotePage>();
      for (SalQuote main : salQuoteList) {
          SalQuotePage vo = new SalQuotePage();
          BeanUtils.copyProperties(main, vo);
          List<SalQuoteDetail> salQuoteDetailList = salQuoteDetailService.selectByMainId(main.getId());
          vo.setSalQuoteDetailList(salQuoteDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售报价列表");
      mv.addObject(NormalExcelConstants.CLASS, SalQuotePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售报价数据", "导出人:"+sysUser.getRealname(), "销售报价"));
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
    @RequiresPermissions("salquote:sal_quote:importExcel")
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
              List<SalQuotePage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalQuotePage.class, params);
              for (SalQuotePage page : list) {
                  SalQuote po = new SalQuote();
                  BeanUtils.copyProperties(page, po);
                  salQuoteService.saveMain(po, page.getSalQuoteDetailList());
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
