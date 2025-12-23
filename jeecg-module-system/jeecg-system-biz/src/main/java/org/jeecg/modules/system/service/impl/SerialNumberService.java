package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.system.entity.SysSerialNumber;
import org.jeecg.modules.system.mapper.SysSerialNumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.YearMonth;

@Service
public class SerialNumberService {

    @Resource
    private SysSerialNumberMapper sysSerialNumberMapper;

    /**
     * 生成规则编号
     *
     * @param bizType 单据类型，如 ZY, XY
     * @return 生成的规则编号，格式为：YYYYMM + 单据类型 + 4位流水号
     */
    @Transactional
    public String generateRuleCode(String bizType) {
        // 获取当前年月，格式为 YYYYMM
        String currentYearMonth = YearMonth.now().toString().replace("-", "");

        // 查询当前单据类型和年月的记录
        QueryWrapper<SysSerialNumber> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("biz_type", bizType)
                    .eq("current_year_month", currentYearMonth);
        SysSerialNumber serialNumber = sysSerialNumberMapper.selectOne(queryWrapper);

        int currentSerial;
        if (serialNumber == null) {
            // 如果记录不存在，插入新记录，初始流水号为 1
            sysSerialNumberMapper.insertSerialNumber(bizType, currentYearMonth, 1);
            currentSerial = 1;
        } else {
            // 如果记录存在，更新流水号并获取更新后的值
            sysSerialNumberMapper.incrementSerialNumber(bizType, currentYearMonth);
            currentSerial = serialNumber.getCurrentSerial() + 1;
        }

        // 格式化流水号为 4 位数字，不足补零
        String serialPart = String.format("%04d", currentSerial);

        // 拼接规则编号
        return currentYearMonth + bizType + serialPart;
    }
}