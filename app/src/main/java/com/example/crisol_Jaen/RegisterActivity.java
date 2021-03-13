package com.example.crisol_Jaen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.crisol_Jaen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // btnViewLogin
        Button btnViewLogin = findViewById(R.id.btnViewLogin);
        btnViewLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // btnRegister
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextInputEditText etName = findViewById(R.id.etName);
        TextInputEditText etLastname = findViewById(R.id.etLastname);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPasswrod = findViewById(R.id.etPassword);

        final String name = etName.getText().toString().trim();
        final String lastname = etLastname.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        String password = etPasswrod.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Create a new user with a first and last name
                        FirebaseUser user = mAuth.getCurrentUser();
                        User crisolUser = new User(user.getUid(), name, lastname, email);

                        // Add a new document with a generated ID
                        db.collection("user")
                                .add(crisolUser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // do nothing
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // do nothing
                                    }
                                });

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error al registrar.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
