package ru.mirea.musin.mireaproject.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyBackgroundWorker extends Worker {

    public static final String KEY_PROGRESS = "progress";

    public MyBackgroundWorker(@NonNull Context context,
                              @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        for (int i = 0; i <= 100; i += 10) {
            setProgressAsync(new Data.Builder().putInt(KEY_PROGRESS, i).build());
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
        }

        return Result.success();
    }
}

