package com.jasbuber.allpolls.models;

import java.util.List;

/**
 * Created by Jasbuber on 09/06/2016.
 */
public class PartialPoll {

    private Long id;

    Poller poller;

    List<PartialPollChoice> pollerChoices;

    public PartialPoll() {
    }

    public Long getId() {
        return id;
    }
}
