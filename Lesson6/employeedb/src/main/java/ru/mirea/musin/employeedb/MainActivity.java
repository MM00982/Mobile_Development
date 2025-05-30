package ru.mirea.musin.employeedb;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.musin.employeedb.data.Hero;
import ru.mirea.musin.employeedb.data.HeroDao;
import ru.mirea.musin.employeedb.ui.HeroesAdapter;

public class MainActivity extends AppCompatActivity {

    private HeroesAdapter adapter;
    private final List<Hero> heroes = new ArrayList<>();
    private HeroDao heroDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heroDao = App.getInstance().getDatabase().heroDao();

        RecyclerView rv = findViewById(R.id.rvHeroes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        heroes.addAll(heroDao.getAll());
        adapter = new HeroesAdapter(heroes);
        rv.setAdapter(adapter);

        findViewById(R.id.fabAdd).setOnClickListener(v -> showAddHeroDialog());
    }

    private void showAddHeroDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_hero, null);

        EditText etName      = dialogView.findViewById(R.id.etName);
        EditText etPower     = dialogView.findViewById(R.id.etPower);
        EditText etUniverse  = dialogView.findViewById(R.id.etUniverse);
        EditText etStrength  = dialogView.findViewById(R.id.etStrength);

        new AlertDialog.Builder(this)
                .setTitle("Новый герой")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (d, which) -> {
                    String name     = etName.getText().toString().trim();
                    String power    = etPower.getText().toString().trim();
                    String universe = etUniverse.getText().toString().trim();
                    String strLvl   = etStrength.getText().toString().trim();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(universe)) {
                        Toast.makeText(this, "Имя и вселенная обязательны",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int level = 0;
                    try { level = Integer.parseInt(strLvl); }
                    catch (NumberFormatException ignore) { }

                    Hero hero = new Hero(name, power, universe, level);
                    long id = heroDao.insert(hero);
                    hero.id = id;
                    heroes.add(hero);
                    adapter.notifyItemInserted(heroes.size() - 1);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}
