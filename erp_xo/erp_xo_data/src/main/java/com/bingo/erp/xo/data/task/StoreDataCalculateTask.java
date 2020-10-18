package com.bingo.erp.xo.data.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.xo.data.dto.StoreDataDTO;
import com.bingo.erp.xo.data.entity.StoreData;
import com.bingo.erp.xo.data.feign.OrderFeignClient;
import com.bingo.erp.xo.data.service.StoreDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Slf4j
public class StoreDataCalculateTask {

    @Resource
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StoreDataService storeDataService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void calculateStoreData() {

        try {

            //调取data方法获取库存数据
            String dataJson = orderFeignClient.getStoreData();

            log.info("获取storeData数据返回结果：" + dataJson);

            Map<String ,Object> resultMap = JsonUtils.jsonToMap(dataJson);

            String resultJson = JsonUtils.objectToJson(resultMap.get("data"));

            List<StoreDataDTO> storeDataDTOS = JsonUtils.jsonToList(resultJson, StoreDataDTO.class);

            if (null == storeDataDTOS && storeDataDTOS.size() <= 0) {
                log.info("获取StoreData数据为空！！！");
                return;
            }

            QueryWrapper<StoreData> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("create_time",DateUtils.getTodayStartTime());

            List<StoreData> todayStoreDatas = storeDataService.list(queryWrapper);

            //库内还未有今天数据 新增数据
            if (null == todayStoreDatas || todayStoreDatas.size() <= 0){

                List<StoreData> storeDatas = convertByDTO(storeDataDTOS);

                storeDataService.saveBatch(storeDatas);
            }else {

                //只需修改数据
                for (StoreData storeData:todayStoreDatas) {

                    String storeType = storeData.getStoreType();

                    StoreDataDTO dataDTO =storeDataDTOS.stream().filter(storeDataDTO -> {
                        return storeDataDTO.getStoreType().equals(storeType);
                    }).collect(Collectors.toList()).get(0);

                    storeData.setStoreTotalType(dataDTO.getStoreTotalType());
                    storeData.setStoreTotalNum(dataDTO.getStoreTotalNum());
                    storeData.setStoreTotalPrice(dataDTO.getStoreTotalPrice());
                    storeData.setStoreTotalWeight(dataDTO.getStoreTotalWeight());

                    storeData.setUpdateTime(new Date());
                }

                //更新数据
                storeDataService.updateBatchById(todayStoreDatas);
            }

        } catch (Exception e) {
            log.error("获取保存storeData数据失败！原因：" + e.getMessage());
        }

    }

    /**
     * 将调取web项目的库存数据转换成数据库对象
     *
     * @param storeDataDTOS
     * @return
     */
    private List<StoreData> convertByDTO(List<StoreDataDTO> storeDataDTOS) {

        List<StoreData> result = new ArrayList<>();

        for (StoreDataDTO dto : storeDataDTOS) {

            StoreData data = new StoreData();

            data.setStoreType(dto.getStoreType());
            data.setStoreTotalNum(dto.getStoreTotalNum());
            data.setStoreTotalPrice(dto.getStoreTotalPrice());
            data.setStoreTotalWeight(dto.getStoreTotalWeight());
            data.setStoreTotalType(dto.getStoreTotalType());

            result.add(data);

        }

        return result;

    }

}
