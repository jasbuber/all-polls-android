package com.jasbuber.allpolls.services;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jasbuber.allpolls.PollActivity;
import com.jasbuber.allpolls.models.Poll;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jasbuber on 11/06/2016.
 */
public class ProviderService {

    private static final String baseUrl = "http://elections.huffingtonpost.com/pollster/api/";

    public interface ProviderServerService {
        @GET("polls.json")
        Call<JsonArray> getPartialPolls(@Query("topic") String topic);
    }

    public void getPartialPollsFromProvider(final PollActivity activity, final Poll poll) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProviderServerService service = retrofit.create(ProviderServerService.class);

        Call<JsonArray> call = service.getPartialPolls(poll.getTopic());

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                HashMap<String, JsonObject> results = new HashMap<>();

                for (JsonElement poll : response.body()) {
                    JsonObject pollObj = poll.getAsJsonObject();
                    String pollster = pollObj.get("pollster").getAsString();

                    if (!results.containsKey(pollster)) {
                        results.put(pollster, pollObj);
                    }
                }

                new ProviderDataConverter().fillPollWithProviderData(poll, results);
                activity.displayPieChart(poll);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("tagBle", t.getMessage());
            }
        });
    }
}