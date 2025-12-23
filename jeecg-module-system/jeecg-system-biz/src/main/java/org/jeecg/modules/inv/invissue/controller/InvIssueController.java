package org.jeecg.modules.inv.invissue.controller;

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
import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.entity.InvIssue;
import org.jeecg.modules.inv.invissue.vo.InvIssuePage;
import org.jeecg.modules.inv.invissue.service.IInvIssueService;
import org.jeecg.modules.inv.invissue.service.IInvIssueDetailService;
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
 * @Description: 物料领用
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="物料领用")
@RestController
@RequestMapping("/invissue/invIssue")
@Slf4j
public class InvIssueController {
	@Autowired
	private IInvIssueService invIssueService;
	@Autowired
	private IInvIssueDetailService invIssueDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invIssue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "物料领用-分页列表查询")
	@ApiOperation(value="物料领用-分页列表查询", notes="物料领用-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvIssue>> queryPageList(InvIssue invIssue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvIssue> queryWrapper = QueryGenerator.initQueryWrapper(invIssue, req.getParameterMap());
		Page<InvIssue> page = new Page<InvIssue>(pageNo, pageSize);
		IPage<InvIssue> pageList = invIssueService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invIssuePage
	 * @return
	 */
	@AutoLog(value = "物料领用-添加")
	@ApiOperation(value="物料领用-添加", notes="物料领用-添加")
    @RequiresPermissions("invissue:inv_issue:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvIssuePage invIssuePage) {
		InvIssue invIssue = new InvIssue();
		BeanUtils.copyProperties(invIssuePage, invIssue);
		invIssueService.saveMain(invIssue, invIssuePage.getInvIssueDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invIssuePage
	 * @return
	 */
	@AutoLog(value = "物料领用-编辑")
	@ApiOperation(value="物料领用-编辑", notes="物料领用-编辑")
    @RequiresPermissions("invissue:inv_issue:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvIssuePage invIssuePage) {
		InvIssue invIssue = new InvIssue();
		BeanUtils.copyProperties(invIssuePage, invIssue);
		InvIssue invIssueEntity = invIssueService.getById(invIssue.getId());
		if(invIssueEntity==null) {
			return Result.error("未找到对应数据");
		}
		invIssueService.updateMain(invIssue, invIssuePage.getInvIssueDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料领用-通过id删除")
	@ApiOperation(value="物料领用-通过id删除", notes="物料领用-通过id删除")
    @RequiresPermissions("invissue:inv_issue:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invIssueService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料领用-批量删除")
	@ApiOperation(value="物料领用-批量删除", notes="物料领用-批量删除")
    @RequiresPermissions("invissue:inv_issue:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invIssueService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料领用-通过id查询")
	@ApiOperation(value="物料领用-通过id查询", notes="物料领用-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvIssue> queryById(@RequestParam(name="id",required=true) String id) {
		InvIssue invIssue = invIssueService.getById(id);
		if(invIssue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invIssue);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料领用_明细通过主表ID查询")
	@ApiOperation(value="物料领用_明细主表ID查询", notes="物料领用_明细-通主表ID查询")
	@GetMapping(value = "/queryInvIssueDetailByMainId")
	public Result<List<InvIssueDetail>> queryInvIssueDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InvIssueDetail> invIssueDetailList = invIssueDetailService.selectByMainId(id);
		return Result.OK(invIssueDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invIssue
    */
    @RequiresPermissions("invissue:inv_issue:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvIssue invIssue) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InvIssue> queryWrapper = QueryGenerator.initQueryWrapper(invIssue, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<InvIssue> invIssueList = invIssueService.list(queryWrapper);

      // Step.3 组装pageList
      List<InvIssuePage> pageList = new ArrayList<InvIssuePage>();
      for (InvIssue main : invIssueList) {
          InvIssuePage vo = new InvIssuePage();
          BeanUtils.copyProperties(main, vo);
          List<InvIssueDetail> invIssueDetailList = invIssueDetailService.selectByMainId(main.getId());
          vo.setInvIssueDetailList(invIssueDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "物料领用列表");
      mv.addObject(NormalExcelConstants.CLASS, InvIssuePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料领用数据", "导出人:"+sysUser.getRealname(), "物料领用"));
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
    @RequiresPermissions("invissue:inv_issue:importExcel")
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
              List<InvIssuePage> list = ExcelImportUtil.importExcel(file.getInputStream(), InvIssuePage.class, params);
              for (InvIssuePage page : list) {
                  InvIssue po = new InvIssue();
                  BeanUtils.copyProperties(page, po);
                  invIssueService.saveMain(po, page.getInvIssueDetailList());
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
