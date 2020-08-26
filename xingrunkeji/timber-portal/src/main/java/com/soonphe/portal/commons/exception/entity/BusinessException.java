package com.soonphe.portal.commons.exception.entity;

import com.soonphe.portal.commons.golbal.result.ResultCode;

/**
 * 业务异常
 * @Class:oces-aess-service
 * @Author: wangzipeng
 * @Description:
 * @Date:Created in 2018/3/23.
 * @Modified By:wangzipeng
 */

public class BusinessException extends RuntimeException {

    //业务类型
    private String bizType;
    //业务代码
    private Integer bizCode;

    public BusinessException(ResultCode resultEnum) {
        super(resultEnum.getMessage());

        this.bizType = "";
        this.bizCode = resultEnum.getCode();
    }

    public BusinessException(ResultCode resultEnum, String message) {
        super(message);

        this.bizType = "";
        this.bizCode = resultEnum.getCode();
    }

    public BusinessException(ResultCode resultEnum, String message, String bizType) {
        super(message);

        this.bizType = bizType;
        this.bizCode = resultEnum.getCode();
    }


    public BusinessException(ResultCode resultEnum, Throwable cause) {
        super(resultEnum.getMessage(), cause);

        this.bizType = "";
        this.bizCode = resultEnum.getCode();
    }

    public BusinessException(ResultCode resultEnum, String message, Throwable cause) {
        super(message, cause);

        this.bizType = "";
        this.bizCode = resultEnum.getCode();
    }

    public BusinessException(ResultCode resultEnum, String message, String bizType, Throwable cause) {
        super(message, cause);

        this.bizType = bizType;
        this.bizCode = resultEnum.getCode();
    }

    public String getBizType() {
        return bizType;
    }

    public Integer getBizCode() {
        return bizCode;
    }
}