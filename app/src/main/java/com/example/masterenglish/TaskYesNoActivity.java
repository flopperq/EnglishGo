package com.example.masterenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskYesNoActivity extends AppCompatActivity {

    private TextView tvTask, tvScore, tvResult;
    private Button btnYes, btnNo, btnShowResults;

    private List<Question> questions;
    private List<Result> results = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int totalAnswers = 0;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_yes_no);

        tvTask = findViewById(R.id.tv_task);
        tvScore = findViewById(R.id.tv_score);
        tvResult = findViewById(R.id.tv_result);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        btnShowResults = findViewById(R.id.btn_show_results);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        generateQuestions();
        showQuestion();

        btnYes.setOnClickListener(v -> checkAnswer(true));
        btnNo.setOnClickListener(v -> checkAnswer(false));
        btnShowResults.setOnClickListener(v -> navigateToResultsActivity());
    }

    private void generateQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("long-длинный", true));
        questions.add(new Question("power-простой", false));
        questions.add(new Question("rabbit-кролик", true));
        questions.add(new Question("cat-собака", false));
        questions.add(new Question("strong-сильный", true));
        questions.add(new Question("red-красный", true));
        questions.add(new Question("blue-черный", false));
        questions.add(new Question("long-длинный", true));
        questions.add(new Question("power-простой", false));
        questions.add(new Question("rabbit-кролик", true));
        questions.add(new Question("cat-собака", false));
        questions.add(new Question("strong-сильный", true));
        questions.add(new Question("red-красный", true));
        questions.add(new Question("blue-черный", false));
        questions.add(new Question("long-длинный", true));
        questions.add(new Question("power-простой", false));
        questions.add(new Question("rabbit-кролик", true));
        questions.add(new Question("cat-собака", false));
        questions.add(new Question("strong-сильный", true));
        questions.add(new Question("red-красный", true));
        questions.add(new Question("blue-черный", false));

        Collections.shuffle(questions);
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            tvTask.setText(question.getText());
            tvResult.setText("");
        } else {
            finishCourse();
        }
    }

    private void checkAnswer(boolean userAnswer) {
        Question question = questions.get(currentQuestionIndex);
        boolean isCorrect = question.isCorrect() == userAnswer;

        results.add(new Result(question.getText(), question.isCorrect() ? "Да" : "Нет", userAnswer ? "Да" : "Нет", isCorrect));

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

        editor.putInt("lastCorrectAnswers_" + currentUser, correctAnswers);
        editor.putInt("lastTotalAnswers_" + currentUser, totalAnswers);
        editor.apply();

        btnYes.setEnabled(false);
        btnNo.setEnabled(false);

        tvResult.setText(percentage < 70 ? "Попробуйте еще раз!" : "Отличный результат!");
        tvResult.setTextColor(getResources().getColor(percentage < 70 ? android.R.color.holo_red_dark : android.R.color.holo_green_dark));

        btnShowResults.setVisibility(View.VISIBLE);
    }

    private void navigateToResultsActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putParcelableArrayListExtra("results", new ArrayList<>(results));
        startActivity(intent);
    }

    private static class Question {
        private final String text;
        private final boolean correct;

        public Question(String text, boolean correct) {
            this.text = text;
            this.correct = correct;
        }

        public String getText() {
            return text;
        }

        public boolean isCorrect() {
            return correct;
        }
    }
}
