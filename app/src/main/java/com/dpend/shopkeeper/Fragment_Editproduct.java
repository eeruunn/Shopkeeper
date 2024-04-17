package com.dpend.shopkeeper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Fragment_Editproduct extends Fragment implements Adapter_editprob.OnItemClickListener {
    String name,unit,bcode,code,expdateformated;
    int stock,id;
    float price;
    SurfaceView Cameraview;
    BarcodeDetector barcode;
    CameraSource camera;
    SurfaceHolder holder;
    TextInputEditText pname,barcodein;
    AutoCompleteTextView punit;
    List<String> units;
    ImageButton editprobtn,backbtn;
    Button done,newbatchbtn;
    ArrayList<Item_aapbatch> items = new ArrayList<>();
    Adapter_editprob ibadapter = new Adapter_editprob(items);
    RecyclerView editprobatchrv;
    boolean scan = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_editproduct,container,false);
        editprobtn=view.findViewById(R.id.editprobtn);
        editprobatchrv=view.findViewById(R.id.fepbrv);
        newbatchbtn = view.findViewById(R.id.fepaddbatchbtn);
        backbtn=view.findViewById(R.id.editprobackbtn);
        name = getArguments().getString("name");
        unit= getArguments().getString("unit");
        price=getArguments().getFloat("price");
        stock=getArguments().getInt("stock");
        bcode=getArguments().getString("code");
        id=getArguments().getInt("id");
        GetBatches(id);
        pname=view.findViewById(R.id.editpronameinput);
        punit=view.findViewById(R.id.editprounitdropdown);
        units = new ArrayList<String>();
        units.add("No(s)");
        units.add("Kg");
        units.add("Gm");
        units.add("Mtr");
        units.add("Ltr");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.dropdownitem,units);
        punit.setText(unit);
        punit.setAdapter(adapter);
        pname.setText(name);


        done = view.findViewById(R.id.editprodonebtn);
        barcodein = view.findViewById(R.id.fepbarcodein);
        barcodein.setRawInputType(InputType.TYPE_NULL);
        barcodein.setText(bcode);
        editprobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDialog1();
            }
        });
        editprobatchrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        editprobatchrv.setAdapter(ibadapter);
        ibadapter.setOnItemClickListener(this);
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
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDialog2();
            }
        });
        newbatchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDialog4();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }
    private void CreateDialog1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.aysalert_dialog,null);
        TextView txt = mview.findViewById(R.id.txt4);
        Button yes = mview.findViewById(R.id.dltproalyes);
        Button no = mview.findViewById(R.id.dltproalno);
        txt.setText("Edit Product ?");
        yes.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.white));
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editprobtn.setEnabled(false);
                pname.setEnabled(true);
//                pstock.setEnabled(true);
                punit.setEnabled(true);
