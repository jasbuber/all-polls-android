package com.jasbuber.allpolls.models;

import com.jasbuber.allpolls.models.orm.PartialPollORM;
import com.jasbuber.allpolls.models.orm.PollORM;

import java.io.Serializable;
import java.util.ArrayList;
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

    String remoteId;

    Date expirationDate;

    public Poll() {
    }

    public Poll(PollORM poll) {
        this.id = poll.getId();
        this.topic = poll.getTopic();
        this.remoteId = poll.getRemoteId();
        this.expirationDate = poll.getExpirationDate();

        partialPolls = new ArrayList<>();
        for (PartialPollORM partial : poll.getPartialPolls()) {
            this.partialPolls.add(new PartialPoll(partial));
        }
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

    public String getRemoteId() {
        return remoteId;
    }

    public void setPartialPolls(List<PartialPoll> partialPolls) {
        this.partialPolls = partialPolls;
    }
}

