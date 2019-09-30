package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class GeolocalizacaoFragment extends Fragment {
    //Permissão do GPS
    private static final int REQUEST_ACCESS_FINE_LOCATION = 200;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 201;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean permissionGPS = false;
    LocationManager locationManager;

    String latitudes;
    String longitudes;

    //Declara variável
    TextView lati, longi;
    Button getlocation, compartilhar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geolocalizacao, container, false);

        //Solicita as permissões para o usuário
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_ACCESS_COARSE_LOCATION);

        //find id
        getlocation = view.findViewById(R.id.getlocation);
        compartilhar = view.findViewById(R.id.compartilhar);
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
        return view;
    }

    //solicitar permissão de GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION:
                permissionGPS = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            case REQUEST_ACCESS_COARSE_LOCATION:
                permissionGPS = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionGPS) {
            Toast.makeText(getActivity(), "Aceite as permissões", Toast.LENGTH_SHORT).show();
        }
    }

    public void pegaLocalizacao() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new GeolocalizacaoFragment.MyLocationListener();
        //LocationManager.GPS_PROVIDER 使用GPS定位 / LocationManager.NETWORK_PROVIDER 使用網路定位
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

    }

    public void compartilha() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, longitudes + "\n" + latitudes);
        intent.setType("text/plain");
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
            longitudes = longitude;
            latitudes = latitude;
            //Seta valor para textview
            longi.setText(longitude);
            lati.setText(latitude);

            String uri = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
            Log.e("maps", uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }

        @Override
        //Provider被disable時觸發此函數，比如GPS被關閉
        public void onProviderDisabled(String provider) {
            Toast.makeText(getContext(), "GPS DESATIVADA", Toast.LENGTH_SHORT).show();
        }

        @Override
        //Provider被enable時觸發此函數，比如GPS被打開
        public void onProviderEnabled(String provider) {
            Toast.makeText(getContext(), "on ProviderEnabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        //Provider的轉態在可用、暫時不可用和無服務三個狀態直接切換時觸發此函數
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Toast.makeText(getContext(), "onStatusChanged", Toast.LENGTH_SHORT).show();
        }
    }
}