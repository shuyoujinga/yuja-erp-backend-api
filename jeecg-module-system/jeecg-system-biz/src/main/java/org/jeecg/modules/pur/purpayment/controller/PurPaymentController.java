package org.jeecg.modules.pur.purpayment.controller;

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
import org.jeecg.modules.aop.DeleteCheckAudit;
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
import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import org.jeecg.modules.pur.purpayment.entity.PurPayment;
import org.jeecg.modules.pur.purpayment.vo.PurPaymentPage;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentService;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentDetailService;
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
 * @Description: 采购付款
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Api(tags="采购付款")
@RestController
@RequestMapping("/purpayment/purPayment")
@Slf4j
public class PurPaymentController {
	@Autowired
	private IPurPaymentService purPaymentService;
	@Autowired
	private IPurPaymentDetailService purPaymentDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purPayment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购付款-分页列表查询")
	@ApiOperation(value="采购付款-分页列表查询", notes="采购付款-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurPayment>> queryPageList(PurPayment purPayment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurPayment> queryWrapper = QueryGenerator.initQueryWrapper(purPayment, req.getParameterMap());
		Page<PurPayment> page = new Page<PurPayment>(pageNo, pageSize);
		IPage<PurPayment> pageList = purPaymentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param purPaymentPage
	 * @return
	 */
	@AutoLog(value = "采购付款-添加")
	@ApiOperation(value="采购付款-添加", notes="采购付款-添加")
    @RequiresPermissions("purpayment:pur_payment:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurPaymentPage purPaymentPage) {
		PurPayment purPayment = new PurPayment();
		BeanUtils.copyProperties(purPaymentPage, purPayment);
		purPaymentService.saveMain(purPayment, purPaymentPage.getPurPaymentDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purPaymentPage
	 * @return
	 */
	@AutoLog(value = "采购付款-编辑")
	@ApiOperation(value="采购付款-编辑", notes="采购付款-编辑")
    @RequiresPermissions("purpayment:pur_payment:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurPaymentPage purPaymentPage) {
		PurPayment purPayment = new PurPayment();
		BeanUtils.copyProperties(purPaymentPage, purPayment);
		PurPayment purPaymentEntity = purPaymentService.getById(purPayment.getId());
		if(purPaymentEntity==null) {
			return Result.error("未找到对应数据");
		}
		purPaymentService.updateMain(purPayment, purPaymentPage.getPurPaymentDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购付款-通过id删除")
	@ApiOperation(value="采购付款-通过id删除", notes="采购付款-通过id删除")
    @RequiresPermissions("purpayment:pur_payment:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purPaymentService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购付款-批量删除")
	@ApiOperation(value="采购付款-批量删除", notes="采购付款-批量删除")
    @RequiresPermissions("purpayment:pur_payment:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	@DeleteCheckAudit(service = IPurPaymentService.class,entity = PurPayment.class)
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purPaymentService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购付款-通过id查询")
	@ApiOperation(value="采购付款-通过id查询", notes="采购付款-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurPayment> queryById(@RequestParam(name="id",required=true) String id) {
		PurPayment purPayment = purPaymentService.getById(id);
		if(purPayment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purPayment);

	}
	/**
	 * 审核/反审核
	 *
	 * @param auditRequest 审核请求参数，包含ID列表和操作类型（audit/reverse）
	 * @return
	 */
	@AutoLog(value = "采购付款-审核/反审核")
	@ApiOperation(value = "采购付款--审核/反审核", notes = "采购付款--审核/反审核")
	@RequiresPermissions("purpayment:pur_payment:audit")
	@RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result<String> audit(@RequestBody AuditRequest auditRequest) {
		List<String> ids = auditRequest.getIds();
		String type = auditRequest.getType(); // audit 或 reverse

		Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

		int count;
		if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			count = purPaymentService.audit(ids);
		} else {
			count = purPaymentService.unAudit(ids);
		}


		return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购付款_明细通过主表ID查询")
	@ApiOperation(value="采购付款_明细主表ID查询", notes="采购付款_明细-通主表ID查询")
	@GetMapping(value = "/queryPurPaymentDetailByMainId")
	public Result<List<PurPaymentDetail>> queryPurPaymentDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurPaymentDetail> purPaymentDetailList = purPaymentDetailService.selectByMainId(id);
		return Result.OK(purPaymentDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param purPayment
    */
    @RequiresPermissions("purpayment:pur_payment:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurPayment purPayment) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PurPayment> queryWrapper = QueryGenerator.initQueryWrapper(purPayment, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PurPayment> purPaymentList = purPaymentService.list(queryWrapper);

      // Step.3 组装pageList
      List<PurPaymentPage> pageList = new ArrayList<PurPaymentPage>();
      for (PurPayment main : purPaymentList) {
          PurPaymentPage vo = new PurPaymentPage();
          BeanUtils.copyProperties(main, vo);
          List<PurPaymentDetail> purPaymentDetailList = purPaymentDetailService.selectByMainId(main.getId());
          vo.setPurPaymentDetailList(purPaymentDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购付款列表");
      mv.addObject(NormalExcelConstants.CLASS, PurPaymentPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购付款数据", "导出人:"+sysUser.getRealname(), "采购付款"));
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
    @RequiresPermissions("purpayment:pur_payment:importExcel")
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
              List<PurPaymentPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurPaymentPage.class, params);
              for (PurPaymentPage page : list) {
                  PurPayment po = new PurPayment();
                  BeanUtils.copyProperties(page, po);
                  purPaymentService.saveMain(po, page.getPurPaymentDetailList());
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
