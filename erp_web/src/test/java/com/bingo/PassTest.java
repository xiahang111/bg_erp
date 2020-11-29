package com.bingo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PassTest {

    public static void main(String[] args) {

        String password = "qweqweqwe";
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPass = encoder.encode(password);
        System.out.println(newPass);
        String aaa = "$2a$10$PAfgLARCefWPnolymXz1Xen4N3gFuPIkxsqTOsiZhvwO40V0/12TK";

        System.out.println(encoder.matches("qweqweqwe",aaa));

    }

}
