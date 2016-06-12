package com.jasbuber.allpolls.services;

import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        return new PieData(xVals, ds1);
    }

    public PieData generatePieData(HashMap<String, Double> data) {

        List<String> labels = new ArrayList<>();
        labels.addAll(data.keySet());

        List<Entry> values = new ArrayList<>();

        List<Double> valueList = new ArrayList<>();
        valueList.addAll(data.values());

        for (int i = 0; i < valueList.size(); i++) {
            values.add(new Entry((float) (double) valueList.get(i), i));
        }

        PieDataSet ds1 = new PieDataSet(values, "dfhjdfhfjdhfdfjdjfd");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        return new PieData(labels, ds1);
    }
}
