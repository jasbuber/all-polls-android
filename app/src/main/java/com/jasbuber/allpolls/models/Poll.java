package com.jasbuber.allpolls.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasbuber on 07/06/2016.
 */
public class Poll implements Serializable {

    private Long id;

    List<PartialPoll> partialPolls;

    String topic;

    Date expirationDate;

    public Poll() {
    }

    public Poll(String topic) {
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public List<PartialPoll> getPartialPolls() {
        return partialPolls;
    }

    public String getTopic() {
        return topic;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public HashMap<String, Double> results;

    public HashMap<String, Double> getResults() {
        return results;
    }

    public void setResults(HashMap<String, Double> results) {
        this.results = results;
    }
}

