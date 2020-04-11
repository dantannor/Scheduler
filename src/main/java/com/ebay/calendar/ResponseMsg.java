package com.ebay.calendar;

/*
Using this object for response codes and msgs, as I'm not currently familiar with the Spring way
 */
public class ResponseMsg {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    int code;
    String msg;

    public ResponseMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}