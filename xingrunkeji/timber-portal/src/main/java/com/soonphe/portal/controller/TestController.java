package com.soonphe.portal.controller;

import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsGainpacketRewardHistory;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.service.IUmsGainpacketRewardHistoryService;
import com.soonphe.portal.service.IWmsWalletService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult<String> addUserCommunity() {
        map.put(4259, new BigDecimal(23.52));
        map.put(3286, new BigDecimal(21.63));
        map.put(3284, new BigDecimal(25.98));
        map.put(3283, new BigDecimal(24.47));
        map.put(3166, new BigDecimal(27.88));
        map.put(2919, new BigDecimal(24.67));
        map.put(2899, new BigDecimal(19.79));
        map.put(2896, new BigDecimal(20.67));
        map.put(2790, new BigDecimal(23.26));
        map.put(2601, new BigDecimal(27.18));
        map.put(2546, new BigDecimal(25.16));
        map.put(2545, new BigDecimal(24.80));
        map.put(2205, new BigDecimal(21.88));
        map.put(2184, new BigDecimal(25.76));
        map.put(2175, new BigDecimal(26.76));
        map.put(2163, new BigDecimal(23.16));
        map.put(2153, new BigDecimal(24.59));
        map.put(2147, new BigDecimal(25.36));
        map.put(2119, new BigDecimal(24.18));
        map.put(2102, new BigDecimal(25.63));
        map.put(2098, new BigDecimal(24.19));
        map.put(2096, new BigDecimal(23.18));
        map.put(2000, new BigDecimal(25.18));
        map.put(1968, new BigDecimal(24.46));
        map.put(2198, new BigDecimal(191.287));
        map.put(2118, new BigDecimal(169.376));

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
            wmsWallet.setTeamCnyAmount(wmsWallet.getCnyAmount().add(map.get(wmsWallet.getId())));
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
        historyService.saveBatch(historieList);


        return ResponseResult.success(map.toString());
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(23.52));
    }

}
