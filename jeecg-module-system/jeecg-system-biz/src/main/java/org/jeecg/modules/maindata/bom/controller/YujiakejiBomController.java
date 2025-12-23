package org.jeecg.modules.maindata.bom.controller;

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
import org.jeecg.modules.maindata.bom.vo.YujiakejiBomVO;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
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
import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import org.jeecg.modules.maindata.bom.vo.YujiakejiBomPage;
import org.jeecg.modules.maindata.bom.service.IYujiakejiBomService;
import org.jeecg.modules.maindata.bom.service.IYujiakejiBomDetailService;
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
 * @Description: 材料清单_主表
 * @Author: 舒有敬
 * @Date: 2025-11-27
 * @Version: V1.0
 */
@Api(tags = "材料清单_主表")
@RestController
@RequestMapping("/bom/yujiakejiBom")
@Slf4j
public class YujiakejiBomController {
    @Autowired
    private IYujiakejiBomService yujiakejiBomService;
    @Autowired
    private IYujiakejiBomDetailService yujiakejiBomDetailService;

    /**
     * 分页列表查询
     *
     * @param yujiakejiBom
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "材料清单_主表-分页列表查询")
    @ApiOperation(value = "材料清单_主表-分页列表查询", notes = "材料清单_主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<YujiakejiBom>> queryPageList(YujiakejiBom yujiakejiBom,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        QueryWrapper<YujiakejiBom> queryWrapper = QueryGenerator.initQueryWrapper(yujiakejiBom, req.getParameterMap());
        Page<YujiakejiBom> page = new Page<YujiakejiBom>(pageNo, pageSize);
        IPage<YujiakejiBom> pageList = yujiakejiBomService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param yujiakejiBomPage
     * @return
     */
    @AutoLog(value = "材料清单_主表-添加")
    @ApiOperation(value = "材料清单_主表-添加", notes = "材料清单_主表-添加")
    @RequiresPermissions("bom:yujiakeji_bom:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody YujiakejiBomPage yujiakejiBomPage) {
        YujiakejiBom yujiakejiBom = new YujiakejiBom();
        BeanUtils.copyProperties(yujiakejiBomPage, yujiakejiBom);
        yujiakejiBomService.saveMain(yujiakejiBom, yujiakejiBomPage.getYujiakejiBomDetailList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param yujiakejiBomPage
     * @return
     */
    @AutoLog(value = "材料清单_主表-编辑")
    @ApiOperation(value = "材料清单_主表-编辑", notes = "材料清单_主表-编辑")
    @RequiresPermissions("bom:yujiakeji_bom:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody YujiakejiBomPage yujiakejiBomPage) {
        YujiakejiBom yujiakejiBom = new YujiakejiBom();
        BeanUtils.copyProperties(yujiakejiBomPage, yujiakejiBom);
        YujiakejiBom yujiakejiBomEntity = yujiakejiBomService.getById(yujiakejiBom.getId());
        if (yujiakejiBomEntity == null) {
            return Result.error("未找到对应数据");
        }
        yujiakejiBomService.updateMain(yujiakejiBom, yujiakejiBomPage.getYujiakejiBomDetailList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "材料清单_主表-通过id删除")
    @ApiOperation(value = "材料清单_主表-通过id删除", notes = "材料清单_主表-通过id删除")
    @RequiresPermissions("bom:yujiakeji_bom:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        yujiakejiBomService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "材料清单_主表-批量删除")
    @ApiOperation(value = "材料清单_主表-批量删除", notes = "材料清单_主表-批量删除")
    @RequiresPermissions("bom:yujiakeji_bom:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.yujiakejiBomService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "材料清单_主表-通过id查询")
    @ApiOperation(value = "材料清单_主表-通过id查询", notes = "材料清单_主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<YujiakejiBom> queryById(@RequestParam(name = "id", required = true) String id) {
        YujiakejiBom yujiakejiBom = yujiakejiBomService.getById(id);
        if (yujiakejiBom == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(yujiakejiBom);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "材料清单_明细表通过主表ID查询")
    @ApiOperation(value = "材料清单_明细表主表ID查询", notes = "材料清单_明细表-通主表ID查询")
    @GetMapping(value = "/queryYujiakejiBomDetailByMainId")
    public Result<List<YujiakejiBomDetail>> queryYujiakejiBomDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<YujiakejiBomDetail> yujiakejiBomDetailList = yujiakejiBomDetailService.selectByMainId(id);
        return Result.OK(yujiakejiBomDetailList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param yujiakejiBom
     */
    @RequiresPermissions("bom:yujiakeji_bom:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YujiakejiBom yujiakejiBom) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<YujiakejiBom> queryWrapper = QueryGenerator.initQueryWrapper(yujiakejiBom, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //Step.2 获取导出数据
        List<YujiakejiBom> yujiakejiBomList = yujiakejiBomService.list(queryWrapper);

        // Step.3 组装pageList
        List<YujiakejiBomPage> pageList = new ArrayList<YujiakejiBomPage>();
        for (YujiakejiBom main : yujiakejiBomList) {
            YujiakejiBomPage vo = new YujiakejiBomPage();
            BeanUtils.copyProperties(main, vo);
            List<YujiakejiBomDetail> yujiakejiBomDetailList = yujiakejiBomDetailService.selectByMainId(main.getId());
            vo.setYujiakejiBomDetailList(yujiakejiBomDetailList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "材料清单_主表列表");
        mv.addObject(NormalExcelConstants.CLASS, YujiakejiBomPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("材料清单_主表数据", "导出人:" + sysUser.getRealname(), "材料清单_主表"));
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
    @RequiresPermissions("bom:yujiakeji_bom:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(0);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<YujiakejiBomVO> list = ExcelImportUtil.importExcel(file.getInputStream(), YujiakejiBomVO.class, params);
                Map<String, YujiakejiBom> mainDataMap= new HashMap<>();
                Map<String, List<YujiakejiBomDetail>> detailDataMap= new HashMap<>();
                for (YujiakejiBomVO yujiakejiBomVO : list) {
                    YujiakejiBom yujiakejiBom = new YujiakejiBom();
                    yujiakejiBom.setId(yujiakejiBomVO.getId());
                    yujiakejiBom.setCreateBy("admin");
                    yujiakejiBom.setCreateTime(new Date());
                    yujiakejiBom.setMaterialCode(yujiakejiBomVO.getMaterialCode());
                    yujiakejiBom.setBomType(1);
                    mainDataMap.putIfAbsent(yujiakejiBom.getId(), yujiakejiBom);
                    if (detailDataMap.containsKey(yujiakejiBom.getId())) {
                        List<YujiakejiBomDetail> yujiakejiBomDetails = detailDataMap.get(yujiakejiBom.getId());
                        YujiakejiBomDetail yujiakejiBomDetail = new YujiakejiBomDetail();
                        yujiakejiBomDetail.setPid(yujiakejiBomVO.getId());
                        yujiakejiBomDetail.setMaterialCode(yujiakejiBomVO.getMaterialCodeChild());
                        yujiakejiBomDetails.add(yujiakejiBomDetail);
                        detailDataMap.put(yujiakejiBom.getId(), yujiakejiBomDetails);
                    }else{
                        List<YujiakejiBomDetail> yujiakejiBomDetails = new ArrayList<>();
                        YujiakejiBomDetail yujiakejiBomDetail = new YujiakejiBomDetail();
                        yujiakejiBomDetail.setPid(yujiakejiBomVO.getId());
                        yujiakejiBomDetail.setMaterialCode(yujiakejiBomVO.getMaterialCodeChild());
                        yujiakejiBomDetails.add(yujiakejiBomDetail);
                        detailDataMap.put(yujiakejiBom.getId(), yujiakejiBomDetails);
                    }
                }
                for (String s : mainDataMap.keySet()) {
                    YujiakejiBom yujiakejiBom = mainDataMap.get(s);
                    List<YujiakejiBomDetail> yujiakejiBomDetails = detailDataMap.get(s);
                    yujiakejiBomService.saveMain(yujiakejiBom, yujiakejiBomDetails);
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
/***
 *    // 获取上传文件对象
 *             MultipartFile file = entity.getValue();
 *             ImportParams params = new ImportParams();
 *             params.setTitleRows(2);
 *             params.setHeadRows(1);
 *             params.setNeedSave(true);
 *             try {
 *                 List<YujiakejiBomPage> list = ExcelImportUtil.importExcel(file.getInputStream(), YujiakejiBomPage.class, params);
 *                 for (YujiakejiBomPage page : list) {
 *                     YujiakejiBom po = new YujiakejiBom();
 *                     BeanUtils.copyProperties(page, po);
 *                     yujiakejiBomService.saveMain(po, page.getYujiakejiBomDetailList());
 *                 }
 *                 return Result.OK("文件导入成功！数据行数:" + list.size());
 *             } catch (Exception e) {
 *                 log.error(e.getMessage(), e);
 *                 return Result.error("文件导入失败:" + e.getMessage());
 *             } finally {
 *                 try {
 *                     file.getInputStream().close();
 *                 } catch (IOException e) {
 *                     e.printStackTrace();
 *                 }
 *             }
 */

    /**
     * 审核/反审核
     *
     * @param auditRequest 审核请求参数，包含ID列表和操作类型（audit/reverse）
     * @return
     */
    @AutoLog(value = "材料清单_主表-审核/反审核")
    @ApiOperation(value = "材料清单_主表-审核/反审核", notes = "材料清单_主表-审核/反审核")
    @RequiresPermissions("bom:yujiakeji_bom:audit")
    @RequestMapping(value = "/audit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> audit(@RequestBody AuditRequest auditRequest) {
        List<String> ids = auditRequest.getIds();
        String type = auditRequest.getType(); // audit 或 reverse

        Assert.isTrue(CollectionUtil.isEmpty(ids), "请选择要操作的记录");

        int count;
        if (Constants.DICT_AUDIT_FLAG.AUDIT.equals(type)) {
           count= yujiakejiBomService.audit(ids);
        } else {
           count=yujiakejiBomService.unAudit(ids);
        }


        return Result.OK(String.format("操作成功，共计完成对%s条数据的操作！",count));
    }




}
