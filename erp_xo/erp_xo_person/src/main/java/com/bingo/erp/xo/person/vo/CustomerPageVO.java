package com.bingo.erp.xo.person.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

@Data
public class CustomerPageVO extends PageInfo<CustomerPageVO> {

    private String orderBy;

}
