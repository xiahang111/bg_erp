package com.bingo;

import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.web.WebApplication;
import com.bingo.erp.xo.order.dto.StoreDataDTO;
import com.bingo.erp.xo.order.service.feign.StoreDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
public class StoreDataTest {


    @Autowired
    private StoreDataService storeDataService;

    @Test
    public void test1(){

        List<StoreDataDTO> result = storeDataService.getStoreData();

        System.out.println(JsonUtils.objectToJson(result));

    }
}
