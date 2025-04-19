package ru.mirea.musin.independent;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnTimePicker).setOnClickListener(v ->
                new MyTimeDialogFragment().show(getSupportFragmentManager(), "timeDialog"));

        findViewById(R.id.btnDatePicker).setOnClickListener(v ->
                new MyDateDialogFragment().show(getSupportFragmentManager(), "dateDialog"));

        findViewById(R.id.btnProgress).setOnClickListener(v ->
                new MyProgressDialogFragment().show(getSupportFragmentManager(), "progressDialog"));
    }
}