package com.example.oujdashopproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.example.oujdashopproject.DataBase.DataBaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputEditText editNom, editPrenom, editEmail;
    private Button btnModifierProfil, btnChangeProfileImage;
    private DataBaseHelper dbHelper;
    private int userId;
    private TextView nomP, emailP, pass;
    private ImageView profileImage;
    private Uri pathImage = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = DataBaseHelper.getInstance(this);
        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        btnModifierProfil = findViewById(R.id.btnModifierProfil);
        pass = findViewById(R.id.ChangePassword);
        nomP = findViewById(R.id.nomP);
        emailP = findViewById(R.id.emailP);
        profileImage = findViewById(R.id.profileImage);
        btnChangeProfileImage = findViewById(R.id.btnChangeProfileImage);

        afficherInfosUtilisateur();
        loadProfileImage();

        btnChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnModifierProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifierProfil();
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pathImage = data.getData();
            profileImage.setImageURI(pathImage);
        }
    }

    private void loadProfileImage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT image FROM User WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            if (imagePath != null && !imagePath.isEmpty()) {
                Bitmap bitmap = loadImageFromStorage(imagePath);
                if (bitmap != null) {
                    profileImage.setImageBitmap(bitmap);
                } else {
                    profileImage.setImageResource(R.mipmap.profile1);
                }
            } else {
                profileImage.setImageResource(R.mipmap.profile1);
            }
        }
        cursor.close();
        db.close();
    }

    private void afficherInfosUtilisateur() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String nom = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
            String prenom = cursor.getString(cursor.getColumnIndexOrThrow("prenom"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            editNom.setText(nom);
            editPrenom.setText(prenom);
            editEmail.setText(email);
            nomP.setText(nom + " " + prenom);
            emailP.setText(email);
        } else {
            Toast.makeText(this, "Aucune donnée trouvée pour cet utilisateur", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }

    private void modifierProfil() {
        String nouveauNom = editNom.getText().toString().trim();
        String nouveauPrenom = editPrenom.getText().toString().trim();
        String nouveauEmail = editEmail.getText().toString().trim();
        if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty() || nouveauEmail.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String imagePath = null;
        if (pathImage != null) {
            Drawable drawable = profileImage.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                imagePath = saveImageToInternalStorage(bitmap, String.valueOf(System.currentTimeMillis()));
            }
        }

        ContentValues values = new ContentValues();
        values.put("nom", nouveauNom);
        values.put("prenom", nouveauPrenom);
        values.put("email", nouveauEmail);
        if (imagePath != null) {
            values.put("image", imagePath);
        }

        int rowsAffected = db.update("User", values, "id = ?", new String[]{String.valueOf(userId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Profil et image mis à jour avec succès", Toast.LENGTH_SHORT).show();
            nomP.setText(nouveauNom + " " + nouveauPrenom);
            emailP.setText(nouveauEmail);

            loadProfileImage();
        } else {
            Toast.makeText(this, "Échec de la mise à jour du profil", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private String saveImageToInternalStorage(Bitmap bitmap, String imageName) {
        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, imageName + ".jpg");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap loadImageFromStorage(String path) {
        return BitmapFactory.decodeFile(path);
    }
}