package com.bingo.erp.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.DataGather;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.mapper.DataGatherMapper;
import com.bingo.erp.xo.service.DataGatherService;
import com.bingo.erp.xo.vo.VisitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class DataGatherServiceImpl extends SuperServiceImpl<DataGatherMapper, DataGather> implements DataGatherService {

    @Resource
    private DataGatherMapper dataGatherMapper;

    @Override
    public DataGather getLastDataGather() {
        return dataGatherMapper.getLastDataGather();
    }

    @Override
    public VisitVO getLineData() {

        //获取最近七天的订单数和销售额
        //获取七天前的开始时间
        Date startDate = DateUtils.getDate(DateUtils.getOneDayStartTime(new Date()), -7);

        QueryWrapper<DataGather> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", startDate);


        Map<Integer, DataGather> map = new HashMap<>();

        List<DataGather> dataGathers = dataGatherMapper.selectList(queryWrapper);

        for (DataGather dataGather : dataGathers) {

            int n = DateUtils.differentDays(dataGather.getCreateTime(), new Date());

            map.put(n, dataGather);
        }

        List<BigDecimal> expectedData = new ArrayList<>();

        List<BigDecimal> actualData = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            DataGather gather = map.get(i);
            if (null == gather) {
                expectedData.add(new BigDecimal(0));
                actualData.add(new BigDecimal(0));
            } else {
                expectedData.add(gather.getDayOrderNums());
                actualData.add(gather.getDaySaleNums());
            }
        }


        VisitVO visitVO = new VisitVO();
        Collections.reverse(expectedData);
        visitVO.setExpectedData(expectedData);
        Collections.reverse(actualData);
        visitVO.setActualData(actualData);

        return visitVO;

    }
}
