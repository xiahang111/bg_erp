package com.bingo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.web.WebApplication;
import com.bingo.erp.xo.order.dto.StoreDataDTO;
import com.bingo.erp.xo.order.service.StoreSummaryInfoService;
import com.bingo.erp.xo.order.service.feign.StoreDataService;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
public class StoreDataTest {


    @Autowired
    private StoreDataService storeDataService;

    @Autowired
    private StoreSummaryInfoService storeSummaryInfoService;

    @Test
    public void test1(){

        List<StoreDataDTO> result = storeDataService.getStoreData();

        System.out.println(JsonUtils.objectToJson(result));

    }

    @Test
    public void test2(){

        List<insertVO> list = new ArrayList<>();


        /*list.add(new insertVO("玻璃柜H条","黄铜拉丝",new BigDecimal("174"),"3米"));
        list.add(new insertVO("一代玻璃柜横料","黄铜拉丝",new BigDecimal("74"),"3米"));
        list.add(new insertVO("玻璃柜H条","瓷砂黑",new BigDecimal("40"),"3米"));
        list.add(new insertVO("一代玻璃柜横料","瓷砂黑",new BigDecimal("130"),"3米"));
        list.add(new insertVO("一代玻璃柜竖料","瓷砂黑",new BigDecimal("35"),"3米"));
        list.add(new insertVO("二代玻璃柜横料","黄铜拉丝",new BigDecimal("30"),"3米"));
        list.add(new insertVO("二代玻璃柜竖料","黄铜拉丝",new BigDecimal("56"),"3米"));
        list.add(new insertVO("二代玻璃柜横料","瓷砂黑",new BigDecimal("65"),"3米"));
        list.add(new insertVO("二代玻璃柜竖料","瓷砂黑",new BigDecimal("81"),"3米"));
        list.add(new insertVO("二代玻璃柜横料","太空灰",new BigDecimal("70"),"3米"));
        list.add(new insertVO("二代玻璃柜竖料","太空灰",new BigDecimal("78"),"3米"));
        list.add(new insertVO("桌子横梁","黑色",new BigDecimal("154"),"3米"));
        list.add(new insertVO("桌子横梁","氟碳灰",new BigDecimal("379"),"3米"));
        list.add(new insertVO("桌子横梁","欧歌红",new BigDecimal("24"),"3米"));
        list.add(new insertVO("二代层板不带灯","黄铜拉丝",new BigDecimal("292"),"3米"));
        list.add(new insertVO("二代层板不带灯","太空灰",new BigDecimal("78"),"3米"));
        list.add(new insertVO("二代层板不带灯","瓷砂黑",new BigDecimal("160"),"3米"));
        list.add(new insertVO("二代层板带灯","黄铜拉丝",new BigDecimal("447"),"3米"));
        list.add(new insertVO("二代层板带灯","太空灰",new BigDecimal("68"),"3米"));
        list.add(new insertVO("二代层板带灯","瓷砂黑",new BigDecimal("648"),"3米"));

        list.add(new insertVO("联动3号","黄铜拉丝",new BigDecimal("568"),"3米"));
        list.add(new insertVO("联动3号","瓷砂黑",new BigDecimal("673"),"3米"));
        list.add(new insertVO("20连体1100拉手左","黄铜拉丝",new BigDecimal("438"),"3米"));
        list.add(new insertVO("20连体1100拉手右","黄铜拉丝",new BigDecimal("429"),"3米"));
        list.add(new insertVO("20连体168拉手","黄铜拉丝",new BigDecimal("1219"),"3米"));
        list.add(new insertVO("20连体(2个拉手)","黄铜拉丝",new BigDecimal("886"),"3米"));
        list.add(new insertVO("20连体(3个拉手)","黄铜拉丝",new BigDecimal("143"),"3米"));
        list.add(new insertVO("20连体(5个拉手)","黄铜拉丝",new BigDecimal("40"),"3米"));
        list.add(new insertVO("20连体(7个拉手)","黄铜拉丝",new BigDecimal("356"),"3米"));
        list.add(new insertVO("20边框","黄铜拉丝",new BigDecimal("889"),"3米"));
        list.add(new insertVO("20连体1100拉手左","瓷砂黑",new BigDecimal("448"),"3米"));
        list.add(new insertVO("20连体1100拉手右","瓷砂黑",new BigDecimal("422"),"3米"));
        list.add(new insertVO("20连体168拉手","瓷砂黑",new BigDecimal("192"),"3米"));
        list.add(new insertVO("20连体(3个拉手)","瓷砂黑",new BigDecimal("12"),"3米"));
        list.add(new insertVO("20连体(2个拉手)","瓷砂黑",new BigDecimal("283"),"3米"));
        list.add(new insertVO("20连体(7个拉手)","瓷砂黑",new BigDecimal("280"),"3米"));
        list.add(new insertVO("20通体拉手","黄铜拉丝",new BigDecimal("54"),"3米"));
        list.add(new insertVO("20通体拉手","瓷砂黑",new BigDecimal("94"),"3米"));
        list.add(new insertVO("一代22通体拉手","黄铜拉丝",new BigDecimal("26"),"2.5米"));
        list.add(new insertVO("一代22连体1100拉手右","黄铜拉丝",new BigDecimal("70"),"3米"));
        list.add(new insertVO("一代22连体1100拉手左","黄铜拉丝",new BigDecimal("69"),"3米"));
        list.add(new insertVO("一代22连体168拉手","黄铜拉丝",new BigDecimal("83"),"3米"));
        list.add(new insertVO("一代22连体1100拉手右","黄铜拉丝",new BigDecimal("124"),"2.5米"));
        list.add(new insertVO("一代22连体1100拉手左","黄铜拉丝",new BigDecimal("143"),"2.5米"));
        list.add(new insertVO("一代22连体168拉手","黄铜拉丝",new BigDecimal("16"),"2.5米"));
        list.add(new insertVO("一代22边框","黄铜拉丝",new BigDecimal("80"),"2.5米"));

        list.add(new insertVO("一代22连体1100拉手右","哑黑",new BigDecimal("10"),"2.5米"));
        list.add(new insertVO("一代22连体1100拉手左","哑黑",new BigDecimal("25"),"2.5米"));
        list.add(new insertVO("一代22边框","哑黑",new BigDecimal("288"),"2.5米"));
        list.add(new insertVO("一代22边框","绅士灰",new BigDecimal("288"),"2.5米"));
        list.add(new insertVO("二代22连体1100拉手左","黄铜拉丝",new BigDecimal("487"),"3米"));
        list.add(new insertVO("二代22连体168拉手","黄铜拉丝",new BigDecimal("235"),"3米"));
        list.add(new insertVO("二代22边框","黄铜拉丝",new BigDecimal("476"),"3米"));
        list.add(new insertVO("二代22边框","黄铜拉丝",new BigDecimal("520"),"3.3米"));
        list.add(new insertVO("二代22连体(2个拉手)","黄铜拉丝",new BigDecimal("280"),"3米"));
        list.add(new insertVO("二代22连体(3个拉手)","黄铜拉丝",new BigDecimal("130"),"3米"));
        list.add(new insertVO("二代22连体(7个拉手)","黄铜拉丝",new BigDecimal("160"),"3米"));
        list.add(new insertVO("二代22连体1100拉手左","绅士灰",new BigDecimal("210"),"3米"));
        list.add(new insertVO("二代22连体1100拉手右","绅士灰",new BigDecimal("240"),"3米"));
        list.add(new insertVO("二代22连体168拉手","绅士灰",new BigDecimal("272"),"3米"));
        list.add(new insertVO("二代22边框","绅士灰",new BigDecimal("80"),"3米"));
        list.add(new insertVO("二代22连体(2个拉手)","绅士灰",new BigDecimal("58"),"3米"));
        list.add(new insertVO("二代22连体(3个拉手)","绅士灰",new BigDecimal("50"),"3米"));
        list.add(new insertVO("二代22连体1100拉手左","哑黑",new BigDecimal("90"),"3米"));
        list.add(new insertVO("二代22连体1100拉手右","哑黑",new BigDecimal("100"),"3米"));
        list.add(new insertVO("二代22连体168拉手","哑黑",new BigDecimal("30"),"3米"));
        list.add(new insertVO("二代22边框","哑黑",new BigDecimal("80"),"3米"));
        list.add(new insertVO("二代22连体1100拉手左","古铜拉丝",new BigDecimal("138"),"3米"));
        list.add(new insertVO("二代22连体1100拉手右","古铜拉丝",new BigDecimal("129"),"3米"));
        list.add(new insertVO("二代22连体168拉手","古铜拉丝",new BigDecimal("120"),"3米"));
        list.add(new insertVO("二代22边框","古铜拉丝",new BigDecimal("460"),"3米"));
        list.add(new insertVO("二代22连体(3个拉手)","古铜拉丝",new BigDecimal("10"),"3米"));
        list.add(new insertVO("二代22连体(7个拉手)","古铜拉丝",new BigDecimal("20"),"3米"));
        list.add(new insertVO("二代22连体1100拉手左","瓷砂黑",new BigDecimal("294"),"3米"));
        list.add(new insertVO("二代22连体1100拉手右","瓷砂黑",new BigDecimal("330"),"3米"));
        list.add(new insertVO("二代22连体168拉手","瓷砂黑",new BigDecimal("273"),"3米"));
        list.add(new insertVO("二代22边框","瓷砂黑",new BigDecimal("2171"),"3米"));
        list.add(new insertVO("二代22连体(2个拉手)","瓷砂黑",new BigDecimal("163"),"3米"));
        list.add(new insertVO("二代22连体(3个拉手)","瓷砂黑",new BigDecimal("100"),"3米"));
        list.add(new insertVO("二代22连体(7个拉手)","瓷砂黑",new BigDecimal("200"),"3米"));

        list.add(new insertVO("22加厚1100拉手右","黄铜拉丝",new BigDecimal("250"),"3米"));
        list.add(new insertVO("22加厚1100拉手左","黄铜拉丝",new BigDecimal("242"),"3米"));
        list.add(new insertVO("22加厚168拉手","黄铜拉丝",new BigDecimal("465"),"3米"));
        list.add(new insertVO("22加厚168(7个拉手)","黄铜拉丝",new BigDecimal("138"),"3米"));
        list.add(new insertVO("22加厚边框","黄铜拉丝",new BigDecimal("420"),"3米"));

        list.add(new insertVO("22加厚连体1100拉手右","瓷砂黑",new BigDecimal("277"),"3米"));
        list.add(new insertVO("22加厚连体1100拉手左","瓷砂黑",new BigDecimal("263"),"3米"));
        list.add(new insertVO("22加厚连体168拉手","瓷砂黑",new BigDecimal("355"),"3米"));
        list.add(new insertVO("22加厚(7个拉手)","瓷砂黑",new BigDecimal("100"),"3米"));
        list.add(new insertVO("22加厚边框","瓷砂黑",new BigDecimal("278"),"3米"));

        list.add(new insertVO("22加厚1100拉手右","太空灰",new BigDecimal("40"),"3米"));
        list.add(new insertVO("22加厚1100拉手左","太空灰",new BigDecimal("40"),"3米"));
        list.add(new insertVO("22加厚连体168拉手","太空灰",new BigDecimal("60"),"3米"));
        list.add(new insertVO("22加厚边框","太空灰",new BigDecimal("148"),"3米"));

        list.add(new insertVO("扇形1100拉手右","哑黑",new BigDecimal("154"),"3米"));
        list.add(new insertVO("扇形1100拉手左","哑黑",new BigDecimal("172"),"3米"));
        list.add(new insertVO("扇形168拉手","哑黑",new BigDecimal("176"),"3米"));
        list.add(new insertVO("扇形(7个拉手)","哑黑",new BigDecimal("28"),"3米"));
        list.add(new insertVO("扇形边框","哑黑",new BigDecimal("234"),"3米"));

        list.add(new insertVO("扇形1100拉手右","太空灰",new BigDecimal("11"),"3米"));
        list.add(new insertVO("扇形1100拉手左","太空灰",new BigDecimal("11"),"3米"));
        list.add(new insertVO("扇形168拉手","太空灰",new BigDecimal("8"),"3米"));
        list.add(new insertVO("扇形边框","太空灰",new BigDecimal("44"),"3米"));

        list.add(new insertVO("扇形1100拉手右","黄铜拉丝",new BigDecimal("280"),"3米"));
        list.add(new insertVO("扇形1100拉手左","黄铜拉丝",new BigDecimal("282"),"3米"));
        list.add(new insertVO("扇形168(7个拉手)","黄铜拉丝",new BigDecimal("48"),"3米"));
        list.add(new insertVO("扇形168拉手","黄铜拉丝",new BigDecimal("240"),"3米"));
        list.add(new insertVO("扇形边框","黄铜拉丝",new BigDecimal("96"),"3米"));

        list.add(new insertVO("兵歌1号","拉丝金",new BigDecimal("69"),"3米"));
        list.add(new insertVO("兵歌1号","罗马灰",new BigDecimal("97"),"3米"));
        list.add(new insertVO("兵歌2号","拉丝金",new BigDecimal("27"),"3米"));
        list.add(new insertVO("兵歌2号","罗马灰",new BigDecimal("110"),"3米"));
        list.add(new insertVO("兵歌3号","拉丝金",new BigDecimal("76"),"3米"));
        list.add(new insertVO("兵歌3号","罗马灰",new BigDecimal("110"),"3米"));

        list.add(new insertVO("兵歌4号1100拉手右","罗马灰",new BigDecimal("342"),"3米"));
        list.add(new insertVO("兵歌4号1100拉手左","罗马灰",new BigDecimal("372"),"3米"));
        list.add(new insertVO("兵歌4号168拉手","罗马灰",new BigDecimal("470"),"3米"));
        list.add(new insertVO("兵歌4号2个拉手","罗马灰",new BigDecimal("130"),"3米"));
        list.add(new insertVO("兵歌4号3个拉手","罗马灰",new BigDecimal("57"),"3米"));
        list.add(new insertVO("兵歌4号7个拉手","罗马灰",new BigDecimal("80"),"3米"));
        list.add(new insertVO("兵歌4号边框","罗马灰",new BigDecimal("552"),"3米"));

        list.add(new insertVO("兵歌4号1100拉手右","黄铜拉丝",new BigDecimal("180"),"3米"));
        list.add(new insertVO("兵歌4号1100拉手左","黄铜拉丝",new BigDecimal("200"),"3米"));
        list.add(new insertVO("兵歌4号168拉手","黄铜拉丝",new BigDecimal("230"),"3米"));
        list.add(new insertVO("兵歌4号2个拉手","黄铜拉丝",new BigDecimal("60"),"3米"));
        list.add(new insertVO("兵歌4号3个拉手","黄铜拉丝",new BigDecimal("52"),"3米"));
        list.add(new insertVO("兵歌4号7个拉手","黄铜拉丝",new BigDecimal("30"),"3米"));
        list.add(new insertVO("兵歌4号边框","黄铜拉丝",new BigDecimal("720"),"3米"));

        list.add(new insertVO("兵歌4号1100拉手右","瓷砂黑",new BigDecimal("340"),"3米"));
        list.add(new insertVO("兵歌4号1100拉手左","瓷砂黑",new BigDecimal("310"),"3米"));
        list.add(new insertVO("兵歌4号168拉手","瓷砂黑",new BigDecimal("590"),"3米"));
        list.add(new insertVO("兵歌4号2个拉手","瓷砂黑",new BigDecimal("190"),"3米"));
        list.add(new insertVO("兵歌4号3个拉手","瓷砂黑",new BigDecimal("60"),"3米"));
        list.add(new insertVO("兵歌4号7个拉手","瓷砂黑",new BigDecimal("100"),"3米"));
        list.add(new insertVO("兵歌4号边框","瓷砂黑",new BigDecimal("1740"),"3米"));
        list.add(new insertVO("联动1号拉手","瓷砂黑",new BigDecimal("86"),"3米"));
        list.add(new insertVO("联动1号(玻璃横料)","黄铜拉丝",new BigDecimal("130"),"3米"));
        list.add(new insertVO("联动1号(玻璃竖料)","黄铜拉丝",new BigDecimal("140"),"3米"));
        list.add(new insertVO("联动1号(玻璃横料)","瓷砂黑",new BigDecimal("193"),"3米"));
        list.add(new insertVO("联动1号(玻璃竖料)","瓷砂黑",new BigDecimal("136"),"3米"));

        list.add(new insertVO("50斜边竖料","拉丝黑",new BigDecimal("265"),"3米"));
        list.add(new insertVO("50斜边横料","拉丝黑",new BigDecimal("285"),"3米"));
        list.add(new insertVO("50斜边竖料","拉丝灰",new BigDecimal("397"),"3米"));
        list.add(new insertVO("50斜边横料","拉丝灰",new BigDecimal("540"),"3米"));
        list.add(new insertVO("50斜边竖料","绅士灰",new BigDecimal("403"),"3米"));
        list.add(new insertVO("50斜边横料","绅士灰",new BigDecimal("352"),"3米"));
        list.add(new insertVO("50斜边竖料","罗马灰",new BigDecimal("50"),"3米"));
        list.add(new insertVO("50斜边横料","罗马灰",new BigDecimal("57"),"3米"));
        list.add(new insertVO("内藏双向灯条(OP-016)","瓷砂黑",new BigDecimal("205"),"3米"));
        list.add(new insertVO("内藏双向灯条(OP-016)","绅士灰",new BigDecimal("357"),"3米"));
        list.add(new insertVO("内藏双向灯条(OP-016)","黄铜拉丝",new BigDecimal("280"),"3米"));

        list.add(new insertVO("55天地横梁","黄铜拉丝",new BigDecimal("45"),"3米"));
        list.add(new insertVO("55天地横梁","哑黑",new BigDecimal("29"),"3米"));
        list.add(new insertVO("47天地横梁","哑黑",new BigDecimal("23"),"3米"));
        list.add(new insertVO("47天地横梁","黄铜拉丝",new BigDecimal("137"),"3米"));
        list.add(new insertVO("531连体2个拉手","黄铜拉丝",new BigDecimal("20"),"3米"));
        list.add(new insertVO("531连体3个拉手","黄铜拉丝",new BigDecimal("30"),"3米"));
        list.add(new insertVO("531边框","黄铜拉丝",new BigDecimal("40"),"3米"));

        list.add(new insertVO("博古架立柱","黄铜拉丝",new BigDecimal("464"),"3米"));
        list.add(new insertVO("博古架横梁","黄铜拉丝",new BigDecimal("430"),"3米"));
        list.add(new insertVO("博古架顶底横梁","黄铜拉丝",new BigDecimal("670"),"3米"));
        list.add(new insertVO("博古架玻璃横梁","黄铜拉丝",new BigDecimal("584"),"3米"));
        list.add(new insertVO("博古架立柱","瓷砂黑",new BigDecimal("1230"),"3米"));
        list.add(new insertVO("博古架横梁","瓷砂黑",new BigDecimal("1110"),"3米"));
        list.add(new insertVO("博古架顶底横梁","瓷砂黑",new BigDecimal("1320"),"3米"));
        list.add(new insertVO("博古架玻璃横梁","瓷砂黑",new BigDecimal("1357"),"3米"));

        list.add(new insertVO("18斜面扣条","哑黑",new BigDecimal("102"),"3米"));
        list.add(new insertVO("18斜面扣条","黄铜拉丝",new BigDecimal("46"),"3米"));
        list.add(new insertVO("18斜面扣条","罗马灰",new BigDecimal("10"),"3米"));
        list.add(new insertVO("柜体收口条","罗马灰",new BigDecimal("2"),"3米"));
        list.add(new insertVO("柜体收口条","瓷砂黑",new BigDecimal("64"),"3米"));
        list.add(new insertVO("柜体收口条","黄铜拉丝",new BigDecimal("206"),"3米"));
        list.add(new insertVO("柜体底座","黄铜拉丝",new BigDecimal("4"),"3米"));
        list.add(new insertVO("联动4号竖框","罗马灰",new BigDecimal("45"),"3米"));
        list.add(new insertVO("格栅门斜面","瓷砂黑",new BigDecimal("12"),"3米"));
        list.add(new insertVO("格栅门小方管","瓷砂黑",new BigDecimal("60"),"3米"));
        list.add(new insertVO("格栅门平面","黄铜拉丝",new BigDecimal("12"),"3米"));
        list.add(new insertVO("格栅门小方管","黄铜拉丝",new BigDecimal("97"),"3米"));
        list.add(new insertVO("18调直拉手","罗马灰",new BigDecimal("45"),"3米"));
        list.add(new insertVO("18调直拉手","哑黑",new BigDecimal("53"),"3米"));
        list.add(new insertVO("拉直器(铁)","金色",new BigDecimal("26"),"1.5米"));
        list.add(new insertVO("拉直器(铁)","金色",new BigDecimal("30"),"1.8米"));
        list.add(new insertVO("拉直器(铁)","金色",new BigDecimal("30"),"2.1米"));
        list.add(new insertVO("拉直器(铁)","金色",new BigDecimal("40"),"2.44米"));
        list.add(new insertVO("拉直器(铁)","金色",new BigDecimal("10"),"2.76米"));

        list.add(new insertVO("拉直器(不锈钢)","金色",new BigDecimal("30"),"1.5米"));
        list.add(new insertVO("拉直器(不锈钢)","金色",new BigDecimal("30"),"1.8米"));
        list.add(new insertVO("拉直器(不锈钢)","金色",new BigDecimal("22"),"2.44米"));
        list.add(new insertVO("拉直器(不锈钢)","金色",new BigDecimal("25"),"2.76米"));
        list.add(new insertVO("拉直器","深金色",new BigDecimal("19"),"2.44米"));
        list.add(new insertVO("带灯衣杆","金色",new BigDecimal("105"),"3米"));
        list.add(new insertVO("带灯衣杆","黑色",new BigDecimal("153"),"3米"));
        list.add(new insertVO("酒架圆管","太空灰",new BigDecimal("50"),"3米"));
        list.add(new insertVO("酒架圆管","金色",new BigDecimal("384"),"3米"));
        list.add(new insertVO("酒架圆管","黑色",new BigDecimal("274"),"3米"));
        list.add(new insertVO("酒架圆管","黑色",new BigDecimal("40"),"1米"));
        list.add(new insertVO("8厘灯条","黄铜拉丝",new BigDecimal("312"),"3米"));
        list.add(new insertVO("8厘灯条","哑黑",new BigDecimal("188"),"3米"));
        list.add(new insertVO("8厘灯条","绅士灰",new BigDecimal("262"),"3米"));
        list.add(new insertVO("43入式灯槽","黄铜拉丝",new BigDecimal("340"),"3米"));
        list.add(new insertVO("43入式灯槽","哑黑",new BigDecimal("240"),"3米"));
        list.add(new insertVO("43入式灯槽","绅士灰",new BigDecimal("240"),"3米"));
        list.add(new insertVO("45入式灯槽","黄铜拉丝",new BigDecimal("367"),"3米"));
        list.add(new insertVO("45入式灯槽","哑黑",new BigDecimal("298"),"3米"));
        list.add(new insertVO("45入式灯槽","绅士灰",new BigDecimal("370"),"3米"));

        list.add(new insertVO("封边拇指1.1拉手左边","哑黑",new BigDecimal("214"),"2.8米"));
        list.add(new insertVO("封边拇指1.1拉手右边","哑黑",new BigDecimal("195"),"2.8米"));
        list.add(new insertVO("封边拇指1.1拉手左边","黄铜拉丝",new BigDecimal("256"),"2.8米"));
        list.add(new insertVO("封边拇指1.1拉手右边","黄铜拉丝",new BigDecimal("254"),"2.8米"));
        list.add(new insertVO("封边拇指1.1拉手左边","哑黑",new BigDecimal("158"),"2.55米"));
        list.add(new insertVO("封边拇指1.1拉手右边","哑黑",new BigDecimal("18"),"2.55米"));
        list.add(new insertVO("封边拇指1.1拉手左边","黄铜拉丝",new BigDecimal("222"),"2.55米"));
        list.add(new insertVO("封边拇指1.1拉手右边","黄铜拉丝",new BigDecimal("215"),"2.55米"));

        list.add(new insertVO("封边F型1.1拉手左边","哑黑",new BigDecimal("226"),"2.8米"));
        list.add(new insertVO("封边F型1.1拉手右边","哑黑",new BigDecimal("195"),"2.8米"));
        list.add(new insertVO("封边F型1.1拉手左边","哑黑",new BigDecimal("264"),"2.55米"));
        list.add(new insertVO("封边F型1.1拉手右边","哑黑",new BigDecimal("266"),"2.55米"));
        list.add(new insertVO("封边F型1.1拉手左边","黄铜拉丝",new BigDecimal("78"),"2.8米"));
        list.add(new insertVO("封边F型1.1拉手右边","黄铜拉丝",new BigDecimal("148"),"2.8米"));
        list.add(new insertVO("封边F型1.1拉手左边","黄铜拉丝",new BigDecimal("243"),"2.55米"));
        list.add(new insertVO("封边F型1.1拉手右边","黄铜拉丝",new BigDecimal("225"),"2.55米"));

        list.add(new insertVO("封边F型168拉手","哑黑",new BigDecimal("208"),"2.8米"));
        list.add(new insertVO("封边F型168拉手","哑黑",new BigDecimal("224"),"2.55米"));
        list.add(new insertVO("封边F型168拉手","黄铜拉丝",new BigDecimal("178"),"2.8米"));
        list.add(new insertVO("封边F型168拉手","黄铜拉丝",new BigDecimal("160"),"2.55米"));

        list.add(new insertVO("封边条","哑黑",new BigDecimal("680"),"3米"));
        list.add(new insertVO("封边条","哑黑",new BigDecimal("394"),"2.55米"));
        list.add(new insertVO("封边条","黄铜拉丝",new BigDecimal("410"),"3米"));
        list.add(new insertVO("封边条","黄铜拉丝",new BigDecimal("86"),"2.8米"));
        list.add(new insertVO("封边条","黄铜拉丝",new BigDecimal("360"),"2.55米"));

        list.add(new insertVO("T型拉手","黄铜拉丝",new BigDecimal("122"),"2.8米"));
        list.add(new insertVO("T型拉手","哑黑",new BigDecimal("207"),"2.8米"));
        list.add(new insertVO("T型拉手","黄铜拉丝",new BigDecimal("119"),"2.4米"));
        list.add(new insertVO("T型拉手","哑黑",new BigDecimal("112"),"2.4米"));

        list.add(new insertVO("18层板扣条","黄铜拉丝",new BigDecimal("213"),"3米"));
        list.add(new insertVO("18层板扣条","哑黑",new BigDecimal("167"),"3米"));
        list.add(new insertVO("G型拉手","哑黑",new BigDecimal("133"),"3米"));
        list.add(new insertVO("7型拉手","哑黑",new BigDecimal("35"),"3米"));
        list.add(new insertVO("7型拉手","罗马灰",new BigDecimal("3"),"3米"));
        list.add(new insertVO("一代层板","哑黑",new BigDecimal("144"),"4.8米"));
        list.add(new insertVO("天地一号带拉手","哑黑",new BigDecimal("222"),"3米"));
        list.add(new insertVO("天地一号不带拉手","哑黑",new BigDecimal("139"),"3米"));

        list.add(new insertVO("135置物架A","太空灰",new BigDecimal("23"),"3米"));
        list.add(new insertVO("135置物架A","黑色",new BigDecimal("23"),"3米"));
        list.add(new insertVO("135置物架B","太空灰",new BigDecimal("44"),"3米"));
        list.add(new insertVO("135置物架B","黑色",new BigDecimal("28"),"3米"));
        list.add(new insertVO("135置物架C","太空灰",new BigDecimal("173"),"3米"));
        list.add(new insertVO("135置物架C","黑色",new BigDecimal("140"),"3米"));
        list.add(new insertVO("135置物架D","太空灰",new BigDecimal("38"),"3米"));
        list.add(new insertVO("135置物架D","黑色",new BigDecimal("40"),"3米"));
        list.add(new insertVO("135置物架E","太空灰",new BigDecimal("155"),"3米"));
        list.add(new insertVO("135置物架E","黑色",new BigDecimal("140"),"3米"));
        list.add(new insertVO("55隐框","黑色",new BigDecimal("14"),"3米"));
        list.add(new insertVO("玻璃柜底梁","黑色",new BigDecimal("33"),"3米"));
        list.add(new insertVO("玻璃柜底梁","太空灰",new BigDecimal("37"),"3米"));
        list.add(new insertVO("联动5号竖料边框","太空灰",new BigDecimal("21"),"3米"));
        list.add(new insertVO("联动5号拉手料","太空灰",new BigDecimal("49"),"3米"));

        for (insertVO vo:list) {

            QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("material_name",vo.getName());
            queryWrapper.eq("material_color",MaterialColorEnums.getByName(vo.color).code);
            queryWrapper.eq("specification",vo.getSpecification());

            StoreSummaryInfo storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

            if (null != storeSummaryInfo){
                System.out.println("成功" + vo.toString());

                storeSummaryInfo.setMaterialNum(vo.getNum());
                storeSummaryInfo.setTotalWeight(storeSummaryInfo.getWeight().multiply(vo.getNum()).setScale(2, BigDecimal.ROUND_HALF_UP));
                storeSummaryInfo.setTotalPrice(storeSummaryInfo.getPrice().multiply(vo.getNum()).setScale(2,BigDecimal.ROUND_HALF_UP));

                storeSummaryInfoService.updateById(storeSummaryInfo);
            }else {
                System.out.println( "不成功" + vo.toString());
            }*/

        for (insertVO vo:list) {

            QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("material_name",vo.getName());
            queryWrapper.eq("specification",vo.getSpecification());

            StoreSummaryInfo storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

            if (null != storeSummaryInfo){
                System.out.println("成功" + vo.toString());

                storeSummaryInfo.setMaterialNum(vo.getNum());
                storeSummaryInfo.setTotalWeight(storeSummaryInfo.getWeight().multiply(vo.getNum()).setScale(2, BigDecimal.ROUND_HALF_UP));
                storeSummaryInfo.setTotalPrice(storeSummaryInfo.getPrice().multiply(vo.getNum()).setScale(2,BigDecimal.ROUND_HALF_UP));

                storeSummaryInfoService.updateById(storeSummaryInfo);
            }else {
                System.out.println( "不成功" + vo.toString());
            }

        }

    }

    @Data
    class insertVO{
        private String name;
        private String color;
        private BigDecimal num;
        private String specification;

        public insertVO(String name,String color,BigDecimal num,String specification){
            this.name = name;
            this.color = color;
            this.num = num;
            this.specification = specification;
        }
    }
}
