package com.bingo.erp.xo.order.mapper;

import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.xo.order.vo.BarReportResultVO;
import com.bingo.erp.xo.order.vo.DoubleBarReportReslutVO;
import com.bingo.erp.xo.order.vo.OrderMakerReportResultVO;
import com.bingo.erp.xo.order.vo.PieResultVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderInfoMapper extends SuperMapper<OrderInfo> {


    @Select("SELECT * FROM t_order_info where status = 1 ORDER BY create_time DESC limit 10")
    List<OrderInfo> getIndexOrderInfo();

    @Select("SELECT ORDER_ID FROM t_order_info WHERE uid = #{orderInfoUid}")
    String getOrderIdByUid(@Param("orderInfoUid") String orderInfoUid);

    @Select("select count(*) as count,order_maker as orderMaker from t_order_info where status = 1 and order_maker is not null GROUP BY order_maker")
    List<OrderMakerReportResultVO> getOrderMakerReportResult();

    @Select("select count(1) as value1 ,sum(total_price)as value2 ,salesman as name from  t_order_info  where  status = 1 and salesman is not null group by salesman order by name")
    List<DoubleBarReportReslutVO> getSalesmanTotalReport();

    @Select("select SUM(total_price) as count,order_maker as orderMaker from t_order_info where status = 1 and order_maker is not null group BY order_maker")
    List<OrderMakerReportResultVO> getOrderMakerSaleReportResult();

    @Select("select count(*) as value,order_type as name from t_order_info where status = 1 GROUP BY order_type;")
    List<PieResultVO> getOrderTypePie();

    @Select("select order_maker from t_order_info GROUP BY order_maker")
    List<String> getAllOrderMaker();

    @Select("select salesman from t_order_info where salesman is not null GROUP BY salesman")
    List<String> getAllSaleMan();

    @Select(" <script>" +
            " SELECT date(create_time) AS NAME,sum(total_price) AS VALUE " +
            " FROM t_order_info" +
            " <where>" +
            " 1=1 and status = 1" +
            " <if test=\"salesmanName != ''\"> and salesman = #{salesmanName} </if>" +
            " </where>" +
            " group by date(create_time)" +
            " </script>")
    List<BarReportResultVO> getSalesmanMonthReport(@Param("salesmanName") String salesmanName);


}
