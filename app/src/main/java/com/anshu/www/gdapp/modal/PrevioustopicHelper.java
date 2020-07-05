package com.anshu.www.gdapp.modal;

public class PrevioustopicHelper {
    String topic_name;
    String posted;

    public PrevioustopicHelper() {
    }

    public PrevioustopicHelper(String topic_name, String posted) {
        this.topic_name = topic_name;
        this.posted = posted;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }
}
