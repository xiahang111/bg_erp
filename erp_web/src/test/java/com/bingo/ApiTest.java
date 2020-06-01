package com.bingo;

import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.web.WebApplication;
import com.bingo.erp.xo.service.OrderService;
import com.bingo.erp.xo.vo.IndexOrderVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
public class ApiTest {

    @Resource
    private OrderService orderService;

    @Test
    public void test1(){

        List<IndexOrderVO> vos = orderService.getIndexOrderInfo();

        for (IndexOrderVO vo:vos) {
            System.out.println(vo.toString());
        }

    }


}
