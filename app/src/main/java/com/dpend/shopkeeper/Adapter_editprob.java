package com.dpend.shopkeeper;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_editprob extends RecyclerView.Adapter<Adapter_editprob.EditProductViewHolder> {
    ArrayList<Item_aapbatch> items;
    OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onDeleteItemClick(int position);
        void onEditClick(int position);
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }
    public Adapter_editprob(ArrayList<Item_aapbatch> mitems) {
        items = mitems;

    }
    @NonNull
    @Override
    public EditProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_editprobatch,parent,false);
        return new EditProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProductViewHolder holder, int position) {
        String rPrice="₹ " + items.get(position).Get_Rprice();
        String bNo = items.get(position).Get_Batchno();
        String stock = "Stock : "+items.get(position).Get_Stock();
        String expDate = items.get(position).Get_Expdate();
        String pPrice = "₹ " + items.get(position).Get_Pprice();
        String sino = ""+(position+1);
        if(bNo.length() > 25){
            String ebNo= bNo.substring(0,25)+"...";
            holder.bNo.setText(ebNo);
        }
        else{
            holder.bNo.setText(bNo);
        }

        holder.rPrice.setText(rPrice);
        holder.stock.setText(stock);

    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class EditProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView bNo;
        private final TextView rPrice;
        private final TextView stock;
        private final ImageButton bdbtn,bebtn;
        public EditProductViewHolder(@NonNull View itemView) {
            super(itemView);
            bNo = itemView.findViewById(R.id.iepbatchno);
            rPrice =itemView.findViewById(R.id.iepPrice);
            stock = itemView.findViewById(R.id.iepstocks);
            bdbtn=itemView.findViewById(R.id.iepremovebtn);
            bebtn=itemView.findViewById(R.id.iepEditbatchbtn);
            bdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mlistener !=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mlistener.onDeleteItemClick(position);
                        }
                    }
                }
            });
            bebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mlistener !=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mlistener.onEditClick(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mlistener!=null){
                        int position= getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
