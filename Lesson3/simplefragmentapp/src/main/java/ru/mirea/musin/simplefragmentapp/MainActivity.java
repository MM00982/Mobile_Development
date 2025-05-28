package ru.mirea.musin.simplefragmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment1 = new FirstFragment();
    private Fragment fragment2 = new SecondFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFirst = findViewById(R.id.btnFirstFragment);
        Button btnSecond = findViewById(R.id.btnSecondFragment);

        if (btnFirst != null && btnSecond != null) {
            FragmentManager manager = getSupportFragmentManager();

            btnFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment1)
                            .commit();
                }
            });

            btnSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment2)
                            .commit();
                }
            });

            manager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment1)
                    .commit();
        }
    }
}