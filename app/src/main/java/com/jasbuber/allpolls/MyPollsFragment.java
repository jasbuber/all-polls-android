package com.jasbuber.allpolls;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasbuber.allpolls.repositories.PollRepository;
import com.jasbuber.allpolls.services.InternalPollService;

public class MyPollsFragment extends Fragment {

    private int columnCount = 2;
    private OnListMyPollsInteractionListener mListener;

    public MyPollsFragment() {
    }

    public static MyPollsFragment newInstance() {
        MyPollsFragment fragment = new MyPollsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_polls_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            recyclerView.setAdapter(new MyPollsAdapter(new InternalPollService(new PollRepository()).getMyPolls(), mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListMyPollsInteractionListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
