package com.jasbuber.allpolls.models;

/**
 * Created by Jasbuber on 07/06/2016.
 */
public class Poll {

    String topic;

    public Poll(String topic){
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
