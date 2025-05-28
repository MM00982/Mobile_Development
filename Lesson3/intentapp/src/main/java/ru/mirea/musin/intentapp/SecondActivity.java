package ru.mirea.musin.intentapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView tv = findViewById(R.id.tvResult);

        String time = getIntent().getStringExtra("EXTRA_TIME");
        int number = getIntent().getIntExtra("EXTRA_LIST_NUMBER", 0);
        int square = number * number;

        String result = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ " + "СОСТАВЛЯЕТ " + square + ", а текущее время " + time;
        tv.setText(result);
    }
}