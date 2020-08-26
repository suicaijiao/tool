package com.soonphe.portal;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author: soonphe
 * @date: 2019/10/10
 * @description:  演示 mapper 父类，注意这个类不要让 mp 扫描到！！也可直接继承BaseMapper类
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    // 这里可以放一些公共的方法
}
