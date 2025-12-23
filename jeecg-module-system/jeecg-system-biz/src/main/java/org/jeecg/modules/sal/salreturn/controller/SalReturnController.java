package org.jeecg.modules.sal.salreturn.controller;

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
import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import org.jeecg.modules.sal.salreturn.entity.SalReturn;
import org.jeecg.modules.sal.salreturn.vo.SalReturnPage;
import org.jeecg.modules.sal.salreturn.service.ISalReturnService;
import org.jeecg.modules.sal.salreturn.service.ISalReturnDetailService;
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
 * @Description: 销售退货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="销售退货")
@RestController
@RequestMapping("/salreturn/salReturn")
@Slf4j
public class SalReturnController {
	@Autowired
	private ISalReturnService salReturnService;
	@Autowired
	private ISalReturnDetailService salReturnDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param salReturn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售退货-分页列表查询")
	@ApiOperation(value="销售退货-分页列表查询", notes="销售退货-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SalReturn>> queryPageList(SalReturn salReturn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SalReturn> queryWrapper = QueryGenerator.initQueryWrapper(salReturn, req.getParameterMap());
		Page<SalReturn> page = new Page<SalReturn>(pageNo, pageSize);
		IPage<SalReturn> pageList = salReturnService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param salReturnPage
	 * @return
	 */
	@AutoLog(value = "销售退货-添加")
	@ApiOperation(value="销售退货-添加", notes="销售退货-添加")
    @RequiresPermissions("salreturn:sal_return:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SalReturnPage salReturnPage) {
		SalReturn salReturn = new SalReturn();
		BeanUtils.copyProperties(salReturnPage, salReturn);
		salReturnService.saveMain(salReturn, salReturnPage.getSalReturnDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param salReturnPage
	 * @return
	 */
	@AutoLog(value = "销售退货-编辑")
	@ApiOperation(value="销售退货-编辑", notes="销售退货-编辑")
    @RequiresPermissions("salreturn:sal_return:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SalReturnPage salReturnPage) {
		SalReturn salReturn = new SalReturn();
		BeanUtils.copyProperties(salReturnPage, salReturn);
		SalReturn salReturnEntity = salReturnService.getById(salReturn.getId());
		if(salReturnEntity==null) {
			return Result.error("未找到对应数据");
		}
		salReturnService.updateMain(salReturn, salReturnPage.getSalReturnDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售退货-通过id删除")
	@ApiOperation(value="销售退货-通过id删除", notes="销售退货-通过id删除")
    @RequiresPermissions("salreturn:sal_return:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		salReturnService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售退货-批量删除")
	@ApiOperation(value="销售退货-批量删除", notes="销售退货-批量删除")
    @RequiresPermissions("salreturn:sal_return:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.salReturnService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售退货-通过id查询")
	@ApiOperation(value="销售退货-通过id查询", notes="销售退货-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SalReturn> queryById(@RequestParam(name="id",required=true) String id) {
		SalReturn salReturn = salReturnService.getById(id);
		if(salReturn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(salReturn);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售退货_明细通过主表ID查询")
	@ApiOperation(value="销售退货_明细主表ID查询", notes="销售退货_明细-通主表ID查询")
	@GetMapping(value = "/querySalReturnDetailByMainId")
	public Result<List<SalReturnDetail>> querySalReturnDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SalReturnDetail> salReturnDetailList = salReturnDetailService.selectByMainId(id);
		return Result.OK(salReturnDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param salReturn
    */
    @RequiresPermissions("salreturn:sal_return:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SalReturn salReturn) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SalReturn> queryWrapper = QueryGenerator.initQueryWrapper(salReturn, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SalReturn> salReturnList = salReturnService.list(queryWrapper);

      // Step.3 组装pageList
      List<SalReturnPage> pageList = new ArrayList<SalReturnPage>();
      for (SalReturn main : salReturnList) {
          SalReturnPage vo = new SalReturnPage();
          BeanUtils.copyProperties(main, vo);
          List<SalReturnDetail> salReturnDetailList = salReturnDetailService.selectByMainId(main.getId());
          vo.setSalReturnDetailList(salReturnDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售退货列表");
      mv.addObject(NormalExcelConstants.CLASS, SalReturnPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售退货数据", "导出人:"+sysUser.getRealname(), "销售退货"));
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
    @RequiresPermissions("salreturn:sal_return:importExcel")
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
              List<SalReturnPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SalReturnPage.class, params);
              for (SalReturnPage page : list) {
                  SalReturn po = new SalReturn();
                  BeanUtils.copyProperties(page, po);
                  salReturnService.saveMain(po, page.getSalReturnDetailList());
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
