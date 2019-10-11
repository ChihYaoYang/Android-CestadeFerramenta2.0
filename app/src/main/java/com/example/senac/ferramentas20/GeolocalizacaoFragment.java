package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GeolocalizacaoFragment extends Fragment {
    LocationManager locationManager;
    //Declara variável
    TextView lati, longi;
    Button getlocation, compartilhar, openmapas;
    String latitudes;
    String longitudes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geolocalizacao, container, false);
        //find id
        getlocation = view.findViewById(R.id.getlocation);
        compartilhar = view.findViewById(R.id.compartilhar);
        openmapas = view.findViewById(R.id.openmapas);
        lati = view.findViewById(R.id.latitude);
        longi = view.findViewById(R.id.longitude);

        //onclick
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pegaLocalizacao();
            }
        });
        compartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartilha();
            }
        });
        openmapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirmapas();
            }
        });
        return view;
    }

    public void pegaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            //Validar permissão Qunado usuário clicar não mostrar mais permissão
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                //Abrir configuração do APP
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(i);
            }
        } else {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new GeolocalizacaoFragment.MyLocationListener();
            //LocationManager.GPS_PROVIDER 使用GPS定位 / LocationManager.NETWORK_PROVIDER 使用網路定位
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, locationListener);
        }
    }

    public void compartilha() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, latitudes + "\n" + longitudes);
        intent.setType("text/plain");
        if (longitudes != null || latitudes != null) {
            startActivity(intent);
        } else {
            pegaLocalizacao();
        }
    }

    public void abrirmapas() {
        String uri = "http://maps.google.com/maps?q=" + latitudes + "," + longitudes;
        Log.e("maps", uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        if (longitudes != null || latitudes != null) {
            startActivity(intent);
        } else {
            pegaLocalizacao();
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        //當座標改變時觸發此函數，如果Provider傳進相同的座標，它就不會被觸發。
        public void onLocationChanged(Location location) {
            String longitude = Double.toString(location.getLongitude());
            String latitude = Double.toString(location.getLatitude());
            //Passa variavel
            latitudes = latitude;
            longitudes = longitude;
            //Seta valor para textview
            longi.setText(longitude);
            lati.setText(latitude);
        }

        @Override
        //Provider被disable時觸發此函數，比如GPS被關閉
        public void onProviderDisabled(String provider) {
            Toast.makeText(getContext(), "GPS DESATIVADA", Toast.LENGTH_SHORT).show();
        }

        @Override
        //Provider被enable時觸發此函數，比如GPS被打開
        public void onProviderEnabled(String provider) {
            Toast.makeText(getContext(), "GPS ATIVADO", Toast.LENGTH_SHORT).show();
        }

        @Override
        //Provider的轉態在可用、暫時不可用和無服務三個狀態直接切換時觸發此函數
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Toast.makeText(getContext(), "onStatusChanged", Toast.LENGTH_SHORT).show();
        }
    }
}