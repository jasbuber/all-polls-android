package com.jasbuber.allpolls.services;

import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.models.orm.PollORM;
import com.jasbuber.allpolls.repositories.PollRepository;

import java.util.List;

/**
 * Created by Jasbuber on 16/06/2016.
 */
public class InternalPollService {

    private PollRepository repository;

    public InternalPollService(PollRepository repository) {
        this.repository = repository;
    }

    public List<PollORM> getMyPolls() {
        return repository.getMyPolls();
    }

    public void createOrUpdatePoll(Poll poll) {
        PollORM pollORM = new PollORM(poll);
        repository.createOrUpdatePoll(pollORM);
    }

    public Poll getPoll(long id) {
        return new Poll(repository.getPoll(id));
    }

    public boolean pollExists(long id) {
        return repository.pollExists(id);
    }
    public void deletePoll(Poll poll) {
        repository.deletePoll(poll);
    }

}
