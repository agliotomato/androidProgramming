package org.techtwon.quizgame;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Quiz4 extends AppCompatActivity {

    private TextView tvQuestion;
    private ProgressBar progressBar;
    private Button btnNextQuestion;
    private Button[] options = new Button[4];
    private int progress = 100;
    private boolean isAnswered = false;
    private int currentQuestionId = 1; // 첫 질문 ID
    private int correctOption = -1;
    private Handler handler = new Handler(); // 타이머용 핸들러
    private QuizDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz4);

        // DB 초기화
        dbHelper = new QuizDatabaseHelper(this);

        // UI 요소 초기화
        progressBar = findViewById(R.id.progressBar);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);

        options[0] = findViewById(R.id.btnOption1);
        options[1] = findViewById(R.id.btnOption2);
        options[2] = findViewById(R.id.btnOption3);
        options[3] = findViewById(R.id.btnOption4);

        // 버튼 클릭 리스너 등록
        for (int i = 0; i < options.length; i++) {
            int finalI = i + 1; // 정답 확인을 위해 1~4로 맞춤
            options[i].setOnClickListener(view -> checkAnswer(finalI));
        }

        btnNextQuestion.setOnClickListener(view -> loadNextQuestion());

        // 첫 질문 로드
        loadQuestion(currentQuestionId);
        startProgressTimer();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // 현재 Activity 종료
        });
    }

    // 질문 로드
    private void loadQuestion(int questionId) {
        Cursor cursor = dbHelper.getQuestionById(questionId);
        if (cursor.moveToFirst()) {
            tvQuestion.setText(cursor.getString(cursor.getColumnIndexOrThrow("question")));
            options[0].setText(cursor.getString(cursor.getColumnIndexOrThrow("option1")));
            options[1].setText(cursor.getString(cursor.getColumnIndexOrThrow("option2")));
            options[2].setText(cursor.getString(cursor.getColumnIndexOrThrow("option3")));
            options[3].setText(cursor.getString(cursor.getColumnIndexOrThrow("option4")));
            correctOption = cursor.getInt(cursor.getColumnIndexOrThrow("correctOption"));
        }
        cursor.close();
    }

    // 정답 확인
    private void checkAnswer(int selectedOption) {
        if (isAnswered) return;

        isAnswered = true;
        handler.removeCallbacksAndMessages(null);
        resetAllButtonBackgrounds();

        // 정답 확인
        if (selectedOption == correctOption) {
            options[correctOption - 1].setBackgroundResource(R.drawable.button_selected_background);
            showFeedback("정답입니다!", true);
        } else if (selectedOption == -1) { // 시간 초과 시
            handleTimeOut();
        } else {
            options[selectedOption - 1].setBackgroundResource(R.drawable.selected_wrong_button);
            options[correctOption - 1].setBackgroundResource(R.drawable.button_selected_background);
            showFeedback("틀렸습니다!", false);
        }
        btnNextQuestion.setVisibility(View.VISIBLE);
    }

    // ProgressBar 타이머 시작
    private void startProgressTimer() {
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progress > 0) {
                    progress -= 1; // Progress 감소
                    progressBar.setProgress(progress);
                    handler.postDelayed(this, 50); // 50ms마다 실행
                } else {
                    if (!isAnswered) {
                        handleTimeOut(); // 시간 초과 처리
                    }
                }
            }
        };
        handler.post(progressRunnable);
    }

    // 시간 초과 처리
    private void handleTimeOut() {
        isAnswered = true;
        handler.removeCallbacksAndMessages(null); // 타이머 중단
        resetAllButtonBackgrounds();
        options[correctOption - 1].setBackgroundResource(R.drawable.button_selected_background); // 정답 강조
        showFeedback("시간 초과입니다!", false); // 시간 초과 메시지 표시
        btnNextQuestion.setVisibility(View.VISIBLE); // 다음 질문 버튼 표시
    }

    // 피드백 메시지 표시
    private void showFeedback(String message, boolean isCorrect) {
        tvQuestion.setText(message); // 메시지를 tvQuestion에 표시
        tvQuestion.setTextColor(isCorrect ? Color.BLACK : Color.RED); // 색상 설정
    }

    // 다음 질문 로드
    private void loadNextQuestion() {
        if (currentQuestionId < 3) { // 질문 개수에 따라 변경
            currentQuestionId++;
            resetState();
            loadQuestion(currentQuestionId);
        } else {
            tvQuestion.setText("퀴즈 종료!"); // 마지막 메시지
            tvQuestion.setTextColor(Color.BLACK);
        }
    }

    // 버튼 배경 초기화
    private void resetAllButtonBackgrounds() {
        for (Button option : options) {
            option.setBackgroundResource(R.drawable.button_background);
        }
    }

    // 상태 초기화
    private void resetState() {
        isAnswered = false;
        btnNextQuestion.setVisibility(View.GONE);
        tvQuestion.setTextColor(Color.BLACK); // 텍스트 색상 초기화
        resetAllButtonBackgrounds();
        progress = 100;
        startProgressTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // 메모리 누수 방지
    }
}
