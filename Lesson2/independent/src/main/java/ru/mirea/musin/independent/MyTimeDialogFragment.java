package ru.mirea.musin.independent;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(getActivity(),
                (TimePicker view, int hourOfDay, int minute) -> {
                    String result = String.format("Вы выбрали: %02d:%02d", hourOfDay, minute);
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), result, Snackbar.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
    }
}