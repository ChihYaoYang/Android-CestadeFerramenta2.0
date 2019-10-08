package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class QRcodeFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean permissionCAMERA = false;
    private String[] permissions = {Manifest.permission.CAMERA};
    Button qr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);

        qr = view.findViewById(R.id.qr);


        //Solicita as permissões para o usuário
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CAMERA_PERMISSION);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerQR();
            }
        });

        return view;
    }

    //solicitar permissão de áudio
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                permissionCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionCAMERA) {
            Toast.makeText(getActivity(), "Aceite as permissões", Toast.LENGTH_SHORT).show();
        }
    }

    public void scannerQR() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
}
