package com.soonphe.portal.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: soonphe
 * @date: 2018-11-28 15:13
 * @description:  元数据填充类——包括插入和更新是填充实体字段内容
 */
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    /**
     * 识别字段"testDate"，并自动插入对象
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
//        System.out.println("插入方法实体填充");
        setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        System.out.println("更新方法实体填充");
    }
}
