package org.jeecg.modules.prd.prdprocess.controller;

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
import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcess;
import org.jeecg.modules.prd.prdprocess.vo.PrdProcessPage;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessService;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessDetailService;
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
 * @Description: 生产工序
 * @Author: 舒有敬
 * @Date:   2026-01-06
 * @Version: V1.0
 */
@Api(tags="生产工序")
@RestController
@RequestMapping("/prdprocess/prdProcess")
@Slf4j
public class PrdProcessController {
	@Autowired
	private IPrdProcessService prdProcessService;
	@Autowired
	private IPrdProcessDetailService prdProcessDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdProcess
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生产工序-分页列表查询")
	@ApiOperation(value="生产工序-分页列表查询", notes="生产工序-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PrdProcess>> queryPageList(PrdProcess prdProcess,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdProcess> queryWrapper = QueryGenerator.initQueryWrapper(prdProcess, req.getParameterMap());
		Page<PrdProcess> page = new Page<PrdProcess>(pageNo, pageSize);
		IPage<PrdProcess> pageList = prdProcessService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdProcessPage
	 * @return
	 */
	@AutoLog(value = "生产工序-添加")
	@ApiOperation(value="生产工序-添加", notes="生产工序-添加")
    @RequiresPermissions("prdprocess:prd_process:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PrdProcessPage prdProcessPage) {
		PrdProcess prdProcess = new PrdProcess();
		BeanUtils.copyProperties(prdProcessPage, prdProcess);
		prdProcessService.saveMain(prdProcess, prdProcessPage.getPrdProcessDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdProcessPage
	 * @return
	 */
	@AutoLog(value = "生产工序-编辑")
	@ApiOperation(value="生产工序-编辑", notes="生产工序-编辑")
    @RequiresPermissions("prdprocess:prd_process:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PrdProcessPage prdProcessPage) {
		PrdProcess prdProcess = new PrdProcess();
		BeanUtils.copyProperties(prdProcessPage, prdProcess);
		PrdProcess prdProcessEntity = prdProcessService.getById(prdProcess.getId());
		if(prdProcessEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdProcessService.updateMain(prdProcess, prdProcessPage.getPrdProcessDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生产工序-通过id删除")
	@ApiOperation(value="生产工序-通过id删除", notes="生产工序-通过id删除")
    @RequiresPermissions("prdprocess:prd_process:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		prdProcessService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生产工序-批量删除")
	@ApiOperation(value="生产工序-批量删除", notes="生产工序-批量删除")
    @RequiresPermissions("prdprocess:prd_process:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdProcessService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产工序-通过id查询")
	@ApiOperation(value="生产工序-通过id查询", notes="生产工序-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PrdProcess> queryById(@RequestParam(name="id",required=true) String id) {
		PrdProcess prdProcess = prdProcessService.getById(id);
		if(prdProcess==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(prdProcess);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产工序_明细通过主表ID查询")
	@ApiOperation(value="生产工序_明细主表ID查询", notes="生产工序_明细-通主表ID查询")
	@GetMapping(value = "/queryPrdProcessDetailByMainId")
	public Result<List<PrdProcessDetail>> queryPrdProcessDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdProcessDetail> prdProcessDetailList = prdProcessDetailService.selectByMainId(id);
		return Result.OK(prdProcessDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdProcess
    */
    @RequiresPermissions("prdprocess:prd_process:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdProcess prdProcess) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdProcess> queryWrapper = QueryGenerator.initQueryWrapper(prdProcess, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PrdProcess> prdProcessList = prdProcessService.list(queryWrapper);

      // Step.3 组装pageList
      List<PrdProcessPage> pageList = new ArrayList<PrdProcessPage>();
      for (PrdProcess main : prdProcessList) {
          PrdProcessPage vo = new PrdProcessPage();
          BeanUtils.copyProperties(main, vo);
          List<PrdProcessDetail> prdProcessDetailList = prdProcessDetailService.selectByMainId(main.getId());
          vo.setPrdProcessDetailList(prdProcessDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "生产工序列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdProcessPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产工序数据", "导出人:"+sysUser.getRealname(), "生产工序"));
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
    @RequiresPermissions("prdprocess:prd_process:importExcel")
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
              List<PrdProcessPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdProcessPage.class, params);
              for (PrdProcessPage page : list) {
                  PrdProcess po = new PrdProcess();
                  BeanUtils.copyProperties(page, po);
                  prdProcessService.saveMain(po, page.getPrdProcessDetailList());
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
