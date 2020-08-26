package com.soonphe.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.TmAdvert;
import com.soonphe.portal.vo.TmAdvertVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2019-06-10
 */
@Mapper
public interface TmAdvertMapper extends BaseMapper<TmAdvert> {

    /**
     * 自定义注入方法
     */
    int deleteAll();

    /**
     * 自定义查询分页：传递参数Page即为自动分页，必须放在第一位
     *
     * @param page
     * @param TyAdvertVo
     * @return
     */
    @Select("select * from tm_advert ")
    IPage<TmAdvert> getList(Page page, TmAdvertVo TyAdvertVo);

    List<TmAdvert> selectListByWrapper(@Param("ew") Wrapper wrapper);
}
