package com.dpend.shopkeeper;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Fragment_Returns extends Fragment implements Adapter_returnedItem.OnItemClickListener {
    RecyclerView ritemsrview;
    ArrayList<Item_returned> items = new ArrayList<>();
    Adapter_returnedItem adapter = new Adapter_returnedItem(items);
    ImageButton datefilter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_returns,container,false);
        datefilter = view.findViewById(R.id.frdatefilterbtn);
        ritemsrview = view.findViewById(R.id.returnsrview2);
        ritemsrview.setLayoutManager(new LinearLayoutManager(getActivity()));
        ritemsrview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        if(items.size()==0){
            Get_Data();
        }
        datefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }
        });
        return view;

    }
    private void Get_Data(){
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        Cursor cursor = helper.GetReturns();
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                Item_returned item = new Item_returned();
                int bill_no = cursor.getInt(1);
                int id = cursor.getInt(2);
                int qty = cursor.getInt(3);
                String reason = cursor.getString(4);
                String name = cursor.getString(5);
                String date = cursor.getString(6);
                item.Set_Billno(bill_no);
                item.Set_Id(id);
                item.Set_Qty(qty);
                item.Set_Reason(reason);
                item.Set_Name(name);
                item.Set_Date(date);
                items.add(item);
                adapter.notifyDataSetChanged();

            }
        }
        else {
            Toast.makeText(getActivity(),"No Returns",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnItemClick(int position) {
        Bundle bundle = new Bundle();
        Item_returned item_returned = items.get(position);
        String name = item_returned.Get_Name();
        String reason = item_returned.Get_Reason();
        String date = item_returned.Get_Date();
        int qty = item_returned.Get_Qty();
        int billno = item_returned.Get_Billno();
        int productid = item_returned.Get_Id();
        bundle.putString("name",name);
        bundle.putString("reason",reason);
        bundle.putString("date",date);
        bundle.putInt("qty",qty);
        bundle.putInt("billno",billno);
        bundle.putInt("pid",productid);
        Fragment fragment = new Fragment_ReturnsDetailed();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }
    private void DateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.datepicker_layout,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final DatePicker picker = view.findViewById(R.id.dldp);
        Button done = view.findViewById(R.id.dldbtn);
        ImageButton close  = view.findViewById(R.id.dlcbtn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = picker.getDayOfMonth() +"/"+(picker.getMonth()+1)+"/"+picker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//                expdateformated = df.format(calendar.getTime());
//                expdate.setText(expdateformated);
                dialog.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
