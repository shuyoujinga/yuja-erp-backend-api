package org.jeecg.modules.maindata.materials.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.constant.Constants;
import org.constant.TipsMessage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.utils.Assert;

/**
 * @Description: 物料主数据
 * @Author: 舒有敬
 * @Date: 2024-06-08
 * @Version: V1.0
 */
@Api(tags = "物料主数据")
@RestController
@RequestMapping("/materials/yujiakejiMaterials")
@Slf4j
public class YujiakejiMaterialsController extends JeecgController<YujiakejiMaterials, IYujiakejiMaterialsService> {
    @Autowired
    private IYujiakejiMaterialsService yujiakejiMaterialsService;
    @Autowired
    private ISysCategoryService sysCategoryService;

    /**
     * 分页列表查询
     *
     * @param yujiakejiMaterials
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "物料主数据-分页列表查询")
    @ApiOperation(value = "物料主数据-分页列表查询", notes = "物料主数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<YujiakejiMaterials>> queryPageList(YujiakejiMaterials yujiakejiMaterials,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        // 分类字典处理
        String materialCategory = yujiakejiMaterials.getMaterialCategory();
        if (!StringUtils.isEmpty(materialCategory)) {
            SysCategory category = sysCategoryService.getById(materialCategory);
            yujiakejiMaterials.setMaterialCategory(category.getCode() + "*");
        }
        QueryWrapper<YujiakejiMaterials> queryWrapper = QueryGenerator.initQueryWrapper(yujiakejiMaterials, req.getParameterMap());
        Page<YujiakejiMaterials> page = new Page<YujiakejiMaterials>(pageNo, pageSize);
        IPage<YujiakejiMaterials> pageList = yujiakejiMaterialsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param yujiakejiMaterials
     * @return
     */
    @AutoLog(value = "物料主数据-添加")
    @ApiOperation(value = "物料主数据-添加", notes = "物料主数据-添加")
    @RequiresPermissions("materials:yujiakeji_materials:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody YujiakejiMaterials yujiakejiMaterials) {
        yujiakejiMaterialsService.saveEntity(yujiakejiMaterials);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param yujiakejiMaterials
     * @return
     */
    @AutoLog(value = "物料主数据-编辑")
    @ApiOperation(value = "物料主数据-编辑", notes = "物料主数据-编辑")
    @RequiresPermissions("materials:yujiakeji_materials:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody YujiakejiMaterials yujiakejiMaterials) {
        /**
         * 校验入参
         */
        SysCategory category = sysCategoryService.getById(yujiakejiMaterials.getMaterialCategory());
        Assert.isTrue(ObjectUtils.isArray(category), TipsMessage.METERIALS_CODE_ERROR.getMessage());
        Assert.isTrue(category.getCode().length() != 6, TipsMessage.METERIALS_CODE_LENGTH_ERROR.getMessage());
        yujiakejiMaterials.setMaterialCategory(category.getCode());
        yujiakejiMaterialsService.updateById(yujiakejiMaterials);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料主数据-通过id删除")
    @ApiOperation(value = "物料主数据-通过id删除", notes = "物料主数据-通过id删除")
    @RequiresPermissions("materials:yujiakeji_materials:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        yujiakejiMaterialsService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物料主数据-批量删除")
    @ApiOperation(value = "物料主数据-批量删除", notes = "物料主数据-批量删除")
    @RequiresPermissions("materials:yujiakeji_materials:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.yujiakejiMaterialsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "物料主数据-通过id查询")
    @ApiOperation(value = "物料主数据-通过id查询", notes = "物料主数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<YujiakejiMaterials> queryById(@RequestParam(name = "id", required = true) String id) {
        YujiakejiMaterials yujiakejiMaterials = yujiakejiMaterialsService.getById(id);
        if (yujiakejiMaterials == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(yujiakejiMaterials);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param yujiakejiMaterials
     */
    @RequiresPermissions("materials:yujiakeji_materials:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YujiakejiMaterials yujiakejiMaterials) {
        return super.exportXls(request, yujiakejiMaterials, YujiakejiMaterials.class, "物料主数据");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("materials:yujiakeji_materials:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YujiakejiMaterials.class);
    }

    /**
     * 根据物料编码查询
     *
     * @param materialCode 物料编码
     * @return
     */
    @ApiOperation(value = "物料主数据-根据物料编码查询", notes = "根据物料编码查询物料主数据")
    @GetMapping(value = "/queryByCode")
    public Result<YujiakejiMaterials> queryByCode(@RequestParam(name = "materialCode", required = true) String materialCode) {
        if (StringUtils.isEmpty(materialCode)) {
            return Result.error("物料编码不能为空");
        }
        QueryWrapper<YujiakejiMaterials> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YujiakejiMaterials::getMaterialCode, materialCode).last(Constants.CONST_SQL.LIMIT_ONE);
        YujiakejiMaterials yujiakejiMaterials = yujiakejiMaterialsService.getOne(queryWrapper);
        Assert.isTrue(ObjectUtils.isArray(yujiakejiMaterials), TipsMessage.METERIALS_CODE_ERROR.getMessage());
        return Result.OK(yujiakejiMaterials);
    }


}
