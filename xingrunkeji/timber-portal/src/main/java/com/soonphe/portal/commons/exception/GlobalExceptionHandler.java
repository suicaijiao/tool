package com.soonphe.portal.commons.exception;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.soonphe.portal.commons.exception.entity.BusinessException;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.commons.golbal.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通过java原生的@Valid注解和spring的@ControllerAdvice和@ExceptionHandler实现全局异常处理
 *
 * @author wangzipeng
 * @date 2019/8/12 13:39
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 处理所有业务的异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult bindBusinessExceptionHandler(HttpServletRequest request, BusinessException e) {
        //是否记录错误详情
        if (e.getCause() == null) {
            logger.info(getLoggerMessage(request, e));
        } else {
            logger.info(getLoggerMessage(request, e), e.getCause());
        }

        //没有错误代码的，默认一个错误代码
        if (e.getBizCode()==null) {
            return ResponseResult.failed(ResultCode.EXCEPTION.getCode(), ResultCode.EXCEPTION.getMessage());
        }
        return ResponseResult.failed(e.getBizCode(), e.getMessage());
    }

    /**
     * 处理接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult bindExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            logger.info(getLoggerMessage(request, e));

            List<ObjectError> errors = bindingResult.getAllErrors();
            if (CollectionUtils.isNotEmpty(errors))//400请求参数不正确
            {
                StringBuilder errorMessage = new StringBuilder();
                for (ObjectError objectError : errors) {
                    errorMessage.append(objectError.getDefaultMessage()).append(";");
                }
                return ResponseResult.failed(errorMessage.toString());
            }
        }
        return null;
    }

    /**
     * 处理接口数据验证异常
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, BindException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseResult bindArgumentExceptionHandler(HttpServletRequest request, Exception e) {
        logger.info(getLoggerMessage(request, e));
        return ResponseResult.failed(ResultCode.VALIDATE_FAILED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseResult bindArgumentTypeExceptionHandler(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        logger.info(getLoggerMessage(request, e));
        return ResponseResult.failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 处理未知的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult bindExceptionHandler(HttpServletRequest request, Exception e) {
        logger.error(getLoggerMessage(request, e), e);
        return ResponseResult.failed();
    }

    /**
     * 返回格式化错误消息 [异常类型] [请求地址] [异常消息]
     */
    private String getLoggerMessage(HttpServletRequest request, Exception e) {
        return String.format("[%s] [%s] [%s]", e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());
    }
}