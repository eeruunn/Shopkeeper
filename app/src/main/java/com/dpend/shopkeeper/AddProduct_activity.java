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
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddProduct_activity extends AppCompatActivity implements Adapter_aapbatch.OnItemClickListener{
    ImageButton backbtn;
    Button addprodonebtn,addbatchbtn;
    SurfaceView Cameraview;
    BarcodeDetector barcode;
    ArrayList<Item_aapbatch> items = new ArrayList<>();
    CameraSource camera;
    SurfaceHolder holder;
    String code,expdateformated;
    List<String> units;
    AutoCompleteTextView autoCompleteTextView;
    DatabaseHelper databaseHelper;
    TextInputEditText namein,barcodein;
    RecyclerView recyclerView;
    Adapter_aapbatch adapter_aapbatch = new Adapter_aapbatch(items);
    boolean scan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ActionBar actionBar = getSupportActionBar();
        backbtn=findViewById(R.id.addprobackbtn);
        addbatchbtn=findViewById(R.id.aapnewbatchbtn);
        actionBar.hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(AddProduct_activity.this,R.color.black));
        recyclerView = findViewById(R.id.aaprview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_aapbatch);
        adapter_aapbatch.setOnItemClickListener(this);
        addBatch();
        addbatchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBatch();
            }
        });
//        purpricein=findViewById(R.id.addproppricein);
        namein = findViewById(R.id.addpronameinput);
//        stockin = findViewById(R.id.addprostockin);
//        pricein = findViewById(R.id.addpropricein);
        barcodein = findViewById(R.id.addprobarcodein);
//        batchnoin = findViewById(R.id.addprobatchnoin);
//        expdatein = findViewById(R.id.addproexpdatein);
        barcodein.setRawInputType(InputType.TYPE_NULL);
//        expdatein.setRawInputType(InputType.TYPE_NULL);
//        expdatein.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DateDialog();
//            }
//        });
//        expdatein.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(expdatein.isFocused()){
//                    DateDialog();
//                }
//            }
//        });
        barcodein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan = true;
                ShowscanAlert();
            }
        });
        barcodein.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(barcodein.isFocused()){
                    scan = true;
                    ShowscanAlert();
                }
            }
        });
        units = new ArrayList<String>();
        units.add("No(s)");
        units.add("Kg");
        units.add("Gm");
        units.add("Mtr");
        units.add("Ltr");
        autoCompleteTextView=findViewById(R.id.addprounitdropdown);
        ArrayAdapter adapter = new ArrayAdapter(AddProduct_activity.this,R.layout.dropdownitem,units);
        autoCompleteTextView.setAdapter(adapter);
        addprodonebtn = findViewById(R.id.addprodonebtn);
        addprodonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pname=namein.getText().toString();
                String unit = autoCompleteTextView.getText().toString();
                String nt = pname.trim();
                String ut = unit.trim();
                if(nt.equals("")){
                    Toast.makeText(AddProduct_activity.this," Product name can't be empty",Toast.LENGTH_SHORT).show();
                }else if (ut.equals("")){
                    Toast.makeText(AddProduct_activity.this,"Unit cant be empty",Toast.LENGTH_SHORT).show();
                }else{
                    int checkc = checkBatches();
                    if (checkc==1){
                        addProducttodb(pname,unit,code);
                    }

                }

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText || v instanceof AutoCompleteTextView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void ShowscanAlert() {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(AddProduct_activity.this);
        View view = AddProduct_activity.this.getLayoutInflater().inflate(R.layout.newpro_scandialog, null);
        Cameraview = view.findViewById(R.id.newscancam);
        Cameraview.setZOrderMediaOverlay(true);
        holder = Cameraview.getHolder();

        barcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        camera = new CameraSource.Builder(this, barcode)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(800, 1500)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        aBuilder.setView(view);
        final AlertDialog dialog = aBuilder.create();
        Cameraview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddProduct_activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        camera.start(Cameraview.getHolder());
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

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
                if (barcodes.size() > 0) {
                    if(scan) {
                        Barcode bcode = barcodes.valueAt(0);
                        code = "" + bcode.displayValue;
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 400);
                        barcodein.setText(code);
                        scan=false;
                        dialog.dismiss();
                    }

                }
            }
        });
        dialog.show();
    }
