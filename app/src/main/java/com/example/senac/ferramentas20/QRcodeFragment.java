package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class QRcodeFragment extends Fragment {
    //Declara variável
    SurfaceView surfaceView;
    TextView textView;
    Button scanner;
    //Declara câmera
    CameraSource cameraSource;
    //Declara plugins do Google (Vision)(QR_Code)
    BarcodeDetector barcodeDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);

        //find ID
        surfaceView = view.findViewById(R.id.surfaceView);
        textView = view.findViewById(R.id.qrtextview);
        scanner = view.findViewById(R.id.scanner);

        permissioncheck();
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opencamera();
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector).setRequestedPreviewSize(300, 300).build();
        //讓SurfaceView上面顯示東西 (Exibir objeto SurfaceView)
        //把相機畫面印在上面(Exibir câmera no SurfaceView)
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            //在關閉的同時就會關掉相機 (Quando fecha, fecha câmera)
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                //讓他判斷是不是有掃描到條碼，有的話就顯示出來 (Valida scanner)
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(qrCodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
        return view;
    }

    public void permissioncheck() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    public void opencamera() {
        //Validar permissão
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            //Validar permissão Qunado usuário clicar não mostrar mais permissão
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            } else {
                //Abrir configuração do APP
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(i);
            }
        } else {
            try {
                //exibir câmera no surfaceView
                cameraSource.start(surfaceView.getHolder());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}