package com.anshu.www.gdapp;

public class user3 {


private String Topic;
private String Name;
private String Date;

    public user3() {
    }


    public user3(String topic, String name, String date) {
        Topic = topic;
        Name = name;
        Date = date;
    }


    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
