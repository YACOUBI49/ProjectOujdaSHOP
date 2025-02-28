package com.example.oujdashopproject;

import android.content.Intent;
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

import com.example.oujdashopproject.DataBase.DataBaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    Button button , retour;
    TextInputEditText text1, text2, text3, text4 , text5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        button = findViewById(R.id.btnRegister);
        retour = findViewById(R.id.btnConnexion);
        text1 = findViewById(R.id.nomEditText);
        text2 = findViewById(R.id.prenomEditText);
        text3 = findViewById(R.id.emailEditText);
        text4 = findViewById(R.id.passwordEditText);
        text5 = findViewById(R.id.confirmPasswordEditText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String champ1 = text1.getText().toString().trim();
                String champ2 = text2.getText().toString().trim();
                String champ3 = text3.getText().toString().trim();
                String champ4 = text4.getText().toString().trim();
                String champ5 = text5.getText().toString().trim();

                if (champ1.isEmpty() || champ2.isEmpty() || champ3.isEmpty() || champ4.isEmpty() || champ5.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!champ4.equals(champ5)) {
                    Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_LONG).show();
                    return;
                }
                SQLiteDatabase db = DataBaseHelper.getInstance(getApplicationContext()).getWritableDatabase();
                db.execSQL(
                        "INSERT INTO User (nom, prenom, email, password) VALUES (?, ?, ?, ?)",
                        new String[]{champ1, champ2, champ3, champ4}
                );

                Toast.makeText(RegisterActivity.this, "User ajouté avec succès", Toast.LENGTH_LONG).show();
                text1.setText("");
                text2.setText("");
                text3.setText("");
                text4.setText("");
                text5.setText("");
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
