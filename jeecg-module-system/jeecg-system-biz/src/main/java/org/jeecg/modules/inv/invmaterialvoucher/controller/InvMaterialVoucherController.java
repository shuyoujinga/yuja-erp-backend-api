package org.jeecg.modules.inv.invmaterialvoucher.controller;

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

import org.jeecg.common.aspect.annotation.AutoDict;
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
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.vo.InvMaterialVoucherPage;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherService;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherDetailService;
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
 * @Description: 物料凭证
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Api(tags="物料凭证")
@RestController
@RequestMapping("/invmaterialvoucher/invMaterialVoucher")
@Slf4j
public class InvMaterialVoucherController {
	@Autowired
	private IInvMaterialVoucherService invMaterialVoucherService;
	@Autowired
	private IInvMaterialVoucherDetailService invMaterialVoucherDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invMaterialVoucher
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "物料凭证-分页列表查询")
	@ApiOperation(value="物料凭证-分页列表查询", notes="物料凭证-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvMaterialVoucher>> queryPageList(InvMaterialVoucher invMaterialVoucher,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvMaterialVoucher> queryWrapper = QueryGenerator.initQueryWrapper(invMaterialVoucher, req.getParameterMap());
		Page<InvMaterialVoucher> page = new Page<InvMaterialVoucher>(pageNo, pageSize);
		IPage<InvMaterialVoucher> pageList = invMaterialVoucherService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invMaterialVoucherPage
	 * @return
	 */
	@AutoLog(value = "物料凭证-添加")
	@ApiOperation(value="物料凭证-添加", notes="物料凭证-添加")
    @RequiresPermissions("invmaterialvoucher:inv_material_voucher:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvMaterialVoucherPage invMaterialVoucherPage) throws Exception {
		InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher();
		BeanUtils.copyProperties(invMaterialVoucherPage, invMaterialVoucher);
		invMaterialVoucherService.saveMain(invMaterialVoucher, invMaterialVoucherPage.getInvMaterialVoucherDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invMaterialVoucherPage
	 * @return
	 */
	@AutoLog(value = "物料凭证-编辑")
	@ApiOperation(value="物料凭证-编辑", notes="物料凭证-编辑")
    @RequiresPermissions("invmaterialvoucher:inv_material_voucher:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvMaterialVoucherPage invMaterialVoucherPage) throws Exception {
		InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher();
		BeanUtils.copyProperties(invMaterialVoucherPage, invMaterialVoucher);
		InvMaterialVoucher invMaterialVoucherEntity = invMaterialVoucherService.getById(invMaterialVoucher.getId());
		if(invMaterialVoucherEntity==null) {
			return Result.error("未找到对应数据");
		}
		invMaterialVoucherService.updateMain(invMaterialVoucher, invMaterialVoucherPage.getInvMaterialVoucherDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料凭证-通过id删除")
	@ApiOperation(value="物料凭证-通过id删除", notes="物料凭证-通过id删除")
    @RequiresPermissions("invmaterialvoucher:inv_material_voucher:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invMaterialVoucherService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料凭证-批量删除")
	@ApiOperation(value="物料凭证-批量删除", notes="物料凭证-批量删除")
    @RequiresPermissions("invmaterialvoucher:inv_material_voucher:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invMaterialVoucherService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料凭证-通过id查询")
	@ApiOperation(value="物料凭证-通过id查询", notes="物料凭证-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvMaterialVoucher> queryById(@RequestParam(name="id",required=true) String id) {
		InvMaterialVoucher invMaterialVoucher = invMaterialVoucherService.getById(id);
		if(invMaterialVoucher==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invMaterialVoucher);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料凭证_明细通过主表ID查询")
	@ApiOperation(value="物料凭证_明细主表ID查询", notes="物料凭证_明细-通主表ID查询")
	@GetMapping(value = "/queryInvMaterialVoucherDetailByMainId")
	@AutoDict
	public Result<List<InvMaterialVoucherDetail>> queryInvMaterialVoucherDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InvMaterialVoucherDetail> invMaterialVoucherDetailList = invMaterialVoucherDetailService.selectByMainId(id);
		return Result.OK(invMaterialVoucherDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invMaterialVoucher
    */
    @RequiresPermissions("invmaterialvoucher:inv_material_voucher:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvMaterialVoucher invMaterialVoucher) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InvMaterialVoucher> queryWrapper = QueryGenerator.initQueryWrapper(invMaterialVoucher, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<InvMaterialVoucher> invMaterialVoucherList = invMaterialVoucherService.list(queryWrapper);

      // Step.3 组装pageList
      List<InvMaterialVoucherPage> pageList = new ArrayList<InvMaterialVoucherPage>();
      for (InvMaterialVoucher main : invMaterialVoucherList) {
          InvMaterialVoucherPage vo = new InvMaterialVoucherPage();
          BeanUtils.copyProperties(main, vo);
          List<InvMaterialVoucherDetail> invMaterialVoucherDetailList = invMaterialVoucherDetailService.selectByMainId(main.getId());
          vo.setInvMaterialVoucherDetailList(invMaterialVoucherDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "物料凭证列表");
      mv.addObject(NormalExcelConstants.CLASS, InvMaterialVoucherPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料凭证数据", "导出人:"+sysUser.getRealname(), "物料凭证"));
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
    @RequiresPermissions("invmaterialvoucher:inv_material_voucher:importExcel")
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
              List<InvMaterialVoucherPage> list = ExcelImportUtil.importExcel(file.getInputStream(), InvMaterialVoucherPage.class, params);
              for (InvMaterialVoucherPage page : list) {
                  InvMaterialVoucher po = new InvMaterialVoucher();
                  BeanUtils.copyProperties(page, po);
                  invMaterialVoucherService.saveMain(po, page.getInvMaterialVoucherDetailList());
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
