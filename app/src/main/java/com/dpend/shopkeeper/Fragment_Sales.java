package com.dpend.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Fragment_Sales extends Fragment implements Salesitemadapter.OnItemClickListener {
    ImageButton salesbackbtn;
    ImageView showreciept;
    RecyclerView hisrview;
    DatabaseHelper dbhelper;
    Button showhisbtn,returnsbtn;
    ArrayList<Sales_Item> items = new ArrayList<>();
    Salesitemadapter Adapter = new Salesitemadapter(items);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales,container,false);
        hisrview=view.findViewById(R.id.historyrview);
        salesbackbtn = view.findViewById(R.id.salesbackbtn);
        showhisbtn=view.findViewById(R.id.showhisbtn);
        hisrview.setLayoutManager(new LinearLayoutManager(getActivity()));
        hisrview.setAdapter(Adapter);
        Adapter.setOnItemClickListener(this);
        returnsbtn= view.findViewById(R.id.salesanbtn2);
        salesbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        Get_data();
        showhisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment= new Fragment_SalesHistory();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
        returnsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment_Returns();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
    private void Get_data(){
        dbhelper = new DatabaseHelper(getActivity());
        Cursor cursor = dbhelper.ReadSalesData();
        if (cursor.getCount()==0){
            Toast.makeText(getActivity(),"no history",Toast.LENGTH_SHORT).show();
        }else {
                if (cursor.getCount()>=4){
                    for (int i =1;i<4;i++){
                        cursor.moveToNext();
                        Sales_Item item = new Sales_Item();
                        item.Set_Billno(cursor.getInt(0));
                        item.Set_Date(cursor.getString(1));
                        item.Set_Time(cursor.getString(2));
                        item.Set_Price(""+cursor.getFloat(3));
                        items.add(item);
                        Adapter.notifyDataSetChanged();
                    }
                }else{
                    int count= cursor.getCount();
                    for (int i =1;i<=count;i++){
                        cursor.moveToNext();
                        Sales_Item item = new Sales_Item();
                        item.Set_Billno(cursor.getInt(0));
                        item.Set_Date(cursor.getString(1));
                        item.Set_Time(cursor.getString(2));
                        item.Set_Price(""+cursor.getFloat(3));
                        items.add(item);
                        Adapter.notifyDataSetChanged();
                    }
                }


        }
        cursor.close();
    }

    @Override
    public void onDeleteItemClick(int position) {
        Sales_Item item = items.get(position);
        int Billno = item.Get_Billno();
        Create_Dialog("Are You Sure",position,Billno);
    }

    @Override
    public void onItemClick(int position) {
        Sales_Item item = items.get(position);
        int Billno= item.Get_Billno();
        createDialog(Billno, item.Get_Date(), item.Get_Time(),item.Get_Price());



    }
    @SuppressLint("SetTextI18n")
    private void createDialog(final int billno, String date, String time, String tamount){
        androidx.appcompat.app.AlertDialog.Builder abuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.receipt_layout,null);
        final LinearLayout receipt = mview.findViewById(R.id.receipt);
        Button share = mview.findViewById(R.id.receiptsharebtn);
        Button save = mview.findViewById(R.id.receiptsavebtn);
        ImageView closereceipt = mview.findViewById(R.id.closereceiptlayoutbtn);
        TextView rnotxt = mview.findViewById(R.id.receiptnotxt);
        TextView rdatetxt = mview.findViewById(R.id.receiptdatetxt);
        TextView rtimetxt = mview.findViewById(R.id.receipttimetxt);
        TextView ramounttxt = mview.findViewById(R.id.receipttamounttxt);
        ArrayList<Receipt_Item> ritems = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.GetReciept(billno);
        ArrayList<Receipt_Item> Ritems = new ArrayList<>();
        while (cursor.moveToNext()){
            Receipt_Item ritem= new Receipt_Item();
            ritem.Set_Name(cursor.getString(1));
            ritem.Set_Qty(cursor.getInt(2));
            ritem.Set_Mrp(cursor.getFloat(3));
            ritem.Set_Amount(cursor.getFloat(4));
            Ritems.add(ritem);
        }
        RecyclerView recyclerView = mview.findViewById(R.id.receiptRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);

        ReceiptAdapter receiptAdapter = new ReceiptAdapter(Ritems);
        recyclerView.setAdapter(receiptAdapter);

        rnotxt.setText("Receipt No : "+billno);
        rdatetxt.setText("Date : "+date);
        rtimetxt.setText("Time : "+time);
        ramounttxt.setText("Total Amount : ₹ "+tamount);
        abuilder.setView(mview);
        final androidx.appcompat.app.AlertDialog dialog2 = abuilder.create();
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        closereceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveReceipt(""+billno,receipt);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sharereceipt(receipt);
            }
        });
        dialog2.show();
    }
    private void SaveReceipt(String receiptno,LinearLayout receipt){
        String dirname = "Shopreceipts";
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),dirname);
        if(!dir.exists()){
            boolean mkdir = dir.mkdir();
            if (!mkdir){
                Toast.makeText(getActivity(),"Oops something went wrong",Toast.LENGTH_SHORT).show();
            }
        }
        String filename = receiptno+".jpg";
        File rfile = new File(dir,filename);
        Bitmap bitmap = Bitmap.createBitmap(receipt.getWidth(),receipt.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        receipt.draw(canvas);
        if(rfile.exists()){
            rfile.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(rfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(getActivity(),"Receipt Saved",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            System.out.println("xxxxxxxxxxxxxxxxxxxxx"+e+"xxxxxxxxxxxxxxxxxxxxxxx");
        }
    }
    private void Sharereceipt(LinearLayout receipt){
        Bitmap bitmap = Bitmap.createBitmap(receipt.getWidth(),receipt.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        receipt.draw(canvas);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                bitmap, "Title", null);
        Uri imageUri =  Uri.parse(path);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, "Select"));
    }
    private void Create_Dialog(String message, final int position, final int Billno){
        androidx.appcompat.app.AlertDialog.Builder abuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.aysalert_dialog,null);
        TextView txtv = view.findViewById(R.id.txt4);
        txtv.setText(message);
        Button yes = view.findViewById(R.id.dltproalyes);
        Button no = view.findViewById(R.id.dltproalno);
        yes.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.white));
        abuilder.setView(view);
        final AlertDialog dialog = abuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adapter.items.remove(position);
                Adapter.notifyItemRemoved(position);
                Adapter.notifyDataSetChanged();
                DatabaseHelper helper =new DatabaseHelper(getActivity());
                helper.DeleteReciept(Billno);
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
}
