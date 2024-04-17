package com.dpend.shopkeeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class Fragment_returnpro extends Fragment {

    ImageButton decrease,increase;
    FloatingActionButton done,cancel;
    Button scan;
    TextInputEditText Billnoet,reasonet;
    int qty=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_returnproduct,container,false);
        return view;
    }
}
