package com.jasbuber.allpolls.services;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jasbuber on 11/06/2016.
 */
public class ChartDisplayService {

    public PieData generatePieData() {

        int count = 4;

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            xVals.add(String.valueOf(i + 1));

            entries1.add(new Entry((float) (Math.random() * 60) + 40, i));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        ds1.setColors(ColorTemplate.PASTEL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        return new PieData(xVals, ds1);
    }

    public PieData generatePieData(Map<String, Double> data) {

        List<String> labels = new ArrayList<>();
        labels.addAll(data.keySet());

        List<Entry> values = new ArrayList<>();

        List<Double> valueList = new ArrayList<>();
        valueList.addAll(data.values());

        for (int i = 0; i < valueList.size(); i++) {
            values.add(new Entry((float) (double) valueList.get(i), i));
        }

        PieDataSet ds1 = new PieDataSet(values, "");
        ds1.setColors(ColorTemplate.PASTEL_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        return new PieData(labels, ds1);
    }

    public void initializeChart(PieChart chart){
        chart.setDrawHoleEnabled(false);
        chart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        chart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        chart.setDescription("");
        chart.setRotationEnabled(false);
        chart.setTouchEnabled(false);
    }
}
