package com.bingo.erp.xo.order.service.impl;

import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.xo.order.mapper.ExpressRecordMapper;
import com.bingo.erp.xo.order.mapper.OrderInfoMapper;
import com.bingo.erp.xo.order.service.BaseDataService;
import com.bingo.erp.xo.order.vo.SelectVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseDataServiceImpl implements BaseDataService {


    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private ExpressRecordMapper expressRecordMapper;

    @Override
    public List<SelectVO> getAllOrderMaker() throws Exception {


        return revert(orderInfoMapper.getAllOrderMaker());
    }

    @Override
    public List<SelectVO> getAllSaleMan() throws Exception {

        return revert(orderInfoMapper.getAllSaleMan());
    }


    private List<SelectVO> revert(List<String> list) {

        List<SelectVO> result = new ArrayList<>();

        if (null == list || list.size() <= 0) {
            return result;
        }

        for (String s : list) {
            SelectVO selectVO = new SelectVO();
            selectVO.setValue(s);
            selectVO.setLabel(s);
            result.add(selectVO);
        }

        return result;

    }

    @Override
    public List<SelectVO> getAllColor() throws Exception {

        List<SelectVO> result = new ArrayList<>();

        for (MaterialColorEnums colorEnum : MaterialColorEnums.values()) {
            SelectVO selectVO = new SelectVO();
            selectVO.setValue(colorEnum.getValue() + "");
            selectVO.setLabel(colorEnum.getName());
            result.add(selectVO);
        }

        return result;
    }

    @Override
    public List<SelectVO> getAllOrderType() throws Exception {
        List<SelectVO> result = new ArrayList<>();

        for (OrderTypeEnums typeEnums : OrderTypeEnums.values()) {
            SelectVO selectVO = new SelectVO();
            selectVO.setValue(typeEnums.getValue() + "");
            selectVO.setLabel(typeEnums.getName());
            result.add(selectVO);
        }

        return result;
    }

    @Override
    public List<SelectVO> getAllOrderStatus() throws Exception {
        List<SelectVO> result = new ArrayList<>();

        for (OrderStatusEnums statusEnums : OrderStatusEnums.values()) {
            SelectVO selectVO = new SelectVO();
            selectVO.setValue(statusEnums.getValue() + "");
            selectVO.setLabel(statusEnums.getName());
            result.add(selectVO);
        }

        return result;
    }

    @Override
    public List<SelectVO> getAllExpressRecord() throws Exception {

        List<SelectVO> result = new ArrayList<>();

        List<String> expressRecords = expressRecordMapper.getAllExpress();

        if (null == expressRecords || expressRecords.size() <= 0) {
            return result;
        } else {
            for (String expressName : expressRecords) {

                SelectVO selectVO = new SelectVO();
                selectVO.setLabel(expressName);
                selectVO.setValue(expressName);
                result.add(selectVO);
            }
            return result;
        }
    }

}
