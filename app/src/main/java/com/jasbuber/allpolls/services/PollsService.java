package com.jasbuber.allpolls.services;

import com.jasbuber.allpolls.models.Poll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasbuber on 07/06/2016.
 */
public class PollsService {

    public List<Poll> getAvailablePollsList(){
        List<Poll> polls = new ArrayList<>();
        polls.add(new Poll("topic1"));
        polls.add(new Poll("topic2"));
        polls.add(new Poll("topic3"));
        polls.add(new Poll("topic4"));
        polls.add(new Poll("topic5"));

        return polls;
    }

    public List<Poll> getMyPollsList(){
        List<Poll> polls = new ArrayList<>();
        polls.add(new Poll("topic1"));
        polls.add(new Poll("topic2"));
        polls.add(new Poll("topic3"));
        polls.add(new Poll("topic4"));
        polls.add(new Poll("topic5"));

        return polls;
    }
}
