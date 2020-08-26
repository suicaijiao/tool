package com.soonphe.portal.commons.golbal.result;

/**
 * @author: soonphe
 * @date: 2017-11-11 17:14
 * @description:  base：统一状态码
 */
public enum ResultCode {

    /**
     * 基础错误码
     */
    SUCCESS(200, "操作成功"),
    SYS_COM_UNKONW(500, "系统内部异常"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    VALIDATE_FAILED(404, "参数检验失败"),
    EXCEPTION(301, "系统异常"),


    /**
     * USER:100-199
     */
    USER_IS_NULL(101, "用户不存在"),
    PASSWORD_FAIL(102, "您的密码错误!"),
    CAPTCH_FAIL(103, "验证码错误"),
    AUDITFAIL(104, "禁用"),
    AUDITING(105, "审核中"),
    MENU_FAIL(106, "菜单创建失败"),
    REDIS_IS_NULL(107, "redis中不存在此数据"),
    PHONE_REPEAT(108, "您的手机号已被注册"),
    PHONE_NOT(109, "您的手机号还没有注册"),
    ISBLACK(113, "已加入黑名单"),
    NOBLACK(114, "未加入黑名单"),
    DEVICES_REPEAT(701, "此设备已被注册"),

    /**
     * file
     */
    FILEMISS(118, "文件不存在"),
    FILETOOBIG(119, "文件过大"),
    FILEUPLOADFAIL(120, "文件上传失败");
    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
