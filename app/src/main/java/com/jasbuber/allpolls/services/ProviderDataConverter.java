package com.jasbuber.allpolls.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jasbuber.allpolls.models.PartialPoll;
import com.jasbuber.allpolls.models.PartialPollChoice;
import com.jasbuber.allpolls.models.Poll;

import java.util.HashMap;

/**
 * Created by Jasbuber on 11/06/2016.
 */
public class ProviderDataConverter {

    public Poll fillPollWithProviderData(Poll poll, HashMap<String, JsonObject> data) {

        for (PartialPoll partial : poll.getPartialPolls()) {
            fetchPartialPollData(partial, data.get(partial.getPollster()), poll.getTopic());
        }

        new PollCalculator().calculateResults(poll);
        return poll;

    }

    private PartialPoll fetchPartialPollData(PartialPoll partial, JsonObject data, String topic) {

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
