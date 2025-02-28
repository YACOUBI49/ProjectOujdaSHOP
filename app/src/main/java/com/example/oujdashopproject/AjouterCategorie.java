package com.example.oujdashopproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oujdashopproject.DataBase.DataBaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class AjouterCategorie extends AppCompatActivity {
    private   Button button;
    private  TextInputEditText text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie);

        button = findViewById(R.id.btnAjouter);
        text1 = findViewById(R.id.nomEditText);
        text2 = findViewById(R.id.descriptionEditText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String champ1 = text1.getText().toString().trim();
                String champ2 = text2.getText().toString().trim();

                if (champ1.isEmpty() || champ2.isEmpty()) {
                    Toast.makeText(AjouterCategorie.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                    return;
                }
                SQLiteDatabase db = DataBaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
                db.execSQL(
                        "INSERT INTO Category (nom, description) VALUES (?, ?)",
                        new String[]{champ1, champ2}
                );

                Toast.makeText(AjouterCategorie.this, "Catégorie ajoutée avec succès", Toast.LENGTH_LONG).show();
                Intent intent  = new Intent(AjouterCategorie.this , MainActivity.class);
                startActivity(intent);
                text1.setText("");
                text2.setText("");
            }
        });
    }
}
