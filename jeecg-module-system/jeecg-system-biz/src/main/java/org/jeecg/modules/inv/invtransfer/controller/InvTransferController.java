package org.jeecg.modules.inv.invtransfer.controller;

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
import org.jeecg.modules.inv.invtransfer.entity.InvTransfer;
import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import org.jeecg.modules.inv.invtransfer.service.IInvTransferDetailService;
import org.jeecg.modules.inv.invtransfer.service.IInvTransferService;
import org.jeecg.modules.inv.invtransfer.vo.InvTransferPage;
import org.jeecg.modules.maindata.bom.vo.AuditRequest;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
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
 * @Description: 物料调拨
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="物料调拨")
@RestController
@RequestMapping("/invtransfer/invTransfer")
@Slf4j
public class InvTransferController {
	@Autowired
	private IInvTransferService invTransferService;
	@Autowired
	private IInvTransferDetailService invTransferDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invTransfer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "物料调拨-分页列表查询")
	@ApiOperation(value="物料调拨-分页列表查询", notes="物料调拨-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvTransfer>> queryPageList(InvTransfer invTransfer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvTransfer> queryWrapper = QueryGenerator.initQueryWrapper(invTransfer, req.getParameterMap());
		Page<InvTransfer> page = new Page<InvTransfer>(pageNo, pageSize);
		IPage<InvTransfer> pageList = invTransferService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invTransferPage
	 * @return
	 */
	@AutoLog(value = "物料调拨-添加")
	@ApiOperation(value="物料调拨-添加", notes="物料调拨-添加")
    @RequiresPermissions("invtransfer:inv_transfer:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvTransferPage invTransferPage) {
		InvTransfer invTransfer = new InvTransfer();
		BeanUtils.copyProperties(invTransferPage, invTransfer);
		invTransferService.saveMain(invTransfer, invTransferPage.getInvTransferDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invTransferPage
	 * @return
	 */
	@AutoLog(value = "物料调拨-编辑")
	@ApiOperation(value="物料调拨-编辑", notes="物料调拨-编辑")
    @RequiresPermissions("invtransfer:inv_transfer:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvTransferPage invTransferPage) {
		InvTransfer invTransfer = new InvTransfer();
		BeanUtils.copyProperties(invTransferPage, invTransfer);
		InvTransfer invTransferEntity = invTransferService.getById(invTransfer.getId());
		if(invTransferEntity==null) {
			return Result.error("未找到对应数据");
		}
		invTransferService.updateMain(invTransfer, invTransferPage.getInvTransferDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料调拨-通过id删除")
	@ApiOperation(value="物料调拨-通过id删除", notes="物料调拨-通过id删除")
    @RequiresPermissions("invtransfer:inv_transfer:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invTransferService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料调拨-批量删除")
	@ApiOperation(value="物料调拨-批量删除", notes="物料调拨-批量删除")
    @RequiresPermissions("invtransfer:inv_transfer:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	@DeleteCheckAudit(service = IInvTransferService.class,entity = InvTransfer.class)
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invTransferService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料调拨-通过id查询")
	@ApiOperation(value="物料调拨-通过id查询", notes="物料调拨-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvTransfer> queryById(@RequestParam(name="id",required=true) String id) {
		InvTransfer invTransfer = invTransferService.getById(id);
		if(invTransfer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invTransfer);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料调拨_明细通过主表ID查询")
	@ApiOperation(value="物料调拨_明细主表ID查询", notes="物料调拨_明细-通主表ID查询")
	@GetMapping(value = "/queryInvTransferDetailByMainId")
	public Result<List<InvTransferDetail>> queryInvTransferDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InvTransferDetail> invTransferDetailList = invTransferDetailService.selectByMainId(id);
		return Result.OK(invTransferDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invTransfer
    */
    @RequiresPermissions("invtransfer:inv_transfer:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvTransfer invTransfer) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InvTransfer> queryWrapper = QueryGenerator.initQueryWrapper(invTransfer, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<InvTransfer> invTransferList = invTransferService.list(queryWrapper);

      // Step.3 组装pageList
      List<InvTransferPage> pageList = new ArrayList<InvTransferPage>();
      for (InvTransfer main : invTransferList) {
          InvTransferPage vo = new InvTransferPage();
          BeanUtils.copyProperties(main, vo);
          List<InvTransferDetail> invTransferDetailList = invTransferDetailService.selectByMainId(main.getId());
          vo.setInvTransferDetailList(invTransferDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "物料调拨列表");
      mv.addObject(NormalExcelConstants.CLASS, InvTransferPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料调拨数据", "导出人:"+sysUser.getRealname(), "物料调拨"));
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
    @RequiresPermissions("invtransfer:inv_transfer:importExcel")
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
              List<InvTransferPage> list = ExcelImportUtil.importExcel(file.getInputStream(), InvTransferPage.class, params);
              for (InvTransferPage page : list) {
                  InvTransfer po = new InvTransfer();
                  BeanUtils.copyProperties(page, po);
                  invTransferService.saveMain(po, page.getInvTransferDetailList());
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
	 @AutoLog(value = "物资调拨-审核/反审核")
	 @ApiOperation(value = "物资调拨-审核/反审核", notes = "物资调拨-审核/反审核")
	 @RequiresPermissions("invtransfer:inv_transfer:audit")
	 @RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
	 public Result<String> audit(@RequestBody AuditRequest auditRequest) throws Exception {
		 List<String> ids = auditRequest.getIds();
		 String type = auditRequest.getType(); // audit 或 reverse

		 Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");


		 int count;
		 if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
			 count = invTransferService.audit(ids);
		 } else {
			 count = invTransferService.unAudit(ids);
		 }


		 return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
	 }
}
