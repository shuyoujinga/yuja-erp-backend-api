package org.jeecg.modules.prd.prdissue.controller;

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
import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import org.jeecg.modules.prd.prdissue.entity.PrdIssue;
import org.jeecg.modules.prd.prdissue.vo.PrdIssuePage;
import org.jeecg.modules.prd.prdissue.service.IPrdIssueService;
import org.jeecg.modules.prd.prdissue.service.IPrdIssueDetailService;
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
 * @Description: 生产领料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Api(tags="生产领料")
@RestController
@RequestMapping("/prdissue/prdIssue")
@Slf4j
public class PrdIssueController {
	@Autowired
	private IPrdIssueService prdIssueService;
	@Autowired
	private IPrdIssueDetailService prdIssueDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdIssue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生产领料-分页列表查询")
	@ApiOperation(value="生产领料-分页列表查询", notes="生产领料-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PrdIssue>> queryPageList(PrdIssue prdIssue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdIssue> queryWrapper = QueryGenerator.initQueryWrapper(prdIssue, req.getParameterMap());
		Page<PrdIssue> page = new Page<PrdIssue>(pageNo, pageSize);
		IPage<PrdIssue> pageList = prdIssueService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdIssuePage
	 * @return
	 */
	@AutoLog(value = "生产领料-添加")
	@ApiOperation(value="生产领料-添加", notes="生产领料-添加")
    @RequiresPermissions("prdissue:prd_issue:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PrdIssuePage prdIssuePage) {
		PrdIssue prdIssue = new PrdIssue();
		BeanUtils.copyProperties(prdIssuePage, prdIssue);
		prdIssueService.saveMain(prdIssue, prdIssuePage.getPrdIssueDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdIssuePage
	 * @return
	 */
	@AutoLog(value = "生产领料-编辑")
	@ApiOperation(value="生产领料-编辑", notes="生产领料-编辑")
    @RequiresPermissions("prdissue:prd_issue:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PrdIssuePage prdIssuePage) {
		PrdIssue prdIssue = new PrdIssue();
		BeanUtils.copyProperties(prdIssuePage, prdIssue);
		PrdIssue prdIssueEntity = prdIssueService.getById(prdIssue.getId());
		if(prdIssueEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdIssueService.updateMain(prdIssue, prdIssuePage.getPrdIssueDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生产领料-通过id删除")
	@ApiOperation(value="生产领料-通过id删除", notes="生产领料-通过id删除")
    @RequiresPermissions("prdissue:prd_issue:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		prdIssueService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生产领料-批量删除")
	@ApiOperation(value="生产领料-批量删除", notes="生产领料-批量删除")
    @RequiresPermissions("prdissue:prd_issue:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdIssueService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产领料-通过id查询")
	@ApiOperation(value="生产领料-通过id查询", notes="生产领料-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PrdIssue> queryById(@RequestParam(name="id",required=true) String id) {
		PrdIssue prdIssue = prdIssueService.getById(id);
		if(prdIssue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(prdIssue);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产领料_明细通过主表ID查询")
	@ApiOperation(value="生产领料_明细主表ID查询", notes="生产领料_明细-通主表ID查询")
	@GetMapping(value = "/queryPrdIssueDetailByMainId")
	public Result<List<PrdIssueDetail>> queryPrdIssueDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdIssueDetail> prdIssueDetailList = prdIssueDetailService.selectByMainId(id);
		return Result.OK(prdIssueDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdIssue
    */
    @RequiresPermissions("prdissue:prd_issue:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdIssue prdIssue) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdIssue> queryWrapper = QueryGenerator.initQueryWrapper(prdIssue, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PrdIssue> prdIssueList = prdIssueService.list(queryWrapper);

      // Step.3 组装pageList
      List<PrdIssuePage> pageList = new ArrayList<PrdIssuePage>();
      for (PrdIssue main : prdIssueList) {
          PrdIssuePage vo = new PrdIssuePage();
          BeanUtils.copyProperties(main, vo);
          List<PrdIssueDetail> prdIssueDetailList = prdIssueDetailService.selectByMainId(main.getId());
          vo.setPrdIssueDetailList(prdIssueDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "生产领料列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdIssuePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产领料数据", "导出人:"+sysUser.getRealname(), "生产领料"));
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
    @RequiresPermissions("prdissue:prd_issue:importExcel")
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
              List<PrdIssuePage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdIssuePage.class, params);
              for (PrdIssuePage page : list) {
                  PrdIssue po = new PrdIssue();
                  BeanUtils.copyProperties(page, po);
                  prdIssueService.saveMain(po, page.getPrdIssueDetailList());
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
