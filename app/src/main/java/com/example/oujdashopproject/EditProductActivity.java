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

import com.google.android.material.textfield.TextInputEditText;
import com.example.oujdashopproject.DataBase.DataBaseHelper;

public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText nomEditText, priceEditText, descriptionEditText;
    private Button btnModifier;
    private int productId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

        nomEditText = findViewById(R.id.nomEditText);
        priceEditText = findViewById(R.id.priceEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        btnModifier = findViewById(R.id.btnModifier);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        loadProduct();
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifierProduit();
            }
        });
    }

    private void loadProduct() {
        if (productId != -1) {
            SQLiteDatabase db = DataBaseHelper.getInstance(this).getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE id = ?", new String[]{String.valueOf(productId)});
            if (cursor != null && cursor.moveToFirst()) {
                String nom = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                nomEditText.setText(nom);
                priceEditText.setText(String.valueOf(price));
                descriptionEditText.setText(description);
                cursor.close();
            } else {
                Toast.makeText(this, "Produit non trouvé", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de produit invalide", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void modifierProduit() {
        String nouveauNom = nomEditText.getText().toString().trim();
        String nouveauPrix = priceEditText.getText().toString().trim();
        String nouvelleDescription = descriptionEditText.getText().toString().trim();
        if (nouveauNom.isEmpty() || nouveauPrix.isEmpty() || nouvelleDescription.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        if (productId != 0) {
            SQLiteDatabase db = DataBaseHelper.getInstance(this).getWritableDatabase();
            String updateQuery = "UPDATE Product SET nom = ?, price = ?, description = ? WHERE id = ?";
            db.execSQL(updateQuery, new String[]{nouveauNom, nouveauPrix, nouvelleDescription, String.valueOf(productId)});
            Toast.makeText(this, "Produit modifié avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditProductActivity.this, ProductActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erreur : ID de produit invalide", Toast.LENGTH_SHORT).show();
        }
    }
}
