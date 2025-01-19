package com.example.masterenglish;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ListView lvResults = findViewById(R.id.lv_results);

        ArrayList<Result> results = getIntent().getParcelableArrayListExtra("results");

        ArrayList<String> displayResults = new ArrayList<>();
        if (results != null) {
            for (Result result : results) {
                String status = result.isCorrect() ? "Правильно" : "Неправильно";
                displayResults.add(
                        "Вопрос: " + result.getQuestion() + "\n" +
                                "Ответ: " + result.getUserAnswer() + "\n" +
                                "Правильный ответ: " + result.getCorrectAnswer() + "\n" +
                                "Статус: " + status
                );
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayResults
        );
        lvResults.setAdapter(adapter);
    }
}
