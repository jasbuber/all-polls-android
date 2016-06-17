package com.jasbuber.allpolls;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.jasbuber.allpolls.models.orm.PollORM;
import com.jasbuber.allpolls.services.ChartDisplayService;

import java.util.List;

public class MyPollsAdapter extends RecyclerView.Adapter<MyPollsAdapter.ViewHolder> {

    private final List<PollORM> polls;
    private final OnListMyPollsInteractionListener mListener;

    public MyPollsAdapter(List<PollORM> polls, OnListMyPollsInteractionListener listener) {
        this.polls = polls;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_my_polls_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.poll = polls.get(position);
        holder.mIdView.setText(polls.get(position).getTopic());
        holder.mChart.setData(new ChartDisplayService().generatePieData());
        holder.mChart.setDrawHoleEnabled(false);
        holder.mChart.getLegend().setEnabled(false);
        holder.mChart.setDescription("");
        holder.mChart.setRotationEnabled(false);
        holder.mChart.setTouchEnabled(false);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.poll);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final PieChart mChart;
        public PollORM poll;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mChart = (PieChart) view.findViewById(R.id.pie_chart);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
