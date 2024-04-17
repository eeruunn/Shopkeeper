package com.dpend.shopkeeper;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Adapter_aapbatch extends RecyclerView.Adapter<Adapter_aapbatch.AapBatchViewHolder>{
    ArrayList<Item_aapbatch> items;
    Adapter_aapbatch.OnItemClickListener mlistener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
        void OnExpClick(int position,TextInputEditText et);
        void OnBdClick(int position);
    }
    public Adapter_aapbatch(ArrayList<Item_aapbatch> mitems){
        items = mitems;
    }
    public void setOnItemClickListener(Adapter_aapbatch.OnItemClickListener listener){
        mlistener=listener;
    }

    @NonNull
    @Override
    public Adapter_aapbatch.AapBatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addprobatch,parent,false);
        return new Adapter_aapbatch.AapBatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_aapbatch.AapBatchViewHolder holder, int position) {
        String Batchno = items.get(position).Get_Batchno();
        String Expdate = items.get(position).Get_Expdate();
        int Stock = items.get(position).Get_Stock();
        float Pprice = items.get(position).Get_Pprice();
        float Rprice = items.get(position).Get_Rprice();
        holder.bno.setText(Batchno);
        holder.siNo.setText("#"+(position+1));
        holder.pprice.setText(Pprice+"");
        holder.rprice.setText(Rprice+"");
        holder.mstock.setText(Stock+"");
        holder.expdate.setText(Expdate);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class AapBatchViewHolder extends RecyclerView.ViewHolder{
        public TextInputEditText bno;
        public TextInputEditText pprice;
        public TextInputEditText rprice;
        public TextInputEditText expdate;
        public TextInputEditText mstock;
        public TextView siNo;
        public ImageButton removebbtn;

        public AapBatchViewHolder(@NonNull View itemView) {
            super(itemView);
            int pos = getAdapterPosition();
            bno = itemView.findViewById(R.id.addprobatchnoin);
            pprice = itemView.findViewById(R.id.addproppricein);
            rprice = itemView.findViewById(R.id.addpropricein);
            expdate = itemView.findViewById(R.id.addproexpdatein);
            mstock = itemView.findViewById(R.id.addprostockin);
            removebbtn = itemView.findViewById(R.id.aapbatchrbbtn);
            if(pos==0){
                removebbtn.setEnabled(false);
            }
            siNo = itemView.findViewById(R.id.debtxt9);
            bno.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String bNo = bno.getText().toString();
                    Item_aapbatch item = items.get(getAdapterPosition());
                    item.Set_Batchno(bNo);
                }
            });
            pprice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String pp = pprice.getText().toString();
                    float pP = 0;
                    if(!pp.equals("")){
                        pP = Float.parseFloat(pp);
                    }
                    Item_aapbatch item = items.get(getAdapterPosition());
                    item.Set_Pprice(pP);
                }
            });
            rprice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String rp = rprice.getText().toString();
                    float rP = 0;
                    try{
                        rP = Float.parseFloat(rp);
                    }catch (Exception e){
                           System.out.println(e+"XXXXXXXXXXXXXXXXXxx");
                    }
                    Item_aapbatch item = items.get(getAdapterPosition());
                    item.Set_Rprice(rP);
                }
            });
            expdate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String ed = expdate.getText().toString();
                    Item_aapbatch item = items.get(getAdapterPosition());
                    item.Set_Expdate(ed);
                }
            });
            mstock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String ms = mstock.getText().toString();
                    int mS = 0;
                    if(!ms.equals("")){
                        mS = Integer.parseInt(ms);
                    }

                    Item_aapbatch item = items.get(getAdapterPosition());
                    item.Set_Stock(mS);
                }
            });
            expdate.setRawInputType(InputType.TYPE_NULL);
            expdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mlistener.OnExpClick(position,expdate);
                        }
                    }
                }
            });
            expdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(expdate.isFocused()){
                        if (mlistener!=null){
                            int position = getAdapterPosition();
                            if (position!=RecyclerView.NO_POSITION){
                                mlistener.OnExpClick(position,expdate);
                            }
                        }
                    }
                }
            });
            removebbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            if(position!=0){
                                mlistener.OnBdClick(position);
                            }

                        }
                    }
                }
            });

        }
    }

}
