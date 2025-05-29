package ru.mirea.musin.lesson5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ru.mirea.musin.lesson5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SensorManager sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (Sensor s : sensors) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("Name", s.getName());
            item.put("Value",
                    String.format(Locale.getDefault(), "%.1f", s.getMaximumRange()));
            data.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"Name", "Value"},
                new int[]{android.R.id.text1, android.R.id.text2});

        binding.sensorListView.setAdapter(adapter);
    }
}
