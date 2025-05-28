package ru.mirea.musin.mireaproject.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import ru.mirea.musin.mireaproject.R;
import ru.mirea.musin.mireaproject.work.MyBackgroundWorker;

public class TaskFragment extends Fragment {

    private MaterialButton btnStart;
    private MaterialTextView txtStatus;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnStart  = view.findViewById(R.id.btnStart);
        txtStatus = view.findViewById(R.id.txtStatus);

        btnStart.setOnClickListener(v -> startWork());
    }

    private void startWork() {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyBackgroundWorker.class)

                .build();

        WorkManager wm = WorkManager.getInstance(requireContext());
        wm.enqueueUniqueWork("my_unique_task", ExistingWorkPolicy.REPLACE, request);

        wm.getWorkInfoByIdLiveData(request.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo == null) return;

                    int prog = workInfo.getProgress().getInt(MyBackgroundWorker.KEY_PROGRESS, -1);
                    if (prog >= 0) txtStatus.setText("Прогресс: " + prog + "%");

                    if (workInfo.getState().isFinished()) {
                        switch (workInfo.getState()) {
                            case SUCCEEDED: txtStatus.setText("Завершено успешно"); break;
                            case FAILED:    txtStatus.setText("Ошибка"); break;
                            case CANCELLED: txtStatus.setText("Отменено"); break;
                        }
                    }
                });
    }
}
