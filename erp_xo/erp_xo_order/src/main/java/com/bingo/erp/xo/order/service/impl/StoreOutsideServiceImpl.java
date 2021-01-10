package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.MaterialStatusEnums;
import com.bingo.erp.base.enums.StoreInOutMapperEnums;
import com.bingo.erp.base.enums.StoreMaterialResource;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.commons.entity.StoreOutsideInfo;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.StoreOutsideInfoMapper;
import com.bingo.erp.xo.order.service.StoreOutsideInfoService;
import com.bingo.erp.xo.order.service.StoreOutsideService;
import com.bingo.erp.xo.order.service.StoreRecordInfoService;
import com.bingo.erp.xo.order.vo.StoreOutsidePageVO;
import com.bingo.erp.xo.order.vo.StoreOutsideVO;
import com.bingo.erp.xo.order.vo.StoreRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 材料外出服务实现类
 */
@Service
public class StoreOutsideServiceImpl implements StoreOutsideService {

    @Resource
    private StoreOutsideInfoService storeOutsideInfoService;

    @Resource
    private StoreOutsideInfoMapper storeOutsideInfoMapper;

    @Resource
    private StoreRecordInfoService storeRecordInfoService;

    @Override
    public void saveByOriginalRecord(StoreOriginalRecordInfo storeOriginalRecordInfo,Integer materialColor) {

        StoreOutsideInfo storeOutsideInfo = new StoreOutsideInfo();
        storeOutsideInfo.setOriginUid(storeOriginalRecordInfo.getUid());
        storeOutsideInfo.setMaterialName(storeOriginalRecordInfo.getMaterialName());
        storeOutsideInfo.setSpecification(storeOriginalRecordInfo.getSpecification());
        storeOutsideInfo.setUnit(storeOriginalRecordInfo.getUnit());
        storeOutsideInfo.setPrice(storeOriginalRecordInfo.getPrice());
        storeOutsideInfo.setTotalPrice(storeOriginalRecordInfo.getTotalPrice());
        storeOutsideInfo.setMaterialStatus(storeOriginalRecordInfo.getMaterialStatus());

        String location = StoreInOutMapperEnums.getNameByOutName(storeOriginalRecordInfo.getOriginalResource().name);
        storeOutsideInfo.setLocation(location);
        storeOutsideInfo.setOutTime(new Date());
        storeOutsideInfo.setCompleteTime(DateUtils.getDate(DateUtils.dateTimeToStr(new Date()), 10));
        storeOutsideInfo.setMaterialNum(storeOriginalRecordInfo.getMaterialNum().intValue());
        storeOutsideInfo.setMaterialColor(MaterialColorEnums.getByCode(materialColor));

        storeOutsideInfoService.save(storeOutsideInfo);

    }

    @Override
    @Transactional
    public void updateStoreOutsideData(StoreOutsideVO storeOutsideVO)throws Exception{

        QueryWrapper<StoreOutsideInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("uid", storeOutsideVO.getUid());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        StoreOutsideInfo storeOutsideInfo = storeOutsideInfoService.getOne(queryWrapper);

        if (null == storeOutsideInfo) {
            throw new MessageException("没有此数据");
        }

        storeOutsideInfo.setRemark(storeOutsideVO.getRemark());
        storeOutsideInfo.setCompleteTime(storeOutsideVO.getCompleteTime());

        storeOutsideInfoService.saveOrUpdate(storeOutsideInfo);
    }

