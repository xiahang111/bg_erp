package com.bingo.erp.base.fatocry.material;


import com.bingo.erp.base.fatocry.CalculateFactory;
import com.bingo.erp.base.vo.BaseCalculateResultVO;
import com.bingo.erp.base.vo.BaseCalculateVO;

/**
 * 下料计算方法工厂
 * @param <>
 */
public interface MaterialCalculateFactory<T> extends CalculateFactory {


    BaseCalculateResultVO calculate(T t) throws Exception;

}
