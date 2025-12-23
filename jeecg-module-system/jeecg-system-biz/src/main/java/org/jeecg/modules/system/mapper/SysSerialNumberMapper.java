package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysSerialNumber;

public interface SysSerialNumberMapper extends BaseMapper<SysSerialNumber> {

    /**
     * 更新流水号
     *
     * @param bizType         单据类型
     * @param currentYearMonth 当前年月
     * @return 更新后的流水号
     */
    @Update("UPDATE sys_serial_number SET current_serial = current_serial + 1 " +
            "WHERE biz_type = #{bizType} AND current_year_month = #{currentYearMonth}")
    int incrementSerialNumber(@Param("bizType") String bizType, @Param("currentYearMonth") String currentYearMonth);

    /**
     * 插入新的流水号记录
     *
     * @param bizType         单据类型
     * @param currentYearMonth 当前年月
     * @param initialSerial    初始流水号
     * @return 插入结果
     */
    @Insert("INSERT INTO sys_serial_number (biz_type, current_year_month, current_serial) " +
            "VALUES (#{bizType}, #{currentYearMonth}, #{initialSerial})")
    int insertSerialNumber(@Param("bizType") String bizType, @Param("currentYearMonth") String currentYearMonth, @Param("initialSerial") int initialSerial);
}