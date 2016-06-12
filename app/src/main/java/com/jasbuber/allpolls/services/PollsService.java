package com.jasbuber.allpolls.services;

import android.support.v7.widget.RecyclerView;

import com.jasbuber.allpolls.OnListFragmentInteractionListener;
import com.jasbuber.allpolls.PollsListAdapter;
import com.jasbuber.allpolls.models.Poll;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Jasbuber on 07/06/2016.
 */
public class PollsService {

    private static final String baseUrl = "http://192.168.100.189:9000/";

    public interface MainServerService {
        @GET("allpolls")
        Call<List<Poll>> getAvailablePolls();
    }

    public void getAvailablePollsList(final RecyclerView view, final OnListFragmentInteractionListener mListener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainServerService service = retrofit.create(MainServerService.class);

        Call<List<Poll>> call = service.getAvailablePolls();

        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {
                view.setAdapter(new PollsListAdapter(response.body(), mListener));
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
            }
        });
    }

    public List<Poll> getMyPollsList() {
        List<Poll> polls = new ArrayList<>();
        polls.add(new Poll("topic1"));
        polls.add(new Poll("topic2"));
        polls.add(new Poll("topic3"));
        polls.add(new Poll("topic4"));
        polls.add(new Poll("topic5"));

        return polls;
    }
}
