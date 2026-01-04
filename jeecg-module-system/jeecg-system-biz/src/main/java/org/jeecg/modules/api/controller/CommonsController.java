package org.jeecg.modules.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.constant.TipsMessage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.api.vo.MaterialInfoVO;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.utils.Assert;

/**
 * 服务化 system模块 对外接口请求类
 * @Author: 舒有敬
 */
@Slf4j
@RestController
@RequestMapping("/sys/commons")
public class CommonsController {

    @Autowired
    private IYujiakejiMaterialsService yujiakejiMaterialsService;

    /**
     * 根据物料编码查询
     *
     * @param materialCode 物料编码
     * @return
     */
    @ApiOperation(value = "公共接口-根据物料编码查询", notes = "根据物料编码查询物料主数据")
    @GetMapping(value = "/queryByMaterialCode")
    public Result<YujiakejiMaterials> queryByMaterialCode(@RequestParam(name = "materialCode", required = true) String materialCode,@RequestParam(name = "warehouseCode", required = true) String warehouseCode) {
        if (StringUtils.isEmpty(materialCode)) {
            return Result.error("物料编码不能为空");
        }
        YujiakejiMaterials yujiakejiMaterials = yujiakejiMaterialsService.queryByMaterialCode(materialCode,warehouseCode);
        return Result.OK(yujiakejiMaterials);
    }
    /**
     * 根据物料编码查询
     *
     * @param materialCode 物料编码
     * @return
     */
    @ApiOperation(value = "公共接口-根据物料编码查询-销售模块专用", notes = "根据物料编码查询物料主数据")
    @GetMapping(value = "/queryByMaterialCodeInSale")
    public Result<YujiakejiMaterials> queryByMaterialCodeInSale(@RequestParam(name = "materialCode", required = true) String materialCode) {
        if (StringUtils.isEmpty(materialCode)) {
            return Result.error("物料编码不能为空");
        }
        YujiakejiMaterials yujiakejiMaterials = yujiakejiMaterialsService.queryByMaterialCodeInSale(materialCode);
        return Result.OK(yujiakejiMaterials);
    }


    /**
     * 根据物料编码查询
     *
     * @param materialCode 物料编码
     * @return
     */
    @ApiOperation(value = "公共接口-根据物料编码查询-包含BOMLIST", notes = "包含BOMLIST")
    @GetMapping(value = "/queryByMaterialInfoAndBomList")
    public Result<MaterialInfoVO> queryByMaterialInfoAndBomList(@RequestParam(name = "materialCode", required = true) String materialCode) {
        if (StringUtils.isEmpty(materialCode)) {
            return Result.error("物料编码不能为空");
        }
        MaterialInfoVO entity = yujiakejiMaterialsService.queryByMaterialInfoAndBomList(materialCode);
        return Result.OK(entity);
    }
}
