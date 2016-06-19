package com.jasbuber.allpolls;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.services.PollsService;

import java.util.ArrayList;
import java.util.List;

public class PollsListFragment extends Fragment {

    private int columnsNr = 1;
    private OnListFragmentInteractionListener mListener;

    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PollsListFragment() {
    }

    public static PollsListFragment newInstance() {
        PollsListFragment fragment = new PollsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_polls_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        this.recyclerView = (RecyclerView) view.findViewById(R.id.available_polls_list);
        this.recyclerView.setLayoutManager(new GridLayoutManager(context, columnsNr));

        final SwipeRefreshLayout layout = (SwipeRefreshLayout) view.findViewById(R.id.polls_list_layout);
        layout.setColorSchemeColors(R.color.colorPrimary);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PollsService().getAvailablePollsList(recyclerView, mListener, layout);
            }
        });

        if (savedInstanceState != null) {
            List<Poll> polls = (List<Poll>) savedInstanceState.getSerializable("polls");
            this.recyclerView.setAdapter(new PollsListAdapter(polls, mListener));
        } else {
            layout.setRefreshing(true);
            new PollsService().getAvailablePollsList(recyclerView, mListener, layout);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        List<Poll> polls = ((PollsListAdapter) recyclerView.getAdapter()).getPolls();
        savedInstanceState.putParcelableArrayList("polls", (ArrayList) polls);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
