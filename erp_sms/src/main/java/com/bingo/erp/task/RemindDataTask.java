package com.bingo.erp.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.MaterialRemindConfig;
import com.bingo.erp.commons.entity.MessageRemindInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.AdminMapper;
import com.bingo.erp.xo.order.mapper.MaterialRemindConfigMapper;
import com.bingo.erp.xo.order.mapper.MessageRemindInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreSummaryInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 告警消息定时处理类
 */
@Configuration
@EnableScheduling
@Slf4j
public class RemindDataTask {

    @Resource
    private MaterialRemindConfigMapper materialRemindConfigMapper;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;

    @Resource
    private MessageRemindInfoMapper messageRemindInfoMapper;

    @Resource
    private AdminMapper adminMapper;

    List<String> roleNames = Arrays.asList("admin", "store");

    /**
     * 每3个小时跑一次
     */
    @Scheduled(cron = " 0 0 0/3 * * ?")
    public void analyzeRemind() {

        QueryWrapper<MaterialRemindConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        //获取库存告警配置
        List<MaterialRemindConfig> remindConfigs = materialRemindConfigMapper.selectList(queryWrapper);

        if (remindConfigs.size() <= 0) {
            return;
        }

        List<String> adminUids = adminMapper.getAdminUidByRoleNames(roleNames);


        for (MaterialRemindConfig remindConfig : remindConfigs) {

            QueryWrapper<StoreSummaryInfo> summaryInfoQueryWrapper = new QueryWrapper<>();
            summaryInfoQueryWrapper.eq("status", SysConf.NORMAL_STATUS);
            summaryInfoQueryWrapper.eq("material_name", remindConfig.getMaterialName());
            summaryInfoQueryWrapper.eq("material_color", remindConfig.getMaterialColor());
            summaryInfoQueryWrapper.eq("specification", remindConfig.getSpecification());
            try {
                StoreSummaryInfo storeSummaryInfo = storeSummaryInfoMapper.selectOne(summaryInfoQueryWrapper);

                if (storeSummaryInfo.getMaterialNum().intValue() < remindConfig.getThreshold()) {
                    //需要往告警表中添加一条信息
                    //判断是否已经存在一条未读信息
                    for (String adminUid : adminUids) {
                        QueryWrapper<MessageRemindInfo> remindInfoQueryWrapper = new QueryWrapper<>();
                        remindInfoQueryWrapper.eq("status", SysConf.NORMAL_STATUS);
                        remindInfoQueryWrapper.eq("material_uid", storeSummaryInfo.getUid());
                        remindInfoQueryWrapper.eq("admin_uid",adminUid);

                        MessageRemindInfo messageRemindInfo = messageRemindInfoMapper.selectOne(remindInfoQueryWrapper);

                        if (null == messageRemindInfo) {
                            messageRemindInfo = new MessageRemindInfo();
                            messageRemindInfo.setAdminUid(adminUid);
                            messageRemindInfo.setMaterialUid(storeSummaryInfo.getUid());
                            //普通告警级别
                            messageRemindInfo.setRemindLevel(1);
                            messageRemindInfo.setMessage(getRemindMessage(remindConfig));

                            messageRemindInfoMapper.insert(messageRemindInfo);
                        }
                    }
                }

            } catch (Exception e) {
                log.error("查询成品库信息出错,原因:" + e.getMessage());
            }

        }

    }

    private String getRemindMessage(MaterialRemindConfig remindConfig) {

        String message = "您好!材料名称:-" + remindConfig.getMaterialName() +
                "-,颜色-" + remindConfig.getMaterialColor().name + "-,的成品料已经少于-" + remindConfig.getThreshold() + "-支,请尽快补货!";

        return message;

    }
}





































