package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

@TableName("m_message_remind")
@Data
public class MessageRemindInfo extends SuperEntity<MessageRemindInfo> {

    private String materialUid;

    private String adminUid;

    private String message;

    private Integer remindLevel;

    /**
     * status : 1、未读或未发出 2、已读或已发出
     */

}
