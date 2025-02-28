package com.example.oujdashopproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.oujdashopproject.Adapter.CategoryAdapter;
import com.example.oujdashopproject.DataBase.DataBaseHelper;
import com.example.oujdashopproject.Users.Category;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.ListView);
        categoryList = new ArrayList<>();
        dataBaseHelper = DataBaseHelper.getInstance(this);

        loadCategories();

        categoryAdapter = new CategoryAdapter(this, categoryList);
        listView.setAdapter(categoryAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = categoryList.get(position);
                showOptionsDialog(selectedCategory);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = categoryList.get(position);
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("CATEGORY_ID", selectedCategory.getId());
                startActivity(intent);
            }
        });

    }

    private void loadCategories() {
        Cursor cursor = dataBaseHelper.getReadableDatabase().rawQuery("SELECT * FROM Category", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex("nom"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                categoryList.add(new Category(id, nom, description));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void showOptionsDialog(final Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setMessage("Que voulez-vous faire avec cette catégorie ?");
        builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, EditCategoryActivity.class);
                intent.putExtra("CATEGORY_ID", category.getId());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCategory(category);
            }
        });

        builder.create().show();
    }

    private void deleteCategory(Category category) {
        int id = category.getId();
        if (id != 0) {
            SQLiteDatabase db = DataBaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
            db.execSQL("DELETE FROM Category WHERE id = ?", new String[]{String.valueOf(id)});
            Toast.makeText(MainActivity.this, "Supprimé avec succès", Toast.LENGTH_LONG).show();
            startActivity(getIntent());
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Erreur : ID de catégorie invalide", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_category) {
            Intent intent = new Intent(MainActivity.this, AjouterCategorie.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_scan) {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_deconnexion) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}