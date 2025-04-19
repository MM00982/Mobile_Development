package ru.mirea.musin.multiactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getSimpleName();

    @Override protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        Log.i(TAG,"onCreate");
        setContentView(R.layout.activity_second);

        String txt = getIntent().getStringExtra(MainActivity.EXTRA_MSG);
        ((TextView) findViewById(R.id.tvResult)).setText(txt);
    }
    @Override protected void onStart()   { super.onStart();   Log.i(TAG,"onStart"); }
    @Override protected void onRestart() { super.onRestart(); Log.i(TAG,"onRestart");}
    @Override protected void onResume()  { super.onResume();  Log.i(TAG,"onResume"); }
    @Override protected void onPause()   { super.onPause();   Log.i(TAG,"onPause"); }
    @Override protected void onStop()    { super.onStop();    Log.i(TAG,"onStop"); }
    @Override protected void onDestroy() { super.onDestroy(); Log.i(TAG,"onDestroy");}
}
