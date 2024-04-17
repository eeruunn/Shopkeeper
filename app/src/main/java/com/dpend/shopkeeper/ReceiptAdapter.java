package com.dpend.shopkeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.RViewHolder> {
    ArrayList<Receipt_Item> items;
    public ReceiptAdapter(ArrayList<Receipt_Item> items){
        this.items = items;
    }
    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciept_item,parent,false);
        return new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptAdapter.RViewHolder holder, int position) {
        String name=items.get(position).Get_Name();
        String qty = ""+items.get(position).Get_Qty();
        String mrp = ""+items.get(position).Get_Mrp();
        String amount = ""+items.get(position).Get_Amount();
        String sino = ""+(position+1);
        holder.si_no.setText(sino);
        holder.amount.setText(amount);
        holder.name.setText(name);
        holder.qty.setText(qty);
        holder.mrp.setText(mrp);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class RViewHolder extends RecyclerView.ViewHolder{
        public TextView si_no;
        public TextView name;
        public TextView qty;
        public TextView mrp;
        public TextView amount;
        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            si_no=itemView.findViewById(R.id.rsino);
            qty=itemView.findViewById(R.id.rqty);
            name=itemView.findViewById(R.id.rname);
            mrp=itemView.findViewById(R.id.rmrp);
            amount=itemView.findViewById(R.id.ramount);
        }
    }
}
