package com.dpend.shopkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Fragment_Home extends Fragment {
    private BarChart bchart;
    private PieChart pieChart;
    Button addpro,returnpro,restock;
    DatabaseHelper helper;
    TextView salesno,date;
    Date c;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        salesno= view.findViewById(R.id.dashtsalesno);
        bchart = view.findViewById(R.id.mainactivitychart);
        pieChart =view.findViewById(R.id.mainacivitypiechart);
        addpro = view.findViewById(R.id.dashaddprobtn);
        returnpro=view.findViewById(R.id.dashreturnprobtn);
        restock=view.findViewById(R.id.dashrestockbtn);
        date = view.findViewById(R.id.dashdatetxt);
        c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(c);
        Get_weekdays();
        date.setText(formattedDate);
        addpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddProduct_activity.class);
                startActivity(intent);
            }
        });
        returnpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),returnProActivity.class);
                startActivity(intent);
            }
        });
        restock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Activity_Restock.class);
                startActivity(intent);
            }
        });
        helper=new DatabaseHelper(getActivity());
        Cursor cursor = helper.Getsalesno(formattedDate);
        cursor.moveToFirst();
        int no= cursor.getInt(0);
        salesno.setText(""+no);

        return view;
    }
    private void Chart(ArrayList<String> list){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for(int i=1;i<= list.size();i++){
            int s= Integer.parseInt(list.get(i-1));
            barEntries.add(new BarEntry(i-1,s));
        }
//        barEntries.add(new BarEntry(0,10000));

//        barEntries.add(new BarEntry(1,20000));
//        barEntries.add(new BarEntry(2,50000));
//        barEntries.add(new BarEntry(3,20000));
//        barEntries.add(new BarEntry(4,23000));
//        barEntries.add(new BarEntry(5,45000));
//        barEntries.add(new BarEntry(6,24000));


        pieEntries.add(new PieEntry(200,"Rin"));
        pieEntries.add(new PieEntry(100,"Pen"));
        pieEntries.add(new PieEntry(55,"Book"));
        pieEntries.add(new PieEntry(350,"Oreo"));
        pieEntries.add(new PieEntry(120,"Box "));
        String[] labels = new String[]{ "SUN","MON","TUE","WED","THU","FRI","SAT" };
        String[] pielabels = new String[]{ "RIN","PEN","BOOK","BOX","OREO" };
//        for(int i=0; i<intArray.length; i++){
//            float value = (float) intArray[i];
//
//            BarEntry barEntry = new BarEntry(i,value);
//            barEntries.add(barEntry);
//        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Sales");
        barDataSet.setColors(Color.rgb(98, 0, 238));
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


        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Products");
        pieDataSet.setColors(Pie_color);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(2000,2000);
        Description description= new Description();
        description.setText("hey");
        pieChart.setDescription(description);
        pieChart.getDescription().setEnabled(false);



    }
    private int[] Pie_color = {
            Color.rgb(252, 102, 3), Color.rgb(50, 168, 82), Color.rgb(3, 252, 248),
            Color.rgb(221, 62, 47), Color.rgb(222, 181, 47), Color.rgb(50, 168, 82)
    };
    private void Get_weekdays(){
        Cursor cursor = null;
        DatabaseHelper dbhelper = new DatabaseHelper(getActivity());

        int daycount = 0;
        String Wday="";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat sdfday = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat sdftxtmonth = new SimpleDateFormat("MMM", Locale.getDefault());
        SimpleDateFormat sdfyear = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat sddate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        String fday = sdfday.format(c);
        String fmonth = sdfmonth.format(c);
        String fyear = sdfyear.format(c);
        String ftxtmonth =sdftxtmonth.format(c);
        String fdate=sddate.format(c);

        int fDay=Integer.parseInt(fday);
        int fMonth=Integer.parseInt(fmonth);
        int fYear=Integer.parseInt(fyear);

        switch(day){
            case 1:
                Wday = "Sunday";
                daycount=0;
                break;
            case 2:
                Wday = "Monday";
                daycount=1;
                break;
            case 3:
                Wday = "Tuesday";
                daycount=2;
                break;
            case 4:
                Wday = "Wednesday";
                daycount=3;
                break;
            case 5:
                Wday = "Thursday";
                daycount=4;
                break;
            case 6:
                Wday = "Friday";
                daycount=5;
                break;
            case 7:
                Wday = "Saturday";
                daycount=6;
                break;

        }
        ArrayList<String> salelist = new ArrayList<String>();
        Calendar cc=Calendar.getInstance();
        String aabb = sddate.format(cc.getTime());
        Cursor c = dbhelper.GetWeeklysales(aabb);
        if(c.getCount()==0){
            dbhelper.AddDSaleData(aabb,0,0);
        }
        c.close();
        int added = 0;
        if(!Wday.equals("Sunday")){
            for(int i=0;i<=daycount;i++){
                if(added==0){
                    String dda = sddate.format(cc.getTime());
                    try {
                        cursor = dbhelper.GetWeeklysales(dda);
                        cursor.moveToFirst();
                        salelist.add(cursor.getInt(0)+"");
                        added+=1;
                    }catch (Exception e){
                        dbhelper.AddDSaleData(aabb,0,0);
                    }

                }
                else{
                    try{
                        cc.add(Calendar.DAY_OF_YEAR, -(1));
                        String dda = sddate.format(cc.getTime());
                        cursor = dbhelper.GetWeeklysales(dda);
                        cursor.moveToNext();
                        salelist.add(cursor.getInt(0)+"");
                    }catch (Exception e){
                        salelist.add("0");
                    }

                }


            }
            Collections.reverse(salelist);
            int nta = 6-daycount;
            for (int i=1;i<=nta;i++){
                salelist.add("0");
            }
        }
        else {
            cursor = dbhelper.GetWeeklysales(aabb);
            cursor.moveToFirst();
            salelist.add(cursor.getInt(0)+"");
            salelist.add("0");
            salelist.add("0");
            salelist.add("0");
            salelist.add("0");
            salelist.add("0");
            salelist.add("0");

        }

        cursor.close();
        Chart(salelist);

    }
}
