package com.jasbuber.allpolls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.jasbuber.allpolls.models.PartialPoll;
import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.services.ChartDisplayService;
import com.jasbuber.allpolls.services.PollCalculator;
import com.jasbuber.allpolls.services.ProviderService;

import java.util.Map;

public class PollActivity extends AppCompatActivity {

    Poll poll;

    PieChart pieChart;

    boolean isPartialView = false;

    Map<String, Double> partialResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new ProviderService().getPartialPollsFromProvider(this, (Poll) getIntent().getSerializableExtra("poll"));

    }

    public void displayPieChart(Poll poll) {
        this.poll = poll;

        pieChart = (PieChart) findViewById(R.id.poll_pie_chart);
        pieChart.setData(new ChartDisplayService().generatePieData(poll.getResults()));
        pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.setDescription("");
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

        ((TextView) findViewById(R.id.poll_title)).setText(poll.getTopic());

        LinearLayout partialLayout = (LinearLayout) findViewById(R.id.poll_partial_polls);

        PartialPollsAdapter adapter = new PartialPollsAdapter(this, poll.getPartialPolls());

        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getView(i, null, null).getTag();
            partialLayout.addView(adapter.getView(i, null, null));
        }
    }

    public void displayPartialPoll(PartialPoll partial) {

        isPartialView = true;
        partialResults = new PollCalculator().calculatePartialResults(partial);
        pieChart.setData(new ChartDisplayService().generatePieData(partialResults));
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
        findViewById(R.id.poll_partial_polls).setVisibility(View.GONE);
        findViewById(R.id.partial_polls_title).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.poll_title)).setText(poll.getTopic() + " - " + partial.getPollster());
    }

    @Override
    public void onBackPressed() {
        if (isPartialView) {
            pieChart.setData(new ChartDisplayService().generatePieData(poll.getResults()));
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
            findViewById(R.id.poll_partial_polls).setVisibility(View.VISIBLE);
            findViewById(R.id.partial_polls_title).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.poll_title)).setText(poll.getTopic());
            isPartialView = false;
        } else {
            super.onBackPressed();
        }

    }

}
