package org.jeecg.modules.pur.purreturn.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.constant.Constants;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.aop.DeleteCheckAudit;
import org.jeecg.modules.maindata.bom.vo.AuditRequest;
import org.jeecg.modules.pur.purreturn.entity.PurReturn;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.service.IPurReturnDetailService;
import org.jeecg.modules.pur.purreturn.service.IPurReturnService;
import org.jeecg.modules.pur.purreturn.vo.PurReturnPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.utils.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


 /**
 * @Description: 采购退货
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Api(tags="采购退货")
@RestController
@RequestMapping("/purreturn/purReturn")
@Slf4j
public class PurReturnController {
	@Autowired
	private IPurReturnService purReturnService;
	@Autowired
	private IPurReturnDetailService purReturnDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purReturn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购退货-分页列表查询")
	@ApiOperation(value="采购退货-分页列表查询", notes="采购退货-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurReturn>> queryPageList(PurReturn purReturn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurReturn> queryWrapper = QueryGenerator.initQueryWrapper(purReturn, req.getParameterMap());
		Page<PurReturn> page = new Page<PurReturn>(pageNo, pageSize);
		IPage<PurReturn> pageList = purReturnService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param purReturnPage
	 * @return
	 */
	@AutoLog(value = "采购退货-添加")
	@ApiOperation(value="采购退货-添加", notes="采购退货-添加")
    @RequiresPermissions("purreturn:pur_return:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurReturnPage purReturnPage) {
		PurReturn purReturn = new PurReturn();
		BeanUtils.copyProperties(purReturnPage, purReturn);
		purReturnService.saveMain(purReturn, purReturnPage.getPurReturnDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purReturnPage
	 * @return
	 */
	@AutoLog(value = "采购退货-编辑")
	@ApiOperation(value="采购退货-编辑", notes="采购退货-编辑")
    @RequiresPermissions("purreturn:pur_return:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurReturnPage purReturnPage) {
		PurReturn purReturn = new PurReturn();
		BeanUtils.copyProperties(purReturnPage, purReturn);
		PurReturn purReturnEntity = purReturnService.getById(purReturn.getId());
		if(purReturnEntity==null) {
			return Result.error("未找到对应数据");
		}
		purReturnService.updateMain(purReturn, purReturnPage.getPurReturnDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购退货-通过id删除")
	@ApiOperation(value="采购退货-通过id删除", notes="采购退货-通过id删除")
    @RequiresPermissions("purreturn:pur_return:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purReturnService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购退货-批量删除")
	@ApiOperation(value="采购退货-批量删除", notes="采购退货-批量删除")
    @RequiresPermissions("purreturn:pur_return:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	@DeleteCheckAudit(service = IPurReturnService.class,entity = PurReturn.class)
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purReturnService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购退货-通过id查询")
	@ApiOperation(value="采购退货-通过id查询", notes="采购退货-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurReturn> queryById(@RequestParam(name="id",required=true) String id) {
		PurReturn purReturn = purReturnService.getById(id);
		if(purReturn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purReturn);

	}
	 @GetMapping("/smartRemark")
	 public Result<String> smartRemark(@RequestParam String orderDetailId,
									   @RequestParam String supplierCode) {

		String result = purReturnService.smarkRemark(orderDetailId,supplierCode);

		 return Result.OK(result);
	 }

	 /**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购退货_明细通过主表ID查询")
	@ApiOperation(value="采购退货_明细主表ID查询", notes="采购退货_明细-通主表ID查询")
	@GetMapping(value = "/queryPurReturnDetailByMainId")
	public Result<List<PurReturnDetail>> queryPurReturnDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurReturnDetail> purReturnDetailList = purReturnDetailService.selectByMainId(id);
		return Result.OK(purReturnDetailList);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "采购退货_明细通过主表ID查询")
	 @ApiOperation(value="采购退货_明细主表ID查询", notes="采购退货_明细-通主表ID查询")
	 @GetMapping(value = "/queryPurReturnDetailByTargetId")
	 public Result<List<PurReturnDetail>> queryPurReturnDetailByTargetId(@RequestParam(name="id",required=true) String id) {
		 List<PurReturnDetail> purReturnDetailList = purReturnDetailService.selectByTargetId(id);
		 return Result.OK(purReturnDetailList);
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param purReturn
    */
    @RequiresPermissions("purreturn:pur_return:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurReturn purReturn) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PurReturn> queryWrapper = QueryGenerator.initQueryWrapper(purReturn, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<PurReturn> purReturnList = purReturnService.list(queryWrapper);

      // Step.3 组装pageList
      List<PurReturnPage> pageList = new ArrayList<PurReturnPage>();
      for (PurReturn main : purReturnList) {
          PurReturnPage vo = new PurReturnPage();
          BeanUtils.copyProperties(main, vo);
          List<PurReturnDetail> purReturnDetailList = purReturnDetailService.selectByMainId(main.getId());
          vo.setPurReturnDetailList(purReturnDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购退货列表");
      mv.addObject(NormalExcelConstants.CLASS, PurReturnPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购退货数据", "导出人:"+sysUser.getRealname(), "采购退货"));
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
    @RequiresPermissions("purreturn:pur_return:importExcel")
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
              List<PurReturnPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurReturnPage.class, params);
              for (PurReturnPage page : list) {
                  PurReturn po = new PurReturn();
                  BeanUtils.copyProperties(page, po);
                  purReturnService.saveMain(po, page.getPurReturnDetailList());
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
	 @AutoLog(value = "采购退货-审核/反审核")
	 @ApiOperation(value = "采购退货-审核/反审核", notes = "采购退货-审核/反审核")
	 @RequiresPermissions("purreturn:pur_return:audit")
	 @RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	 public Result<String> audit(@RequestBody AuditRequest auditRequest) throws Exception {
		 List<String> ids = auditRequest.getIds();
		 String type = auditRequest.getType(); // audit 或 reverse

		 Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

		 int count;
		 if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			 count = purReturnService.audit(ids);
		 } else {
			 count = purReturnService.unAudit(ids);
		 }


		 return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	 }
}
