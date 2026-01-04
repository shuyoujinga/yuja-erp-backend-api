package org.jeecg.modules.sal.salbizplan.controller;

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
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanBomDetail;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlan;
import org.jeecg.modules.sal.salbizplan.vo.SalBizPlanPage;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanService;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanDetailService;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanBomDetailService;
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
 * @Description: 业务计划
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="业务计划")
@RestController
@RequestMapping("/salbizplan/salBizPlan")
@Slf4j
public class SalBizPlanController {
	@Autowired
	private ISalBizPlanService salBizPlanService;
	@Autowired
	private ISalBizPlanDetailService salBizPlanDetailService;
	@Autowired
	private ISalBizPlanBomDetailService salBizPlanBomDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salBizPlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "业务计划-分页列表查询")
	@ApiOperation(value="业务计划-分页列表查询", notes="业务计划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalBizPlan>> queryPageList(SalBizPlan salBizPlan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalBizPlan> queryWrapper = QueryGenerator.initQueryWrapper(salBizPlan, req.getParameterMap());
		Page<SalBizPlan> page = new Page<SalBizPlan>(pageNo, pageSize);
		IPage<SalBizPlan> pageList = salBizPlanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salBizPlanPage
	 * @return
	 */
	@AutoLog(value = "业务计划-添加")
	@ApiOperation(value="业务计划-添加", notes="业务计划-添加")
    @RequiresPermissions("salbizplan:sal_biz_plan:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalBizPlanPage salBizPlanPage) {
		Assert.isTrue(CollectionUtil.isEmpty(salBizPlanPage.getSalBizPlanBomDetailList()),"材料清单为空,不能提交数据!");
		Assert.isTrue(CollectionUtil.isEmpty(salBizPlanPage.getSalBizPlanDetailList()),"业务计划_明细为空,不能提交数据!");
		SalBizPlan salBizPlan = new SalBizPlan();
		BeanUtils.copyProperties(salBizPlanPage, salBizPlan);
		salBizPlanService.saveMain(salBizPlan, salBizPlanPage.getSalBizPlanDetailList(),salBizPlanPage.getSalBizPlanBomDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salBizPlanPage
	 * @return
	 */
	@AutoLog(value = "业务计划-编辑")
	@ApiOperation(value="业务计划-编辑", notes="业务计划-编辑")
    @RequiresPermissions("salbizplan:sal_biz_plan:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalBizPlanPage salBizPlanPage) {
		Assert.isTrue(CollectionUtil.isEmpty(salBizPlanPage.getSalBizPlanBomDetailList()),"材料清单为空,不能提交数据!");
		Assert.isTrue(CollectionUtil.isEmpty(salBizPlanPage.getSalBizPlanDetailList()),"业务计划_明细为空,不能提交数据!");
		SalBizPlan salBizPlan = new SalBizPlan();
		BeanUtils.copyProperties(salBizPlanPage, salBizPlan);
		SalBizPlan salBizPlanEntity = salBizPlanService.getById(salBizPlan.getId());
		if(salBizPlanEntity==null) {
			return Result.error("未找到对应数据");
		}
		salBizPlanService.updateMain(salBizPlan, salBizPlanPage.getSalBizPlanDetailList(),salBizPlanPage.getSalBizPlanBomDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业务计划-通过id删除")
	@ApiOperation(value="业务计划-通过id删除", notes="业务计划-通过id删除")
    @RequiresPermissions("salbizplan:sal_biz_plan:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salBizPlanService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业务计划-批量删除")
	@ApiOperation(value="业务计划-批量删除", notes="业务计划-批量删除")
    @RequiresPermissions("salbizplan:sal_biz_plan:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salBizPlanService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业务计划-通过id查询")
	@ApiOperation(value="业务计划-通过id查询", notes="业务计划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalBizPlan> queryById(@RequestParam(name="id",required=true) String id) {
		SalBizPlan salBizPlan = salBizPlanService.getById(id);
		if(salBizPlan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salBizPlan);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业务计划_明细通过主表ID查询")
	@ApiOperation(value="业务计划_明细主表ID查询", notes="业务计划_明细-通主表ID查询")
	@GetMapping(value = "/querySalBizPlanDetailByMainId")
	public Result<List<SalBizPlanDetail>> querySalBizPlanDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalBizPlanDetail> salBizPlanDetailList = salBizPlanDetailService.selectByMainId(id);
		return Result.OK(salBizPlanDetailList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业务计划_材料明细通过主表ID查询")
	@ApiOperation(value="业务计划_材料明细主表ID查询", notes="业务计划_材料明细-通主表ID查询")
	@GetMapping(value = "/querySalBizPlanBomDetailByMainId")
	public Result<List<SalBizPlanBomDetail>> querySalBizPlanBomDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalBizPlanBomDetail> salBizPlanBomDetailList = salBizPlanBomDetailService.selectByMainId(id);
		return Result.OK(salBizPlanBomDetailList);
	}
	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @ApiOperation(value="通销售订单ID查询", notes="业务计划_明细-通销售订单ID查询")
	 @GetMapping(value = "/querySalBizPlanDetailByTargetId")
	 public Result<List<SalBizPlanDetail>> querySalBizPlanDetailListByTargetId(@RequestParam(name="id",required=true) String id) {
		 List<SalBizPlanDetail> salBizPlanDetailList = salBizPlanDetailService.selectByTargetId(id);
		 return Result.OK(salBizPlanDetailList);
	 }
	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @ApiOperation(value="业务计划_材料明细通销售订单ID查询", notes="业务计划_材料明细-通销售订单ID查询")
	 @GetMapping(value = "/querySalBizPlanBomDetailByTargetId")
	 public Result<List<SalBizPlanBomDetail>> querySalBizPlanBomDetailListByTargetId(@RequestParam(name="id",required=true) String id) {
		 List<SalBizPlanBomDetail> salBizPlanBomDetailList = salBizPlanBomDetailService.selectByTargetId(id);
		 return Result.OK(salBizPlanBomDetailList);
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param salBizPlan
    */
    @RequiresPermissions("salbizplan:sal_biz_plan:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalBizPlan salBizPlan) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalBizPlan> queryWrapper = QueryGenerator.initQueryWrapper(salBizPlan, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalBizPlan> salBizPlanList = salBizPlanService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalBizPlanPage> pageList = new ArrayList<SalBizPlanPage>();
      for (SalBizPlan main : salBizPlanList) {
          SalBizPlanPage vo = new SalBizPlanPage();
          BeanUtils.copyProperties(main, vo);
          List<SalBizPlanDetail> salBizPlanDetailList = salBizPlanDetailService.selectByMainId(main.getId());
          vo.setSalBizPlanDetailList(salBizPlanDetailList);
          List<SalBizPlanBomDetail> salBizPlanBomDetailList = salBizPlanBomDetailService.selectByMainId(main.getId());
          vo.setSalBizPlanBomDetailList(salBizPlanBomDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "业务计划列表");
      mv.addObject(NormalExcelConstants.CLASS, SalBizPlanPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("业务计划数据", "导出人:"+sysUser.getRealname(), "业务计划"));
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
    @RequiresPermissions("salbizplan:sal_biz_plan:importExcel")
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
              List<SalBizPlanPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalBizPlanPage.class, params);
              for (SalBizPlanPage page : list) {
                  SalBizPlan po = new SalBizPlan();
                  BeanUtils.copyProperties(page, po);
                  salBizPlanService.saveMain(po, page.getSalBizPlanDetailList(),page.getSalBizPlanBomDetailList());
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
