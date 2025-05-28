package ru.mirea.musin.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LIST_NUMBER = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpen = findViewById(R.id.btnOpenSecond);
        btnOpen.setOnClickListener(v -> {
            long dateInMillis = System.currentTimeMillis();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            String dateString = new SimpleDateFormat(pattern, Locale.getDefault())
                    .format(new Date(dateInMillis));

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("EXTRA_TIME", dateString);
            intent.putExtra("EXTRA_LIST_NUMBER", LIST_NUMBER);
            startActivity(intent);
        });
    }
}