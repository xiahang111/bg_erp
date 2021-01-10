package com.bingo.erp.xo.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StoreOutsideVO {

    private String uid;

    private String  materialStatus;

    private String location;

    private String materialColor;

    private Integer materialNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date completeTime;

    private String remark;
}
