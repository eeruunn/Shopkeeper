package com.dpend.shopkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class scan_activity extends AppCompatActivity implements ScanAdapter.OnItemClickListener,Adapter_dsb.OnItemClickListener {
    private static int PERMISSION_REQUEST_CODE = 7;
    SurfaceView Cameraview;
    BarcodeDetector barcode;
    com.dpend.shopkeeper.CameraSource camera;
    com.dpend.shopkeeper.CameraSource.Builder builder;
    SurfaceHolder holder;
    Button scnbtn;
    FloatingActionButton donebtn;
    FloatingActionButton cancelbtn;
    float tprice = 0;
    TextView tpricetxt;
    String sdate, stime,tname,tmrp,tamount;
    String cameraId;
    long sbillno;
    int Flashon = 0,tid;
    boolean scan = false;
    boolean hasFlash;
    String nth  = "";
    boolean torchon = false;
    private ArrayList<ScannedItem> items = new ArrayList<>();
    private ArrayList<Item_aapbatch> bitems = new ArrayList<>();
    ScanAdapter adapter = new ScanAdapter(items);
    DatabaseHelper dbHelper;
    Adapter_dsb adapterDsb = new Adapter_dsb(bitems);
    AlertDialog tdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanactivity);
        dbHelper = new DatabaseHelper(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(scan_activity.this, R.color.black));
        RecyclerView recyclerView = findViewById(R.id.scanned_pro_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(scan_activity.this);
        adapterDsb.setOnItemClickListener(this);
        add_blankitem();
        adapter.notifyItemInserted(items.size() - 1);
        scnbtn = findViewById(R.id.proscanbtn);
        tpricetxt = findViewById(R.id.totalpricetxt);
        donebtn = findViewById(R.id.donebtnscatv);
        cancelbtn = findViewById(R.id.cancelbtnscnatv);
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Cameraview = findViewById(R.id.cameraview);
        Cameraview.setZOrderMediaOverlay(true);
        holder = Cameraview.getHolder();

        barcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        final ImageView flash = findViewById(R.id.cameraflash);
        final CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        builder = new com.dpend.shopkeeper.CameraSource.Builder(this,barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        if(hasFlash){
            camera = builder
                    .setFlashMode(Camera.Parameters.FLASH_MODE_AUTO)
                    .build();
        }else {
            camera= builder.build();
        }

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(Flashon==0){
                        camera.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        flash.setImageResource(R.drawable.icon_flashon);
                        Flashon=1;
                        Toast.makeText(getApplicationContext(),"Flash On",Toast.LENGTH_SHORT).show();
                    }else {
                        camera.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        flash.setImageResource(R.drawable.icon_flashoff);
                        Flashon=0;
                        Toast.makeText(getApplicationContext(),"Flash Off",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Cameraview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(scan_activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        camera.start(Cameraview.getHolder());
                    }
                }

                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                try {
                    if(hasFlash){
                        cameraManager.setTorchMode(cameraId, true);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                flash.setImageResource(R.drawable.icon_flashoff);
                torchon = true;
//                GetCamready();
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size()>0) {
                    if (scan) {
                        Barcode bcode = barcodes.valueAt(0);
                        final String code = "" + bcode.displayValue;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    uGetPItem(code);
                                } catch (Exception e) {
                                    Toast.makeText(scan_activity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                                // Stuff that updates the UI

                            }
                        });
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 400);
                        scan = false;
                    }

                }

            }
        });
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items.size()==1){
                    if (!Objects.equals(items.get(0).Get_Name(), "")){
                        Create_Dialog("Are you Sure You Want to proceed ?");
                    }else{
                        Toast.makeText(scan_activity.this,"Add atleast 1 product",Toast.LENGTH_SHORT).show();
                    }
                }else if(items.size()>0){
                    Create_Dialog("Are you Sure You Want to proceed ?");
                }
                else{
                    Toast.makeText(scan_activity.this,"Add atleast 1 product",Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        scnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan=true;
            }
        });
    }
    private void additem(String name,String price,String qty,int Id,int mstock,int BId){
        ScannedItem item = new ScannedItem();
        item.Set_Name(name);
        item.Set_Qty(Integer.parseInt(qty));
        item.Set_Mrp(Float.parseFloat(price));
        item.Set_ID(Id);
        item.Set_BID(BId);
        item.Set_Stock(mstock);
        if((Objects.equals(items.get(items.size() - 1).Get_Name(), ""))){
            items.set(items. size() - 1,item);
            add_blankitem();
            adapter.notifyDataSetChanged();
            adapter.notifyItemInserted(items.size()-1);
        }else{
            items.add(item);
            adapter.notifyItemInserted(items.size()-1);
        }
        tprice +=Float.parseFloat(price);
        tpricetxt.setText("₹ "+tprice);

    }
    private void uadditem(String name,String price,String qty,int Id,int mstock,int BId){
        ScannedItem item = new ScannedItem();
        item.Set_Name(name);
        item.Set_Qty(Integer.parseInt(qty));
        item.Set_Mrp(Float.parseFloat(price));
        item.Set_ID(Id);
        item.Set_Stock(mstock);
        item.Set_BID(BId);
        String iname = items.get(items.size()-1).Get_Name();
        if(iname==""){
            items.set(items. size() - 1,item);
            add_blankitem();
        }else{
            items.add(item);
        }
        tprice +=Float.parseFloat(price);




    }
    public void removeItem(int position) {
        tprice -= items.get(position).Get_Mrp();
        tpricetxt.setText("₹ "+ tprice);
        adapter.items.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteItemClick(int position) {
     if (!Objects.equals(items.get(position).Get_Name(), "")){

         removeItem(position);

     }
    }

    @Override
    public void onItemClick(int position) {
        AlertDialog.Builder Abuilder = new AlertDialog.Builder(scan_activity.this);
        View view = scan_activity.this.getLayoutInflater().inflate(R.layout.scan_manual_entry_popup,null);
        Button done = view.findViewById(R.id.manual_input_dnbtn);
        final EditText pname=view.findViewById(R.id.pnameedtxt);
        final EditText pqty=view.findViewById(R.id.pqtyedtxt);
        final EditText pmrp=view.findViewById(R.id.pmrpedtxt);
        Abuilder.setView(view);
        final AlertDialog dialog = Abuilder.create();
        dialog.show();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = pname.getText().toString();
                String price = pmrp.getText().toString();
                String qty = pqty.getText().toString();
                additem(name,price,qty,0,Integer.parseInt(qty),0);
                dialog.dismiss();
            }
        });
    }

    private void add_blankitem(){
        ScannedItem item = new ScannedItem();
        item.Set_Name("");
        item.Set_Mrp(0);
        item.Set_Qty(0);
        item.Set_ID(-1);
        items.add(item);
    }
