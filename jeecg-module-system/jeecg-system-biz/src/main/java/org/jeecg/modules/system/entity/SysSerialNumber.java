package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_serial_number")
public class SysSerialNumber {

    private Long id; // 主键ID
    private String bizType; // 单据类型，如 ZY, XY
    private String currentYearMonth; // 当前年月，格式为 YYYYMM
    private Integer currentSerial; // 当前流水号
}