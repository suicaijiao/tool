package com.soonphe.portal.mapper;

import com.soonphe.portal.entity.SmsDestory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 销毁表 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-06-29
 */
@Mapper
public interface SmsDestoryMapper extends BaseMapper<SmsDestory> {

    @Select("select sum(amount) from sms_destory")
    BigDecimal getTotal();


}
