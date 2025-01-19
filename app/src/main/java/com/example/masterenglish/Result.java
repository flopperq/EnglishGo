package com.example.masterenglish;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {
    private final String question;
    private final String correctAnswer;
    private final String userAnswer;
    private final boolean isCorrect;

    public Result(String question, String correctAnswer, String userAnswer, boolean isCorrect) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

    protected Result(Parcel in) {
        question = in.readString();
        correctAnswer = in.readString();
        userAnswer = in.readString();
        isCorrect = in.readByte() != 0;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(correctAnswer);
        dest.writeString(userAnswer);
        dest.writeByte((byte) (isCorrect ? 1 : 0));
    }
}
