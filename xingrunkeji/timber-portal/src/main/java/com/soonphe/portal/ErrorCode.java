package com.soonphe.portal;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

/**
 * @author: soonphe
 * @date: 2019-06-12 15:26
 * @description:  通用返回错误码
 */
public enum ErrorCode implements IErrorCode {
    TEST(1000, "测试错误编码"),
    EXCEPTION(1001, "测试异常编码");

    private long code;
    private String msg;

    ErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
