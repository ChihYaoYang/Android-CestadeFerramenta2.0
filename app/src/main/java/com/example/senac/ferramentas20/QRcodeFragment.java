package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class QRcodeFragment extends Fragment {
    Button qr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);
        //find ID
        qr = view.findViewById(R.id.qr);

        //Onclick
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerQR();
            }
        });
        return view;
    }


    public void scannerQR() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {

        }
    }
}
