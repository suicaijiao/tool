package com.soonphe.portal.service;

import com.soonphe.portal.entity.TmsTrade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soonphe.portal.vo.TradeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 交易表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
public interface ITmsTradeService extends IService<TmsTrade> {


    List<TradeVo> getTradeList(int state, int type);
}
