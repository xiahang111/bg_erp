package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.OrderProcess;
import com.bingo.erp.commons.entity.OrderProcessAnalyze;
import com.bingo.erp.xo.order.vo.OrderProcessAnalyzePageVO;
import com.bingo.erp.xo.order.vo.OrderProcessPageVO;
import com.bingo.erp.xo.order.vo.OrderProcessVO;
import org.springframework.web.multipart.MultipartFile;

public interface OrderProcessService extends SuperService<OrderProcess> {

    IPage<OrderProcess> getProcessRecord(OrderProcessPageVO orderProcessPageVO);

    IPage<OrderProcessAnalyze> getProcessRecordAnalyze(OrderProcessAnalyzePageVO orderProcessAnalyzePageVO);

    void deleteByUid(String uid) throws Exception;

    void deleteByOrderUid(String orderUid) throws Exception;

    void updateProcessRecord(OrderProcessVO orderProcessVO) throws Exception;

    void saveOrderProcessByOrderInfo(OrderInfo orderInfo) throws Exception;

    void fileAnalyze(MultipartFile file) throws Exception;
}
