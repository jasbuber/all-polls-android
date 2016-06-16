package com.jasbuber.allpolls;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.jasbuber.allpolls.models.PartialPoll;
import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.services.ChartDisplayService;
import com.jasbuber.allpolls.services.PollCalculator;
import com.jasbuber.allpolls.services.ProviderService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class PollActivity extends AppCompatActivity {

    Poll poll;

    PieChart pieChart;

    boolean isPartialView = false;

    Map<String, Double> partialResults;

    Dialog dialog;

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

        ChartDisplayService service = new ChartDisplayService();
        pieChart = (PieChart) findViewById(R.id.poll_pie_chart);
        pieChart.setData(service.generatePieData(poll.getResults()));
        service.initializeChart(pieChart);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

        ((TextView) findViewById(R.id.poll_title)).setText(poll.getTopic());

        LinearLayout partialLayout = (LinearLayout) findViewById(R.id.poll_partial_polls);

        Collections.sort(poll.getPartialPolls(), new Comparator<PartialPoll>() {
            public int compare(PartialPoll p1, PartialPoll p2) {
                return p2.getLastUpdated().compareTo(p1.getLastUpdated());
            }
        });

        PartialPollsAdapter adapter = new PartialPollsAdapter(this, poll.getPartialPolls());

        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getView(i, null, null).getTag();
            partialLayout.addView(adapter.getView(i, null, null));
        }
    }

    public void displayPartialPoll(PartialPoll partial) {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_partial_poll);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        PieChart partialChart = (PieChart) dialog.findViewById(R.id.partial_pie_chart);

        ((TextView) dialog.findViewById(R.id.partial_poll_title)).setText(partial.getPollster());
        ((TextView) dialog.findViewById(R.id.dialog_last_updated))
                .setText( new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(partial.getLastUpdated()));

        isPartialView = true;

        partialResults = new PollCalculator().calculatePartialResults(partial);

        ChartDisplayService service = new ChartDisplayService();
        partialChart.setData(service.generatePieData(partialResults));
        service.initializeChart(partialChart);
        partialChart.notifyDataSetChanged();
        partialChart.invalidate();

        dialog.show();

    }

}
