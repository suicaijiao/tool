package com.soonphe.portal.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.soonphe.portal.method.DeleteAll;

import java.util.List;

/**
 * @author: soonphe
 * @date: 2019-06-13 17:02
 * @description:  自定义SqlInjector
 */
public class MyLogicSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = super.getMethodList();
        methodList.add(new DeleteAll());
        return methodList;
    }
}