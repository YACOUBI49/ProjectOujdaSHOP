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

public class AjouterProduct extends AppCompatActivity {
    private Button button;
    private TextInputEditText nomEditText, priceEditText, descriptionEditText;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_product);
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        if (categoryId == -1) {
            Toast.makeText(this, "Catégorie invalide", Toast.LENGTH_SHORT).show();
            finish();
        }
        button = findViewById(R.id.btnAjouter);
        nomEditText = findViewById(R.id.nomEditText);
        priceEditText = findViewById(R.id.prixEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomEditText.getText().toString().trim();
                String priceStr = priceEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if (nom.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AjouterProduct.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                    return;
                }

                double price = Double.parseDouble(priceStr);
                SQLiteDatabase db = DataBaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
                db.execSQL(
                        "INSERT INTO Product (nom, price, description, category_id) VALUES (?, ?, ?, ?)",
                        new String[]{nom, String.valueOf(price), description, String.valueOf(categoryId)}
                );

                Toast.makeText(AjouterProduct.this, "Produit ajouté avec succès", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AjouterProduct.this, ProductActivity.class);
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivity(intent);
                finish(); // Fermer cette activité
            }

        });
    }
}