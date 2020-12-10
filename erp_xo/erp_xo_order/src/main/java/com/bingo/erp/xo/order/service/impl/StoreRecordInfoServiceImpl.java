package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.StoreMaterialResource;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.dto.StoreRecordDTO;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.StoreOriginalRecordInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreRecordInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreSummaryInfoMapper;
import com.bingo.erp.xo.order.service.StoreRecordInfoService;
import com.bingo.erp.xo.order.service.StoreSummaryInfoService;
import com.bingo.erp.xo.order.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class StoreRecordInfoServiceImpl
        extends SuperServiceImpl<StoreRecordInfoMapper, StoreRecordInfo> implements StoreRecordInfoService {


    @Resource
    private StoreRecordInfoMapper storeRecordInfoMapper;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;

    @Resource
    private StoreSummaryInfoService storeSummaryInfoService;

    @Resource
    private StoreRecordInfoService storeRecordInfoService;

    @Resource
    private StoreOriginalRecordInfoMapper storeOriginalRecordInfoMapper;

    @Override
    public IPage<StoreRecordInfo> getStoreRecord(StoreRecordPageVO storeRecordPageVO) {

        QueryWrapper<StoreRecordInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("material_status", storeRecordPageVO.getMaterialStatus());

        //查询状态为1的数据记录
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        if (StringUtils.isNotBlank(storeRecordPageVO.getKeyword())) {
            queryWrapper.like("material_name", storeRecordPageVO.getKeyword());
        }

        if (StringUtils.isNotBlank(storeRecordPageVO.getOrderBy())) {

            if (storeRecordPageVO.getDesc()) {
                queryWrapper.orderByDesc(storeRecordPageVO.getOrderBy(), "create_time");
            } else {
                queryWrapper.orderByAsc(storeRecordPageVO.getOrderBy());
            }

        } else {
            queryWrapper.orderByDesc("create_time");
        }

        if (null != storeRecordPageVO.getMaterialColor()) {
            queryWrapper.eq("material_color", storeRecordPageVO.getMaterialColor());
        }

        //分页查询
        Page<StoreRecordInfo> page = new Page<>();
        page.setCurrent(storeRecordPageVO.getCurrentPage());
        page.setSize(storeRecordPageVO.getPageSize());

        IPage<StoreRecordInfo> storeRecordInfoIPage = storeRecordInfoService.page(page, queryWrapper);


        return storeRecordInfoIPage;
    }

    @Override
    @Transactional
    public synchronized void saveStoreRecord(StoreRecordVO storeRecordVO) throws Exception {


        QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_name", storeRecordVO.getMaterialName());
        queryWrapper.eq("material_color", storeRecordVO.getMaterialColor());
        queryWrapper.eq("specification", storeRecordVO.getSpecification());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        StoreSummaryInfo storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

        if (null == storeSummaryInfo) {
            throw new MessageException("库存内没有此材料，请确认或添加！");
        }

        StoreRecordInfo storeRecordInfo = new StoreRecordInfo();
        //出入库状态
        storeRecordInfo.setMaterialStatus(StoreMaterialStatus.getByCode(storeRecordVO.getMaterialStatus()));
        //出入库源
        storeRecordInfo.setMaterialResource(StoreMaterialResource.getByCode(storeRecordVO.getMaterialResource()));
        //材料名称
        storeRecordInfo.setMaterialName(storeRecordVO.getMaterialName());
        //材料颜色
        storeRecordInfo.setMaterialColor(MaterialColorEnums.getByCode(storeRecordVO.getMaterialColor()));
        //材料数量
        storeRecordInfo.setMaterialNum(storeRecordVO.getMaterialNum());
        //材料规格
        storeRecordInfo.setSpecification(storeRecordVO.getSpecification());
        //材料单位
        storeRecordInfo.setUnit(storeSummaryInfo.getUnit());

        //材料单价
        storeRecordInfo.setPrice(storeSummaryInfo.getPrice());

        //材料总价格
        storeRecordInfo.setTotalPrice(storeRecordInfo.getMaterialNum().multiply(storeRecordInfo.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));


        if (storeRecordVO.getMaterialStatus() == StoreMaterialStatus.IN.code) {
            storeSummaryInfo.setTotalPrice(storeSummaryInfo.getTotalPrice().add(storeRecordInfo.getTotalPrice()));
            storeSummaryInfo.setTotalWeight(storeSummaryInfo.getTotalWeight().add(storeRecordInfo.getMaterialNum().multiply(storeSummaryInfo.getWeight())));
            storeSummaryInfo.setMaterialNum(storeSummaryInfo.getMaterialNum().add(storeRecordInfo.getMaterialNum()));

        } else {

            if (storeRecordInfo.getMaterialNum().compareTo(storeSummaryInfo.getMaterialNum()) > 0) {
                throw new MessageException("材料不足,请确认！");
            }

            storeSummaryInfo.setTotalPrice(storeSummaryInfo.getTotalPrice().subtract(storeRecordInfo.getTotalPrice()));
            storeSummaryInfo.setTotalWeight(storeSummaryInfo.getTotalWeight().subtract(storeRecordInfo.getMaterialNum().multiply(storeSummaryInfo.getWeight())));
            storeSummaryInfo.setMaterialNum(storeSummaryInfo.getMaterialNum().subtract(storeRecordInfo.getMaterialNum()));

        }


        //更新总表
        storeRecordInfoMapper.insert(storeRecordInfo);
        storeSummaryInfoMapper.updateById(storeSummaryInfo);

    }

    @Override
    @Transactional
    public synchronized void callbackStoreRecord(String storeRecordUid) throws Exception {

        StoreRecordInfo storeRecordInfo = storeRecordInfoMapper.selectById(storeRecordUid);

        if (null == storeRecordInfo) {
            throw new MessageException("无此库存记录信息");
        }

        QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_name", storeRecordInfo.getMaterialName());
        queryWrapper.eq("material_color", storeRecordInfo.getMaterialColor());
        queryWrapper.eq("specification", storeRecordInfo.getSpecification());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        StoreSummaryInfo storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

        if (null == storeSummaryInfo) {
            throw new MessageException("无此库存材料信息");
        }

        //入库，回滚的话总库存应该减去相应数值
        if (storeRecordInfo.getMaterialStatus().code == StoreMaterialStatus.IN.code) {

            storeSummaryInfo.setTotalWeight(storeSummaryInfo.getTotalWeight().
                    subtract(storeSummaryInfo.getWeight().
                            multiply(storeRecordInfo.getMaterialNum())).
                    setScale(2, BigDecimal.ROUND_HALF_UP));

            storeSummaryInfo.setTotalPrice(storeSummaryInfo.getTotalPrice().
                    subtract(storeRecordInfo.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));

            storeSummaryInfo.setMaterialNum(storeSummaryInfo.getMaterialNum().subtract(storeRecordInfo.getMaterialNum()));

        } else {

            storeSummaryInfo.setTotalWeight(storeSummaryInfo.getTotalWeight().
                    add(storeSummaryInfo.getWeight().
                            multiply(storeRecordInfo.getMaterialNum())).
                    setScale(2, BigDecimal.ROUND_HALF_UP));

            storeSummaryInfo.setTotalPrice(storeSummaryInfo.getTotalPrice().
                    add(storeRecordInfo.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));

            storeSummaryInfo.setMaterialNum(storeSummaryInfo.getMaterialNum().add(storeRecordInfo.getMaterialNum()));

        }

        storeSummaryInfoMapper.updateById(storeSummaryInfo);

        storeRecordInfo.setStatus(SysConf.DELETE_STATUS);

        storeRecordInfoMapper.updateById(storeRecordInfo);

    }

    @Override
    public List<Map<String, String>> getStoreNameList() {

        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> head = new HashMap<>();
        head.put("storeName","");
        result.add(head);
        Set<String> nameList = storeSummaryInfoMapper.getStoreNameList();
        for (String name : nameList) {
            Map<String, String> map = new HashMap<>();
            map.put("storeName", name);
            result.add(map);
        }


        return result;
    }

    @Override
    public List<StatementResultVO> getStoreStatement(StoreStatementVO storeStatementVO) throws MessageException {

        //参数校验
        //如果查询是坯料但是查询条件中又带有颜色
        if(storeStatementVO.getMaterialType() == 2 &&
                (StringUtils.isNotBlank(storeStatementVO.getMaterialColor()) ||
                        null != storeStatementVO.getIsPartColor() && storeStatementVO.getIsPartColor())){

            throw new MessageException("坯料没有颜色哦~请确认查询条件");

        }

        //如果是坯料就查坯料库 成品料就查成品料库
        StatementQueryVO statementQueryVO = new StatementQueryVO();

        statementQueryVO.setDateType(storeStatementVO.getDateType());

        statementQueryVO.setIsPartName(storeStatementVO.getIsPartName());
        statementQueryVO.setIsPartColor(storeStatementVO.getIsPartColor());

        if (StringUtils.isNotBlank(storeStatementVO.getMaterialName())){
            statementQueryVO.setMaterialName(storeStatementVO.getMaterialName());
        }

        if (null != storeStatementVO.getMaterialColor()){
            statementQueryVO.setMaterialColor(storeStatementVO.getMaterialColor());
        }


        List<StoreRecordDTO> storeRecordDTOS = null;

        List<StatementResultVO> statementResultVOS = null;

        if (storeStatementVO.getMaterialType() == 1){
            statementResultVOS = storeRecordInfoMapper.queryStoreRecordStatement(statementQueryVO);
        }

        if (storeStatementVO.getMaterialType() == 2){
            statementResultVOS = storeOriginalRecordInfoMapper.queryStoreOriginalRecordStatement(statementQueryVO);
        }

        //解析数据
        //analyzeRecordDTO(statementResultVOS,storeStatementVO.getDateType());
        return statementResultVOS;
    }


    private void getStatementDateByDateType(StatementQueryVO statementQueryVO, Integer dateType) {

        //如果是按年计算
        if (dateType == 1) {
            statementQueryVO.setStartTime(DateUtil.beginOfYear(new Date()));
            statementQueryVO.setEndTime(DateUtil.endOfYear(new Date()));
        }

        if (dateType == 2){
            statementQueryVO.setStartTime(DateUtil.beginOfMonth(new Date()));
            statementQueryVO.setEndTime(DateUtil.endOfMonth(new Date()));
        }

        if (dateType == 3){
            statementQueryVO.setStartTime(DateUtil.beginOfDay(new Date()));
            statementQueryVO.setEndTime(DateUtil.endOfDay(new Date()));
        }
    }

    /**
     * 坯料 成品料数据转换成通用对象
     * @param list
     * @param materialType
     * @return
     */
    private List<StoreRecordDTO> convertRecordDTO(List list,Integer materialType){

        List<StoreRecordDTO> result = new ArrayList<>();

        if (materialType == 1){
            List<StoreRecordInfo> storeRecordInfos = list;
            for (StoreRecordInfo storeRecordInfo:storeRecordInfos) {
                StoreRecordDTO dto = new StoreRecordDTO();
                dto.setDateTime(storeRecordInfo.getCreateTime());
                dto.setMaterialName(storeRecordInfo.getMaterialName());
                dto.setMaterialColor(storeRecordInfo.getMaterialColor().code);
                dto.setMaterialStatus(storeRecordInfo.getMaterialStatus().name);
                dto.setTotalNum(storeRecordInfo.getMaterialNum());
                dto.setTotalPrice(storeRecordInfo.getTotalPrice());
                result.add(dto);
            }
        }

        if (materialType == 2){
            List<StoreOriginalRecordInfo> storeRecordInfos = list;
            for (StoreOriginalRecordInfo storeOriginalRecordInfo:storeRecordInfos) {
                StoreRecordDTO dto = new StoreRecordDTO();
                dto.setDateTime(storeOriginalRecordInfo.getCreateTime());
                dto.setMaterialName(storeOriginalRecordInfo.getMaterialName());

                dto.setMaterialStatus(storeOriginalRecordInfo.getMaterialStatus().name);
                dto.setTotalNum(storeOriginalRecordInfo.getMaterialNum());
                dto.setTotalPrice(storeOriginalRecordInfo.getTotalPrice());
                result.add(dto);
            }
        }

        return result;
    }

    public void analyzeRecordDTO(List<StatementResultVO> statementResultVOS,Integer dateType){

        Map<String , List<StatementResultVO>> stringListMap = new HashMap<>();

        if (null == statementResultVOS || statementResultVOS.size() <= 0 ) return;

        for (StatementResultVO statementResultVO:statementResultVOS) {

            String date = "";


            if (null == stringListMap.get(date)){
                List<StatementResultVO> statementResultList = new ArrayList<>();
                statementResultList.add(statementResultVO);
                stringListMap.put(date,statementResultList);
            }else {
                stringListMap.get(date).add(statementResultVO);
            }
        }







    }
}
