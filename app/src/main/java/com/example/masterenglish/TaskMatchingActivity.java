package com.example.masterenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskMatchingActivity extends AppCompatActivity {

    private TextView tvTask, tvScore, tvResult;
    private RadioGroup rgOptions;
    private Button btnSubmit, btnShowResults;

    private List<MatchingQuestion> questions;
    private List<Result> results = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int totalAnswers = 0;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_matching);

        tvTask = findViewById(R.id.tv_task);
        tvScore = findViewById(R.id.tv_score);
        tvResult = findViewById(R.id.tv_result);
        rgOptions = findViewById(R.id.rg_options);
        btnSubmit = findViewById(R.id.btn_submit);
        btnShowResults = findViewById(R.id.btn_show_results);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        generateQuestions();
        showQuestion();

        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnShowResults.setOnClickListener(v -> navigateToResultsActivity());
    }

    private void generateQuestions() {
        questions = new ArrayList<>();
        questions.add(new MatchingQuestion("rabbit", "кролик", new String[]{"хорек", "удав", "кролик"}));
        questions.add(new MatchingQuestion("cat", "кошка", new String[]{"кошка", "собака", "лошадь"}));
        questions.add(new MatchingQuestion("strong", "сильный", new String[]{"мягкий", "сильный", "слабый"}));
        questions.add(new MatchingQuestion("red", "красный", new String[]{"синий", "черный", "красный"}));

        Collections.shuffle(questions);
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            rgOptions.clearCheck();
            tvResult.setText("");

            MatchingQuestion question = questions.get(currentQuestionIndex);
            tvTask.setText("Сопоставьте слово: " + question.getWord());

            for (int i = 0; i < rgOptions.getChildCount(); i++) {
                ((RadioButton) rgOptions.getChildAt(i)).setText(question.getOptions()[i]);
            }
        } else {
            finishCourse();
        }
    }

    private void checkAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();

        if (selectedId == -1) {
            tvResult.setText("Выберите вариант!");
            return;
        }

        RadioButton selectedOption = findViewById(selectedId);
        String userAnswer = selectedOption.getText().toString();

        MatchingQuestion question = questions.get(currentQuestionIndex);
        boolean isCorrect = question.getCorrectAnswer().equals(userAnswer);

        results.add(new Result(question.getWord(), question.getCorrectAnswer(), userAnswer, isCorrect));

        if (isCorrect) {
            correctAnswers++;
            tvResult.setText("Правильно!");
            tvResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvResult.setText("Неправильно!");
            tvResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        totalAnswers++;
        currentQuestionIndex++;
        updateScore();

        tvResult.postDelayed(this::showQuestion, 1000);
    }

    private void updateScore() {
        tvScore.setText("Счет: " + correctAnswers + "/" + totalAnswers);
    }

    private void finishCourse() {
        double percentage = (correctAnswers / (double) totalAnswers) * 100;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentUser = sharedPreferences.getString("currentUser", "unknown_user");

        editor.putInt("lastMatchingCorrectAnswers_" + currentUser, correctAnswers);
        editor.putInt("lastMatchingTotalAnswers_" + currentUser, totalAnswers);
        editor.apply();

        btnSubmit.setEnabled(false);

        tvResult.setText(percentage < 70 ? "Попробуйте еще раз!" : "Отличный результат!");
        tvResult.setTextColor(getResources().getColor(percentage < 70 ? android.R.color.holo_red_dark : android.R.color.holo_green_dark));

        btnShowResults.setVisibility(View.VISIBLE);
    }

    private void navigateToResultsActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putParcelableArrayListExtra("results", new ArrayList<>(results));
        startActivity(intent);
    }

    private static class MatchingQuestion {
        private final String word;
        private final String correctAnswer;
        private final String[] options;

        public MatchingQuestion(String word, String correctAnswer, String[] options) {
            this.word = word;
            this.correctAnswer = correctAnswer;
            this.options = options;
        }

        public String getWord() {
            return word;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public String[] getOptions() {
            return options;
        }
    }
}
