package com.dpend.shopkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class returnProActivity extends AppCompatActivity {
    private static int PERMISSION_REQUEST_CODE = 7;
    SurfaceView Cameraview;
    BarcodeDetector barcode;
    com.dpend.shopkeeper.CameraSource camera;
    com.dpend.shopkeeper.CameraSource.Builder builder;
    SurfaceHolder holder;
    Button scnbtn;
    FloatingActionButton donebtn,cancelbtn;
    ImageButton flash,increase,decrease;
    CameraManager cameraManager;
    int Flashon=0,id,qtyno=0;
    String cameraId,name;
    TextInputEditText billnoet,reason;
    TextView pronametxt,rproqtytxt;
    boolean torchon = false, scan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_pro);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(returnProActivity.this, R.color.black));

        Cameraview = findViewById(R.id.returnprocamview);
        Cameraview.setZOrderMediaOverlay(true);
        holder = Cameraview.getHolder();

        barcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        flash = findViewById(R.id.arpFlashbtn);
        scnbtn=findViewById(R.id.arpScanbtn);
        donebtn=findViewById(R.id.arpDonebtn);
        pronametxt=findViewById(R.id.arptxt1);
        rproqtytxt=findViewById(R.id.returnproqty);
        billnoet=findViewById(R.id.returnprobillnoet);
        cancelbtn=findViewById(R.id.arpCancelbtn);
        increase = findViewById(R.id.arpqtyibtn);
        decrease=findViewById(R.id.arpqtydbtn);
        reason = findViewById(R.id.arpreasonet);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        builder = new com.dpend.shopkeeper.CameraSource.Builder(this,barcode)
                .setFacing(com.google.android.gms.vision.CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        camera = builder
                .setFlashMode(Camera.Parameters.FLASH_MODE_AUTO)
                .build();
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
                    if (ActivityCompat.checkSelfPermission(returnProActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                    cameraManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                flash.setImageResource(R.drawable.icon_flashoff);
                torchon = true;

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
                if(barcodes.size()>0){
                    if (scan){
                        Barcode bcode =barcodes.valueAt(0);
                        final String code = ""+bcode.displayValue;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    GetPItem(code);
                                }catch (Exception e){
                                    Toast.makeText(returnProActivity.this,"Oops Something went wrong",Toast.LENGTH_SHORT).show();
                                }

                                // Stuff that updates the UI

                            }
                        });
                        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,400);
                        scan=false;
                    }


                }
            }
        });
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bno = billnoet.getText().toString();
                if(!bno.equals("")){
                    int no = Integer.parseInt(bno);
                    String rsn = reason.getText().toString();
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    Create_Dialog("Are you Sure ?",no,id,rsn,name,formattedDate);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter Bill No",Toast.LENGTH_SHORT).show();
                }

            }
        });
        scnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan=true;
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qtyno+=1;
                rproqtytxt.setText(qtyno+"");
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qtyno>1){
                    qtyno-=1;
                    rproqtytxt.setText(qtyno+"");
                }
            }
        });
    }
    private void Create_Dialog(String message, final int billno,final int mid, final String rsn,final String name,final String date){
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
                DatabaseHelper databaseHelper = new DatabaseHelper(returnProActivity.this);
                int qty=Integer.parseInt(rproqtytxt.getText().toString());
                long result = databaseHelper.AddReturndata(billno,id,qty,rsn,name,date);
                if (result!=-1){
                    CreateSuccessDialog();
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
    private void GetPItem(String code){
        DatabaseHelper dbHelper=new DatabaseHelper(this);
        Cursor cursor = dbHelper.GetProduct(code);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Product found in Database", Toast.LENGTH_SHORT).show();
        }else{
            cursor.moveToFirst();
            id = cursor.getInt(0);
            name = cursor.getString(2);
            pronametxt.setText(name);
            rproqtytxt.setText("1");
            qtyno+=1;
        }
    }
    private void CreateSuccessDialog(){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.returnsuccessfulldialog,null);
        Button scanagain = view.findViewById(R.id.rscnaga);
        Button scnexit = view.findViewById(R.id.rscnexit);
        abuilder.setView(view);
        final AlertDialog dialog2 = abuilder.create();
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        scanagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rproqtytxt.setText("0");
                pronametxt.setText("");
                billnoet.setText("");
                dialog2.dismiss();
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

}