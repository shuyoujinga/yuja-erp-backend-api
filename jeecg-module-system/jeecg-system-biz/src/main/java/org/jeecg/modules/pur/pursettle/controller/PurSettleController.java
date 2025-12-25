package org.jeecg.modules.pur.pursettle.controller;

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
import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import org.jeecg.modules.pur.pursettle.entity.PurSettle;
import org.jeecg.modules.pur.pursettle.vo.PurSettlePage;
import org.jeecg.modules.pur.pursettle.service.IPurSettleService;
import org.jeecg.modules.pur.pursettle.service.IPurSettleDetailService;
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
 * @Description: 采购结算
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Api(tags="采购结算")
@RestController
@RequestMapping("/pursettle/purSettle")
@Slf4j
public class PurSettleController {
	@Autowired
	private IPurSettleService purSettleService;
	@Autowired
	private IPurSettleDetailService purSettleDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purSettle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购结算-分页列表查询")
	@ApiOperation(value="采购结算-分页列表查询", notes="采购结算-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurSettle>> queryPageList(PurSettle purSettle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurSettle> queryWrapper = QueryGenerator.initQueryWrapper(purSettle, req.getParameterMap());
		Page<PurSettle> page = new Page<PurSettle>(pageNo, pageSize);
		IPage<PurSettle> pageList = purSettleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param purSettlePage
	 * @return
	 */
	@AutoLog(value = "采购结算-添加")
	@ApiOperation(value="采购结算-添加", notes="采购结算-添加")
    @RequiresPermissions("pursettle:pur_settle:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurSettlePage purSettlePage) {
		PurSettle purSettle = new PurSettle();
		BeanUtils.copyProperties(purSettlePage, purSettle);
		purSettleService.saveMain(purSettle, purSettlePage.getPurSettleDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purSettlePage
	 * @return
	 */
	@AutoLog(value = "采购结算-编辑")
	@ApiOperation(value="采购结算-编辑", notes="采购结算-编辑")
    @RequiresPermissions("pursettle:pur_settle:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurSettlePage purSettlePage) {
		PurSettle purSettle = new PurSettle();
		BeanUtils.copyProperties(purSettlePage, purSettle);
		PurSettle purSettleEntity = purSettleService.getById(purSettle.getId());
		if(purSettleEntity==null) {
			return Result.error("未找到对应数据");
		}
		purSettleService.updateMain(purSettle, purSettlePage.getPurSettleDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购结算-通过id删除")
	@ApiOperation(value="采购结算-通过id删除", notes="采购结算-通过id删除")
    @RequiresPermissions("pursettle:pur_settle:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purSettleService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购结算-批量删除")
	@ApiOperation(value="采购结算-批量删除", notes="采购结算-批量删除")
    @RequiresPermissions("pursettle:pur_settle:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purSettleService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购结算-通过id查询")
	@ApiOperation(value="采购结算-通过id查询", notes="采购结算-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurSettle> queryById(@RequestParam(name="id",required=true) String id) {
		PurSettle purSettle = purSettleService.getById(id);
		if(purSettle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purSettle);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购结算_明细通过主表ID查询")
	@ApiOperation(value="采购结算_明细主表ID查询", notes="采购结算_明细-通主表ID查询")
	@GetMapping(value = "/queryPurSettleDetailByMainId")
	public Result<List<PurSettleDetail>> queryPurSettleDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurSettleDetail> purSettleDetailList = purSettleDetailService.selectByMainId(id);
		return Result.OK(purSettleDetailList);
	}
	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "采购结算_明细通过主表ID查询")
	 @ApiOperation(value="采购结算_明细主表ID查询", notes="采购结算_明细-通主表ID查询")
	 @GetMapping(value = "/queryPurSettleDetailByTargetId")
	 public Result<List<PurSettleDetail>> queryPurSettleDetailListByTargetId(@RequestParam(name="id",required=true) String id) {
		 List<PurSettleDetail> purSettleDetailList = purSettleDetailService.selectByTargetId(id);
		 return Result.OK(purSettleDetailList);
	 }
	/**
	 * 审核/反审核
	 *
	 * @param auditRequest 审核请求参数，包含ID列表和操作类型（audit/reverse）
	 * @return
	 */
	@AutoLog(value = "采购收货-审核/反审核")
	@ApiOperation(value = "采购收货-审核/反审核", notes = "采购收货-审核/反审核")
	@RequiresPermissions("pursettle:pur_settle:audit")
	@RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result<String> audit(@RequestBody AuditRequest auditRequest) throws Exception {
		List<String> ids = auditRequest.getIds();
		String type = auditRequest.getType(); // audit 或 reverse

		Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

		int count;
		if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			count = purSettleService.audit(ids);
		} else {
			count = purSettleService.unAudit(ids);
		}


		return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	}
    /**
    * 导出excel
    *
    * @param request
    * @param purSettle
    */
    @RequiresPermissions("pursettle:pur_settle:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurSettle purSettle) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PurSettle> queryWrapper = QueryGenerator.initQueryWrapper(purSettle, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PurSettle> purSettleList = purSettleService.list(queryWrapper);

      // Step.3 组装pageList
      List<PurSettlePage> pageList = new ArrayList<PurSettlePage>();
      for (PurSettle main : purSettleList) {
          PurSettlePage vo = new PurSettlePage();
          BeanUtils.copyProperties(main, vo);
          List<PurSettleDetail> purSettleDetailList = purSettleDetailService.selectByMainId(main.getId());
          vo.setPurSettleDetailList(purSettleDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购结算列表");
      mv.addObject(NormalExcelConstants.CLASS, PurSettlePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购结算数据", "导出人:"+sysUser.getRealname(), "采购结算"));
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
    @RequiresPermissions("pursettle:pur_settle:importExcel")
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
              List<PurSettlePage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurSettlePage.class, params);
              for (PurSettlePage page : list) {
                  PurSettle po = new PurSettle();
                  BeanUtils.copyProperties(page, po);
                  purSettleService.saveMain(po, page.getPurSettleDetailList());
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
