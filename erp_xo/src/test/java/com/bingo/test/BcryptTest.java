package com.bingo.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {


    @Test
    public void test1(){

        String pass = "lxygwqf";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashpass = bCryptPasswordEncoder.encode(pass);

        String hashpass1 = "$2a$10$51QG1bPBUV.qxrMnwEYyWeGSmy1P6RxGSG1gbZS5WwZ4l0oZ0CIjG";

        System.out.println(hashpass);

        boolean f = bCryptPasswordEncoder.matches("lxygwqf",hashpass1);

        System.out.println(f);


    }
}
