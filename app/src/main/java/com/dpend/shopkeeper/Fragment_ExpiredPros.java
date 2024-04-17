package com.dpend.shopkeeper;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Fragment_ExpiredPros extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Item_aapbatch> itemlist = new ArrayList<>();
    Adapter_editprob adapter_aapbatch = new Adapter_editprob(itemlist);
    DatabaseHelper helper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_exppros,container,false);
        recyclerView = view.findViewById(R.id.fexprview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter_aapbatch);
        Get_data();
        return view;
    }
    private void Get_data(){
        helper = new DatabaseHelper(getActivity());
        Cursor cursor = helper.Exp_pros();
        if(cursor!=null){
            while(cursor.moveToNext()){
                Item_aapbatch item = new Item_aapbatch();
                int id = cursor.getInt(0);
                String bno = cursor.getString(2);
                int stock  = cursor.getInt(5);
                Date c = new Date(cursor.getLong(4));
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String date = df.format(c);
                float rprice = cursor.getFloat(6);
                float pprice = cursor.getFloat(4);
                item.Set_Stock(stock);
                item.Set_Batchno(bno);
                item.Set_Id(id);
                item.Set_Expdate(date);
                item.Set_Pprice(pprice);
                item.Set_Rprice(rprice);
                itemlist.add(item);
                adapter_aapbatch.notifyDataSetChanged();
            }
        }
    }
}
