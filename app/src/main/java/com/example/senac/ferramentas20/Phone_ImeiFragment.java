package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Phone_ImeiFragment extends Fragment {
    TextView imeinumber, modelo;
    Button getimei;
    TelephonyManager tm;
    String ImeiInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_imei, container, false);

        //find ID
        getimei = view.findViewById(R.id.imei);
        imeinumber = view.findViewById(R.id.imeinumber);
        modelo = view.findViewById(R.id.modelo);

        //Onclick
        getimei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceIMEI();
            }
        });
        return view;
    }

    public void getDeviceIMEI() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //Validar permissão Qunado usuário clicar não mostrar mais permissão
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
            } else {
                //Abrir configuração do APP
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(i);
            }
        } else {
            //Device imei
            tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                ImeiInfo = tm.getDeviceId();
            }
            if (null == ImeiInfo || 0 == ImeiInfo.length()) {
                ImeiInfo = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            //Device name
            String modeloinfo = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

//        device number
//        String PhoneNumber =  tm.getLine1Number();
//        number.setText(PhoneNumber);
            modelo.setText(modeloinfo);
            imeinumber.setText(ImeiInfo);
        }
    }
}
