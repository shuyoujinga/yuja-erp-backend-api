package org.jeecg.modules.maindata.suppliers.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.constant.Constants;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.maindata.suppliers.service.IYujiakejiSuppliersService;
import org.jeecg.modules.maindata.suppliers.entity.YujiakejiSuppliers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.utils.Assert;
import org.utils.CommonMethodUtils;

/**
 * @Description: 供应商管理
 * @Author: 舒有敬
 * @Date: 2024-06-02
 * @Version: V1.0
 */
@Api(tags = "供应商管理")
@RestController
@RequestMapping("/suppliers/yujiakejiSuppliers")
@Slf4j
public class YujiakejiSuppliersController extends JeecgController<YujiakejiSuppliers, IYujiakejiSuppliersService> {
    @Autowired
    private IYujiakejiSuppliersService yujiakejiSuppliersService;

    /**
     * 分页列表查询
     *
     * @param yujiakejiSuppliers
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "供应商管理-分页列表查询")
    @ApiOperation(value = "供应商管理-分页列表查询", notes = "供应商管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<YujiakejiSuppliers>> queryPageList(YujiakejiSuppliers yujiakejiSuppliers,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        QueryWrapper<YujiakejiSuppliers> queryWrapper = QueryGenerator.initQueryWrapper(yujiakejiSuppliers, req.getParameterMap());
        Page<YujiakejiSuppliers> page = new Page<YujiakejiSuppliers>(pageNo, pageSize);
        IPage<YujiakejiSuppliers> pageList = yujiakejiSuppliersService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param yujiakejiSuppliers
     * @return
     */
    @AutoLog(value = "供应商管理-添加")
    @ApiOperation(value = "供应商管理-添加", notes = "供应商管理-添加")
    @RequiresPermissions("suppliers:yujiakeji_suppliers:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody YujiakejiSuppliers yujiakejiSuppliers) {
        yujiakejiSuppliersService.saveEntity(yujiakejiSuppliers);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param yujiakejiSuppliers
     * @return
     */
    @AutoLog(value = "供应商管理-编辑")
    @ApiOperation(value = "供应商管理-编辑", notes = "供应商管理-编辑")
    @RequiresPermissions("suppliers:yujiakeji_suppliers:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody YujiakejiSuppliers yujiakejiSuppliers) {
        yujiakejiSuppliersService.updateById(yujiakejiSuppliers);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "供应商管理-通过id删除")
    @ApiOperation(value = "供应商管理-通过id删除", notes = "供应商管理-通过id删除")
    @RequiresPermissions("suppliers:yujiakeji_suppliers:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        yujiakejiSuppliersService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "供应商管理-批量删除")
    @ApiOperation(value = "供应商管理-批量删除", notes = "供应商管理-批量删除")
    @RequiresPermissions("suppliers:yujiakeji_suppliers:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.yujiakejiSuppliersService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "供应商管理-通过id查询")
    @ApiOperation(value = "供应商管理-通过id查询", notes = "供应商管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<YujiakejiSuppliers> queryById(@RequestParam(name = "id", required = true) String id) {
        YujiakejiSuppliers yujiakejiSuppliers = yujiakejiSuppliersService.getById(id);
        if (yujiakejiSuppliers == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(yujiakejiSuppliers);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param yujiakejiSuppliers
     */
    @RequiresPermissions("suppliers:yujiakeji_suppliers:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YujiakejiSuppliers yujiakejiSuppliers) {
        return super.exportXls(request, yujiakejiSuppliers, YujiakejiSuppliers.class, "供应商管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("suppliers:yujiakeji_suppliers:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YujiakejiSuppliers.class);
    }

}
