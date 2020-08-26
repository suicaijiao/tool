package com.soonphe.portal.service.impl;

import com.soonphe.portal.entity.TmsTrade;
import com.soonphe.portal.mapper.TmsTradeMapper;
import com.soonphe.portal.service.ITmsTradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soonphe.portal.vo.TradeVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 交易表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Service
public class TmsTradeServiceImpl extends ServiceImpl<TmsTradeMapper, TmsTrade> implements ITmsTradeService {

    @Override
    public List<TradeVo> getTradeList(int state, int type) {
        return baseMapper.getTradeList(state,type);
    }
}
