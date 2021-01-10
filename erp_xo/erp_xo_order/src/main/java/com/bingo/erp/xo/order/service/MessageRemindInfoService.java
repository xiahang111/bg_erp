package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.MessageRemindInfo;
import com.bingo.erp.xo.order.dto.MessageRemindResultDTO;
import com.bingo.erp.xo.order.vo.MessageRemindPageVO;

import java.util.List;

public interface MessageRemindInfoService extends SuperService<MessageRemindInfo> {

    List<MessageRemindResultDTO> getMessageRemindList(String adminUid);

    IPage<MessageRemindInfo> getMessageRemindPage(MessageRemindPageVO messageRemindPageVO);
}
