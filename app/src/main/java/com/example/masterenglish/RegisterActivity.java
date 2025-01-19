package com.example.masterenglish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etLogin = findViewById(R.id.et_register_login);
        EditText etPassword = findViewById(R.id.et_register_password);
        ImageButton btnStart = findViewById(R.id.btn_start);

        databaseHelper = new DatabaseHelper(this);

        btnStart.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                boolean isRegistered = databaseHelper.registerUser(email, login, password);
                if (isRegistered) {
                    Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
