package org.techtwon.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Quiz2 extends AppCompatActivity {
    private TextView questionText;
    private Button button1, button2, button3, button4, nextButton, prevButton;
    private int currentQuestionIndex = 0; // 현재 질문 번호
    private int correctOption; // 현재 질문의 정답 번호
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        questionText = findViewById(R.id.questionText);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);

        // 데이터베이스 연결
        WordDatabaseHelper2 dbHelper = new WordDatabaseHelper2(this);
        db = dbHelper.getReadableDatabase();

        // 질문 데이터 가져오기
        cursor = db.rawQuery("SELECT * FROM questions", null);
        cursor.moveToFirst(); // 첫 번째 질문으로 이동
        displayQuestion();

        // 버튼 클릭 이벤트 설정
        button1.setOnClickListener(v -> checkAnswer(1));
        button2.setOnClickListener(v -> checkAnswer(2));
        button3.setOnClickListener(v -> checkAnswer(3));
        button4.setOnClickListener(v -> checkAnswer(4));
        nextButton.setOnClickListener(v -> showNextQuestion());
        prevButton.setOnClickListener(v -> showPreviousQuestion());

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // 현재 Activity 종료
        });
    }

    private void displayQuestion() {
        String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
        String option1 = cursor.getString(cursor.getColumnIndexOrThrow("option1"));
        String option2 = cursor.getString(cursor.getColumnIndexOrThrow("option2"));
        String option3 = cursor.getString(cursor.getColumnIndexOrThrow("option3"));
        String option4 = cursor.getString(cursor.getColumnIndexOrThrow("option4"));
        correctOption = cursor.getInt(cursor.getColumnIndexOrThrow("correctOption"));

        questionText.setText(word);
        button1.setText(option1);
        button2.setText(option2);
        button3.setText(option3);
        button4.setText(option4);
        nextButton.setEnabled(false);
        prevButton.setEnabled(currentQuestionIndex > 0); // 첫번째 질문에서는 비활성화
    }

    private void checkAnswer(int selectedOption) {
        if (selectedOption == correctOption) {
            Toast.makeText(this, "정답입니다!", Toast.LENGTH_SHORT).show();
            nextButton.setEnabled(true);
        } else {
            Toast.makeText(this, "오답입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < cursor.getCount() - 1) {
            currentQuestionIndex++;
            cursor.moveToPosition(currentQuestionIndex);
            displayQuestion();
        } else {
            Toast.makeText(this, "퀴즈가 끝났습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            cursor.moveToPosition(currentQuestionIndex);
            displayQuestion();
        }
    }
}
