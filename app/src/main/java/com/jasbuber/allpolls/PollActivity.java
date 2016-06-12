package com.jasbuber.allpolls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.services.ChartDisplayService;
import com.jasbuber.allpolls.services.ProviderService;

public class PollActivity extends AppCompatActivity {

    Poll poll;

    PieChart pieChart;

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

    }

}
