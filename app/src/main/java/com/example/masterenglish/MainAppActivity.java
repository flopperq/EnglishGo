package com.example.masterenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String userRole = sharedPreferences.getString("userRole", "user");

        Button btnUserList = findViewById(R.id.btn_user_list);
        if (!userRole.equals("admin")) {
            btnUserList.setVisibility(View.GONE);
        } else {
            btnUserList.setOnClickListener(v -> {
                Intent intent = new Intent(MainAppActivity.this, UserListActivity.class);
                startActivity(intent);
            });
        }

        Button btnTaskYesNo = findViewById(R.id.btn_task_yes_no);
        btnTaskYesNo.setOnClickListener(v -> {
            Intent intent = new Intent(MainAppActivity.this, TaskYesNoActivity.class);
            startActivity(intent);
        });

        Button btnTaskSpelling = findViewById(R.id.btn_task_spelling);
        btnTaskSpelling.setOnClickListener(v -> {
            Intent intent = new Intent(MainAppActivity.this, TaskSpellingActivity.class);
            startActivity(intent);
        });

        Button btnTaskMatching = findViewById(R.id.btn_task_matching);
        btnTaskMatching.setOnClickListener(v -> {
            Intent intent = new Intent(MainAppActivity.this, TaskMatchingActivity.class);
            startActivity(intent);
        });

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.putString("userRole", null);
            editor.apply();

            startActivity(new Intent(MainAppActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("currentUser", "unknown_user");

        int lastCorrectYesNo = sharedPreferences.getInt("lastCorrectAnswers_" + currentUser, 0);
        int lastTotalYesNo = sharedPreferences.getInt("lastTotalAnswers_" + currentUser, 0);

        int lastCorrectSpelling = sharedPreferences.getInt("lastSpellingCorrectAnswers_" + currentUser, 0);
        int lastTotalSpelling = sharedPreferences.getInt("lastSpellingTotalAnswers_" + currentUser, 0);

        int lastCorrectMatching = sharedPreferences.getInt("lastMatchingCorrectAnswers_" + currentUser, 0);
        int lastTotalMatching = sharedPreferences.getInt("lastMatchingTotalAnswers_" + currentUser, 0);

        TextView tvLastResultYesNo = findViewById(R.id.tv_last_result);
        TextView tvLastResultSpelling = findViewById(R.id.tv_last_result_second);
        TextView tvLastResultMatching = findViewById(R.id.tv_last_result_matching);

        tvLastResultYesNo.setText("Да/Нет: " + lastCorrectYesNo + "/" + lastTotalYesNo);
        tvLastResultSpelling.setText("Правописание: " + lastCorrectSpelling + "/" + lastTotalSpelling);
        tvLastResultMatching.setText("Сопоставление: " + lastCorrectMatching + "/" + lastTotalMatching);
    }

}

