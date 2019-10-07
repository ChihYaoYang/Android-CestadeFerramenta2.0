package com.example.senac.ferramentas20;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import static android.content.Context.SENSOR_SERVICE;

public class AcelerometroFragment extends Fragment implements SensorEventListener {
    //import sensor
    private SensorManager sensorManager;
    private Sensor accelerometer;

    TextView direction;
    StringBuilder builder = new StringBuilder();
    float[] history = new float[2];
    String[] direcao = {"NONE", "NONE"};

    ImageView diresao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_acelerometro, container, false);
        //Find id
        direction = view.findViewById(R.id.direction);
        diresao = view.findViewById(R.id.diresao);

        //Get Sensor
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        Log.e("teste", "X: " + sensorEvent.values[0] + " Y: " + sensorEvent.values[1] + " Z: " + sensorEvent.values[2]);
        float xChange = history[0] - sensorEvent.values[0];
        float yChange = history[1] - sensorEvent.values[1];
        history[0] = sensorEvent.values[0]; //sensorEvent.values[0] x
        history[1] = sensorEvent.values[1]; //sensorEvent.values[1] y
        //Se o valor delta em um eixo for positivo,
        // o dispositivo estará se movendo em uma direção,
        // se o negativo estiver se movendo na direção oposta.
        if (xChange > 2) {
            direcao[0] = "RIGHT";
            diresao.setImageResource(R.drawable.setaright);
        } else if (xChange < -2) {
            direcao[0] = "LEFT";
            diresao.setImageResource(R.drawable.setaleft);
        }
        if (yChange > 2) {
            direcao[1] = "DOWN";
            diresao.setImageResource(R.drawable.setadown);
        } else if (yChange < -2) {
            direcao[1] = "UP";
            diresao.setImageResource(R.drawable.setaup);
        }

        builder.setLength(0);
        builder.append("X: ");
        builder.append(direcao[0]);
        builder.append(" Y: ");
        builder.append(direcao[1]);
        direction.setText(builder.toString());
        Log.e("teste", builder.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
