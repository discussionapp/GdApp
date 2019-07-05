package com.anshu.www.gdapp;

import java.util.Date;

public class message {

    String msg;
    String name;
    String key;
    private long time;


    public message() {}

    public message(String msg, String name) {
        this.msg = msg;
        this.name = name;
        time=new Date().getTime();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "message{" +
                "msg='" + msg + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", time=" + time +
                '}';
    }
}
