package com.soonphe.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.soonphe.portal.entity.UmsGainpacketRewardHistory;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.service.IUmsGainpacketRewardHistoryService;
import com.soonphe.portal.service.IWmsWalletService;
import com.soonphe.timber.framework.api.CommonJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-08-21 10:11
 **/
@RestController
@RequestMapping("/reward")
public class TestController<Mpa> {

    @Autowired
    private IWmsWalletService walletService;

    @Autowired
    private IUmsGainpacketRewardHistoryService historyService;

    private static Map<Integer, BigDecimal> map = new HashMap<>();

    @GetMapping("/grant")
    public CommonJsonResult<String> addUserCommunity() {
        map.put(4259, new BigDecimal(15.59));
        map.put(4171, new BigDecimal(16.89));
        map.put(3295, new BigDecimal(17.11));
        map.put(3286, new BigDecimal(16.67));
        map.put(3285, new BigDecimal(16.78));
        map.put(3284, new BigDecimal(20.67));
        map.put(3283, new BigDecimal(16.89));
        map.put(3282, new BigDecimal(19.87));
        map.put(3281, new BigDecimal(18.12));
        map.put(3280, new BigDecimal(19.06));
        map.put(3166, new BigDecimal(17.88));
        map.put(3071, new BigDecimal(14.73));
        map.put(2931, new BigDecimal(14.55));
        map.put(2919, new BigDecimal(17.58));
        map.put(2899, new BigDecimal(15.68));
        map.put(2896, new BigDecimal(16.79));
        map.put(2831, new BigDecimal(15.09));
        map.put(2790, new BigDecimal(17.23));
        map.put(2601, new BigDecimal(16.79));
        map.put(2546, new BigDecimal(20.57));
        map.put(2267, new BigDecimal(17.35));
        map.put(2254, new BigDecimal(14.89));
        map.put(2205, new BigDecimal(15.47));
        map.put(2200, new BigDecimal(16.68));
        map.put(2189, new BigDecimal(17.86));
        map.put(2184, new BigDecimal(17.57));
        map.put(2180, new BigDecimal(15.79));
        map.put(2175, new BigDecimal(16.78));
        map.put(2172, new BigDecimal(18.56));
        map.put(2169, new BigDecimal(16.68));
        map.put(2166, new BigDecimal(23.56));
        map.put(2163, new BigDecimal(22.88));
        map.put(2153, new BigDecimal(22.58));
        map.put(2151, new BigDecimal(16.36));
        map.put(2147, new BigDecimal(23.68));
        map.put(2125, new BigDecimal(24.56));
        map.put(2121, new BigDecimal(14.26));
        map.put(2119, new BigDecimal(14.68));
        map.put(2102, new BigDecimal(15.58));
        map.put(2098, new BigDecimal(15.96));
        map.put(2096, new BigDecimal(28.23));
        map.put(2088, new BigDecimal(15.36));
        map.put(2062, new BigDecimal(28.34));
        map.put(2061, new BigDecimal(19.46));
        map.put(2044, new BigDecimal(29.36));
        map.put(2017, new BigDecimal(38.26));
        map.put(2016, new BigDecimal(32.86));
        map.put(2000, new BigDecimal(18.26));
        map.put(1999, new BigDecimal(28.12));
        map.put(1968, new BigDecimal(27.72));
        map.put(2545, new BigDecimal(168.28));
        map.put(2198, new BigDecimal(158.27));
        map.put(2118, new BigDecimal(268.28));

        List<Integer> idList = new ArrayList<>();

        for (Integer userId : map.keySet()) {
            idList.add(userId);
        }
        // 查询钱包
        Collection<WmsWallet> wmsWalletList = walletService.listByIds(idList);
        List<UmsGainpacketRewardHistory> historieList = new ArrayList<>();
        UmsGainpacketRewardHistory history;

        List<WmsWallet> currList = new ArrayList<>();
        for (WmsWallet wmsWallet : wmsWalletList) {
            wmsWallet.setTrxAmount(wmsWallet.getTrxAmount().add(map.get(wmsWallet.getId())));
            if (wmsWallet.getCnyAmount() == null) {
                wmsWallet.setCnyAmount(map.get(wmsWallet.getId()));
            } else {
                wmsWallet.setCnyAmount(wmsWallet.getCnyAmount().add(map.get(wmsWallet.getId())));
            }
            history = new UmsGainpacketRewardHistory();
            history.setUid(wmsWallet.getId());
            history.setShareType(2);
            history.setType(1);
            history.setCreateDate(new Date());
            history.setAmount(map.get(wmsWallet.getId()));
            historieList.add(history);
            currList.add(wmsWallet);
        }

        // 执行修改钱包
        walletService.updateBatchById(currList);
//        historyService.saveBatch(historieList);


        return CommonJsonResult.success(map.toString());
    }


    public static void main(String[] args) {
        System.out.println(new BigDecimal(23.52));
    }


}
