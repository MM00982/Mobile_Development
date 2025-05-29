package ru.mirea.musin.mireaproject.ui.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.mirea.musin.mireaproject.BasePermissionFragment;
import ru.mirea.musin.mireaproject.R;

import java.util.Locale;

public class CompassFragment extends BasePermissionFragment implements SensorEventListener {

    private SensorManager sm;
    private Sensor accel, magnet;

    private final float[] gravity = new float[3];
    private final float[] geomag  = new float[3];

    private ImageView needle;
    private TextView tvAz;

    @Override public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle b) {
        return inf.inflate(R.layout.fragment_compass, c, false);
    }

    @Override public void onViewCreated(@NonNull View v,@Nullable Bundle s) {
        needle = v.findViewById(R.id.ivNeedle);
        tvAz   = v.findViewById(R.id.tvAzimuth);

        sm = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        accel  = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override public void onResume() {
        super.onResume();
        sm.registerListener(this, accel , SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(this, magnet, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            System.arraycopy(e.values,0,gravity,0,3);
        else if (e.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
            System.arraycopy(e.values,0,geomag,0,3);

        float[] R = new float[9], I = new float[9];
        if (SensorManager.getRotationMatrix(R, I, gravity, geomag)) {
            float[] o = new float[3];
            SensorManager.getOrientation(R, o);
            float az = (float) Math.toDegrees(o[0]);
            az = (az + 360) % 360;

            tvAz.setText(String.format(Locale.getDefault(), "%.0f°", az));
            needle.setRotation(-az); // игла смотрит на север
        }
    }
    @Override public void onAccuracyChanged(Sensor s, int i) { }
}
