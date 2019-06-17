package com.example.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
/**
 * 异常处理
 * @author suicaijiao
 *
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	
//	@ExceptionHandler(Exception.class)
//	@ResponseBody
//	public String defaultErrorHandler(HttpServletRequest request,Exception e){
//		// 如果返回的事string
//		return "对不起服务繁忙，请稍后再试";
//	}
}
