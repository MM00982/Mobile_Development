package ru.mirea.musin.mireaproject.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import ru.mirea.musin.mireaproject.R;

public class ProfileFragment extends Fragment {

    private EditText etName, etCourse, etAbout;
    private SharedPreferences prefs;

    @Override public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle b) {
        return inf.inflate(R.layout.fragment_profile, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        etName   = v.findViewById(R.id.etName);
        etCourse = v.findViewById(R.id.etCourse);
        etAbout  = v.findViewById(R.id.etAbout);
        Button btnSave = v.findViewById(R.id.btnSave);

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        // загрузка
        etName.setText(prefs.getString("name", ""));
        etCourse.setText(prefs.getString("course", ""));
        etAbout.setText(prefs.getString("about", ""));

        btnSave.setOnClickListener(vv -> save());
    }

    private void save() {
        String name   = etName.getText().toString();
        String course = etCourse.getText().toString();
        String about  = etAbout.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(course)) {
            Toast.makeText(getContext(),"Заполните имя и курс",Toast.LENGTH_SHORT).show();
            return;
        }

        prefs.edit()
                .putString("name", name)
                .putString("course", course)
                .putString("about", about)
                .apply();

        Toast.makeText(getContext(),"Сохранено!",Toast.LENGTH_SHORT).show();
    }
}
