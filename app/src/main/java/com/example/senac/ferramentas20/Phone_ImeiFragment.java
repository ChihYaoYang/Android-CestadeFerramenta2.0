package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Phone_ImeiFragment extends Fragment {
    //Permissão
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 200;
    private static final int REQUEST_READ_PHONE_NUMBERS_PERMISSION = 201;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 202;
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE};
    private String[] permission = {Manifest.permission.READ_PHONE_NUMBERS};
    private String[] permissio = {Manifest.permission.READ_CONTACTS};
    private boolean permissionState = false;

    TextView imeinumber, nome, number;
    Button getimei;
    TelephonyManager tm;
    String IMEI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_imei, container, false);


        //find id
        getimei = view.findViewById(R.id.imei);
        imeinumber = view.findViewById(R.id.imeinumber);
        nome = view.findViewById(R.id.nome);
        number = view.findViewById(R.id.number);

        //Solicita as permissões para o usuário
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_READ_PHONE_STATE_PERMISSION);
        ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_READ_PHONE_NUMBERS_PERMISSION);
        ActivityCompat.requestPermissions(getActivity(), permissio, REQUEST_READ_CONTACTS_PERMISSION);


        getimei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceIMEI();
            }
        });
        return view;
    }

    //solicitar permissão de áudio
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE_PERMISSION:
                permissionState = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case REQUEST_READ_PHONE_NUMBERS_PERMISSION:
                permissionState = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case REQUEST_READ_CONTACTS_PERMISSION:
                permissionState = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionState) {
            Toast.makeText(getActivity(), "Aceite as permissões", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDeviceIMEI() {
        //Device imei
        tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            IMEI = tm.getDeviceId();
        }
        if (null == IMEI || 0 == IMEI.length()) {
            IMEI = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        //Device name
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

//        device number
//        String PhoneNumber =  tm.getLine1Number();
//        number.setText(PhoneNumber);
        nome.setText(reqString);
        imeinumber.setText(IMEI);
    }

}
