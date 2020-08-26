package com.soonphe.portal.commons.golbal.result;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * 通用返回对象
 * Created by wangzipeng on 2019/9/3.
 */
@Getter
@Setter
@ApiModel(value = "通用返回对象")
public class ResponseResult<T> {

    @ApiModelProperty(value = "响应编码")
    private Integer code;

    @ApiModelProperty(value = "响应信息")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    private String ext = "";

    protected ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    protected ResponseResult(Integer code, String message, T data, String ext) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.ext = ext;
    }

    protected ResponseResult() {
    }


    /**
     * 成功返回结果
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ResponseResult<T> success(T data, String message, String ext) {
        return new ResponseResult<T>(ResultCode.SUCCESS.getCode(), message, data, ext);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // 成功返回 分页
    public static ResponseResult<HashMap<String, Object>> success(String key, PageInfo page) {
        HashMap<String, Object> r = Maps.newHashMapWithExpectedSize(5);
        r.put(key, page.getList());
        r.put("currentPage", page.getPageNum());
        r.put("pageSize", page.getPageSize());
        r.put("totalPages", page.getPages());
        r.put("totalRecords", page.getTotal());
        ResponseResult<HashMap<String, Object>> success = success(r);
        return success;
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        return new ResponseResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> ResponseResult<T> failed() {
        return failed(ResultCode.SYS_COM_UNKONW);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ResponseResult<T> failed(String message) {
        return new ResponseResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ResponseResult<T> failed(Integer code, String message) {
        return new ResponseResult<T>(code, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 错误码
     */
    public static <T> ResponseResult<T> failed(ResultCode resultCode) {
        return new ResponseResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }


}
