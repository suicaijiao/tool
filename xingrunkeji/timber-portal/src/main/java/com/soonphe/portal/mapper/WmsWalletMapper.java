package com.soonphe.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.vo.StatsCurrencyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 钱包 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Mapper
public interface WmsWalletMapper extends BaseMapper<WmsWallet> {


    /**
     * 自定义sql使用Wrapper
     * @param wrapper
     * @return
     */
//    @Select("select * from mysql_data ${ew.customSqlSegment}")
//    List<WalletVo> getAll(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 获取游戏总数
     * @return
     */
    @Select("select sum(game_total) from wms_wallet")
    int getTotalGame();

    /**
     * 获取Trx总数
     * @return
     */
    @Select("select sum(trx_amount) trxAmount,sum(cny_amount) cnyAmount,sum(ft_amount) ftAmount,sum(win_ft) winFt,sum(release_total) releaseTotal from wms_wallet")
    StatsCurrencyVo getTotalCurrencyAmount();


    /**
     * 更新
     * @param id
     * @param click
     * @param stayTime
     * @param carouselCount
     * @param exposure
     * @return
     */
    @Update("update ty_advert set click = click+#{click}," +
            "stayTime = stayTime+#{stayTime}," +
            "carouselCount = carouselCount+#{carouselCount}," +
            "exposure = exposure+#{exposure} where id = #{id}")
    int updateClick(@Param(value = "id") int id,
                    @Param(value = "click") int click,
                    @Param(value = "stayTime") int stayTime,
                    @Param(value = "carouselCount") int carouselCount,
                    @Param(value = "exposure") int exposure);



}
