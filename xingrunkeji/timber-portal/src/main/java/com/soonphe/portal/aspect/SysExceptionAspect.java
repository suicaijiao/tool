package com.soonphe.portal.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author: soonphe
 * @date: 2018-03-29 17:31
 * @description:  异常拦截+异常日志打印
 */
@Aspect
//@Component
public class SysExceptionAspect {

    @Resource
    HttpServletResponse response;

    private static final Logger logger = LoggerFactory.getLogger(SysExceptionAspect.class);


    @Pointcut("execution(public * com.soonphe.portal.app.controller..*.*(..))")
    public void apiController() {
    }


    @Pointcut("execution(public * com.soonphe.portal.controller..*.*(..))")
    public void cmsController() {
    }


    @AfterThrowing(value = "apiController()", throwing = "e")
    @ResponseBody
    public void apiLoggingException(JoinPoint joinPoint, Exception e) {
        loggingException(joinPoint, e);

    }


    @AfterThrowing(value = "cmsController()", throwing = "e")
    public void cmsLoggingException(JoinPoint joinPoint, Exception e) {
        loggingException(joinPoint, e);
    }


    public void loggingException(JoinPoint joinPoint, Exception e) {
        // 拦截的实体类
        Object target = joinPoint.getTarget();
        // 拦截的方法名称
        String methodName = joinPoint.getSignature().getName();
        logger.error("实体类:" + target);
        logger.error("方法名:" + methodName);
        logger.error("异常类名：" + joinPoint.getSignature().getName().getClass());
        // 得到被拦截方法参数，并打印
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            logger.error("抛异常拦截： 被拦截到的方法参数：" + i + " -- " + args[i]);
        }
        logger.error("异常信息: " + e.getMessage());
    }

    @Around("apiController()")
    public Object apiControllerExceptionIterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            logger.info("The method " + joinPoint.getSignature().getDeclaringTypeName() + " "
                    + joinPoint.getSignature().getName()
                    + "() begins with "
                    + Arrays.toString(joinPoint.getArgs()));
            //继续执行下一个通知
            result = joinPoint.proceed();
            logger.info("The method " + joinPoint.getSignature().getDeclaringTypeName() + " "
                    + joinPoint.getSignature().getName()
                    + "() ends with " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //打印异常
            loggingException(joinPoint, e);
            throw e;
        }
    }

    @Around("cmsController()")
    public Object cmsControllerExceptionIterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            logger.info("The method " + joinPoint.getSignature().getDeclaringTypeName() + " "
                    + joinPoint.getSignature().getName()
                    + "() begins with "
                    + Arrays.toString(joinPoint.getArgs()));
            result = joinPoint.proceed();
            logger.info("The method " + joinPoint.getSignature().getDeclaringTypeName()
                    + " " + joinPoint.getSignature().getName()
                    + "() ends with " + result);
            return result;
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            loggingException(joinPoint, iae);
            throw iae;
        } catch (Exception e) {
            e.printStackTrace();
            loggingException(joinPoint, e);
            throw e;
        }
    }

}
