package com.jasbuber.allpolls.models;

/**
 * Created by Jasbuber on 09/06/2016.
 */
public class PartialPollChoice {

    private Long id;

    PollChoice choice;

    private String name;

    public PartialPollChoice() {
    }

    public Long getId() {
        return id;
    }

    public PollChoice getChoice() {
        return choice;
    }

    public String getName() {
        return name;
    }
}

