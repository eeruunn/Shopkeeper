package com.dpend.shopkeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder> {
    ArrayList<ScannedItem> items;
    public ScanViewHolder viewHolder;
    private ScanAdapter.OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onDeleteItemClick(int position);
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }
    public ScanAdapter(ArrayList<ScannedItem> items){
        this.items = items;
    }
    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanned_item,parent,false);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanViewHolder holder, int position) {
        if (items.get(position).Get_Name().length() < 6){
            holder.scnname.setText(items.get(position).Get_Name());
            String pos = ""+(position+1);
            holder.sinum.setText(pos);
        }else{
            String txt = items.get(position).Get_Name().substring(0,6)+"...";
            holder.scnname.setText(txt);
            String pos = ""+(position+1);
            holder.sinum.setText(pos);
        }
        String mrp = ""+items.get(position).Get_Mrp();
        String qty = ""+items.get(position).Get_Qty();
        String amount = ""+(Integer.parseInt(qty)*Float.parseFloat(mrp));
        if (!items.get(position).Get_Name().equals("")){
            holder.scnmrp.setText(mrp);
            holder.scnqty.setText(qty);
            holder.scnamount.setText(amount);
        }else{
            holder.scnmrp.setText("");
            holder.scnqty.setText("");
            holder.scnamount.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ScanViewHolder extends RecyclerView.ViewHolder{
        public TextView scnname;
        public TextView scnamount;
        public TextView sinum;
        public TextView scnqty;
        public TextView scnmrp;
        public ImageView scndlt;
        public ScanViewHolder(@NonNull View itemView) {
            super(itemView);
            scnname = itemView.findViewById(R.id.scnproductname);
            scnamount = itemView.findViewById(R.id.scnamount);
            scnqty = itemView.findViewById(R.id.scnqty);
            scnmrp = itemView.findViewById(R.id.scnmrp);
            scndlt = itemView.findViewById(R.id.scndlt);
            sinum = itemView.findViewById(R.id.sinum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mlistener !=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });
            scndlt.setOnClickListener(new View.OnClickListener() {
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
        }
    }

}
