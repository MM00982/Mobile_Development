package ru.mirea.musin.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.musin.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d(MainActivity.class.getSimpleName(), "Результат: " + result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        };

        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLooper.mHandler == null) {                 // поток ещё не готов
                    Toast.makeText(MainActivity.this,
                            "Поток ещё инициализируется…", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ageStr = binding.editTextAge.getText().toString().trim();
                String jobStr = binding.editTextJob.getText().toString().trim();

                if (ageStr.isEmpty() || jobStr.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Введите и возраст, и профессию", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this,
                            "Возраст должен быть числом", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putInt("age", age);
                bundle.putString("job", jobStr);

                Message msg = Message.obtain();
                msg.setData(bundle);

                myLooper.mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myLooper.mHandler != null) {
            myLooper.mHandler.getLooper().quit();
        }
    }
}