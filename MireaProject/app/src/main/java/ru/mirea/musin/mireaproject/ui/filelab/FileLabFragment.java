package ru.mirea.musin.mireaproject.ui.filelab;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.google.android.material.snackbar.Snackbar;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import ru.mirea.musin.mireaproject.R;

public class FileLabFragment extends Fragment {

    private FileAdapter adapter;

    @Override public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle b) {
        return i.inflate(R.layout.fragment_file_lab, c, false);
    }

    @Override public void onViewCreated(@NonNull View v,@Nullable Bundle s) {
        RecyclerView rv = v.findViewById(R.id.rvFiles);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileAdapter();
        rv.setAdapter(adapter);

        v.findViewById(R.id.fab).setOnClickListener(btn -> showDialog());
    }

    /** Диалог создания записи */
    private void showDialog() {
        View dialog = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_file_create, null, false);

        EditText etText   = dialog.findViewById(R.id.etText);
        RadioGroup group  = dialog.findViewById(R.id.radioGroup);

        new AlertDialog.Builder(requireContext())
                .setTitle("Создать файл")
                .setView(dialog)
                .setPositiveButton("OK", (d, w) -> {
                    String src = etText.getText().toString();
                    int sel = group.getCheckedRadioButtonId();
                    if (src.isEmpty() || sel==-1) { Snackbar.make(requireView(),"Заполните всё",Snackbar.LENGTH_SHORT).show(); return; }

                    String processed;
                    String suffix;
                    if (sel == R.id.rbBase64) {
                        processed = Base64.getEncoder().encodeToString(src.getBytes(StandardCharsets.UTF_8));
                        suffix = "_b64.txt";
                    } else {
                        // Caesar +3
                        StringBuilder sb = new StringBuilder();
                        for (char c : src.toCharArray()) sb.append((char)(c+3));
                        processed = sb.toString();
                        suffix = "_caesar.txt";
                    }
                    saveFile(processed, suffix);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void saveFile(String text,String suffix) {
        try {
            File dir = new File(requireContext().getFilesDir(), "lab");
            if (!dir.exists()) dir.mkdirs();
            String name = "file_" + System.currentTimeMillis() + suffix;
            File f = new File(dir, name);
            try (FileOutputStream os = new FileOutputStream(f)) {
                os.write(text.getBytes(StandardCharsets.UTF_8));
            }
            adapter.add(new FileItem(name));
            Snackbar.make(requireView(),"Сохранено: "+name,Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            Snackbar.make(requireView(),"Ошибка "+e.getMessage(),Snackbar.LENGTH_LONG).show();
        }
    }
}
