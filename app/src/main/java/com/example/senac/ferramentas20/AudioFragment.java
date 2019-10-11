package com.example.senac.ferramentas20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AudioFragment extends Fragment {
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    boolean mStartPlaying = true;
    boolean mStartRecording = true;
    //Declara variável
    Button gravar, escutar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        //onde salva áudio temporiamente
        fileName = getActivity().getExternalCacheDir().getAbsolutePath() + "/audioSenac.3gp";

        //find ID
        gravar = view.findViewById(R.id.gravar);
        escutar = view.findViewById(R.id.escutar);

        //setar OnClickListener
        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    //Validar permissão Qunado usuário clicar não mostrar mais permissão
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                    } else {
                        //Abrir configuração do APP
                        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                        startActivity(i);
                    }
                } else {
                    gravar();
                }
            }
        });
        escutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escutar();
            }
        });
        return view;
    }

    //Cria function gravar/escutar
    public void gravar() {
        onRecord(mStartRecording);
        if (mStartRecording) {
            gravar.setText("Parar Gravação");
        } else {
            gravar.setText("Gravar");
        }
        mStartRecording = !mStartRecording;
    }

    public void escutar() {
        onPlay(mStartPlaying);
        if (mStartPlaying) {
            escutar.setText("Parar Áudio");
        } else {
            escutar.setText("Escutar");
        }
        mStartPlaying = !mStartPlaying;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (Exception e) {
            Log.e("audio", "Erro => startPlaying");
        }
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (Exception e) {
            Log.e("audio", "Erro => startRecording");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}