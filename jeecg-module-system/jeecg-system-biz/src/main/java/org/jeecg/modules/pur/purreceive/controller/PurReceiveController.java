package org.jeecg.modules.pur.purreceive.controller;

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
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
import org.jeecg.modules.pur.purreceive.vo.PurReceivePage;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveService;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveDetailService;
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
 * @Description: 采购收货
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
@Api(tags="采购收货")
@RestController
@RequestMapping("/purreceive/purReceive")
@Slf4j
public class PurReceiveController {
	@Autowired
	private IPurReceiveService purReceiveService;
	@Autowired
	private IPurReceiveDetailService purReceiveDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purReceive
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购收货-分页列表查询")
	@ApiOperation(value="采购收货-分页列表查询", notes="采购收货-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurReceive>> queryPageList(PurReceive purReceive,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurReceive> queryWrapper = QueryGenerator.initQueryWrapper(purReceive, req.getParameterMap());
		Page<PurReceive> page = new Page<PurReceive>(pageNo, pageSize);
		IPage<PurReceive> pageList = purReceiveService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param purReceivePage
	 * @return
	 */
	@AutoLog(value = "采购收货-添加")
	@ApiOperation(value="采购收货-添加", notes="采购收货-添加")
    @RequiresPermissions("purreceive:pur_receive:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurReceivePage purReceivePage) {
		PurReceive purReceive = new PurReceive();
		BeanUtils.copyProperties(purReceivePage, purReceive);
		purReceiveService.saveMain(purReceive, purReceivePage.getPurReceiveDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purReceivePage
	 * @return
	 */
	@AutoLog(value = "采购收货-编辑")
	@ApiOperation(value="采购收货-编辑", notes="采购收货-编辑")
    @RequiresPermissions("purreceive:pur_receive:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurReceivePage purReceivePage) {
		PurReceive purReceive = new PurReceive();
		BeanUtils.copyProperties(purReceivePage, purReceive);
		PurReceive purReceiveEntity = purReceiveService.getById(purReceive.getId());
		if(purReceiveEntity==null) {
			return Result.error("未找到对应数据");
		}
		purReceiveService.updateMain(purReceive, purReceivePage.getPurReceiveDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购收货-通过id删除")
	@ApiOperation(value="采购收货-通过id删除", notes="采购收货-通过id删除")
    @RequiresPermissions("purreceive:pur_receive:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purReceiveService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购收货-批量删除")
	@ApiOperation(value="采购收货-批量删除", notes="采购收货-批量删除")
    @RequiresPermissions("purreceive:pur_receive:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purReceiveService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购收货-通过id查询")
	@ApiOperation(value="采购收货-通过id查询", notes="采购收货-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurReceive> queryById(@RequestParam(name="id",required=true) String id) {
		PurReceive purReceive = purReceiveService.getById(id);
		if(purReceive==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purReceive);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购收货_明细通过主表ID查询")
	@ApiOperation(value="采购收货_明细主表ID查询", notes="采购收货_明细-通主表ID查询")
	@GetMapping(value = "/queryPurReceiveDetailByMainId")
	public Result<List<PurReceiveDetail>> queryPurReceiveDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurReceiveDetail> purReceiveDetailList = purReceiveDetailService.selectByMainId(id);
		return Result.OK(purReceiveDetailList);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购收货_明细通过主表ID查询")
	@ApiOperation(value="采购收货_明细主表ID查询", notes="采购收货_明细-通特定ID查询")
	@GetMapping(value = "/queryPurReceiveDetailByTargetId")
	public Result<List<PurReceiveDetail>> queryPurReceiveDetailListByTargetId(@RequestParam(name="id",required=true) String id) {
		List<PurReceiveDetail> purReceiveDetailList = purReceiveDetailService.selectByTargetId(id);
		return Result.OK(purReceiveDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param purReceive
    */
    @RequiresPermissions("purreceive:pur_receive:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurReceive purReceive) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PurReceive> queryWrapper = QueryGenerator.initQueryWrapper(purReceive, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PurReceive> purReceiveList = purReceiveService.list(queryWrapper);

      // Step.3 组装pageList
      List<PurReceivePage> pageList = new ArrayList<PurReceivePage>();
      for (PurReceive main : purReceiveList) {
          PurReceivePage vo = new PurReceivePage();
          BeanUtils.copyProperties(main, vo);
          List<PurReceiveDetail> purReceiveDetailList = purReceiveDetailService.selectByMainId(main.getId());
          vo.setPurReceiveDetailList(purReceiveDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购收货列表");
      mv.addObject(NormalExcelConstants.CLASS, PurReceivePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购收货数据", "导出人:"+sysUser.getRealname(), "采购收货"));
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
    @RequiresPermissions("purreceive:pur_receive:importExcel")
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
              List<PurReceivePage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurReceivePage.class, params);
              for (PurReceivePage page : list) {
                  PurReceive po = new PurReceive();
                  BeanUtils.copyProperties(page, po);
                  purReceiveService.saveMain(po, page.getPurReceiveDetailList());
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
	@AutoLog(value = "采购收货-审核/反审核")
	@ApiOperation(value = "采购收货-审核/反审核", notes = "采购收货-审核/反审核")
	@RequiresPermissions("purreceive:pur_receive:audit")
	@RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result<String> audit(@RequestBody AuditRequest auditRequest) throws Exception {
		List<String> ids = auditRequest.getIds();
		String type = auditRequest.getType(); // audit 或 reverse

		Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

		int count;
		if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			count = purReceiveService.audit(ids);
		} else {
			count = purReceiveService.unAudit(ids);
		}


		return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	}
}
