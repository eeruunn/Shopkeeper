package com.dpend.shopkeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_dsb extends RecyclerView.Adapter<Adapter_dsb.DsbViewHolder> {
    ArrayList<Item_aapbatch> items;
    Adapter_dsb.OnItemClickListener mlistener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public Adapter_dsb(ArrayList<Item_aapbatch> mitems){
        items=mitems;
    }
    public void setOnItemClickListener(Adapter_dsb.OnItemClickListener listener){
        mlistener=listener;
    }

    @NonNull
    @Override
    public Adapter_dsb.DsbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scansbatch,parent,false);
        return new DsbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DsbViewHolder holder, int position) {
        String bno = items.get(position).Get_Batchno();
        float rprice = items.get(position).Get_Rprice();
        int stock = items.get(position).Get_Stock();
        holder.Stock.setText(stock+"");
        holder.rPrice.setText("₹"+rprice);
        holder.Bno.setText(bno);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class DsbViewHolder extends RecyclerView.ViewHolder{
        public TextView Bno;
        public TextView rPrice;
        public TextView Stock,aQty;
        ImageButton addbtn,removebtn;
        public DsbViewHolder(@NonNull View itemView) {
            super(itemView);
            Bno =itemView.findViewById(R.id.isbbatchno);
            rPrice = itemView.findViewById(R.id.isbPrice);
            Stock = itemView.findViewById(R.id.isbstocks);
            aQty = itemView.findViewById(R.id.tv_isb_qty);
            addbtn = itemView.findViewById(R.id.btn_isb_add);
            removebtn = itemView.findViewById(R.id.btn_isb_remove);

            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Item_aapbatch eitem = items.get(position);
                    int bqty = eitem.Get_Qty();
                    int stock = eitem.Get_Stock();
                    if(bqty!=stock) {
                        eitem.Set_Qty(bqty + 1);
                        aQty.setText((bqty + 1) + "");
                    }
                }
            });
            removebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Item_aapbatch eitem = items.get(position);
                    int bqty = eitem.Get_Qty();
                    if(bqty!=0){
                        eitem.Set_Qty(bqty-1);
                        aQty.setText((bqty-1)+"");
                    }


                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(mlistener!=null){
//                        int position = getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION){
//                            mlistener.OnItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}
