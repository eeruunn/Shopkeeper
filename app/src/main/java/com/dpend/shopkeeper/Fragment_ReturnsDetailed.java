package com.dpend.shopkeeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_ReturnsDetailed extends Fragment {
    TextView namet,qtyt,datet,reasont,pidt,billnot;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rpd,container,false);
        namet=view.findViewById(R.id.frpdpnt);
        qtyt=view.findViewById(R.id.frpdpqt);
        datet=view.findViewById(R.id.frpdpdt);
        reasont=view.findViewById(R.id.frpdprt);
        pidt=view.findViewById(R.id.frpdpidt);
        billnot=view.findViewById(R.id.frpdpbnt);
        Bundle bundle = getArguments();
        if(bundle!=null){
            String name = bundle.getString("name");
            String date = bundle.getString("date");
            String reason = bundle.getString("reason");

            int qty = bundle.getInt("qty");
            int billno = bundle.getInt("billno");
            int pid = bundle.getInt("pid");

            namet.setText(name);
            datet.setText(date);
            reasont.setText(reason);
            qtyt.setText(qty+"");
            billnot.setText(billno+"");
            pidt.setText(pid+"");
        }else {
            Toast.makeText(getActivity(), "Something went wrong !!!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
