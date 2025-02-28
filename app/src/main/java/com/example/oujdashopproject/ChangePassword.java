package com.example.oujdashopproject;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
}