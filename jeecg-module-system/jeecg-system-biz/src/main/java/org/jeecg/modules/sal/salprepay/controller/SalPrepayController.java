package org.jeecg.modules.sal.salprepay.controller;

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
import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import org.jeecg.modules.sal.salprepay.entity.SalPrepay;
import org.jeecg.modules.sal.salprepay.vo.SalPrepayPage;
import org.jeecg.modules.sal.salprepay.service.ISalPrepayService;
import org.jeecg.modules.sal.salprepay.service.ISalPrepayDetailService;
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
 * @Description: 销售预收
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="销售预收")
@RestController
@RequestMapping("/salprepay/salPrepay")
@Slf4j
public class SalPrepayController {
	@Autowired
	private ISalPrepayService salPrepayService;
	@Autowired
	private ISalPrepayDetailService salPrepayDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salPrepay
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售预收-分页列表查询")
	@ApiOperation(value="销售预收-分页列表查询", notes="销售预收-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalPrepay>> queryPageList(SalPrepay salPrepay,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalPrepay> queryWrapper = QueryGenerator.initQueryWrapper(salPrepay, req.getParameterMap());
		Page<SalPrepay> page = new Page<SalPrepay>(pageNo, pageSize);
		IPage<SalPrepay> pageList = salPrepayService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salPrepayPage
	 * @return
	 */
	@AutoLog(value = "销售预收-添加")
	@ApiOperation(value="销售预收-添加", notes="销售预收-添加")
    @RequiresPermissions("salprepay:sal_prepay:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalPrepayPage salPrepayPage) {
		SalPrepay salPrepay = new SalPrepay();
		BeanUtils.copyProperties(salPrepayPage, salPrepay);
		salPrepayService.saveMain(salPrepay, salPrepayPage.getSalPrepayDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salPrepayPage
	 * @return
	 */
	@AutoLog(value = "销售预收-编辑")
	@ApiOperation(value="销售预收-编辑", notes="销售预收-编辑")
    @RequiresPermissions("salprepay:sal_prepay:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalPrepayPage salPrepayPage) {
		SalPrepay salPrepay = new SalPrepay();
		BeanUtils.copyProperties(salPrepayPage, salPrepay);
		SalPrepay salPrepayEntity = salPrepayService.getById(salPrepay.getId());
		if(salPrepayEntity==null) {
			return Result.error("未找到对应数据");
		}
		salPrepayService.updateMain(salPrepay, salPrepayPage.getSalPrepayDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售预收-通过id删除")
	@ApiOperation(value="销售预收-通过id删除", notes="销售预收-通过id删除")
    @RequiresPermissions("salprepay:sal_prepay:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salPrepayService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售预收-批量删除")
	@ApiOperation(value="销售预收-批量删除", notes="销售预收-批量删除")
    @RequiresPermissions("salprepay:sal_prepay:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salPrepayService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售预收-通过id查询")
	@ApiOperation(value="销售预收-通过id查询", notes="销售预收-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalPrepay> queryById(@RequestParam(name="id",required=true) String id) {
		SalPrepay salPrepay = salPrepayService.getById(id);
		if(salPrepay==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salPrepay);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "预收使用_明细通过主表ID查询")
	@ApiOperation(value="预收使用_明细主表ID查询", notes="预收使用_明细-通主表ID查询")
	@GetMapping(value = "/querySalPrepayDetailByMainId")
	public Result<List<SalPrepayDetail>> querySalPrepayDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalPrepayDetail> salPrepayDetailList = salPrepayDetailService.selectByMainId(id);
		return Result.OK(salPrepayDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param salPrepay
    */
    @RequiresPermissions("salprepay:sal_prepay:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalPrepay salPrepay) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalPrepay> queryWrapper = QueryGenerator.initQueryWrapper(salPrepay, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalPrepay> salPrepayList = salPrepayService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalPrepayPage> pageList = new ArrayList<SalPrepayPage>();
      for (SalPrepay main : salPrepayList) {
          SalPrepayPage vo = new SalPrepayPage();
          BeanUtils.copyProperties(main, vo);
          List<SalPrepayDetail> salPrepayDetailList = salPrepayDetailService.selectByMainId(main.getId());
          vo.setSalPrepayDetailList(salPrepayDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售预收列表");
      mv.addObject(NormalExcelConstants.CLASS, SalPrepayPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售预收数据", "导出人:"+sysUser.getRealname(), "销售预收"));
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
    @RequiresPermissions("salprepay:sal_prepay:importExcel")
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
              List<SalPrepayPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalPrepayPage.class, params);
              for (SalPrepayPage page : list) {
                  SalPrepay po = new SalPrepay();
                  BeanUtils.copyProperties(page, po);
                  salPrepayService.saveMain(po, page.getSalPrepayDetailList());
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