    @Override
    @Transactional
    public void updateStoreOutsideByUid(StoreOutsideVO storeOutsideVO) throws Exception {


        QueryWrapper<StoreOutsideInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("uid", storeOutsideVO.getUid());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        StoreOutsideInfo storeOutsideInfo = storeOutsideInfoService.getOne(queryWrapper);

        if (null == storeOutsideInfo) {
            throw new MessageException("没有此数据");
        }

        if(storeOutsideInfo.getMaterialStatus().code == MaterialStatusEnums.BAOFEI.code){
            throw new MessageException("不得操作已经报废的材料!!!");
        }

        if (storeOutsideVO.getMaterialNum() == 0){
            throw new MessageException("变动材料数量不得小于0!");
        }

        if (storeOutsideVO.getMaterialNum() > storeOutsideInfo.getMaterialNum()){
            throw new MessageException("操作数量大于原有数量!请检查!");
        }

        if (storeOutsideVO.getMaterialStatus().equals(MaterialStatusEnums.STOREIN.name)){

            if (SysConf.NO_COLOR.equals(storeOutsideVO.getMaterialColor())){
                throw new MessageException("无颜色的材料不得入库!!!");
            }

            //入库操作 需要减去在外材料相关数据并且在成品库中添加相对应的数据 并且入成品库
            //全部入库
            StoreRecordVO storeRecordVO = new StoreRecordVO();
            String location = storeOutsideInfo.getLocation();
            storeRecordVO.setMaterialResource( StoreMaterialResource.getCodeByName(StoreInOutMapperEnums.getInNameByName(location)));
            storeRecordVO.setMaterialName(storeOutsideInfo.getMaterialName());
            storeRecordVO.setMaterialColor(MaterialColorEnums.getCodeByInformation(storeOutsideVO.getMaterialColor()));
            storeRecordVO.setMaterialNum(new BigDecimal(storeOutsideVO.getMaterialNum()));
            storeRecordVO.setSpecification(storeOutsideInfo.getSpecification());
            storeRecordVO.setUnit(storeOutsideInfo.getUnit());
            storeRecordVO.setMaterialStatus(MaterialStatusEnums.STOREIN.code);
            storeRecordInfoService.saveStoreRecord(storeRecordVO);

            if (storeOutsideVO.getMaterialNum() == storeOutsideInfo.getMaterialNum()){

                storeOutsideInfo.setStatus(SysConf.DELETE_STATUS);
                storeOutsideInfoService.saveOrUpdate(storeOutsideInfo);
            }
            //只有部分入成品库
            else {

                if (StringUtils.isNotBlank(storeOutsideVO.getRemark())){
                    storeOutsideInfo.setRemark(storeOutsideVO.getRemark());
                }else {
                    storeOutsideInfo.setRemark("总共"+storeOutsideInfo.getMaterialNum()+"支料,入库"+
                            storeOutsideVO.getMaterialNum()+"支,报废"+
                            (storeOutsideInfo.getMaterialNum() - storeOutsideVO.getMaterialNum())+"支");
                }
                storeOutsideInfo.setMaterialStatus(MaterialStatusEnums.BAOFEI);
                storeOutsideInfo.setMaterialNum(storeOutsideInfo.getMaterialNum() - storeOutsideVO.getMaterialNum());
                storeOutsideInfo.setTotalPrice(storeOutsideInfo.getTotalPrice().
                        subtract(storeOutsideInfo.getPrice().multiply(new BigDecimal(storeOutsideVO.getMaterialNum())).setScale(2)));
                storeOutsideInfoService.saveOrUpdate(storeOutsideInfo);
            }
        }else {
            //需要操作在外材料表
            StoreOutsideInfo newStoreOutsideInfo = new StoreOutsideInfo();
            newStoreOutsideInfo.setOriginUid(storeOutsideInfo.getOriginUid());
            newStoreOutsideInfo.setMaterialName(storeOutsideInfo.getMaterialName());
            newStoreOutsideInfo.setSpecification(storeOutsideInfo.getSpecification());
            newStoreOutsideInfo.setUnit(storeOutsideInfo.getUnit());
            newStoreOutsideInfo.setPrice(storeOutsideInfo.getPrice());
            newStoreOutsideInfo.setMaterialNum(storeOutsideVO.getMaterialNum());
            newStoreOutsideInfo.setTotalPrice(storeOutsideInfo.getPrice().multiply(new BigDecimal(storeOutsideVO.getMaterialNum())).setScale(2));
            newStoreOutsideInfo.setMaterialStatus(MaterialStatusEnums.getByName(storeOutsideVO.getMaterialStatus()));

            String location = StoreInOutMapperEnums.getNameByOutName(storeOutsideVO.getLocation());
            newStoreOutsideInfo.setLocation(location);
            newStoreOutsideInfo.setOutTime(new Date());

            if (null == storeOutsideVO.getCompleteTime()){
                if (!storeOutsideVO.getMaterialStatus().equals(MaterialStatusEnums.WAIXIAO.name)){
                    newStoreOutsideInfo.setCompleteTime(DateUtils.getDate(DateUtils.dateTimeToStr(new Date()), 10));
                }
            }else {
                newStoreOutsideInfo.setCompleteTime(storeOutsideVO.getCompleteTime());
            }
            if (StringUtils.isNotBlank(storeOutsideVO.getMaterialColor())){
                newStoreOutsideInfo.setMaterialColor(MaterialColorEnums.getEnumByInformation(storeOutsideVO.getMaterialColor()));
            }else {
                if (storeOutsideVO.getMaterialStatus().equals(MaterialStatusEnums.WAIXIAO.name)){
                    newStoreOutsideInfo.setMaterialColor(storeOutsideInfo.getMaterialColor());
                }
                newStoreOutsideInfo.setMaterialColor(MaterialColorEnums.WYS);
            }

            storeOutsideInfoService.saveOrUpdate(newStoreOutsideInfo);

            //全部转走 需要删掉原来的数据
            if (storeOutsideInfo.getMaterialNum() == storeOutsideVO.getMaterialNum()){
                storeOutsideInfo.setStatus(SysConf.DELETE_STATUS);
                storeOutsideInfoService.saveOrUpdate(storeOutsideInfo);
            }else {
                storeOutsideInfo.setMaterialNum(storeOutsideInfo.getMaterialNum() - storeOutsideVO.getMaterialNum());
                storeOutsideInfo.setTotalPrice(storeOutsideInfo.getTotalPrice().
                        subtract(storeOutsideInfo.getPrice().multiply(new BigDecimal(storeOutsideVO.getMaterialNum())).setScale(2)));
                if (StringUtils.isNotBlank(storeOutsideVO.getRemark())){
                    storeOutsideInfo.setRemark(storeOutsideVO.getRemark());
                }
                storeOutsideInfoService.saveOrUpdate(storeOutsideInfo);
            }

        }

    }

