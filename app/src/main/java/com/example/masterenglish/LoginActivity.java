package com.example.masterenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(LoginActivity.this, MainAppActivity.class));
            finish();
        }

        EditText etLogin = findViewById(R.id.et_login);
        EditText etPassword = findViewById(R.id.et_password);
        ImageButton btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register);

        databaseHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
            } else {
                if (login.equals("admin") && password.equals("root")) {
                    Toast.makeText(this, "Вход выполнен как администратор", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("userRole", "admin");
                    editor.putString("currentUser", "admin");
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainAppActivity.class));
                    finish();
                } else {
                    boolean isAuthenticated = databaseHelper.checkUser(login, password);
                    if (isAuthenticated) {
                        Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userRole", "user");
                        editor.putString("currentUser", login);
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, MainAppActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
