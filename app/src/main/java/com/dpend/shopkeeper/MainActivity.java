package com.dpend.shopkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private View scanbutton;
    private int REQUEST_CODE = 100;
    Fragment fragment;
    BottomNavigationView navview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        DatabaseHelper db = new DatabaseHelper(this);
//        Cursor cursor101 = db.Exp_pros();
//        if(cursor101!=null){
//            while(cursor101.moveToNext()){
//                String bno =  cursor101.getString(2);
//                Toast.makeText(this,bno,Toast.LENGTH_SHORT).show();
//            }
//            cursor101.close();
//        }
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FFFFFF"));

        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.black));
        scanbutton = findViewById(R.id.scanbtn);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            int PERMISSION_REQUEST = 200;
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,scan_activity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        navview = findViewById(R.id.bottomnavview);
        navview.setOnNavigationItemSelectedListener(this);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment_Home()).commit();
        }


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                ShowHome();
                break;
            case R.id.sales:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment_Sales()).addToBackStack(null).commit();
                break;
            case R.id.products:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment_Products()).addToBackStack(null).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment_Settings()).addToBackStack(null).commit();
                break;


        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof Fragment_Home) {

            MainActivity.this.finish();

        }else if(currentFragment instanceof Fragment_Products){
            ShowHome();
            navview.setSelectedItemId(R.id.dashboard);
        }
        else if(currentFragment instanceof Fragment_Settings){
            ShowHome();
            navview.setSelectedItemId(R.id.dashboard);
        }
        else if(currentFragment instanceof Fragment_Sales){
            ShowHome();
            navview.setSelectedItemId(R.id.dashboard);
        }
        else {
            super.onBackPressed();
        }
    }
    public void ShowHome() {
        fragment = new Fragment_Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,"Home_frag").commit();

    }


}