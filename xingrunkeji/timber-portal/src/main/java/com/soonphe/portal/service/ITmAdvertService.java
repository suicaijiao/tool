package com.soonphe.portal.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.TmAdvert;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soonphe.portal.vo.TmAdvertVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soonphe
 * @since 2019-06-10
 */
public interface ITmAdvertService extends IService<TmAdvert> {

    boolean deleteAll();

    /**
     * 分页查询
     *
     * @return
     */
    IPage<TmAdvert> selectByAdvert(Page page, TmAdvertVo tyAdvertVo);

    List<TmAdvert> selectListByWrapper(Wrapper wrapper);
}
