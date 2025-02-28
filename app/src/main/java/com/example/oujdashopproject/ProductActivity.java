package com.example.oujdashopproject;

import android.annotation.SuppressLint;
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

import com.example.oujdashopproject.Adapter.ProductAdapter;
import com.example.oujdashopproject.DataBase.DataBaseHelper;
import com.example.oujdashopproject.Users.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private ListView listView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DataBaseHelper dataBaseHelper;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        if (categoryId == -1) {

            finish();
            return;
        }

        listView = findViewById(R.id.listViewProducts);
        productList = new ArrayList<>();
        dataBaseHelper = DataBaseHelper.getInstance(this);
        productAdapter = new ProductAdapter(this, productList);
        listView.setAdapter(productAdapter);
        loadProduct();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Product selectedProduct = productList.get(position);
            Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", selectedProduct.getId());
            startActivity(intent);
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Product selectedProduct = productList.get(position);
            new AlertDialog.Builder(ProductActivity.this)
                    .setTitle("Options")
                    .setMessage("Que voulez-vous faire avec ce produit ?")
                    .setPositiveButton("Modifier", (dialog, which) -> {
                        Intent intent = new Intent(ProductActivity.this, EditProductActivity.class);
                        intent.putExtra("PRODUCT_ID", selectedProduct.getId());
                        startActivity(intent);
                    })
                    .setNegativeButton("Supprimer", (dialog, which) -> {
                        deleteProduct(selectedProduct.getId());
                    })
                    .setNeutralButton("Annuler", null)
                    .show();

            return true;
        });
    }

    private void loadProduct() {
        productList.clear(); // Vider la liste pour éviter les doublons
        String query = "SELECT * FROM Product WHERE category_id = ?";
        Cursor cursor = dataBaseHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(categoryId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex("nom"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                productList.add(new Product(id, nom, price, description, categoryId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        productAdapter.notifyDataSetChanged(); // Rafraîchir l'affichage
    }

    private void deleteProduct(int productId) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int deletedRows = db.delete("Product", "id = ?", new String[]{String.valueOf(productId)});

        if (deletedRows > 0) {
            Toast.makeText(this, "Produit supprimé avec succès", Toast.LENGTH_SHORT).show();
            startActivity(getIntent());
            finish();
        } else {
            Toast.makeText(this, "Échec de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_produit) {
            Intent intent = new Intent(this, AjouterProduct.class);
            intent.putExtra("CATEGORY_ID", categoryId);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, UserActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_Home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadProduct(); // Recharge la liste après modification
    }

}