//    private void GetPItem(String code){
//        Cursor cursor = dbHelper.GetProduct(code);
//        if(cursor.getCount() == 0){
//            Toast.makeText(this, "No Product found in Database", Toast.LENGTH_SHORT).show();
//        }else{
//            cursor.moveToFirst();
//            int id = cursor.getInt(0);
//            String name = cursor.getString(2);
//            String price = ""+ cursor.getFloat(3);
//            int stock = cursor.getInt(4);
//            boolean addnew = true;
//            for(int i=0;i<items.size();i++){
//                int id2=items.get(i).Get_ID();
//
//                if(id==id2){
//                    EditItem(i);
//                    addnew=false;
//                }
//            }
//            if (addnew){
//                additem(name,price,"1",id,stock);
//            }
//        }
//    }

    private void uGetPItem(String code){
        Cursor cursor = dbHelper.GetProduct(code);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Product found in Database", Toast.LENGTH_SHORT).show();
        }else{
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(2);
            boolean addnew = true;
            DatabaseHelper helper = new DatabaseHelper(this);
            Cursor cursor1 = helper.GetBatches(id);
//            for(int i=0;i<items.size();i++){
//                int id2=items.get(i).Get_ID();
//
//                if(id==id2){
//                    EditItem(i);
//                    addnew=false;
//                }
//            }
            if(cursor1.getCount() == 0){
                Toast.makeText(this, "oops something went wrong", Toast.LENGTH_SHORT).show();
            }
            else if(cursor1.getCount()==1){
                cursor1.moveToFirst();
                float brprice = cursor1.getFloat(6);
                int stock = cursor1.getInt(5);
                int cbid = cursor1.getInt(0);
                Toast.makeText(this, id+"XXXXXXXXXX"+cbid, Toast.LENGTH_LONG).show();
                for(int i=0;i<items.size();i++){
                    int id2=items.get(i).Get_ID();
                    int cbid2 = items.get(i).Get_BID();
                    Toast.makeText(this, id2+"XXXXXXXXXX"+cbid2, Toast.LENGTH_LONG).show();
                    if(id==id2 && cbid==cbid2){
                        EditItem(i,1);
                        addnew=false;
                        Toast.makeText(this, "Add new False", Toast.LENGTH_LONG).show();
                    }
                }
                if (addnew){
                    additem(name,brprice+"","1",id,stock,cbid);
                }
            }
            else{
                if(bitems.size()>0){
                    bitems.clear();
                }
                while(cursor1.moveToNext()){
                    Item_aapbatch bitem = new Item_aapbatch();
                    int bid = cursor1.getInt(0);
                    String bexpdate = cursor1.getString(1);
                    String batch_no = cursor1.getString(2);
                    float bpprice = cursor1.getFloat(4);
                    float brprice = cursor1.getFloat(6);
                    int stock = cursor1.getInt(5);
                    bitem.Set_Rprice(brprice);
                    bitem.Set_Pprice(bpprice);
                    bitem.Set_Expdate(bexpdate);
                    bitem.Set_Batchno(batch_no);
                    bitem.Set_Id(bid);
                    bitem.Set_Stock(stock);
                    bitem.Set_Qty(0);
                    bitems.add(bitem);
                    tname = name;
                    tid=id;

                }
                Create_ScanbDialog(id);
            }




        }
    }
    private void GetCamready(){
        com.dpend.shopkeeper.CameraSource.Builder builder = new com.dpend.shopkeeper.CameraSource.Builder(this,barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        camera = builder
                .setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                .build();
    }

    private void Create_Dialog(String message){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(this);
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
                DatabaseHelper databaseHelper = new DatabaseHelper(scan_activity.this);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(c);
                long result = databaseHelper.AddSaleData(formattedDate,currentDateTimeString,tprice);
                long result2 = 0;
                if (result!=-1){
                    for(int i = 0; i<items.size();i++){
                        if (items.get(i).Get_Name()!=""){
                            String name = items.get(i).Get_Name();
                            int qty = items.get(i).Get_Qty();
                            int pstock = items.get(i).Get_Stock();
                            float price = items.get(i).Get_Mrp();
                            float total_price = price*qty;
                            int res = (int) result;
                            result2 = databaseHelper.AddSaleproData(name,qty,price,total_price,res);
                            int newstock = pstock-qty;
                            databaseHelper.Decreasestock(newstock,items.get(i).Get_ID());

                            
                        }
                    }


                }
                if (result2!=-1){
                    AddDsalesdata(formattedDate,tprice);
                    CreateSuccessDialog(result,formattedDate,currentDateTimeString);
                    dialog.dismiss();
                }

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
    private void Create_ScanbDialog(final int pid){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_scansbatch,null);
        RecyclerView drview = view.findViewById(R.id.dsbrview);
        drview.setLayoutManager(new LinearLayoutManager(this));
        drview.setAdapter(adapterDsb);
        abuilder.setView(view);
        Button done = view.findViewById(R.id.dsbdonebtn);
        Button cancel = view.findViewById(R.id.dsbcancelbtn);

        tdialog = abuilder.create();
        tdialog.setCanceledOnTouchOutside(true);
        tdialog.setCancelable(true);
        tdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tdialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tdialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btAddItem task = new btAddItem();
                task.execute(pid);
                adapter.notifyDataSetChanged();
//                tpricetxt.setText("₹ "+tprice);
                tdialog.dismiss();
            }
        });
