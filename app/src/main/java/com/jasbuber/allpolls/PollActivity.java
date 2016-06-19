package com.jasbuber.allpolls;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.jasbuber.allpolls.models.PartialPoll;
import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.repositories.PollRepository;
import com.jasbuber.allpolls.services.ChartDisplayService;
import com.jasbuber.allpolls.services.InternalPollService;
import com.jasbuber.allpolls.services.PollCalculator;
import com.jasbuber.allpolls.services.PollsService;
import com.jasbuber.allpolls.services.ProviderService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class PollActivity extends AppCompatActivity {

    Poll poll;

    PartialPoll selectedPartial;

    PieChart pieChart;

    boolean isPartialView = false;

    Dialog dialog;

    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_to_my_polls);
        final ImageButton refresh = (ImageButton) findViewById(R.id.refresh_poll);

        if (savedInstanceState != null) {
            restoreInstance(savedInstanceState, fab);
            refreshPoll(refresh);
        } else {
            initializeNewInstance(fab, refresh);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) {
                    new InternalPollService(new PollRepository()).deletePoll(poll);
                    fab.setImageResource(R.mipmap.star_inactive);
                    Toast.makeText(PollActivity.this, PollActivity.this.getString(R.string.poll_deleted), Toast.LENGTH_SHORT).show();
                    isFavorite = false;

                } else {
                    if (poll.getResults() == null || poll.getResults().isEmpty()) {
                        Toast.makeText(PollActivity.this, PollActivity.this.getString(R.string.no_adding_to_favorites), Toast.LENGTH_SHORT).show();
                    } else {
                        new InternalPollService(new PollRepository()).createOrUpdatePoll(poll);
                        fab.setImageResource(R.mipmap.star_active);
                        Toast.makeText(PollActivity.this, PollActivity.this.getString(R.string.poll_added), Toast.LENGTH_SHORT).show();
                        isFavorite = true;
                    }
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPoll(refresh);
            }
        });
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
        partialLayout.removeAllViews();

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

        findViewById(R.id.refresh_poll).clearAnimation();
        if (isFavorite) {
            new InternalPollService(new PollRepository()).createOrUpdatePoll(poll);
        }
    }

    public void displayPartialPoll(PartialPoll partial) {

        selectedPartial = partial;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_partial_poll);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        PieChart partialChart = (PieChart) dialog.findViewById(R.id.partial_pie_chart);

        ((TextView) dialog.findViewById(R.id.partial_poll_title)).setText(partial.getPollster());
        ((TextView) dialog.findViewById(R.id.dialog_last_updated))
                .setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(partial.getLastUpdated()));

        isPartialView = true;

        Map<String, Double> partialResults = new PollCalculator().calculatePartialResults(partial);

        ChartDisplayService service = new ChartDisplayService();
        partialChart.setData(service.generatePieData(partialResults));
        service.initializeChart(partialChart);
        partialChart.notifyDataSetChanged();
        partialChart.invalidate();

        dialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isPartialView", isPartialView);
        savedInstanceState.putBoolean("isFavorite", isFavorite);
        savedInstanceState.putSerializable("poll", poll);
        if (isPartialView) {
            savedInstanceState.putSerializable("selectedPartial", selectedPartial);
        }
    }

    private void initializeNewInstance(FloatingActionButton fab, ImageButton but) {
        InternalPollService service = new InternalPollService(new PollRepository());

        if (getIntent().getSerializableExtra("poll") != null) {
            poll = (Poll) getIntent().getSerializableExtra("poll");
            boolean pollExists = service.pollExists(poll.getId());

            if (pollExists) {
                isFavorite = true;
                fab.setImageResource(R.mipmap.star_active);
            }

            if (PollsService.isNetworkAvailable(this)) {
                new ProviderService().getPartialPollsFromProvider(this, poll);
            } else {
                if (pollExists) {
                    Poll dbPoll = service.getPoll(poll.getId());
                    new PollCalculator().calculateResults(dbPoll);
                    displayPieChart(dbPoll);
                }
                Toast.makeText(this, this.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        } else {
            long id = getIntent().getLongExtra("pollId", -1);
            isFavorite = true;
            fab.setImageResource(R.mipmap.star_active);
            Poll poll = service.getPoll(id);
            new PollCalculator().calculateResults(poll);
            displayPieChart(poll);
            refreshPoll(but);
        }
    }

    private void restoreInstance(Bundle savedInstance, FloatingActionButton fab) {
        isFavorite = savedInstance.getBoolean("isFavorite");
        isPartialView = savedInstance.getBoolean("isPartialView");
        poll = (Poll) savedInstance.getSerializable("poll");

        displayPieChart(poll);
        if (isPartialView) {
            selectedPartial = (PartialPoll) savedInstance.getSerializable("selectedPartial");
            displayPartialPoll(selectedPartial);
        }

        if (isFavorite) {
            fab.setImageResource(R.mipmap.star_active);
        } else {
            fab.setImageResource(R.mipmap.star_inactive);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void refreshPoll(ImageView refresh) {

        if (PollsService.isNetworkAvailable(this)) {
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(700);

            refresh.startAnimation(anim);
            new PollsService().refreshPoll(PollActivity.this, poll.getId(), refresh);
        } else {
            Toast.makeText(this, this.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

}
