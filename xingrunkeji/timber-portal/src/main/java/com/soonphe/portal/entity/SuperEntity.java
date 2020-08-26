//package com.soonphe.portal.entity;
//
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.extension.activerecord.Model;
//
//import java.io.Serializable;
//
///**
// * @author: soonphe
// * @date: 2019-06-11 17:10
// * @description:  实体通用父类——暂未启用
// */
//public class SuperEntity<T extends Model> extends Model {
//
//    /**
//     * 主键ID , 这里故意演示注解可以无
//     */
//    @TableId
//    private Long id;
//    private Long tenantId;
//
//    public Long getId() {
//        return this.id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getTenantId() {
//        return tenantId;
//    }
//
//    public SuperEntity setTenantId(Long tenantId) {
//        this.tenantId = tenantId;
//        return this;
//    }
//
//    @Override
//    protected Serializable pkVal() {
//        return this.id;
//    }
//}