//        Toast.makeText(this,"test1",Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,"test2",Toast.LENGTH_SHORT).show();
    }
    private void CreateSuccessDialog(final long billno, final String date, final String time){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.scansavesuccessdialog,null);
        Button genrecbtn = view.findViewById(R.id.generaterecbtn);
        Button scanagain = view.findViewById(R.id.scnaga);
        Button scnexit = view.findViewById(R.id.scnexit);
        sbillno = billno;
        sdate = ""+date;
        stime = ""+time;
        abuilder.setView(view);
        final AlertDialog dialog2 = abuilder.create();
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        scanagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.removeAll(items);
                add_blankitem();
                adapter.notifyDataSetChanged();
                tprice=0;
                tpricetxt.setText("₹ "+ tprice);
                dialog2.dismiss();
            }
        });
        genrecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(scan_activity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    GenerateReceipt(billno,date,time);
                }else {
                    AskPermission();

                }

            }
        });
        scnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                finish();
            }
        });
        dialog2.show();
    }
    private void EditItem(int pos,int qty){
        int id = items.get(pos).Get_ID();
        int q = items.get(pos).Get_Qty();
        int bid = items.get(pos).Get_BID();
        String n = items.get(pos).Get_Name();
        float p = items.get(pos).Get_Mrp();
        ScannedItem item=new ScannedItem();
        item.Set_Qty(q+qty);
        item.Set_Name(n);
        item.Set_Mrp(p);
        item.Set_ID(id);
        item.Set_BID(bid);
        items.set(pos,item);
        adapter.notifyItemChanged(pos);
        tprice+=(p*qty) ;
        tpricetxt.setText("₹ "+ tprice);
    }

    private void GenerateReceipt(final long billno, String date, String time){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.receipt_layout,null);
        final LinearLayout receipt = view.findViewById(R.id.receipt);
        Button share = view.findViewById(R.id.receiptsharebtn);
        Button save = view.findViewById(R.id.receiptsavebtn);
        ImageView closereceipt = view.findViewById(R.id.closereceiptlayoutbtn);

        TextView rnotxt = view.findViewById(R.id.receiptnotxt);
        TextView rdatetxt = view.findViewById(R.id.receiptdatetxt);
        TextView rtimetxt = view.findViewById(R.id.receipttimetxt);
        TextView ramounttxt = view.findViewById(R.id.receipttamounttxt);
        ArrayList<Receipt_Item> ritems = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.receiptRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        for(int i=0;i<(items.size()-1);i++){
            Receipt_Item ritem = new Receipt_Item();
            float imrp = items.get(i).Get_Mrp();
            int iqty = items.get(i).Get_Qty();
            ritem.Set_Name(items.get(i).Get_Name());
            ritem.Set_Qty(iqty);
            ritem.Set_Mrp(imrp);
            ritem.Set_Amount(imrp*iqty);
            ritems.add(ritem);
        }
        ReceiptAdapter receiptAdapter = new ReceiptAdapter(ritems);
        recyclerView.setAdapter(receiptAdapter);

        rnotxt.setText("Receipt No : "+billno);
        rdatetxt.setText("Date : "+date);
        rtimetxt.setText("Time : "+time);
        ramounttxt.setText("Total Amount : ₹ "+tprice);
        abuilder.setView(view);
        final AlertDialog dialog2 = abuilder.create();
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
                Toast.makeText(this,"Oops something went wrong",Toast.LENGTH_SHORT).show();
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
             Toast.makeText(scan_activity.this,"Receipt Saved",Toast.LENGTH_SHORT).show();
         }catch (Exception e){
             System.out.println(e);
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
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                bitmap, "Title", null);
        Uri imageUri =  Uri.parse(path);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, "Select"));
    }

    private void AskPermission(){
        ActivityCompat.requestPermissions(scan_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                GenerateReceipt(sbillno,sdate,stime);
            }else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void AddDsalesdata(String date,float total_price){
        try{
            Cursor csor = dbHelper.GetDailysales(date);
            if (csor.getCount()!=0){
                csor.moveToFirst();
                float damount= csor.getFloat(3);
                float tamount = damount+ total_price;
                int sales = csor.getInt(1);
                int tsales = sales+1;
                int res = dbHelper.UpdateDsdata(date,tamount,tsales);
                if (res==1){
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }else {
                int tsales=1;
                dbHelper.AddDSaleData(date,tsales,total_price);
            }
            csor.close();
        }catch (Exception e){
            dbHelper.Create_dstable();
            int tsales=1;
            dbHelper.AddDSaleData(date,tsales,total_price);

        }


    }

    @Override
    public void OnItemClick(int position) {
        Item_aapbatch iaitem = new Item_aapbatch();
        String iainame = tname;
        float iairprice = bitems.get(position).Get_Rprice();
        int mstock = bitems.get(position).Get_Stock();
        tdialog.dismiss();

    }

    private class btAddItem extends AsyncTask<Integer,Integer,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0;i<bitems.size();i++){
                boolean pinl = false;
                Item_aapbatch item_aapbatch = bitems.get(i);
                String iainame = tname;
                float iairprice = item_aapbatch.Get_Rprice();
                int mstock = item_aapbatch.Get_Stock();
                int mbid = item_aapbatch.Get_Id();
                int size = items.size();
                if(item_aapbatch.Get_Qty()>0){
                    for(int b=0;b<size;b++){
                        int id2 = items.get(b).Get_ID();
                        int proid = integers[0];
                        int bid2 = items.get(b).Get_BID();
                        if(proid==id2 && bid2==mbid){
                            pinl = true;
                            EditItem(b,item_aapbatch.Get_Qty());
                        }
                    }
                    if(!pinl){
                        uadditem(iainame,iairprice+"",item_aapbatch.Get_Qty()+"",tid,mstock,mbid);
                    }
                }

            }
            return "s";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tpricetxt.setText("₹ "+tprice);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

}