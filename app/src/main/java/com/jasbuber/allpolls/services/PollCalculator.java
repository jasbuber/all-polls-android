package com.jasbuber.allpolls.services;

import com.jasbuber.allpolls.models.PartialPoll;
import com.jasbuber.allpolls.models.PartialPollChoice;
import com.jasbuber.allpolls.models.Poll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jasbuber on 11/06/2016.
 */
public class PollCalculator {

    Set<String> universalValues;

    public Poll calculateResults(Poll poll) {

        List<Map<String, Double>> choicesList = new ArrayList<>();

        List<PartialPoll> partialPolls = poll.getPartialPolls();

        universalValues = new HashSet<>();

        for (PartialPoll partial : partialPolls) {
            if (universalValues.isEmpty()) {
                universalValues = partial.getUniversalValues();
            } else {
                universalValues.retainAll(partial.getUniversalValues());
            }
        }

        for (PartialPoll partial : partialPolls) {
            Collection<PartialPollChoice> choices = partial.getPollerChoices().values();
            Map<String, Double> partChoices = new HashMap<>();

            Double total = 0.0;
            boolean isRecalculate = false;

            for (PartialPollChoice choice : choices) {
                if (universalValues.contains(choice.getUniversalValue())) {
                    partChoices.put(choice.getUniversalValue(), choice.getValue());
                    total += choice.getValue();
                } else {
                    isRecalculate = true;
                }
            }
            double difference = 100.0 - total;

            partChoices.put(PartialPollChoice.UNDECIDED, difference);

            if (isRecalculate) {
                partChoices = getRecalculatedMap(partChoices);

            }
            choicesList.add(partChoices);
        }

        universalValues.add(PartialPollChoice.UNDECIDED);
        poll.setResults(getFinalResults(choicesList));

        return poll;
    }

    private Map<String, Double> getRecalculatedMap(Map<String, Double> choicesMap) {

        double sum = 0.0;
        Map<String, Double> newMap = new HashMap<>();

        for (Double choice : choicesMap.values()) {
            sum += choice;
        }
        for (Map.Entry<String, Double> choice : choicesMap.entrySet()) {
            double newValue = (choice.getValue() / sum) * 100;
            newMap.put(choice.getKey(), newValue);
        }

        return newMap;
    }

    private HashMap<String, Double> getFinalResults(List<Map<String, Double>> choicesList) {

        HashMap<String, Double> result = new HashMap<>();

        for (String universalValue : universalValues) {
            double newValue = 0.0;
            for (Map<String, Double> partial : choicesList) {
                newValue += partial.get(universalValue);
            }
            newValue = newValue / choicesList.size();
            result.put(universalValue, newValue);
        }

        return result;
    }


}
