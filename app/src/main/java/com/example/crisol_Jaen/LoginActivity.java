package com.example.crisol_Jaen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // btnViewRegister
        Button btnViewRegister = findViewById(R.id.btnViewRegister);
        btnViewRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // btnLogin
        mAuth = FirebaseAuth.getInstance();

        //Validando si el usuario ya está registrado
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextInputEditText etemail = findViewById(R.id.etEmail);
        TextInputEditText etpassword = findViewById(R.id.etPassword);

        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Correo y contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
