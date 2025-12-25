package org.jeecg.modules.prd.prdreport.controller;

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
import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import org.jeecg.modules.prd.prdreport.entity.PrdReport;
import org.jeecg.modules.prd.prdreport.vo.PrdReportPage;
import org.jeecg.modules.prd.prdreport.service.IPrdReportService;
import org.jeecg.modules.prd.prdreport.service.IPrdReportDetailService;
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
 * @Description: 生产报工
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Api(tags="生产报工")
@RestController
@RequestMapping("/prdreport/prdReport")
@Slf4j
public class PrdReportController {
	@Autowired
	private IPrdReportService prdReportService;
	@Autowired
	private IPrdReportDetailService prdReportDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生产报工-分页列表查询")
	@ApiOperation(value="生产报工-分页列表查询", notes="生产报工-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PrdReport>> queryPageList(PrdReport prdReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdReport> queryWrapper = QueryGenerator.initQueryWrapper(prdReport, req.getParameterMap());
		Page<PrdReport> page = new Page<PrdReport>(pageNo, pageSize);
		IPage<PrdReport> pageList = prdReportService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdReportPage
	 * @return
	 */
	@AutoLog(value = "生产报工-添加")
	@ApiOperation(value="生产报工-添加", notes="生产报工-添加")
    @RequiresPermissions("prdreport:prd_report:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PrdReportPage prdReportPage) {
		PrdReport prdReport = new PrdReport();
		BeanUtils.copyProperties(prdReportPage, prdReport);
		prdReportService.saveMain(prdReport, prdReportPage.getPrdReportDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdReportPage
	 * @return
	 */
	@AutoLog(value = "生产报工-编辑")
	@ApiOperation(value="生产报工-编辑", notes="生产报工-编辑")
    @RequiresPermissions("prdreport:prd_report:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PrdReportPage prdReportPage) {
		PrdReport prdReport = new PrdReport();
		BeanUtils.copyProperties(prdReportPage, prdReport);
		PrdReport prdReportEntity = prdReportService.getById(prdReport.getId());
		if(prdReportEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdReportService.updateMain(prdReport, prdReportPage.getPrdReportDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生产报工-通过id删除")
	@ApiOperation(value="生产报工-通过id删除", notes="生产报工-通过id删除")
    @RequiresPermissions("prdreport:prd_report:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		prdReportService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生产报工-批量删除")
	@ApiOperation(value="生产报工-批量删除", notes="生产报工-批量删除")
    @RequiresPermissions("prdreport:prd_report:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdReportService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产报工-通过id查询")
	@ApiOperation(value="生产报工-通过id查询", notes="生产报工-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PrdReport> queryById(@RequestParam(name="id",required=true) String id) {
		PrdReport prdReport = prdReportService.getById(id);
		if(prdReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(prdReport);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产报工_明细通过主表ID查询")
	@ApiOperation(value="生产报工_明细主表ID查询", notes="生产报工_明细-通主表ID查询")
	@GetMapping(value = "/queryPrdReportDetailByMainId")
	public Result<List<PrdReportDetail>> queryPrdReportDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdReportDetail> prdReportDetailList = prdReportDetailService.selectByMainId(id);
		return Result.OK(prdReportDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdReport
    */
    @RequiresPermissions("prdreport:prd_report:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdReport prdReport) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdReport> queryWrapper = QueryGenerator.initQueryWrapper(prdReport, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PrdReport> prdReportList = prdReportService.list(queryWrapper);

      // Step.3 组装pageList
      List<PrdReportPage> pageList = new ArrayList<PrdReportPage>();
      for (PrdReport main : prdReportList) {
          PrdReportPage vo = new PrdReportPage();
          BeanUtils.copyProperties(main, vo);
          List<PrdReportDetail> prdReportDetailList = prdReportDetailService.selectByMainId(main.getId());
          vo.setPrdReportDetailList(prdReportDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "生产报工列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdReportPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产报工数据", "导出人:"+sysUser.getRealname(), "生产报工"));
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
    @RequiresPermissions("prdreport:prd_report:importExcel")
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
              List<PrdReportPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdReportPage.class, params);
              for (PrdReportPage page : list) {
                  PrdReport po = new PrdReport();
                  BeanUtils.copyProperties(page, po);
                  prdReportService.saveMain(po, page.getPrdReportDetailList());
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
