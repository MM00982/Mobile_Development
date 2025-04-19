package ru.mirea.musin.intentfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String STUDENT_FIO = "Мусин Марат Радикович";
    private static final String UNIVERSITY  = "РТУ МИРЭА";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnOpenBrowser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://www.mirea.ru/");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);
            }
        });

        findViewById(R.id.btnShareInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, UNIVERSITY);
                shareIntent.putExtra(Intent.EXTRA_TEXT, STUDENT_FIO + " – " + UNIVERSITY);
                startActivity(Intent.createChooser(shareIntent, "МОИ ФИО"));
            }
        });
    }
}
