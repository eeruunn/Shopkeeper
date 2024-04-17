package com.dpend.shopkeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ArrayList<ProductItem> items;
    OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onDeleteItemClick(int position);
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }
    public ProductAdapter(ArrayList<ProductItem> mitems) {
        items = mitems;

    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        String price="₹ " + items.get(position).Get_Price();
        String name = items.get(position).Get_Name();
        String stock = "Stock : "+items.get(position).Get_Stock();
        String Unit = "Unit : "+items.get(position).Get_Unit();
        String sino = ""+(position+1);
        if(name.length() > 25){
            String ename= name.substring(0,25)+"...";
            holder.pname.setText(ename);
        }
        else{
            holder.pname.setText(name);
        }

        holder.pprice.setText(price);
        holder.pstock.setText(stock);
        holder.punit.setText(Unit);
        holder.txtsi.setText(sino);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView pname;
        private final TextView pprice;
        private final TextView punit;
        private final TextView pstock;
        private final TextView txtsi;
        private final ImageView prodltbtn;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.productitempname);
            pprice = itemView.findViewById(R.id.productitemmrp);
            pstock =itemView.findViewById(R.id.productitemstock);
            punit = itemView.findViewById(R.id.productitemunit);
            txtsi = itemView.findViewById(R.id.txtsi);
            prodltbtn=itemView.findViewById(R.id.productdeletebtn);
            prodltbtn.setOnClickListener(new View.OnClickListener() {
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