    @Override
    public IPage<StoreOutsideInfo> getList(StoreOutsidePageVO storeOutsidePageVO) {

        QueryWrapper<StoreOutsideInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",SysConf.NORMAL_STATUS);

        if (StringUtils.isNotBlank(storeOutsidePageVO.getMaterialName())){
            queryWrapper.eq("material_name",storeOutsidePageVO.getMaterialName());
        }

        if (StringUtils.isNotBlank(storeOutsidePageVO.getLocation())){
            queryWrapper.like("location",storeOutsidePageVO.getLocation());
        }

        if (StringUtils.isNotBlank(storeOutsidePageVO.getMaterialStatus())){
            queryWrapper.eq("material_status",MaterialStatusEnums.getByName(storeOutsidePageVO.getMaterialStatus()).code);
        }

        if (null != storeOutsidePageVO.getBeginTime()){
            queryWrapper.ge("out_time",storeOutsidePageVO.getBeginTime());
        }

        if (null != storeOutsidePageVO.getEndTime()){
            queryWrapper.le("out_time",storeOutsidePageVO.getEndTime());
        }

        if (StringUtils.isNotBlank(storeOutsidePageVO.getOrderBy())){
            if (storeOutsidePageVO.getDesc()){
                queryWrapper.orderByDesc(storeOutsidePageVO.getOrderBy());
            }else {
                queryWrapper.orderByAsc(storeOutsidePageVO.getOrderBy());
            }
        }else {
            queryWrapper.orderByDesc("out_time");
        }

        Page<StoreOutsideInfo> page = new Page<>();
        page.setCurrent(storeOutsidePageVO.getCurrentPage());
        page.setSize(storeOutsidePageVO.getPageSize());

        IPage<StoreOutsideInfo> infoIPage = storeOutsideInfoService.page(page,queryWrapper);

        return infoIPage;
    }
}
