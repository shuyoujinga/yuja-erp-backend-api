package org.jeecg.modules.prd.prdmps.controller;

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
import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMps;
import org.jeecg.modules.prd.prdmps.vo.PrdMpsPage;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsService;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsDetailService;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsBomDetailService;
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
 * @Description: 生产计划
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Api(tags="生产计划")
@RestController
@RequestMapping("/prdmps/prdMps")
@Slf4j
public class PrdMpsController {
	@Autowired
	private IPrdMpsService prdMpsService;
	@Autowired
	private IPrdMpsDetailService prdMpsDetailService;
	@Autowired
	private IPrdMpsBomDetailService prdMpsBomDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdMps
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生产计划-分页列表查询")
	@ApiOperation(value="生产计划-分页列表查询", notes="生产计划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PrdMps>> queryPageList(PrdMps prdMps,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdMps> queryWrapper = QueryGenerator.initQueryWrapper(prdMps, req.getParameterMap());
		Page<PrdMps> page = new Page<PrdMps>(pageNo, pageSize);
		IPage<PrdMps> pageList = prdMpsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdMpsPage
	 * @return
	 */
	@AutoLog(value = "生产计划-添加")
	@ApiOperation(value="生产计划-添加", notes="生产计划-添加")
    @RequiresPermissions("prdmps:prd_mps:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PrdMpsPage prdMpsPage) {
		PrdMps prdMps = new PrdMps();
		BeanUtils.copyProperties(prdMpsPage, prdMps);
		prdMpsService.saveMain(prdMps, prdMpsPage.getPrdMpsDetailList(),prdMpsPage.getPrdMpsBomDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdMpsPage
	 * @return
	 */
	@AutoLog(value = "生产计划-编辑")
	@ApiOperation(value="生产计划-编辑", notes="生产计划-编辑")
    @RequiresPermissions("prdmps:prd_mps:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PrdMpsPage prdMpsPage) {
		PrdMps prdMps = new PrdMps();
		BeanUtils.copyProperties(prdMpsPage, prdMps);
		PrdMps prdMpsEntity = prdMpsService.getById(prdMps.getId());
		if(prdMpsEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdMpsService.updateMain(prdMps, prdMpsPage.getPrdMpsDetailList(),prdMpsPage.getPrdMpsBomDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生产计划-通过id删除")
	@ApiOperation(value="生产计划-通过id删除", notes="生产计划-通过id删除")
    @RequiresPermissions("prdmps:prd_mps:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		prdMpsService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生产计划-批量删除")
	@ApiOperation(value="生产计划-批量删除", notes="生产计划-批量删除")
    @RequiresPermissions("prdmps:prd_mps:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdMpsService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产计划-通过id查询")
	@ApiOperation(value="生产计划-通过id查询", notes="生产计划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PrdMps> queryById(@RequestParam(name="id",required=true) String id) {
		PrdMps prdMps = prdMpsService.getById(id);
		if(prdMps==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(prdMps);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产计划_明细通过主表ID查询")
	@ApiOperation(value="生产计划_明细主表ID查询", notes="生产计划_明细-通主表ID查询")
	@GetMapping(value = "/queryPrdMpsDetailByMainId")
	public Result<List<PrdMpsDetail>> queryPrdMpsDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdMpsDetail> prdMpsDetailList = prdMpsDetailService.selectByMainId(id);
		return Result.OK(prdMpsDetailList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产计划_材料清单通过主表ID查询")
	@ApiOperation(value="生产计划_材料清单主表ID查询", notes="生产计划_材料清单-通主表ID查询")
	@GetMapping(value = "/queryPrdMpsBomDetailByMainId")
	public Result<List<PrdMpsBomDetail>> queryPrdMpsBomDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdMpsBomDetail> prdMpsBomDetailList = prdMpsBomDetailService.selectByMainId(id);
		return Result.OK(prdMpsBomDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdMps
    */
    @RequiresPermissions("prdmps:prd_mps:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdMps prdMps) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdMps> queryWrapper = QueryGenerator.initQueryWrapper(prdMps, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PrdMps> prdMpsList = prdMpsService.list(queryWrapper);

      // Step.3 组装pageList
      List<PrdMpsPage> pageList = new ArrayList<PrdMpsPage>();
      for (PrdMps main : prdMpsList) {
          PrdMpsPage vo = new PrdMpsPage();
          BeanUtils.copyProperties(main, vo);
          List<PrdMpsDetail> prdMpsDetailList = prdMpsDetailService.selectByMainId(main.getId());
          vo.setPrdMpsDetailList(prdMpsDetailList);
          List<PrdMpsBomDetail> prdMpsBomDetailList = prdMpsBomDetailService.selectByMainId(main.getId());
          vo.setPrdMpsBomDetailList(prdMpsBomDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "生产计划列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdMpsPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产计划数据", "导出人:"+sysUser.getRealname(), "生产计划"));
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
    @RequiresPermissions("prdmps:prd_mps:importExcel")
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
              List<PrdMpsPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdMpsPage.class, params);
              for (PrdMpsPage page : list) {
                  PrdMps po = new PrdMps();
                  BeanUtils.copyProperties(page, po);
                  prdMpsService.saveMain(po, page.getPrdMpsDetailList(),page.getPrdMpsBomDetailList());
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
