package com.dpend.shopkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class Chart_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        BarChart bchart = findViewById(R.id.barchart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int[] intArray = new int[]{ 10000,34000,50000,24440,12000,70000,25000 };
        barEntries.add(new BarEntry(0,10000));
        barEntries.add(new BarEntry(1,20000));
        barEntries.add(new BarEntry(2,50000));
        barEntries.add(new BarEntry(3,70000));
        barEntries.add(new BarEntry(4,23000));
        barEntries.add(new BarEntry(5,56000));
        barEntries.add(new BarEntry(6,24000));
        String[] labels = new String[]{ "SUN","MON","TUE","WED","THU","FRI","SAT" };
//        for(int i=0; i<intArray.length; i++){
//            float value = (float) intArray[i];
//
//            BarEntry barEntry = new BarEntry(i,value);
//            barEntries.add(barEntry);
//        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Sales");
        barDataSet.setColors(Color.rgb(52, 164, 235));
        barDataSet.setDrawValues(false);
        bchart.setData(new BarData(barDataSet));
        bchart.getXAxis().setDrawGridLines(false);
        bchart.animateY(2000);
        bchart.getDescription().setText((""));
        bchart.setDrawBorders(false);
        bchart.getAxisRight().setEnabled(false);
        bchart.getAxisLeft().setDrawAxisLine(false);
        bchart.getAxisRight().setDrawAxisLine(false);
        bchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bchart.getAxisLeft().setTextColor(Color.LTGRAY);
        bchart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

//        bchart.getDescription().setTextColor(Color.BLUE);
    }
}