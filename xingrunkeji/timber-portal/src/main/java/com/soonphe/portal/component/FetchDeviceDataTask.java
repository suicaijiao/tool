package com.soonphe.portal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: soonphe
 * @date: 2019/10/14
 * @description: 定时任务
 */
@Component
public class FetchDeviceDataTask {

    private static final Logger logger = LoggerFactory.getLogger(FetchDeviceDataTask.class);

    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     * 0/5：从0分开始，每过5分钟触发一次
     */
    @Scheduled(cron = "0 0/5 * ? * ?")
    private void fetchDeviceData() {
        logger.info("拉取数据" + new Date().toString());
//        service.fetchDeviceData(null, null);

    }
}