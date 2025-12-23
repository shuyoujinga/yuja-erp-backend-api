package org.jeecg.modules.inv.invassembly.controller;

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
import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import org.jeecg.modules.inv.invassembly.entity.InvAssembly;
import org.jeecg.modules.inv.invassembly.vo.InvAssemblyPage;
import org.jeecg.modules.inv.invassembly.service.IInvAssemblyService;
import org.jeecg.modules.inv.invassembly.service.IInvAssemblyDetailService;
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
 * @Description: 组装单
 * @Author: 舒有敬
 * @Date:   2025-12-16
 * @Version: V1.0
 */
@Api(tags="组装单")
@RestController
@RequestMapping("/invassembly/invAssembly")
@Slf4j
public class InvAssemblyController {
	@Autowired
	private IInvAssemblyService invAssemblyService;
	@Autowired
	private IInvAssemblyDetailService invAssemblyDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invAssembly
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "组装单-分页列表查询")
	@ApiOperation(value="组装单-分页列表查询", notes="组装单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvAssembly>> queryPageList(InvAssembly invAssembly,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvAssembly> queryWrapper = QueryGenerator.initQueryWrapper(invAssembly, req.getParameterMap());
		Page<InvAssembly> page = new Page<InvAssembly>(pageNo, pageSize);
		IPage<InvAssembly> pageList = invAssemblyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invAssemblyPage
	 * @return
	 */
	@AutoLog(value = "组装单-添加")
	@ApiOperation(value="组装单-添加", notes="组装单-添加")
    @RequiresPermissions("invassembly:inv_assembly:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvAssemblyPage invAssemblyPage) {
		InvAssembly invAssembly = new InvAssembly();
		BeanUtils.copyProperties(invAssemblyPage, invAssembly);
		invAssemblyService.saveMain(invAssembly, invAssemblyPage.getInvAssemblyDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invAssemblyPage
	 * @return
	 */
	@AutoLog(value = "组装单-编辑")
	@ApiOperation(value="组装单-编辑", notes="组装单-编辑")
    @RequiresPermissions("invassembly:inv_assembly:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvAssemblyPage invAssemblyPage) {
		InvAssembly invAssembly = new InvAssembly();
		BeanUtils.copyProperties(invAssemblyPage, invAssembly);
		InvAssembly invAssemblyEntity = invAssemblyService.getById(invAssembly.getId());
		if(invAssemblyEntity==null) {
			return Result.error("未找到对应数据");
		}
		invAssemblyService.updateMain(invAssembly, invAssemblyPage.getInvAssemblyDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "组装单-通过id删除")
	@ApiOperation(value="组装单-通过id删除", notes="组装单-通过id删除")
    @RequiresPermissions("invassembly:inv_assembly:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invAssemblyService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "组装单-批量删除")
	@ApiOperation(value="组装单-批量删除", notes="组装单-批量删除")
    @RequiresPermissions("invassembly:inv_assembly:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invAssemblyService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "组装单-通过id查询")
	@ApiOperation(value="组装单-通过id查询", notes="组装单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvAssembly> queryById(@RequestParam(name="id",required=true) String id) {
		InvAssembly invAssembly = invAssemblyService.getById(id);
		if(invAssembly==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invAssembly);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "组装单_明细通过主表ID查询")
	@ApiOperation(value="组装单_明细主表ID查询", notes="组装单_明细-通主表ID查询")
	@GetMapping(value = "/queryInvAssemblyDetailByMainId")
	public Result<List<InvAssemblyDetail>> queryInvAssemblyDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InvAssemblyDetail> invAssemblyDetailList = invAssemblyDetailService.selectByMainId(id);
		return Result.OK(invAssemblyDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invAssembly
    */
    @RequiresPermissions("invassembly:inv_assembly:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvAssembly invAssembly) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InvAssembly> queryWrapper = QueryGenerator.initQueryWrapper(invAssembly, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<InvAssembly> invAssemblyList = invAssemblyService.list(queryWrapper);

      // Step.3 组装pageList
      List<InvAssemblyPage> pageList = new ArrayList<InvAssemblyPage>();
      for (InvAssembly main : invAssemblyList) {
          InvAssemblyPage vo = new InvAssemblyPage();
          BeanUtils.copyProperties(main, vo);
          List<InvAssemblyDetail> invAssemblyDetailList = invAssemblyDetailService.selectByMainId(main.getId());
          vo.setInvAssemblyDetailList(invAssemblyDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "组装单列表");
      mv.addObject(NormalExcelConstants.CLASS, InvAssemblyPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("组装单数据", "导出人:"+sysUser.getRealname(), "组装单"));
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
    @RequiresPermissions("invassembly:inv_assembly:importExcel")
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
              List<InvAssemblyPage> list = ExcelImportUtil.importExcel(file.getInputStream(), InvAssemblyPage.class, params);
              for (InvAssemblyPage page : list) {
                  InvAssembly po = new InvAssembly();
                  BeanUtils.copyProperties(page, po);
                  invAssemblyService.saveMain(po, page.getInvAssemblyDetailList());
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
