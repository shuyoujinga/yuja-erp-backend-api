package org.jeecg.modules.inv.invstocktake.controller;

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
import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import org.jeecg.modules.inv.invstocktake.entity.InvStockTake;
import org.jeecg.modules.inv.invstocktake.vo.InvStockTakePage;
import org.jeecg.modules.inv.invstocktake.service.IInvStockTakeService;
import org.jeecg.modules.inv.invstocktake.service.IInvStockTakeDetailService;
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
 * @Description: 物料盘点
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Api(tags="物料盘点")
@RestController
@RequestMapping("/invstocktake/invStockTake")
@Slf4j
public class InvStockTakeController {
	@Autowired
	private IInvStockTakeService invStockTakeService;
	@Autowired
	private IInvStockTakeDetailService invStockTakeDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invStockTake
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "物料盘点-分页列表查询")
	@ApiOperation(value="物料盘点-分页列表查询", notes="物料盘点-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvStockTake>> queryPageList(InvStockTake invStockTake,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvStockTake> queryWrapper = QueryGenerator.initQueryWrapper(invStockTake, req.getParameterMap());
		Page<InvStockTake> page = new Page<InvStockTake>(pageNo, pageSize);
		IPage<InvStockTake> pageList = invStockTakeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invStockTakePage
	 * @return
	 */
	@AutoLog(value = "物料盘点-添加")
	@ApiOperation(value="物料盘点-添加", notes="物料盘点-添加")
    @RequiresPermissions("invstocktake:inv_stock_take:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvStockTakePage invStockTakePage) {
		InvStockTake invStockTake = new InvStockTake();
		BeanUtils.copyProperties(invStockTakePage, invStockTake);
		invStockTakeService.saveMain(invStockTake, invStockTakePage.getInvStockTakeDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invStockTakePage
	 * @return
	 */
	@AutoLog(value = "物料盘点-编辑")
	@ApiOperation(value="物料盘点-编辑", notes="物料盘点-编辑")
    @RequiresPermissions("invstocktake:inv_stock_take:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvStockTakePage invStockTakePage) {
		InvStockTake invStockTake = new InvStockTake();
		BeanUtils.copyProperties(invStockTakePage, invStockTake);
		InvStockTake invStockTakeEntity = invStockTakeService.getById(invStockTake.getId());
		if(invStockTakeEntity==null) {
			return Result.error("未找到对应数据");
		}
		invStockTakeService.updateMain(invStockTake, invStockTakePage.getInvStockTakeDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料盘点-通过id删除")
	@ApiOperation(value="物料盘点-通过id删除", notes="物料盘点-通过id删除")
    @RequiresPermissions("invstocktake:inv_stock_take:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invStockTakeService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料盘点-批量删除")
	@ApiOperation(value="物料盘点-批量删除", notes="物料盘点-批量删除")
    @RequiresPermissions("invstocktake:inv_stock_take:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invStockTakeService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料盘点-通过id查询")
	@ApiOperation(value="物料盘点-通过id查询", notes="物料盘点-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvStockTake> queryById(@RequestParam(name="id",required=true) String id) {
		InvStockTake invStockTake = invStockTakeService.getById(id);
		if(invStockTake==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invStockTake);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存盘点_明细通过主表ID查询")
	@ApiOperation(value="库存盘点_明细主表ID查询", notes="库存盘点_明细-通主表ID查询")
	@GetMapping(value = "/queryInvStockTakeDetailByMainId")
	public Result<List<InvStockTakeDetail>> queryInvStockTakeDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InvStockTakeDetail> invStockTakeDetailList = invStockTakeDetailService.selectByMainId(id);
		return Result.OK(invStockTakeDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invStockTake
    */
    @RequiresPermissions("invstocktake:inv_stock_take:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvStockTake invStockTake) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InvStockTake> queryWrapper = QueryGenerator.initQueryWrapper(invStockTake, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<InvStockTake> invStockTakeList = invStockTakeService.list(queryWrapper);

      // Step.3 组装pageList
      List<InvStockTakePage> pageList = new ArrayList<InvStockTakePage>();
      for (InvStockTake main : invStockTakeList) {
          InvStockTakePage vo = new InvStockTakePage();
          BeanUtils.copyProperties(main, vo);
          List<InvStockTakeDetail> invStockTakeDetailList = invStockTakeDetailService.selectByMainId(main.getId());
          vo.setInvStockTakeDetailList(invStockTakeDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "物料盘点列表");
      mv.addObject(NormalExcelConstants.CLASS, InvStockTakePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料盘点数据", "导出人:"+sysUser.getRealname(), "物料盘点"));
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
    @RequiresPermissions("invstocktake:inv_stock_take:importExcel")
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
              List<InvStockTakePage> list = ExcelImportUtil.importExcel(file.getInputStream(), InvStockTakePage.class, params);
              for (InvStockTakePage page : list) {
                  InvStockTake po = new InvStockTake();
                  BeanUtils.copyProperties(page, po);
                  invStockTakeService.saveMain(po, page.getInvStockTakeDetailList());
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
