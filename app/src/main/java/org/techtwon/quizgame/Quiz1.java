package org.techtwon.quizgame;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

public class Quiz1 extends AppCompatActivity {
    private LinearLayout cardFront, cardBack;
    private TextView wordText, wordMeaning, wordPartofspeech, exampleSentence, progressText;
    private WordRepository repository;
    private boolean isFrontVisible = true;

    private int currentWordIndex = 0; // 현재 단어의 인덱스
    private Cursor wordCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        // UI 초기화
        cardFront = findViewById(R.id.cardFront);
        cardBack = findViewById(R.id.cardBack);
        wordText = findViewById(R.id.wordText);
        wordMeaning = findViewById(R.id.wordMeaning);
        wordPartofspeech = findViewById(R.id.wordPartofspeech);
        exampleSentence = findViewById(R.id.exampleSentence);
        progressText = findViewById(R.id.progressText);

        // 데이터베이스 초기화
        repository = new WordRepository(this);

        // 샘플 데이터 삽입 (첫 실행 시)
        if (isDatabaseEmpty()) {
            insertSampleData();
        }

        // 데이터 로드
        loadWordCursor();
        updateWordDisplay();

        // 버튼 이벤트 설정
        Button prevButton = findViewById(R.id.prevButton);
        Button nextButton = findViewById(R.id.nextButton);

        prevButton.setOnClickListener(v -> {
            showPreviousWord();
        });

        nextButton.setOnClickListener(v -> {
            showNextWord();
        });

        // 카드 컨테이너 클릭 이벤트 추가 (카드 뒤집기)
        findViewById(R.id.cardContainer).setOnClickListener(v -> flipCard());

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

    }

    /**
     * 데이터베이스가 비어 있는지 확인
     */
    private boolean isDatabaseEmpty() {
        Cursor cursor = repository.getAllWords();
        boolean isEmpty = !cursor.moveToFirst();
        cursor.close();
        return isEmpty;
    }

    /**
     * 샘플 데이터 삽입
     */
    private void insertSampleData() {
        repository.insertWord("therapy", "치료", "NOUN", "She received therapy for her anxiety.");
        repository.insertWord("solution", "해결책", "NOUN", "We need a solution to this problem.");
        repository.insertWord("practice", "연습", "NOUN", "Practice makes perfect.");
        repository.insertWord("motivation", "동기", "NOUN", "She found her motivation to succeed.");
    }

    /**
     * 단어 데이터를 Cursor로 로드
     */
    private void loadWordCursor() {
        wordCursor = repository.getAllWords();
        if (!wordCursor.moveToFirst()) {
            throw new IllegalStateException("데이터베이스에 단어가 없습니다.");
        }
    }

    /**
     * 현재 단어를 UI에 표시
     */
    private void updateWordDisplay() {
        if (wordCursor != null && wordCursor.moveToPosition(currentWordIndex)) {
            String word = wordCursor.getString(wordCursor.getColumnIndexOrThrow("word"));
            String meaning = wordCursor.getString(wordCursor.getColumnIndexOrThrow("meaning"));
            String pronunciation = wordCursor.getString(wordCursor.getColumnIndexOrThrow("partofspeech"));
            String example = wordCursor.getString(wordCursor.getColumnIndexOrThrow("example"));

            wordText.setText(word);
            wordMeaning.setText(meaning);
            wordPartofspeech.setText(pronunciation);
            exampleSentence.setText(example);

            // 진행 상황 업데이트
            progressText.setText((currentWordIndex + 1) + " / " + wordCursor.getCount());
        }
    }

    /**
     * 이전 단어로 이동
     */
    private void showPreviousWord() {
        if (currentWordIndex > 0) {
            currentWordIndex--;
            resetCardState(); // 카드를 항상 앞면으로 초기화
            updateWordDisplay();
        }
    }

    /**
     * 다음 단어로 이동
     */
    private void showNextWord() {
        if (currentWordIndex < wordCursor.getCount() - 1) {
            currentWordIndex++;
            resetCardState(); // 카드를 항상 앞면으로 초기화
            updateWordDisplay();
        }
    }

    /**
     * 카드 상태 초기화 (항상 앞면으로 돌아감)
     */
    private void resetCardState() {
        cardFront.setRotationY(0); // 앞면 초기화
        cardBack.setRotationY(0); // 뒷면 초기화
        cardFront.setVisibility(View.VISIBLE); // 앞면 표시
        cardBack.setVisibility(View.GONE); // 뒷면 숨기기
        isFrontVisible = true; // 상태를 앞면으로 설정

    }

    /**
     * 카드 뒤집기
     */
    private void flipCard() {
        AnimatorSet frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flip_out);
        AnimatorSet backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flip_in);

        if (isFrontVisible) {
            // 앞면에서 뒷면으로
            frontAnim.setTarget(cardFront);
            backAnim.setTarget(cardBack);
            frontAnim.start();
            backAnim.start();
            cardFront.setVisibility(View.GONE);
            cardBack.setVisibility(View.VISIBLE);
        } else {
            // 뒷면에서 앞면으로
            frontAnim.setTarget(cardBack);
            backAnim.setTarget(cardFront);
            frontAnim.start();
            backAnim.start();
            cardBack.setVisibility(View.GONE);
            cardFront.setVisibility(View.VISIBLE);
        }
        isFrontVisible = !isFrontVisible;
    }
}
