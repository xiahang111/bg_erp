package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MessageRemindInfo;
import com.bingo.erp.xo.order.dto.MessageRemindResultDTO;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.MessageRemindInfoMapper;
import com.bingo.erp.xo.order.service.MessageRemindInfoService;
import com.bingo.erp.xo.order.vo.MessageRemindPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageRemindInfoServiceImpl  extends
        SuperServiceImpl<MessageRemindInfoMapper, MessageRemindInfo> implements MessageRemindInfoService {

    @Resource
    private MessageRemindInfoMapper messageRemindInfoMapper;

    @Override
    public List<MessageRemindResultDTO> getMessageRemindList(String adminUid) {

        List<MessageRemindResultDTO> dtos = new ArrayList<>();

        QueryWrapper<MessageRemindInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",SysConf.NORMAL_STATUS);
        queryWrapper.eq("admin_uid",adminUid);

        List<MessageRemindInfo> messageRemindInfos =  messageRemindInfoMapper.selectList(queryWrapper);

        if (messageRemindInfos.size() <= 0){
            return dtos;
        }

        messageRemindInfos.stream().forEach(messageRemindInfo -> {
            MessageRemindResultDTO dto = new MessageRemindResultDTO();
            dto.setRemindLevel(messageRemindInfo.getRemindLevel());
            dto.setMessage(messageRemindInfo.getMessage());
            dtos.add(dto);

            messageRemindInfo.setStatus(SysConf.DELETE_STATUS);
        });

        this.updateBatchById(messageRemindInfos);

        return dtos;
    }

    @Override
    public IPage<MessageRemindInfo> getMessageRemindPage(MessageRemindPageVO messageRemindPageVO) {

        QueryWrapper<MessageRemindInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("admin_uid",messageRemindPageVO.getAdminUid());

        Page<MessageRemindInfo> page = new Page<>();
        page.setCurrent(messageRemindPageVO.getCurrentPage());
        page.setSize(messageRemindPageVO.getPageSize());
        IPage<MessageRemindInfo> remindInfoIPage = this.page(page,queryWrapper);

        return remindInfoIPage;
    }
}






















