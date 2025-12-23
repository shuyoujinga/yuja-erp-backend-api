package org.jeecg.modules.sal.salreceipt.controller;

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
import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import org.jeecg.modules.sal.salreceipt.entity.SalReceipt;
import org.jeecg.modules.sal.salreceipt.vo.SalReceiptPage;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptService;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptDetailService;
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
 * @Description: 销售收款
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="销售收款")
@RestController
@RequestMapping("/salreceipt/salReceipt")
@Slf4j
public class SalReceiptController {
	@Autowired
	private ISalReceiptService salReceiptService;
	@Autowired
	private ISalReceiptDetailService salReceiptDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salReceipt
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售收款-分页列表查询")
	@ApiOperation(value="销售收款-分页列表查询", notes="销售收款-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalReceipt>> queryPageList(SalReceipt salReceipt,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalReceipt> queryWrapper = QueryGenerator.initQueryWrapper(salReceipt, req.getParameterMap());
		Page<SalReceipt> page = new Page<SalReceipt>(pageNo, pageSize);
		IPage<SalReceipt> pageList = salReceiptService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salReceiptPage
	 * @return
	 */
	@AutoLog(value = "销售收款-添加")
	@ApiOperation(value="销售收款-添加", notes="销售收款-添加")
    @RequiresPermissions("salreceipt:sal_receipt:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalReceiptPage salReceiptPage) {
		SalReceipt salReceipt = new SalReceipt();
		BeanUtils.copyProperties(salReceiptPage, salReceipt);
		salReceiptService.saveMain(salReceipt, salReceiptPage.getSalReceiptDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salReceiptPage
	 * @return
	 */
	@AutoLog(value = "销售收款-编辑")
	@ApiOperation(value="销售收款-编辑", notes="销售收款-编辑")
    @RequiresPermissions("salreceipt:sal_receipt:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalReceiptPage salReceiptPage) {
		SalReceipt salReceipt = new SalReceipt();
		BeanUtils.copyProperties(salReceiptPage, salReceipt);
		SalReceipt salReceiptEntity = salReceiptService.getById(salReceipt.getId());
		if(salReceiptEntity==null) {
			return Result.error("未找到对应数据");
		}
		salReceiptService.updateMain(salReceipt, salReceiptPage.getSalReceiptDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售收款-通过id删除")
	@ApiOperation(value="销售收款-通过id删除", notes="销售收款-通过id删除")
    @RequiresPermissions("salreceipt:sal_receipt:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salReceiptService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售收款-批量删除")
	@ApiOperation(value="销售收款-批量删除", notes="销售收款-批量删除")
    @RequiresPermissions("salreceipt:sal_receipt:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salReceiptService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售收款-通过id查询")
	@ApiOperation(value="销售收款-通过id查询", notes="销售收款-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalReceipt> queryById(@RequestParam(name="id",required=true) String id) {
		SalReceipt salReceipt = salReceiptService.getById(id);
		if(salReceipt==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salReceipt);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售收款_明细通过主表ID查询")
	@ApiOperation(value="销售收款_明细主表ID查询", notes="销售收款_明细-通主表ID查询")
	@GetMapping(value = "/querySalReceiptDetailByMainId")
	public Result<List<SalReceiptDetail>> querySalReceiptDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalReceiptDetail> salReceiptDetailList = salReceiptDetailService.selectByMainId(id);
		return Result.OK(salReceiptDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param salReceipt
    */
    @RequiresPermissions("salreceipt:sal_receipt:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalReceipt salReceipt) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalReceipt> queryWrapper = QueryGenerator.initQueryWrapper(salReceipt, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalReceipt> salReceiptList = salReceiptService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalReceiptPage> pageList = new ArrayList<SalReceiptPage>();
      for (SalReceipt main : salReceiptList) {
          SalReceiptPage vo = new SalReceiptPage();
          BeanUtils.copyProperties(main, vo);
          List<SalReceiptDetail> salReceiptDetailList = salReceiptDetailService.selectByMainId(main.getId());
          vo.setSalReceiptDetailList(salReceiptDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售收款列表");
      mv.addObject(NormalExcelConstants.CLASS, SalReceiptPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售收款数据", "导出人:"+sysUser.getRealname(), "销售收款"));
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
    @RequiresPermissions("salreceipt:sal_receipt:importExcel")
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
              List<SalReceiptPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalReceiptPage.class, params);
              for (SalReceiptPage page : list) {
                  SalReceipt po = new SalReceipt();
                  BeanUtils.copyProperties(page, po);
                  salReceiptService.saveMain(po, page.getSalReceiptDetailList());
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
