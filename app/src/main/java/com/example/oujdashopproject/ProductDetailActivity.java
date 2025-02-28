package com.example.oujdashopproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.oujdashopproject.DataBase.DataBaseHelper;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productName, productPrice, productDescription;
    private DataBaseHelper dataBaseHelper;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        dataBaseHelper = DataBaseHelper.getInstance(this);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        if (productId == -1) {
            Toast.makeText(this, "Produit introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProductDetails();
    }
    private void loadProductDetails() {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE id = ?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            productName.setText(name);
            productPrice.setText("Prix : " + price + " MAD");
            productDescription.setText(description);

            cursor.close();
        } else {
            Toast.makeText(this, "Erreur lors du chargement du produit", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