//    private void Addproducttodb(String name,String unit,Float price,Float pprice,int stock){
//
//
//        databaseHelper = new DatabaseHelper(this);
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("p_name", name);
//        values.put("price",price);
//        values.put("unit", unit);
//        values.put("stock", stock);
//        values.put("barcode", code);
//        values.put("pprice",pprice);
//        long id = db.insert("products", null, values);
//        if(id!=-1){
//            String ExpDate = expdatein.getText().toString();
//            String BatchNo = batchnoin.getText().toString();
//            int Id = Integer.parseInt(""+id);
//            ContentValues values2 = new ContentValues();
//            values2.put("exp_date",ExpDate);
//            values2.put("batch_no",BatchNo);
//            values2.put("product",Id);
//            long result = db.insert("pro_batches", null, values2);
//            if (result!=-1){
//                Toast.makeText(this,"added Successfully",Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this,"Oops Something Went Wrong !!!",Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            Toast.makeText(this,"Oops Something Went Wrong !!!",Toast.LENGTH_SHORT).show();
//        }
//
//        namein.setText("");
//        stockin.setText("");
//        pricein.setText("");
//        purpricein.setText("");
//        expdatein.setText("");
//        batchnoin.setText("");
//        autoCompleteTextView.setText("");
//        barcodein.setText("");
//    }
//    private void DateDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = AddProduct_activity.this.getLayoutInflater().inflate(R.layout.datepicker_layout,null);
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//        final DatePicker picker = view.findViewById(R.id.dldp);
//        Button done = view.findViewById(R.id.dldbtn);
//        ImageButton close  = view.findViewById(R.id.dlcbtn);
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String date = picker.getDayOfMonth() +"/"+(picker.getMonth()+1)+"/"+picker.getYear();
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//                expdateformated = df.format(calendar.getTime());
//                expdatein.setText(expdateformated);
//                dialog.dismiss();
//            }
//        });
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//    }
    private void addBatch(){
        Item_aapbatch item = new Item_aapbatch();
        item.Set_Batchno("");
        item.Set_Pprice(0);
        item.Set_Rprice(0);
        item.Set_Stock(0);
        item.Set_Expdate("");
        items.add(item);
        adapter_aapbatch.notifyDataSetChanged();
    }
    private void addProducts(){
        String p_name = namein.getText().toString();
        String unit = autoCompleteTextView.getText().toString();
        String b_code = barcodein.getText().toString();


  }
    private void addProducttodb(String name,String unit,String bCode){


        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("p_name", name);
        values.put("unit", unit);
        values.put("barcode", code);
        long id = db.insert("products", null, values);
        if(id!=-1){
            int Id = Integer.parseInt(""+id);
            for(int i = 1;i<=items.size();i++){
                Item_aapbatch item_aapbatch = items.get(i-1);
                String b_No = item_aapbatch.Get_Batchno();
                float pPrice = item_aapbatch.Get_Pprice();
                float rPrice = item_aapbatch.Get_Rprice();
                int stock = item_aapbatch.Get_Stock();
                String exp_Date = item_aapbatch.Get_Expdate();
                databaseHelper.addBatchdetails(b_No,stock,pPrice,rPrice,exp_Date,Id);
            }
            Toast.makeText(this,"added Successfully",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Oops Something Went Wrong !!!",Toast.LENGTH_SHORT).show();
        }

        namein.setText("");
        autoCompleteTextView.setText("");
        barcodein.setText("");
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnExpClick(int position, TextInputEditText et) {
        dateDialog(et);
    }

    @Override
    public void OnBdClick(int position) {
        items.remove(position);
        adapter_aapbatch.notifyDataSetChanged();
    }

    private void dateDialog(final TextInputEditText editText){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = AddProduct_activity.this.getLayoutInflater().inflate(R.layout.datepicker_layout,null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            final DatePicker picker = view.findViewById(R.id.dldp);
            picker.setMinDate(System.currentTimeMillis()+24*60*60*1000);
            Button done = view.findViewById(R.id.dldbtn);
            ImageButton close  = view.findViewById(R.id.dlcbtn);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = picker.getDayOfMonth() +"/"+(picker.getMonth()+1)+"/"+picker.getYear();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    expdateformated = df.format(calendar.getTime());
                    editText.setText(expdateformated);
                    dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    private int checkBatches(){
        int code = 1;
        for(int i = 1;i<=items.size();i++){
            Item_aapbatch item = items.get(i-1);
            float rp = item.Get_Rprice();
            if (rp==0.0 || rp==0){
                code=0;
                Toast.makeText(this,"#"+(i+1)+": Retail price can't be 0",Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return code;
    }
}