package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

@Data
@TableName("t_express_record")
public class ExpressRecord extends SuperEntity<ExpressRecord> {

    private String expressName;

    private String expressUrl;
}
