package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialRemindConfig;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.MaterialRemindConfigMapper;
import com.bingo.erp.xo.order.service.MaterialRemindConfigService;
import com.bingo.erp.xo.order.service.StoreSummaryInfoService;
import com.bingo.erp.xo.order.vo.RemindConfigPageVO;
import com.bingo.erp.xo.order.vo.RemindConfigVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MaterialRemindConfigServiceImpl extends
        SuperServiceImpl<MaterialRemindConfigMapper, MaterialRemindConfig> implements MaterialRemindConfigService {

    @Resource
    private MaterialRemindConfigMapper materialRemindConfigMapper;

    @Resource
    private StoreSummaryInfoService storeSummaryInfoService;

    @Resource
    private MaterialRemindConfigService materialRemindConfigService;

    @Override
    public void saveRemindConfig(RemindConfigVO remindConfigVO) throws Exception {

        if (remindConfigVO.getRemindMethod() == 2 || remindConfigVO.getRemindMethod() == 3){

            throw new MessageException("系统暂时只支持弹窗告警方式~");

        }

        QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",SysConf.NORMAL_STATUS);

        if (StringUtils.isNotBlank(remindConfigVO.getUid())){
           MaterialRemindConfig materialRemindConfig =  materialRemindConfigMapper.selectById(remindConfigVO.getUid());

           if (null == materialRemindConfig){
               throw new Exception("无此配置信息!请联系开发者");
           }
            queryWrapper.eq("material_name",materialRemindConfig.getMaterialName());
            queryWrapper.eq("material_color",materialRemindConfig.getMaterialColor().code);
            queryWrapper.eq("specification",materialRemindConfig.getSpecification());
        }else {
            queryWrapper.eq("material_name",remindConfigVO.getMaterialName());
            queryWrapper.eq("material_color",remindConfigVO.getMaterialColor());
            queryWrapper.eq("specification",remindConfigVO.getSpecification());
        }
        StoreSummaryInfo storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

        if (null == storeSummaryInfo){
            throw new MessageException("抱歉!成品库中无此材料");
        }

        MaterialRemindConfig materialRemindConfig = new MaterialRemindConfig();

        if (StringUtils.isNotBlank(remindConfigVO.getUid())){
            materialRemindConfig.setUid(remindConfigVO.getUid());
        }

        materialRemindConfig.setMaterialName(remindConfigVO.getMaterialName());
        if (StringUtils.isBlank(remindConfigVO.getUid())){
            materialRemindConfig.setMaterialColor(MaterialColorEnums.getByCode(remindConfigVO.getMaterialColor()));
            materialRemindConfig.setSpecification(remindConfigVO.getSpecification());
        }
        materialRemindConfig.setRemindMethod(remindConfigVO.getRemindMethod());
        materialRemindConfig.setThreshold(remindConfigVO.getThreshold());


        materialRemindConfigService.saveOrUpdate(materialRemindConfig);

    }

    @Override
    public IPage<MaterialRemindConfig> getRemindConfigList(RemindConfigPageVO remindConfigPageVO) throws Exception {

        QueryWrapper<MaterialRemindConfig> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(remindConfigPageVO.getMaterialName())){
            queryWrapper.eq("material_name", remindConfigPageVO.getMaterialName());
        }

        if (null != remindConfigPageVO.getMaterialColor() && remindConfigPageVO.getMaterialColor() != 0){
            queryWrapper.eq("material_color", remindConfigPageVO.getMaterialColor());
        }

        Page<MaterialRemindConfig> page = new Page<>();
        page.setCurrent(remindConfigPageVO.getCurrentPage());
        page.setSize(remindConfigPageVO.getPageSize());
        IPage<MaterialRemindConfig> configIPage = materialRemindConfigService.page(page,queryWrapper);

        return configIPage;
    }

    @Override
    public void deleteRemindConfig(String uid) throws Exception {

        materialRemindConfigMapper.deleteById(uid);

    }
}
