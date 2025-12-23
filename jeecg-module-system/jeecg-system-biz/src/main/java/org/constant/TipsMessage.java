package org.constant;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public enum TipsMessage {


    // 供应商信息
    SUPPLIERS_TYPE_ERROR(""),
    // 客户信息
    CUSTOMERS_TYPE_ERROR("操作失败！[客户分类]不能为空！请检查！"),

    // 物料信息
    METERIALS_CODE_ERROR("操作失败，编码不存在！"),
    METERIALS_CODE_LENGTH_ERROR("操作失败，请选择第三级的分类！"),

    // 用户相关错误信息
    USER_NOT_FOUND("用户未找到"),
    INVALID_PASSWORD("无效的密码"),
    USER_ALREADY_EXISTS("用户已存在"),

    // 权限相关错误信息
    ACCESS_DENIED("权限不足"),
    TOKEN_EXPIRED("令牌已过期"),

    // 数据库相关错误信息
    DB_CONNECTION_FAILED("数据库连接失败"),
    DATA_NOT_FOUND("数据未找到"),
    DATA_SAVE_FAILED("数据保存失败"),

    // 系统相关错误信息
    SYSTEM_ERROR("系统错误"),
    UNKNOWN_ERROR("未知错误");

    /**
     * -- GETTER --
     *  获取错误信息描述
     *
     * @return 错误信息描述
     */
    private final String message;

    /**
     * 私有构造函数
     *
     * @param message 错误信息描述
     */
    TipsMessage(String message) {
        this.message = message;
    }


}
