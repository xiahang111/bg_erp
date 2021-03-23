package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.GroupSalesmanInfo;
import com.bingo.erp.commons.entity.HangingInfo;
import com.bingo.erp.xo.order.mapper.GroupSalesmanMapper;
import com.bingo.erp.xo.order.mapper.HangingInfoMapper;
import com.bingo.erp.xo.order.service.GroupSalesmanService;
import org.springframework.stereotype.Service;

@Service
public class GroupSalesmanServiceImpl extends
        SuperServiceImpl<GroupSalesmanMapper, GroupSalesmanInfo> implements GroupSalesmanService {
}
