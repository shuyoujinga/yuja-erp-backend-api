package org.jeecg.modules.prd.prdworkorder.controller;

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
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import org.jeecg.modules.prd.prdworkorder.vo.PrdWorkOrderPage;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderService;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderDetailService;
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
 * @Description: 生产工单
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Api(tags="生产工单")
@RestController
@RequestMapping("/prdworkorder/prdWorkOrder")
@Slf4j
public class PrdWorkOrderController {
	@Autowired
	private IPrdWorkOrderService prdWorkOrderService;
	@Autowired
	private IPrdWorkOrderDetailService prdWorkOrderDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdWorkOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生产工单-分页列表查询")
	@ApiOperation(value="生产工单-分页列表查询", notes="生产工单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PrdWorkOrder>> queryPageList(PrdWorkOrder prdWorkOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdWorkOrder> queryWrapper = QueryGenerator.initQueryWrapper(prdWorkOrder, req.getParameterMap());
		Page<PrdWorkOrder> page = new Page<PrdWorkOrder>(pageNo, pageSize);
		IPage<PrdWorkOrder> pageList = prdWorkOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdWorkOrderPage
	 * @return
	 */
	@AutoLog(value = "生产工单-添加")
	@ApiOperation(value="生产工单-添加", notes="生产工单-添加")
    @RequiresPermissions("prdworkorder:prd_work_order:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PrdWorkOrderPage prdWorkOrderPage) {
		PrdWorkOrder prdWorkOrder = new PrdWorkOrder();
		BeanUtils.copyProperties(prdWorkOrderPage, prdWorkOrder);
		prdWorkOrderService.saveMain(prdWorkOrder, prdWorkOrderPage.getPrdWorkOrderDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdWorkOrderPage
	 * @return
	 */
	@AutoLog(value = "生产工单-编辑")
	@ApiOperation(value="生产工单-编辑", notes="生产工单-编辑")
    @RequiresPermissions("prdworkorder:prd_work_order:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PrdWorkOrderPage prdWorkOrderPage) {
		PrdWorkOrder prdWorkOrder = new PrdWorkOrder();
		BeanUtils.copyProperties(prdWorkOrderPage, prdWorkOrder);
		PrdWorkOrder prdWorkOrderEntity = prdWorkOrderService.getById(prdWorkOrder.getId());
		if(prdWorkOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdWorkOrderService.updateMain(prdWorkOrder, prdWorkOrderPage.getPrdWorkOrderDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生产工单-通过id删除")
	@ApiOperation(value="生产工单-通过id删除", notes="生产工单-通过id删除")
    @RequiresPermissions("prdworkorder:prd_work_order:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		prdWorkOrderService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生产工单-批量删除")
	@ApiOperation(value="生产工单-批量删除", notes="生产工单-批量删除")
    @RequiresPermissions("prdworkorder:prd_work_order:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdWorkOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产工单-通过id查询")
	@ApiOperation(value="生产工单-通过id查询", notes="生产工单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PrdWorkOrder> queryById(@RequestParam(name="id",required=true) String id) {
		PrdWorkOrder prdWorkOrder = prdWorkOrderService.getById(id);
		if(prdWorkOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(prdWorkOrder);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产工单_物料明细通过主表ID查询")
	@ApiOperation(value="生产工单_物料明细主表ID查询", notes="生产工单_物料明细-通主表ID查询")
	@GetMapping(value = "/queryPrdWorkOrderDetailByMainId")
	public Result<List<PrdWorkOrderDetail>> queryPrdWorkOrderDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdWorkOrderDetail> prdWorkOrderDetailList = prdWorkOrderDetailService.selectByMainId(id);
		return Result.OK(prdWorkOrderDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdWorkOrder
    */
    @RequiresPermissions("prdworkorder:prd_work_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdWorkOrder prdWorkOrder) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdWorkOrder> queryWrapper = QueryGenerator.initQueryWrapper(prdWorkOrder, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PrdWorkOrder> prdWorkOrderList = prdWorkOrderService.list(queryWrapper);

      // Step.3 组装pageList
      List<PrdWorkOrderPage> pageList = new ArrayList<PrdWorkOrderPage>();
      for (PrdWorkOrder main : prdWorkOrderList) {
          PrdWorkOrderPage vo = new PrdWorkOrderPage();
          BeanUtils.copyProperties(main, vo);
          List<PrdWorkOrderDetail> prdWorkOrderDetailList = prdWorkOrderDetailService.selectByMainId(main.getId());
          vo.setPrdWorkOrderDetailList(prdWorkOrderDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "生产工单列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdWorkOrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产工单数据", "导出人:"+sysUser.getRealname(), "生产工单"));
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
    @RequiresPermissions("prdworkorder:prd_work_order:importExcel")
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
              List<PrdWorkOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdWorkOrderPage.class, params);
              for (PrdWorkOrderPage page : list) {
                  PrdWorkOrder po = new PrdWorkOrder();
                  BeanUtils.copyProperties(page, po);
                  prdWorkOrderService.saveMain(po, page.getPrdWorkOrderDetailList());
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
