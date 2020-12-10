package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.xo.order.dto.SaleStatementDTO;
import com.bingo.erp.xo.order.vo.SaleStatementVO;

public interface StatementService{

    SaleStatementDTO getSaleStatement(SaleStatementVO saleStatementVO) throws MessageException;

}
