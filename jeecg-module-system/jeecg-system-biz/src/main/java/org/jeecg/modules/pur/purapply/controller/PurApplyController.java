package org.jeecg.modules.pur.purapply.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
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
import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import org.jeecg.modules.pur.purapply.entity.PurApply;
import org.jeecg.modules.pur.purapply.vo.PurApplyPage;
import org.jeecg.modules.pur.purapply.service.IPurApplyService;
import org.jeecg.modules.pur.purapply.service.IPurApplyDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
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
 * @Description: 采购申请
 * @Author: 舒有敬
 * @Date: 2025-11-28
 * @Version: V1.0
 */
@Api(tags = "采购申请")
@RestController
@RequestMapping("/purapply/purApply")
@Slf4j
public class PurApplyController {
    @Autowired
    private IPurApplyService purApplyService;
    @Autowired
    private IPurApplyDetailService purApplyDetailService;

    /**
     * 分页列表查询
     *
     * @param purApply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "采购申请-分页列表查询")
    @ApiOperation(value = "采购申请-分页列表查询", notes = "采购申请-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<PurApply>> queryPageList(PurApply purApply,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<PurApply> queryWrapper = QueryGenerator.initQueryWrapper(purApply, req.getParameterMap());
        Page<PurApply> page = new Page<PurApply>(pageNo, pageSize);
        IPage<PurApply> pageList = purApplyService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param purApplyPage
     * @return
     */
    @AutoLog(value = "采购申请-添加")
    @ApiOperation(value = "采购申请-添加", notes = "采购申请-添加")
    @RequiresPermissions("purapply:pur_apply:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody PurApplyPage purApplyPage) {
        PurApply purApply = new PurApply();
        BeanUtils.copyProperties(purApplyPage, purApply);
        purApplyService.saveMain(purApply, purApplyPage.getPurApplyDetailList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param purApplyPage
     * @return
     */
    @AutoLog(value = "采购申请-编辑")
    @ApiOperation(value = "采购申请-编辑", notes = "采购申请-编辑")
    @RequiresPermissions("purapply:pur_apply:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody PurApplyPage purApplyPage) {
        PurApply purApply = new PurApply();
        BeanUtils.copyProperties(purApplyPage, purApply);
        PurApply purApplyEntity = purApplyService.getById(purApply.getId());
        if (purApplyEntity == null) {
            return Result.error("未找到对应数据");
        }
        purApplyService.updateMain(purApply, purApplyPage.getPurApplyDetailList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购申请-通过id删除")
    @ApiOperation(value = "采购申请-通过id删除", notes = "采购申请-通过id删除")
    @RequiresPermissions("purapply:pur_apply:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        PurApply purApplies = purApplyService.getById(id);
        // 判断集合是否为空
        Assert.isTrue(ObjectUtils.isEmpty(purApplies), "操作失败！单据已经删除！");

        // 判断是否存在已审核或状态为1的单据
        boolean hasInvalid = Constants.DICT_AUDIT_STATUS.YES.equals(purApplies.getAudit())||Constants.DICT_AUDIT_STATUS.YES.equals(purApplies.getStatus());

        Assert.isTrue(hasInvalid, "操作失败！存在引用/审核的单据，请注意状态！");
        purApplyService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "采购申请-批量删除")
    @ApiOperation(value = "采购申请-批量删除", notes = "采购申请-批量删除")
    @RequiresPermissions("purapply:pur_apply:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        List<PurApply> purApplies = purApplyService.listByIds(list);
        // 判断集合是否为空
        Assert.isTrue(CollectionUtil.isEmpty(purApplies), "操作失败！单据已经删除！");

        // 判断是否存在已审核或状态为1的单据
        boolean hasInvalid = purApplies.stream().anyMatch(p -> p.getAudit() == 1 || p.getStatus() == 1);

        Assert.isTrue(hasInvalid, "操作失败！存在引用/审核的单据，请注意状态！");
        this.purApplyService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "采购申请-通过id查询")
    @ApiOperation(value = "采购申请-通过id查询", notes = "采购申请-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<PurApply> queryById(@RequestParam(name = "id", required = true) String id) {
        PurApply purApply = purApplyService.getById(id);
        if (purApply == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(purApply);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "采购申请明细通过主表ID查询")
    @ApiOperation(value = "采购申请明细主表ID查询", notes = "采购申请明细-通主表ID查询")
    @GetMapping(value = "/queryPurApplyDetailByMainId")
    public Result<List<PurApplyDetail>> queryPurApplyDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<PurApplyDetail> purApplyDetailList = purApplyDetailService.selectByMainId(id);
        return Result.OK(purApplyDetailList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param purApply
     */
    @RequiresPermissions("purapply:pur_apply:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurApply purApply) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<PurApply> queryWrapper = QueryGenerator.initQueryWrapper(purApply, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //Step.2 获取导出数据
        List<PurApply> purApplyList = purApplyService.list(queryWrapper);

        // Step.3 组装pageList
        List<PurApplyPage> pageList = new ArrayList<PurApplyPage>();
        for (PurApply main : purApplyList) {
            PurApplyPage vo = new PurApplyPage();
            BeanUtils.copyProperties(main, vo);
            List<PurApplyDetail> purApplyDetailList = purApplyDetailService.selectByMainId(main.getId());
            vo.setPurApplyDetailList(purApplyDetailList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "采购申请列表");
        mv.addObject(NormalExcelConstants.CLASS, PurApplyPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购申请数据", "导出人:" + sysUser.getRealname(), "采购申请"));
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
    @RequiresPermissions("purapply:pur_apply:importExcel")
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
                List<PurApplyPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurApplyPage.class, params);
                for (PurApplyPage page : list) {
                    PurApply po = new PurApply();
                    BeanUtils.copyProperties(page, po);
                    purApplyService.saveMain(po, page.getPurApplyDetailList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
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
    @AutoLog(value = "采购申请-审核/反审核")
    @ApiOperation(value = "采购申请-审核/反审核", notes = "采购申请-审核/反审核")
    @RequiresPermissions("purapply:pur_apply:audit")
    @RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> audit(@RequestBody AuditRequest auditRequest) {
        List<String> ids = auditRequest.getIds();
        String type = auditRequest.getType(); // audit 或 reverse

        Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

        int count;
        if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
            count = purApplyService.audit(ids);
        } else {
            count = purApplyService.unAudit(ids);
        }


        return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！", count));
    }
}
