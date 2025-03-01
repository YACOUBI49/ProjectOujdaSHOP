package com.example.oujdashopproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.example.oujdashopproject.DataBase.DataBaseHelper;

public class ChangePassword extends AppCompatActivity {

    private TextInputEditText oldPassword, newPassword, confirmPassword;
    private Button btnChangePassword;
    private DataBaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = DataBaseHelper.getInstance(this);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMotDePasse();
            }
        });
    }

    private void changerMotDePasse() {
        String ancienMotDePasse = oldPassword.getText().toString().trim();
        String nouveauMotDePasse = newPassword.getText().toString().trim();
        String confirmerMotDePasse = confirmPassword.getText().toString().trim();
        if (ancienMotDePasse.isEmpty() || nouveauMotDePasse.isEmpty() || confirmerMotDePasse.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!nouveauMotDePasse.equals(confirmerMotDePasse)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM User WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String motDePasseActuel = cursor.getString(cursor.getColumnIndexOrThrow("password"));

            if (!ancienMotDePasse.equals(motDePasseActuel)) {
                Toast.makeText(this, "Ancien mot de passe incorrect", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                return;
            }
        }
        cursor.close();
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", nouveauMotDePasse);
        int rows = dbWrite.update("User", values, "id = ?", new String[]{String.valueOf(userId)});
        if (rows > 0) {
            Toast.makeText(this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show();
        }

        dbWrite.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu3, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(ChangePassword.this, UserActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_Home) {
            Intent intent = new Intent(ChangePassword.this, MainActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}