package ru.mirea.musin.multiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG       = MainActivity.class.getSimpleName();
    public  static final String EXTRA_MSG = "message";
    private EditText etMessage;

    @Override protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        etMessage = findViewById(R.id.etMessage);
    }
    @Override protected void onStart()                    { super.onStart();  Log.i(TAG,"onStart"); }
    @Override protected void onRestart()                  { super.onRestart();Log.i(TAG,"onRestart");}
    @Override protected void onResume()                   { super.onResume(); Log.i(TAG,"onResume"); }
    @Override protected void onPause()                    { super.onPause();  Log.i(TAG,"onPause"); }
    @Override protected void onStop()                     { super.onStop();   Log.i(TAG,"onStop"); }
    @Override protected void onDestroy()                  { super.onDestroy();Log.i(TAG,"onDestroy");}

    public void onClickNewActivity(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(EXTRA_MSG,
                etMessage.getText().toString().trim());
        startActivity(intent);
    }
}
