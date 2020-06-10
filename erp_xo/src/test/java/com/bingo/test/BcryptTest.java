package com.bingo.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {


    @Test
    public void test1(){

        String pass = "123456";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashpass = bCryptPasswordEncoder.encode(pass);

        String hashpass1 = "$2a$10$51QG1bPBUV.qxrMnwEYyWeGSmy1P6RxGSG1gbZS5WwZ4l0oZ0CIjG";

        System.out.println(hashpass);

        boolean f = bCryptPasswordEncoder.matches("lxygwqf",hashpass1);

        System.out.println(f);



    }


    @Test
    public void test2(){

        System.out.println(convert(2372349));

    }

    /**
     * @param args
     *            add by zxx ,Nov 29, 2008
     */
    private static final char[] data = new char[] { '零', '壹', '贰', '叁', '肆',
            '伍', '陆', '柒', '捌', '玖' };

    private static final char[] units = new char[] { '元', '拾', '佰', '仟', '万',
            '拾', '佰', '仟', '亿' };

    public static String convert(int money) {
        StringBuffer sbf = new StringBuffer();
        int unit = 0;
        while (money != 0) {
            sbf.insert(0, units[unit++]);
            int number = money % 10;
            sbf.insert(0, data[number]);
            money /= 10;
        }
        return sbf.toString();
    }

}
