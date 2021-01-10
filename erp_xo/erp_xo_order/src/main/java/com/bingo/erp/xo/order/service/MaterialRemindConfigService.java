package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.MaterialRemindConfig;
import com.bingo.erp.xo.order.vo.RemindConfigPageVO;
import com.bingo.erp.xo.order.vo.RemindConfigVO;

public interface MaterialRemindConfigService extends SuperService<MaterialRemindConfig> {

    void saveRemindConfig(RemindConfigVO remindConfigVO) throws Exception;

    IPage<MaterialRemindConfig> getRemindConfigList(RemindConfigPageVO remindConfigPageVO) throws Exception;

    void deleteRemindConfig(String uid) throws Exception;
}
