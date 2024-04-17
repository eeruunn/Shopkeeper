package com.dpend.shopkeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Salesitemadapter extends RecyclerView.Adapter<Salesitemadapter.SalesitemViewholder> {
    ArrayList<Sales_Item> items;
    OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onDeleteItemClick(int position);
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }
    public Salesitemadapter(ArrayList<Sales_Item> mitems) {
        items = mitems;

    }
    @NonNull
    @Override
    public SalesitemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_item,parent,false);
        return new SalesitemViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesitemViewholder holder, int position) {
        String price="₹ " + items.get(position).Get_Price();
        String date = ""+items.get(position).Get_Date();
        String time = ""+items.get(position).Get_Time();

        holder.sdate.setText(date);
        holder.stime.setText(time);
        holder.samount.setText(price);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class SalesitemViewholder extends RecyclerView.ViewHolder {
        private final TextView sdate;
        private final TextView stime;
        private final TextView samount;
        private final TextView sitemno;
        private final ImageButton dltreciept;

        public SalesitemViewholder(@NonNull View itemView) {
            super(itemView);
            sdate = itemView.findViewById(R.id.salesitemdate);
            stime = itemView.findViewById(R.id.salesitemtime);
            samount =itemView.findViewById(R.id.salesitemprice);
            sitemno = itemView.findViewById(R.id.salesitemsno);
            dltreciept= itemView.findViewById(R.id.deletereceipt);
            dltreciept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mlistener.onDeleteItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });
//            txtsi = itemView.findViewById(R.id.txtsi);
//            prodltbtn=itemView.findViewById(R.id.productdeletebtn);
//            prodltbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(mlistener !=null){
//                        int position = getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION){
//                            mlistener.onDeleteItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}
