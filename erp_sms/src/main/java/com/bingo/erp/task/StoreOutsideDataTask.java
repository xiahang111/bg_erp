package com.bingo.erp.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 在外材料处理消息类
 */
@Configuration
@EnableScheduling
@Slf4j
public class StoreOutsideDataTask {


    /**
     * 删除掉已经报废的数据
     */
    public void deleteNoneData(){

    }
}
