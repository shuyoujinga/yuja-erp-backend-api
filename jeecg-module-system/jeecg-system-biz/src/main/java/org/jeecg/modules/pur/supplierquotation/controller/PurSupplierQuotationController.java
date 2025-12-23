package org.jeecg.modules.pur.supplierquotation.controller;

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

import cn.hutool.core.collection.CollectionUtil;
import org.constant.Constants;
import org.jeecg.modules.maindata.bom.vo.AuditRequest;
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
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotation;
import org.jeecg.modules.pur.supplierquotation.vo.PurSupplierQuotationPage;
import org.jeecg.modules.pur.supplierquotation.service.IPurSupplierQuotationService;
import org.jeecg.modules.pur.supplierquotation.service.IPurSupplierQuotationDetailService;
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
import org.utils.Assert;


/**
 * @Description: 采购报价
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Api(tags="采购报价")
@RestController
@RequestMapping("/supplierquotation/purSupplierQuotation")
@Slf4j
public class PurSupplierQuotationController {
	@Autowired
	private IPurSupplierQuotationService purSupplierQuotationService;
	@Autowired
	private IPurSupplierQuotationDetailService purSupplierQuotationDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purSupplierQuotation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购报价-分页列表查询")
	@ApiOperation(value="采购报价-分页列表查询", notes="采购报价-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurSupplierQuotation>> queryPageList(PurSupplierQuotation purSupplierQuotation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurSupplierQuotation> queryWrapper = QueryGenerator.initQueryWrapper(purSupplierQuotation, req.getParameterMap());
		Page<PurSupplierQuotation> page = new Page<PurSupplierQuotation>(pageNo, pageSize);
		IPage<PurSupplierQuotation> pageList = purSupplierQuotationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param purSupplierQuotationPage
	 * @return
	 */
	@AutoLog(value = "采购报价-添加")
	@ApiOperation(value="采购报价-添加", notes="采购报价-添加")
    @RequiresPermissions("supplierquotation:pur_supplier_quotation:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurSupplierQuotationPage purSupplierQuotationPage) {
		PurSupplierQuotation purSupplierQuotation = new PurSupplierQuotation();
		BeanUtils.copyProperties(purSupplierQuotationPage, purSupplierQuotation);
		purSupplierQuotationService.saveMain(purSupplierQuotation, purSupplierQuotationPage.getPurSupplierQuotationDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purSupplierQuotationPage
	 * @return
	 */
	@AutoLog(value = "采购报价-编辑")
	@ApiOperation(value="采购报价-编辑", notes="采购报价-编辑")
    @RequiresPermissions("supplierquotation:pur_supplier_quotation:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurSupplierQuotationPage purSupplierQuotationPage) {
		PurSupplierQuotation purSupplierQuotation = new PurSupplierQuotation();
		BeanUtils.copyProperties(purSupplierQuotationPage, purSupplierQuotation);
		PurSupplierQuotation purSupplierQuotationEntity = purSupplierQuotationService.getById(purSupplierQuotation.getId());
		if(purSupplierQuotationEntity==null) {
			return Result.error("未找到对应数据");
		}
		purSupplierQuotationService.updateMain(purSupplierQuotation, purSupplierQuotationPage.getPurSupplierQuotationDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购报价-通过id删除")
	@ApiOperation(value="采购报价-通过id删除", notes="采购报价-通过id删除")
    @RequiresPermissions("supplierquotation:pur_supplier_quotation:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purSupplierQuotationService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购报价-批量删除")
	@ApiOperation(value="采购报价-批量删除", notes="采购报价-批量删除")
    @RequiresPermissions("supplierquotation:pur_supplier_quotation:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purSupplierQuotationService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购报价-通过id查询")
	@ApiOperation(value="采购报价-通过id查询", notes="采购报价-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurSupplierQuotation> queryById(@RequestParam(name="id",required=true) String id) {
		PurSupplierQuotation purSupplierQuotation = purSupplierQuotationService.getById(id);
		if(purSupplierQuotation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purSupplierQuotation);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购报价_明细通过主表ID查询")
	@ApiOperation(value="采购报价_明细主表ID查询", notes="采购报价_明细-通主表ID查询")
	@GetMapping(value = "/queryPurSupplierQuotationDetailByMainId")
	public Result<List<PurSupplierQuotationDetail>> queryPurSupplierQuotationDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurSupplierQuotationDetail> purSupplierQuotationDetailList = purSupplierQuotationDetailService.selectByMainId(id);
		return Result.OK(purSupplierQuotationDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param purSupplierQuotation
    */
    @RequiresPermissions("supplierquotation:pur_supplier_quotation:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurSupplierQuotation purSupplierQuotation) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PurSupplierQuotation> queryWrapper = QueryGenerator.initQueryWrapper(purSupplierQuotation, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PurSupplierQuotation> purSupplierQuotationList = purSupplierQuotationService.list(queryWrapper);

      // Step.3 组装pageList
      List<PurSupplierQuotationPage> pageList = new ArrayList<PurSupplierQuotationPage>();
      for (PurSupplierQuotation main : purSupplierQuotationList) {
          PurSupplierQuotationPage vo = new PurSupplierQuotationPage();
          BeanUtils.copyProperties(main, vo);
          List<PurSupplierQuotationDetail> purSupplierQuotationDetailList = purSupplierQuotationDetailService.selectByMainId(main.getId());
          vo.setPurSupplierQuotationDetailList(purSupplierQuotationDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购报价列表");
      mv.addObject(NormalExcelConstants.CLASS, PurSupplierQuotationPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购报价数据", "导出人:"+sysUser.getRealname(), "采购报价"));
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
    @RequiresPermissions("supplierquotation:pur_supplier_quotation:importExcel")
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
              List<PurSupplierQuotationPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurSupplierQuotationPage.class, params);
              for (PurSupplierQuotationPage page : list) {
                  PurSupplierQuotation po = new PurSupplierQuotation();
                  BeanUtils.copyProperties(page, po);
                  purSupplierQuotationService.saveMain(po, page.getPurSupplierQuotationDetailList());
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
	/**
	 * 审核/反审核
	 *
	 * @param auditRequest 审核请求参数，包含ID列表和操作类型（audit/reverse）
	 * @return
	 */
	@AutoLog(value = "采购报价-审核/反审核")
	@ApiOperation(value = "采购报价-审核/反审核", notes = "采购报价-审核/反审核")
	@RequiresPermissions("supplierquotation:pur_supplier_quotation:audit")
	@RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result<String> audit(@RequestBody AuditRequest auditRequest) {
		List<String> ids = auditRequest.getIds();
		String type = auditRequest.getType(); // audit 或 reverse

		Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

		int count;
		if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			count= purSupplierQuotationService.audit(ids);
		} else {
			count=purSupplierQuotationService.unAudit(ids);
		}


		return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！",count));
	}

}
