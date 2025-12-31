package org.jeecg.modules.sal.salorder.controller;

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
import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.entity.SalOrder;
import org.jeecg.modules.sal.salorder.vo.SalOrderPage;
import org.jeecg.modules.sal.salorder.service.ISalOrderService;
import org.jeecg.modules.sal.salorder.service.ISalOrderDetailService;
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
 * @Description: 销售订单
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Api(tags="销售订单")
@RestController
@RequestMapping("/salorder/salOrder")
@Slf4j
public class SalOrderController {
	@Autowired
	private ISalOrderService salOrderService;
	@Autowired
	private ISalOrderDetailService salOrderDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售订单-分页列表查询")
	@ApiOperation(value="销售订单-分页列表查询", notes="销售订单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalOrder>> queryPageList(SalOrder salOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalOrder> queryWrapper = QueryGenerator.initQueryWrapper(salOrder, req.getParameterMap());
		Page<SalOrder> page = new Page<SalOrder>(pageNo, pageSize);
		IPage<SalOrder> pageList = salOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salOrderPage
	 * @return
	 */
	@AutoLog(value = "销售订单-添加")
	@ApiOperation(value="销售订单-添加", notes="销售订单-添加")
    @RequiresPermissions("salorder:sal_order:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalOrderPage salOrderPage) {
		SalOrder salOrder = new SalOrder();
		BeanUtils.copyProperties(salOrderPage, salOrder);
		salOrderService.saveMain(salOrder, salOrderPage.getSalOrderDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salOrderPage
	 * @return
	 */
	@AutoLog(value = "销售订单-编辑")
	@ApiOperation(value="销售订单-编辑", notes="销售订单-编辑")
    @RequiresPermissions("salorder:sal_order:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalOrderPage salOrderPage) {
		SalOrder salOrder = new SalOrder();
		BeanUtils.copyProperties(salOrderPage, salOrder);
		SalOrder salOrderEntity = salOrderService.getById(salOrder.getId());
		if(salOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		salOrderService.updateMain(salOrder, salOrderPage.getSalOrderDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售订单-通过id删除")
	@ApiOperation(value="销售订单-通过id删除", notes="销售订单-通过id删除")
    @RequiresPermissions("salorder:sal_order:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salOrderService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售订单-批量删除")
	@ApiOperation(value="销售订单-批量删除", notes="销售订单-批量删除")
    @RequiresPermissions("salorder:sal_order:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	@DeleteCheckAudit(service = ISalOrderService.class,entity = SalOrder.class)
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售订单-通过id查询")
	@ApiOperation(value="销售订单-通过id查询", notes="销售订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalOrder> queryById(@RequestParam(name="id",required=true) String id) {
		SalOrder salOrder = salOrderService.getById(id);
		if(salOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salOrder);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售订单_明细通过主表ID查询")
	@ApiOperation(value="销售订单_明细主表ID查询", notes="销售订单_明细-通主表ID查询")
	@GetMapping(value = "/querySalOrderDetailByMainId")
	public Result<List<SalOrderDetail>> querySalOrderDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalOrderDetail> salOrderDetailList = salOrderDetailService.selectByMainId(id);
		return Result.OK(salOrderDetailList);
	}
	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "销售订单_明细通过主表ID查询")
	 @ApiOperation(value="销售订单_特定明细主表ID查询", notes="销售订单_明细-通主表ID查询")
	 @GetMapping(value = "/querySalOrderDetailByTargetId")
	 public Result<List<SalOrderDetail>> querySalOrderDetailByTargetId(@RequestParam(name="id",required=true) String id) {
		 List<SalOrderDetail> salOrderDetailList = salOrderDetailService.selectByTargetId(id);
		 return Result.OK(salOrderDetailList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param salOrder
    */
    @RequiresPermissions("salorder:sal_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalOrder salOrder) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalOrder> queryWrapper = QueryGenerator.initQueryWrapper(salOrder, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalOrder> salOrderList = salOrderService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalOrderPage> pageList = new ArrayList<SalOrderPage>();
      for (SalOrder main : salOrderList) {
          SalOrderPage vo = new SalOrderPage();
          BeanUtils.copyProperties(main, vo);
          List<SalOrderDetail> salOrderDetailList = salOrderDetailService.selectByMainId(main.getId());
          vo.setSalOrderDetailList(salOrderDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售订单列表");
      mv.addObject(NormalExcelConstants.CLASS, SalOrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售订单数据", "导出人:"+sysUser.getRealname(), "销售订单"));
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
    @RequiresPermissions("salorder:sal_order:importExcel")
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
              List<SalOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalOrderPage.class, params);
              for (SalOrderPage page : list) {
                  SalOrder po = new SalOrder();
                  BeanUtils.copyProperties(page, po);
                  salOrderService.saveMain(po, page.getSalOrderDetailList());
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
	 @AutoLog(value = "销售报价-审核/反审核")
	 @ApiOperation(value = "销售报价-审核/反审核", notes = "销售报价-审核/反审核")
	 @RequiresPermissions("salorder:sal_order:audit")
	 @RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	 public Result<String> audit(@RequestBody AuditRequest auditRequest) throws Exception {
		 List<String> ids = auditRequest.getIds();
		 String type = auditRequest.getType(); // audit 或 reverse

		 Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");


		 int count;
		 if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			 count = salOrderService.audit(ids);
		 } else {
			 count = salOrderService.unAudit(ids);
		 }


		 return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	 }
}