//                pprice.setEnabled(true);
                done.setEnabled(true);
                barcodein.setEnabled(true);
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
    private void CreateDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.aysalert_dialog,null);
        TextView txt = mview.findViewById(R.id.txt4);
        Button yes = mview.findViewById(R.id.dltproalyes);
        Button no = mview.findViewById(R.id.dltproalno);
        txt.setText("Save Changes ?");
        yes.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.white));
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
                String mpname = pname.getText().toString();
                String mbcode = barcodein.getText().toString();
                String munit = punit.getText().toString();
                int result = dbhelper.UpdateProData(mpname,munit,mbcode,id);
                if(result==1){
                    Toast.makeText(getActivity(),"Saved Successfully",Toast.LENGTH_SHORT).show();
                    editprobtn.setEnabled(true);
                    pname.setEnabled(false);
                    punit.setEnabled(false);
                    done.setEnabled(false);
                    barcodein.setEnabled(false);
                    dialog.dismiss();
                }else {
                    Toast.makeText(getActivity(),"Oops Something Went Wrong !!!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

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
    private void CreateDialog3(final String bno, final String expd, final int stock, final float pprice, final float rprice, final int iid, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.aysalert_dialog,null);
        TextView txt = mview.findViewById(R.id.txt4);
        Button yes = mview.findViewById(R.id.dltproalyes);
        Button no = mview.findViewById(R.id.dltproalno);
        txt.setText("Edit batch details ?");
        yes.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.white));
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEBdialog(bno,expd,stock,pprice,rprice,iid,position);
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
    private void CreateDialog4(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.aysalert_dialog,null);
        TextView txt = mview.findViewById(R.id.txt4);
        Button yes = mview.findViewById(R.id.dltproalyes);
        Button no = mview.findViewById(R.id.dltproalno);
        txt.setText("Add a new Batch ?");
        yes.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.white));
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNBdialog();
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
    private void CreateEBdialog(final String Bno, final String expd, final int stock, final float pprice, final float rprice, final int iid, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.dialog_editbatch,null);
        TextView nametxt = mview.findViewById(R.id.debtxt9);
        final TextInputEditText bno = mview.findViewById(R.id.debbatchnoin);
        final TextInputEditText pp = mview.findViewById(R.id.debppricein);
        final TextInputEditText rp = mview.findViewById(R.id.debrpricein);
        final TextInputEditText st = mview.findViewById(R.id.debstockin);
        final TextInputEditText ed = mview.findViewById(R.id.debexpdatein);

        bno.setText(Bno);
        pp.setText(""+pprice);
        rp.setText(""+rprice);
        st.setText(""+stock);
        ed.setText(expd);
        ed.setRawInputType(InputType.TYPE_NULL);
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog(ed);
            }
        });
        ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(ed.isFocused()){
                    DateDialog(ed);
                }
            }
        });
        Button done = mview.findViewById(R.id.debdonebtn);
        Button cancel = mview.findViewById(R.id.debcancelbtn);
        nametxt.setText(name);
        done.setTextColor(getResources().getColor(R.color.white));
        cancel.setTextColor(getResources().getColor(R.color.white));
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String bano = bno.getText().toString();
                final String bexpd = ed.getText().toString();
                final float bpp = Float.parseFloat(pp.getText().toString());
                final int bsto = Integer.parseInt(st.getText().toString());
                float uprprice;
                if(rp.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Retail Price : Cant be empty",Toast.LENGTH_SHORT).show();
                }else if(rp.getText().toString().charAt(0) == '.'){
                    Toast.makeText(getActivity(),"Retail Price : Enter a valid value",Toast.LENGTH_SHORT).show();
                }else {
                    uprprice = Float.parseFloat(rp.getText().toString());
                    DatabaseHelper helper2 = new DatabaseHelper(getActivity());
                    int result = helper2.UpdateBatchData(bano,bexpd,bpp,uprprice,iid,bsto);
                    if (result==1){
                        Item_aapbatch iab = items.get(position);
                        iab.Set_Batchno(bano);
                        iab.Set_Pprice(bpp);
                        iab.Set_Stock(bsto);
                        iab.Set_Expdate(bexpd);
                        iab.Set_Rprice(uprprice);
                        Toast.makeText(getActivity(),"Batch updated",Toast.LENGTH_SHORT).show();
                        ibadapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }else {
                        Toast.makeText(getActivity(),"Oops something went wrong!!!",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void CreateNBdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.dialog_editbatch,null);
        TextView nametxt = mview.findViewById(R.id.debtxt9);
        final TextInputEditText bno = mview.findViewById(R.id.debbatchnoin);
        final TextInputEditText pp = mview.findViewById(R.id.debppricein);
        final TextInputEditText rp = mview.findViewById(R.id.debrpricein);
        final TextInputEditText st = mview.findViewById(R.id.debstockin);
        final TextInputEditText ed = mview.findViewById(R.id.debexpdatein);
        ed.setRawInputType(InputType.TYPE_NULL);
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog(ed);
            }
        });
        ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(ed.isFocused()){
                    DateDialog(ed);
                }
            }
        });
        Button done = mview.findViewById(R.id.debdonebtn);
        Button cancel = mview.findViewById(R.id.debcancelbtn);
        nametxt.setText(name);
        done.setTextColor(getResources().getColor(R.color.white));
        cancel.setTextColor(getResources().getColor(R.color.white));
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String batchno = bno.getText().toString();
                String expirydate = ed.getText().toString();
                int stock = Integer.parseInt(st.getText().toString());
                String retprice = rp.getText().toString();
                String purprice = pp.getText().toString();
                float upurprice;
                float uprprice;

                try {
                    upurprice = Float.parseFloat(purprice);
                }catch (Exception e){
                    upurprice=0;
                }
                if(retprice.equals("")){
                    Toast.makeText(getActivity(),"Retail Price : Cant be empty",Toast.LENGTH_SHORT).show();
                }else if(retprice.charAt(0) == '.'){
                    Toast.makeText(getActivity(),"Retail Price : Enter a valid value",Toast.LENGTH_SHORT).show();
                }else {
                    uprprice = Float.parseFloat(rp.getText().toString());
                    DatabaseHelper helper2 = new DatabaseHelper(getActivity());
                    long result = helper2.addBatchdetails(batchno,stock,upurprice,uprprice,expirydate,id);
                    if (result!=-1){
                        Item_aapbatch iab = new Item_aapbatch();
                        iab.Set_Batchno(batchno);
                        iab.Set_Pprice(upurprice);
                        iab.Set_Stock(stock);
                        iab.Set_Expdate(expirydate);
                        iab.Set_Rprice(uprprice);
                        Toast.makeText(getActivity(),"Batch Saved",Toast.LENGTH_SHORT).show();
                        items.add(iab);
                        ibadapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }else {
                        Toast.makeText(getActivity(),"Oops something went wrong!!!",Toast.LENGTH_SHORT).show();
                    }



            }

            }
        });
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String batchno = bno.getText().toString();
//                String expirydate = ed.getText().toString();
//                int stock = Integer.parseInt(st.getText().toString());
//                String retprice = rp.getText().toString();
//                String purprice = pp.getText().toString();
//                float upurprice;
//                float uprprice;
//                try {
//                    upurprice = Float.parseFloat(purprice);
//                }catch (Exception e){
//                    upurprice=0;
//                }
//                if(retprice.equals("")){
//                    Toast.makeText(getActivity(),"Retail Price : Cant be empty",Toast.LENGTH_SHORT).show();
//                }else if(retprice.charAt(0) == '.'){
//                    Toast.makeText(getActivity(),"Retail Price : Enter a valid value",Toast.LENGTH_SHORT).show();
//                }else {
//                    uprprice = Float.parseFloat(rp.getText().toString());
//                    DatabaseHelper helper2 = new DatabaseHelper(getActivity());
//                    long result = helper2.addBatchdetails(batchno,stock,upurprice,uprprice,expirydate,id);
//                    if (result==1){
//                        Item_aapbatch iab = items.get(position);
//                        iab.Set_Batchno(bano);
//                        iab.Set_Pprice(bpp);
//                        iab.Set_Stock(bsto);
//                        iab.Set_Expdate(bexpd);
//                        iab.Set_Rprice(uprprice);
//                        Toast.makeText(getActivity(),"Batch updated",Toast.LENGTH_SHORT).show();
//                        ibadapter.notifyDataSetChanged();
//                        dialog.dismiss();
//
//                    }else {
//                        Toast.makeText(getActivity(),"Oops something went wrong!!!",Toast.LENGTH_SHORT).show();
//                    }
//
//
//
//            }
//        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDeleteItemClick(int position) {

    }

    @Override
    public void onEditClick(int position) {
        Item_aapbatch item_aapbatch = items.get(position);
        String ibno = item_aapbatch.Get_Batchno();
        String iexpdate = item_aapbatch.Get_Expdate();
        float ipprice = item_aapbatch.Get_Pprice();
        float irprice = item_aapbatch.Get_Rprice();
        int istock = item_aapbatch.Get_Stock();
        int iid = item_aapbatch.Get_Id();
        CreateDialog3(ibno,iexpdate,istock,ipprice,irprice,iid,position);
    }

    @Override
    public void onItemClick(int position) {

    }
    private void GetBatches(int id){
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        Cursor cursor = helper.GetBatches(id);
        while (cursor.moveToNext()){
            Item_aapbatch item = new Item_aapbatch();
            int pid = cursor.getInt(0);
            String bNo = cursor.getString(2);
            String expDate = cursor.getString(1);
            float rPrice = cursor.getFloat(6);
            float pPrice = cursor.getFloat(4);
            int stock = cursor.getInt(5);
            item.Set_Stock(stock);
            item.Set_Id(pid);
            item.Set_Expdate(expDate);
            item.Set_Pprice(pPrice);
            item.Set_Rprice(rPrice);
            item.Set_Batchno(bNo);
            items.add(item);
            ibadapter.notifyDataSetChanged();

        }
    }
    private void ShowscanAlert() {
        androidx.appcompat.app.AlertDialog.Builder aBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.newpro_scandialog, null);
        Cameraview = view.findViewById(R.id.newscancam);
        Cameraview.setZOrderMediaOverlay(true);
        holder = Cameraview.getHolder();

        barcode = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        camera = new CameraSource.Builder(getActivity(), barcode)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(800, 1500)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        aBuilder.setView(view);
        final androidx.appcompat.app.AlertDialog dialog = aBuilder.create();
        Cameraview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        camera.start(Cameraview.getHolder());
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
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
        private void DateDialog(final TextInputEditText ed){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.datepicker_layout,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final DatePicker picker = view.findViewById(R.id.dldp);
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
                ed.setText(expdateformated);
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
}
