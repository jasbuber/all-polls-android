package com.jasbuber.allpolls.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jasbuber.allpolls.models.PartialPoll;
import com.jasbuber.allpolls.models.PartialPollChoice;
import com.jasbuber.allpolls.models.Poll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jasbuber on 11/06/2016.
 */
public class ProviderDataConverter {

    public Poll fillPollWithProviderData(Poll poll, HashMap<String, JsonObject> data) {

        List<PartialPoll> toDelete = new ArrayList<>();

        for (PartialPoll partial : poll.getPartialPolls()) {
            if(data.get(partial.getPollster()) != null) {
                fetchPartialPollData(partial, data.get(partial.getPollster()), poll.getRemoteId());
            }else{
                toDelete.add(partial);
            }
        }

        poll.getPartialPolls().removeAll(toDelete);

        new PollCalculator().calculateResults(poll);
        return poll;

    }

    private PartialPoll fetchPartialPollData(PartialPoll partial, JsonObject data, String topic) {

        try {
            partial.setLastUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
                    .parse(data.get("last_updated").getAsString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JsonArray questions = data.get("questions").getAsJsonArray();

        for (int i = 0; i < questions.size(); i++) {
            JsonObject question = questions.get(i).getAsJsonObject();

            JsonElement qTopic = question.get("topic");

            if (!qTopic.isJsonNull() && qTopic.getAsString().toLowerCase().equals(topic)) {
                JsonArray choices = question.get("subpopulations").getAsJsonArray()
                        .get(0).getAsJsonObject().get("responses").getAsJsonArray();

                for (JsonElement choiceElem : choices) {
                    JsonObject choice = choiceElem.getAsJsonObject();
                    String partialChoice = choice.get("choice").getAsString();

                    PartialPollChoice partialObj = partial.getPollerChoices().get(partialChoice);

                    if (partialObj != null) {
                        partialObj.setValue(choice.get("value").getAsDouble());
                        partial.getUniversalValues().add(partialObj.getUniversalValue());
                    }
                }
            }
        }

        return partial;
    }
}
