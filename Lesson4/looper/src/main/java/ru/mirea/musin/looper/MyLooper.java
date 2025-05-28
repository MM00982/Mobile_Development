package ru.mirea.musin.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private final Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "Thread started");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                int age = data.getInt("age", 0);
                String job = data.getString("job", "undefined");

                Log.d("MyLooper", "Получено: возраст=" + age + ", работа=" + job);

                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    Log.e("MyLooper", "sleep interrupted", e);
                    Thread.currentThread().interrupt();
                }

                Bundle bundle = new Bundle();
                bundle.putString(
                        "result",
                        String.format("Возраст %d, профессия %s – задержка %d cек.", age, job, age)
                );
                Message message = Message.obtain();
                message.setData(bundle);

                mainHandler.sendMessage(message);
            }
        };

        Looper.loop();
    }
}