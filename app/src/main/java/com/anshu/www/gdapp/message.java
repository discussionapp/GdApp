package com.anshu.www.gdapp;

import java.util.Date;

public class message {

    String msg;
    String name;
    String key;
    int votes;
    int likes;
    int dislikes;
    String fromuserid;
    private long time;
    boolean polllike,polldislike;


    public message() {}

    public message(String msg, String name, int likes, int dislikes, String fromuserid,boolean polllike,boolean polldislike) {
        this.msg = msg;
        this.name = name;
        time=new Date().getTime();
        this.fromuserid=fromuserid;
        this.likes=likes;
        this.dislikes=dislikes;
        this.polllike = polllike;
        this.polldislike = polldislike;
    }

    public boolean isPolllike() {
        return polllike;
    }

    public void setPolllike(boolean polllike) {
        this.polllike = polllike;
    }

    public boolean isPolldislike() {
        return polldislike;
    }

    public void setPolldislike(boolean polldislike) {
        this.polldislike = polldislike;
    }

    public message(String msg, String name, int likes, int dislikes, String fromuserid) {
        this.msg = msg;
        this.name = name;
        time=new Date().getTime();
        this.fromuserid=fromuserid;
        this.likes=likes;
        this.dislikes=dislikes;
    }

    public long getTime() {
        return time;
    }
public String getfromuserid(){ return fromuserid;}
    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public int getVotes(){ return votes;}

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

    public int  getlikes()
    {
        return this.likes;

    }

    public int getDislikes()
    {

        return this.dislikes;

    }

    //public void setVotes(int vote) {
       // this.votes = vote;
    //}

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "message{" +
                "msg='" + msg + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", time=" + time + '\'' + ", votes="+ fromuserid+ '\''+
                '}';
    }
}
