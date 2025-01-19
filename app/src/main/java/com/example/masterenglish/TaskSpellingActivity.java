package com.example.masterenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TaskSpellingActivity extends AppCompatActivity {

    private TextView tvTask, tvScore, tvResult;
    private EditText etAnswer;
    private Button btnSubmit, btnShowResults;

    private List<SpellingQuestion> questions;
    private List<Result> results = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int totalAnswers = 0;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_spelling);

        tvTask = findViewById(R.id.tv_task);
        tvScore = findViewById(R.id.tv_score);
        tvResult = findViewById(R.id.tv_result);
        etAnswer = findViewById(R.id.et_answer);
        btnSubmit = findViewById(R.id.btn_submit);
        btnShowResults = findViewById(R.id.btn_show_results);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        generateQuestions();
        showQuestion();

        btnSubmit.setOnClickListener(v -> checkAnswer());

        btnShowResults.setOnClickListener(v -> {
            Intent intent = new Intent(TaskSpellingActivity.this, ResultsActivity.class);
            intent.putParcelableArrayListExtra("results", new ArrayList<>(results));
            startActivity(intent);
        });

        btnShowResults.setEnabled(false);
    }

    private void generateQuestions() {
        questions = new ArrayList<>();
        questions.add(new SpellingQuestion("b__k", "book"));
        questions.add(new SpellingQuestion("ca__", "cat"));
        questions.add(new SpellingQuestion("d_g", "dog"));
        questions.add(new SpellingQuestion("h__se", "house"));
        questions.add(new SpellingQuestion("ap__e", "apple"));

        Collections.shuffle(questions);
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            SpellingQuestion question = questions.get(currentQuestionIndex);
            tvTask.setText(question.getText());
            etAnswer.setText("");
            tvResult.setText("");
        } else {
            finishCourse();
        }
    }

    private void checkAnswer() {
        String userAnswer = etAnswer.getText().toString().trim();
        SpellingQuestion question = questions.get(currentQuestionIndex);

        boolean isCorrect = userAnswer.equalsIgnoreCase(question.getCorrectAnswer());
        results.add(new Result(question.getText(), question.getCorrectAnswer(), userAnswer, isCorrect));

        if (isCorrect) {
            correctAnswers++;
            tvResult.setText("Правильно!");
            tvResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvResult.setText("Неправильно! Ответ: " + question.getCorrectAnswer());
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
        editor.putInt("lastSpellingCorrectAnswers_" + currentUser, correctAnswers);
        editor.putInt("lastSpellingTotalAnswers_" + currentUser, totalAnswers);
        editor.apply();

        tvTask.setText("Игра завершена!");
        tvResult.setText(percentage < 70 ? "Попробуйте ещё раз!" : "Отличный результат!");
        tvResult.setTextColor(getResources().getColor(percentage < 70 ? android.R.color.holo_red_dark : android.R.color.holo_green_dark));

        etAnswer.setEnabled(false);
        btnSubmit.setEnabled(false);

        btnShowResults.setEnabled(true);
    }

    private static class SpellingQuestion {
        private final String text;
        private final String correctAnswer;

        public SpellingQuestion(String text, String correctAnswer) {
            this.text = text;
            this.correctAnswer = correctAnswer;
        }

        public String getText() {
            return text;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
