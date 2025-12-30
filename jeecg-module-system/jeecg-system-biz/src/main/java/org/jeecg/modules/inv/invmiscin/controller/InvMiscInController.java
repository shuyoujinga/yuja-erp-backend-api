package org.jeecg.modules.inv.invmiscin.controller;

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
import org.jeecg.modules.aop.DeleteCheckAudit;
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
import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscIn;
import org.jeecg.modules.inv.invmiscin.vo.InvMiscInPage;
import org.jeecg.modules.inv.invmiscin.service.IInvMiscInService;
import org.jeecg.modules.inv.invmiscin.service.IInvMiscInDetailService;
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
 * @Description: 其他入库
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Api(tags="其他入库")
@RestController
@RequestMapping("/invmiscin/invMiscIn")
@Slf4j
public class InvMiscInController {
	@Autowired
	private IInvMiscInService invMiscInService;
	@Autowired
	private IInvMiscInDetailService invMiscInDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invMiscIn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "其他入库-分页列表查询")
	@ApiOperation(value="其他入库-分页列表查询", notes="其他入库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvMiscIn>> queryPageList(InvMiscIn invMiscIn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvMiscIn> queryWrapper = QueryGenerator.initQueryWrapper(invMiscIn, req.getParameterMap());
		Page<InvMiscIn> page = new Page<InvMiscIn>(pageNo, pageSize);
		IPage<InvMiscIn> pageList = invMiscInService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invMiscInPage
	 * @return
	 */
	@AutoLog(value = "其他入库-添加")
	@ApiOperation(value="其他入库-添加", notes="其他入库-添加")
    @RequiresPermissions("invmiscin:inv_misc_in:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvMiscInPage invMiscInPage) {
		InvMiscIn invMiscIn = new InvMiscIn();
		BeanUtils.copyProperties(invMiscInPage, invMiscIn);
		invMiscInService.saveMain(invMiscIn, invMiscInPage.getInvMiscInDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invMiscInPage
	 * @return
	 */
	@AutoLog(value = "其他入库-编辑")
	@ApiOperation(value="其他入库-编辑", notes="其他入库-编辑")
    @RequiresPermissions("invmiscin:inv_misc_in:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvMiscInPage invMiscInPage) {
		InvMiscIn invMiscIn = new InvMiscIn();
		BeanUtils.copyProperties(invMiscInPage, invMiscIn);
		InvMiscIn invMiscInEntity = invMiscInService.getById(invMiscIn.getId());
		if(invMiscInEntity==null) {
			return Result.error("未找到对应数据");
		}
		invMiscInService.updateMain(invMiscIn, invMiscInPage.getInvMiscInDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "其他入库-通过id删除")
	@ApiOperation(value="其他入库-通过id删除", notes="其他入库-通过id删除")
    @RequiresPermissions("invmiscin:inv_misc_in:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invMiscInService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "其他入库-批量删除")
	@ApiOperation(value="其他入库-批量删除", notes="其他入库-批量删除")
    @RequiresPermissions("invmiscin:inv_misc_in:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	@DeleteCheckAudit(service = IInvMiscInService.class,entity = InvMiscIn.class)
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invMiscInService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "其他入库-通过id查询")
	@ApiOperation(value="其他入库-通过id查询", notes="其他入库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvMiscIn> queryById(@RequestParam(name="id",required=true) String id) {
		InvMiscIn invMiscIn = invMiscInService.getById(id);
		if(invMiscIn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invMiscIn);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "其他入库_明细通过主表ID查询")
	@ApiOperation(value="其他入库_明细主表ID查询", notes="其他入库_明细-通主表ID查询")
	@GetMapping(value = "/queryInvMiscInDetailByMainId")
	public Result<List<InvMiscInDetail>> queryInvMiscInDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InvMiscInDetail> invMiscInDetailList = invMiscInDetailService.selectByMainId(id);
		return Result.OK(invMiscInDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invMiscIn
    */
    @RequiresPermissions("invmiscin:inv_misc_in:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvMiscIn invMiscIn) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InvMiscIn> queryWrapper = QueryGenerator.initQueryWrapper(invMiscIn, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<InvMiscIn> invMiscInList = invMiscInService.list(queryWrapper);

      // Step.3 组装pageList
      List<InvMiscInPage> pageList = new ArrayList<InvMiscInPage>();
      for (InvMiscIn main : invMiscInList) {
          InvMiscInPage vo = new InvMiscInPage();
          BeanUtils.copyProperties(main, vo);
          List<InvMiscInDetail> invMiscInDetailList = invMiscInDetailService.selectByMainId(main.getId());
          vo.setInvMiscInDetailList(invMiscInDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "其他入库列表");
      mv.addObject(NormalExcelConstants.CLASS, InvMiscInPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("其他入库数据", "导出人:"+sysUser.getRealname(), "其他入库"));
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
    @RequiresPermissions("invmiscin:inv_misc_in:importExcel")
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
              List<InvMiscInPage> list = ExcelImportUtil.importExcel(file.getInputStream(), InvMiscInPage.class, params);
              for (InvMiscInPage page : list) {
                  InvMiscIn po = new InvMiscIn();
                  BeanUtils.copyProperties(page, po);
                  invMiscInService.saveMain(po, page.getInvMiscInDetailList());
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
	 @AutoLog(value = "物资盘点-审核/反审核")
	 @ApiOperation(value = "物资盘点-审核/反审核", notes = "物资盘点-审核/反审核")
	 @RequiresPermissions("invstocktake:inv_stock_take:audit")
	 @RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	 public Result<String> audit(@RequestBody AuditRequest auditRequest) throws Exception {
		 List<String> ids = auditRequest.getIds();
		 String type = auditRequest.getType(); // audit 或 reverse

		 Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");


		 int count;
		 if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			 count = invMiscInService.audit(ids);
		 } else {
			 count = invMiscInService.unAudit(ids);
		 }


		 return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	 }
}
