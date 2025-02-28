package com.example.oujdashopproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.example.oujdashopproject.DataBase.DataBaseHelper;

public class EditCategoryActivity extends AppCompatActivity {

    private TextInputEditText nomEditText, descriptionEditText;
    private Button btnModifier;
    private int categoryId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category);
        nomEditText = findViewById(R.id.nomEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        btnModifier = findViewById(R.id.btnModifer);
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        loadCategory();
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifierCategorie();
            }
        });
    }

    private void loadCategory() {
        if (categoryId != -1) {
            SQLiteDatabase db = DataBaseHelper.getInstance(this).getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Category WHERE id = ?", new String[]{String.valueOf(categoryId)});
            if (cursor != null && cursor.moveToFirst()) {
                String nom = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                nomEditText.setText(nom);
                descriptionEditText.setText(description);

                cursor.close();
            } else {
                Toast.makeText(this, "Catégorie non trouvée", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de catégorie invalide", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void modifierCategorie() {
        String nouveauNom = nomEditText.getText().toString().trim();
        String nouvelleDescription = descriptionEditText.getText().toString().trim();
        if (nouveauNom.isEmpty() || nouvelleDescription.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        if (categoryId != 0) {
            SQLiteDatabase db = DataBaseHelper.getInstance(this).getWritableDatabase();
            String updateQuery = "UPDATE Category SET nom = ?, description = ? WHERE id = ?";
            db.execSQL(updateQuery, new String[]{nouveauNom, nouvelleDescription, String.valueOf(categoryId)});
            Toast.makeText(this, "Catégorie modifiée avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditCategoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erreur : ID de catégorie invalide", Toast.LENGTH_SHORT).show();
        }
    }
}