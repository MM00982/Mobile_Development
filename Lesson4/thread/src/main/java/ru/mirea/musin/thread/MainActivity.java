package ru.mirea.musin.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.musin.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread mainThread = Thread.currentThread();
        binding.textResult.setText("Имя текущего потока: " + mainThread.getName());
        mainThread.setName("БСБО-09-22 №18, любимый фильм: Остров проклятых");
        binding.textResult.append("\nНовое имя: " + mainThread.getName());
        Log.d(getClass().getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.buttonCalc.setOnClickListener(v -> {
            String totalPairsStr = binding.editTotalPairs.getText().toString();
            String studyDaysStr  = binding.editStudyDays.getText().toString();

            if (totalPairsStr.isEmpty() || studyDaysStr.isEmpty()) {
                binding.textResult.setText("Введите оба числа!");
                return;
            }

            int totalPairs = Integer.parseInt(totalPairsStr);
            int studyDays  = Integer.parseInt(studyDaysStr);

            threadPool.execute(new AvgTask(totalPairs, studyDays, counter++));
        });
    }

    private class AvgTask implements Runnable {

        private final int totalPairs;
        private final int studyDays;
        private final int numberThread;

        AvgTask(int totalPairs, int studyDays, int numberThread) {
            this.totalPairs   = totalPairs;
            this.studyDays    = studyDays;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            Log.d("ThreadProject", String.format(
                    "Запущен поток № %d студентом группы № БСБО-09-22 номер по списку № 18",
                    numberThread));

            try { Thread.sleep(2_000); } catch (InterruptedException ignored) { }

            double avg = (double) totalPairs / studyDays;

            uiHandler.post(() -> {
                String txt = String.format("Среднее за месяц: %.2f пар в день", avg);
                binding.textResult.setText(txt);
            });

            Log.d("ThreadProject", "Выполнен поток № " + numberThread);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadPool.shutdown(); // корректно закрываем пул
    }
}