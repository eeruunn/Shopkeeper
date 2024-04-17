//package com.example.shopkeeper;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class Adapter_returnedItem extends RecyclerView.Adapter<Adapter_returnedItem.ReturnedItemViewHolder> {
//    ArrayList<Item_returned> items;
//    OnItemClickListener mlistener;
//
//    public interface OnItemClickListener{
//        void onDeleteItemClick(int position);
//        void onItemClick(int position);
//    }
//    public void setOnItemClickListener(OnItemClickListener listener){
//        mlistener = listener;
//    }
//    public Adapter_returnedItem(ArrayList<Item_returned> mitems) {
//        items = mitems;
//
//    }
//    @NonNull
//    @Override
//    public ReturnedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_returned,parent,false);
//        return new ReturnedItemViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull ReturnedItemViewHolder holder, int position) {
//        int billno=items.get(position).Get_Billno();
//        int id=items.get(position).Get_Id();
//        int qty=items.get(position).Get_Qty();
//
//        String name = items.get(position).Get_Name();
//        if(name.length() > 20){
//            String ename= name.substring(0,20)+"...";
//            holder.rname.setText(ename);
//        }
//        else{
//            holder.rname.setText(name);
//        }
//        String date = items.get(position).Get_Date();
//
//        holder.rqty.setText("Qty: "+qty);
//        holder.rdate.setText(date);
//
//    }
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//    public class ReturnedItemViewHolder extends RecyclerView.ViewHolder {
//        private final TextView rname;
//        private final TextView rdate;
//        private final TextView rqty;
//
//        public ReturnedItemViewHolder(@NonNull View itemView) {
//            super(itemView);
//            rname = itemView.findViewById(R.id.ripname);
//            rdate = itemView.findViewById(R.id.ridate);
//            rqty =itemView.findViewById(R.id.riqty);
//
//        }
//    }
//}
//
//

package com.dpend.shopkeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_returnedItem extends RecyclerView.Adapter<Adapter_returnedItem.ReturneditemViewHolder> {
    ArrayList<Item_returned> items;
    OnItemClickListener mlistener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public Adapter_returnedItem(ArrayList<Item_returned> mitems){
        items = mitems;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener=listener;
    }

    @NonNull
    @Override
    public ReturneditemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_returned,parent,false);
        return new ReturneditemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturneditemViewHolder holder, int position) {
        String name = items.get(position).Get_Name();
        String mname;
        String date = items.get(position).Get_Date();
        int qty = items.get(position).Get_Qty();
        if (name.length()>20){
            mname = name.substring(0,20)+"...";
        }else {
            mname=name;
        }
        holder.name.setText(mname);
        holder.date.setText(date);
        holder.qty.setText("Qty: "+qty);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ReturneditemViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView date;
        public TextView qty;
        public TextView billno;
        public ReturneditemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ripname);
            date= itemView.findViewById(R.id.ridate);
            qty= itemView.findViewById(R.id.riqty);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mlistener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
