package com.jasbuber.allpolls;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jasbuber.allpolls.models.Poll;

import java.util.ArrayList;
import java.util.List;

public class MyPollsAdapter extends RecyclerView.Adapter<MyPollsAdapter.ViewHolder> {

    private final List<Poll> polls;
    private final OnListFragmentInteractionListener mListener;

    public MyPollsAdapter(List<Poll> polls, OnListFragmentInteractionListener listener) {
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
        holder.mChart.setData(generatePieData());
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

    protected PieData generatePieData() {

        int count = 4;

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        /*
        xVals.add("Quarter 1");
        xVals.add("Quarter 2");
        xVals.add("Quarter 3");
        xVals.add("Quarter 4");*/

        for(int i = 0; i < count; i++) {
            xVals.add(String.valueOf(i+1));

            entries1.add(new Entry((float) (Math.random() * 60) + 40, i));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        return new PieData(xVals, ds1);
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final PieChart mChart;
        public Poll poll;

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
