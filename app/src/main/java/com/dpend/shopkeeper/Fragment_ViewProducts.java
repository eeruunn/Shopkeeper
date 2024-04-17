package com.dpend.shopkeeper;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment_ViewProducts extends Fragment implements ProductAdapter.OnItemClickListener {
    ArrayList<ProductItem> items = new ArrayList<>();
    Button addnewprobtn;
    ImageView addprobackbtn;
    DatabaseHelper dbHelper;
    ProductAdapter adapter = new ProductAdapter(items);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewproducts,container,false);
        addprobackbtn=view.findViewById(R.id.addprobackbtn);
        addprobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.productrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        if(items.size()==0){
            Getdata();
        }

        return view;
    }
    private void Getdata(){
        dbHelper=new DatabaseHelper(getActivity());
        Cursor cursor = dbHelper.ReadAlldata();
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "Product not found in database", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                ProductItem productItem = new ProductItem();
                productItem.Set_ID(cursor.getInt(0));
                productItem.Set_Code(cursor.getString(1));
                productItem.Set_Name(cursor.getString(2));
                productItem.Set_Price(cursor.getFloat(3));
                productItem.Set_Unit(cursor.getString(5));
                productItem.Set_Stock(cursor.getInt(4));
                items.add(productItem);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDeleteItemClick(final int position) {
        AlertDialog.Builder abuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.aysalert_dialog,null);
        Button yes = view.findViewById(R.id.dltproalyes);
        Button no = view.findViewById(R.id.dltproalno);
        abuilder.setView(view);
        final AlertDialog dialog = abuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbh = new DatabaseHelper(getActivity());
                int id = items.get(position).Get_ID();
                int dlt = dbh.DeleteProduct(id);
                if (dlt==1){
                    Toast.makeText(getActivity(), "Product deleted", Toast.LENGTH_SHORT).show();
                    removeItem(position);
                }else {
                    Toast.makeText(getActivity(), "Oops Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onItemClick(int position) {
        ProductItem item = items.get(position);
        String name = item.Get_Name();
        String unit = item.Get_Unit();
        String code = item.Get_Code();
        float price = item.Get_Price();
        int id = item.Get_ID();
        int stock = item.Get_Stock();
        Fragment fragment = new Fragment_Editproduct();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("unit",unit);
        bundle.putFloat("price",price);
        bundle.putInt("id",id);
        bundle.putString("code",code);
        bundle.putInt("stock",stock);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }
    public void removeItem(int position) {
        adapter.items.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }
    private class Asynctask extends AsyncTask<Integer,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
