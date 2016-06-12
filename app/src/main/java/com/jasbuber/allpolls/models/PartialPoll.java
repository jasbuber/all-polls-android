package com.jasbuber.allpolls.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jasbuber on 09/06/2016.
 */
public class PartialPoll implements Serializable {

    private Long id;

    private Map<String, PartialPollChoice> pollerChoices;

    private String provider;

    private String pollster;

    private Set<String> universalValues = new HashSet<>();

    public PartialPoll() {
    }

    public Long getId() {
        return id;
    }

    public Map<String, PartialPollChoice> getPollerChoices() {
        return pollerChoices;
    }

    public String getProvider() {
        return provider;
    }

    public String getPollster() {
        return pollster;
    }

    public Set<String> getUniversalValues() {
        return universalValues;
    }
}
