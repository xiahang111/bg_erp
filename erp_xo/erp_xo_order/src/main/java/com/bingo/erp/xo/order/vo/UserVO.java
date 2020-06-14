package com.bingo.erp.xo.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserVO {

    private String avaterUrl;

    private String username;

    private String adminUid;

    private String roleUid;

    private String roleName;

    private String realName;

    private Integer loginCount;

    private String mobile;

    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

}
