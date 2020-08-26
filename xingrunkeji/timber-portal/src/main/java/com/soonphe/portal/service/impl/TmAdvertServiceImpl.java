package com.soonphe.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.TmAdvert;
import com.soonphe.portal.mapper.TmAdvertMapper;
import com.soonphe.portal.service.ITmAdvertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soonphe.portal.vo.TmAdvertVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2019-06-10
 */
@Service
public class TmAdvertServiceImpl extends ServiceImpl<TmAdvertMapper, TmAdvert> implements ITmAdvertService {

    @Override
    public boolean deleteAll() {
        return retBool(baseMapper.deleteAll());
    }

    @Override
    public IPage<TmAdvert> selectByAdvert(Page page, TmAdvertVo baseVo) {

        return baseMapper.getList(page,baseVo);
    }

    @Override
    public List<TmAdvert> selectListByWrapper(Wrapper wrapper) {
        return baseMapper.selectListByWrapper((java.sql.Wrapper) wrapper);

    }
}
