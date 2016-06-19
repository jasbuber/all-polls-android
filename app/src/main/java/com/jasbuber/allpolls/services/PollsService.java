package com.jasbuber.allpolls.services;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.jasbuber.allpolls.OnListFragmentInteractionListener;
import com.jasbuber.allpolls.PollActivity;
import com.jasbuber.allpolls.PollsListAdapter;
import com.jasbuber.allpolls.models.Poll;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jasbuber on 07/06/2016.
 */
public class PollsService {

    private static final String baseUrl = "http://192.168.100.189:9000/";

    public interface MainServerService {
        @GET("allpolls")
        Call<List<Poll>> getAvailablePolls();

        @GET("poll")
        Call<Poll> refreshPoll(@Query("id") long id);
    }

    public void getAvailablePollsList(final RecyclerView view, final OnListFragmentInteractionListener mListener,
                                      final SwipeRefreshLayout layout) {

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
                layout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
                layout.setRefreshing(false);
            }
        });
    }

    public void refreshPoll(final PollActivity activity, final long id, final ImageView loader) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainServerService service = retrofit.create(MainServerService.class);

        Call<Poll> call = service.refreshPoll(id);

        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                new ProviderService().getPartialPollsFromProvider(activity, response.body());
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                loader.clearAnimation();
            }
        });
    }

}
