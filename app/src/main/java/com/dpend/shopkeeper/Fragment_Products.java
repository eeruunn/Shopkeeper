package com.dpend.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_Products extends Fragment {
    Button viewprobtn,addprobtn,expprobtn,catbtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products,container,false);
        viewprobtn = view.findViewById(R.id.productsviewbtn);
        addprobtn = view.findViewById(R.id.productsaddbtn);
        expprobtn = view.findViewById(R.id.productsexpbtn);
        catbtn = view.findViewById(R.id.productscatbtn);
        viewprobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment_ViewProducts();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
        addprobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddProduct_activity.class);
                startActivity(intent);
            }
        });
        expprobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment_ExpiredPros();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
}
