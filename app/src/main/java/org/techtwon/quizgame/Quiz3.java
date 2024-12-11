package org.techtwon.quizgame;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz3 extends AppCompatActivity {

    private TextView questionTextView, answerTextView, resultTextView;
    private Button submitButton, deleteButton, prevButton;
    private GridLayout blockLayout;

    private List<Pair<String, String>> quizData = Arrays.asList(
            new Pair<>("과소평가하다", "UNDERESTIMATE"),
            new Pair<>("(감긴 것을) 풀다", "UNWIND"),
            new Pair<>("지속[유지] 가능성", "SUSTAINABILITY"),
            new Pair<>("처벌하다", "PENALIZE"),
            new Pair<>("밀접한 관계, 영향", "IMPLICATION")
    );

    private int currentIndex = 0; // 현재 문제의 인덱스
    private String correctAnswer; // 현재 문제의 정답
    private String currentAnswer = ""; // 사용자가 입력한 단어
    private List<Button> usedBlocks = new ArrayList<>(); // 선택된 블록 버튼들
    private Map<Button, Integer> blockClickCount = new HashMap<>(); // 블록별 남은 클릭 횟수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);

        // 뷰 초기화
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        resultTextView = findViewById(R.id.resultTextView);
        submitButton = findViewById(R.id.submitButton);
        deleteButton = findViewById(R.id.deleteButton);
        prevButton = findViewById(R.id.prevButton);
        blockLayout = findViewById(R.id.blockLayout);

        // 제출 버튼 클릭 이벤트
        submitButton.setOnClickListener(v -> checkAnswer());

        // 지우기 버튼 클릭 이벤트
        deleteButton.setOnClickListener(v -> removeLastLetter());

        // 뒤로가기 버튼
        prevButton.setOnClickListener(v -> moveToPreviousQuestion()); // 앱 종료 또는 이전 화면으로 이동

        // 첫 번째 문제 표시
        showQuestion();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void showQuestion() {
        // 현재 문제 데이터 가져오기
        String question = quizData.get(currentIndex).first; // 한글 뜻
        correctAnswer = quizData.get(currentIndex).second; // 영어 정답

        // 화면에 표시
        questionTextView.setText("한국어 뜻: " + question);
        currentAnswer = "";
        answerTextView.setText("");
        usedBlocks.clear();
        blockClickCount.clear();

        // 블록 로드
        loadBlocks(correctAnswer);
    }

    private void loadBlocks(String word) {
        // 정답 스펠링 등장 횟수를 계산
        Map<Character, Integer> letterCounts = new HashMap<>();
        for (char c : word.toCharArray()) {
            if (letterCounts.containsKey(c)) {
                letterCounts.put(c, letterCounts.get(c) + 1);
            } else {
                letterCounts.put(c, 1); // 등장 횟수 1로 초기화
            }
        }

        // 랜덤 알파벳 추가 (정답 제외)
        int additionalLettersCount = 5; // 추가할 랜덤 문자 수
        List<Character> allAlphabets = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!letterCounts.containsKey(c)) { // 정답에 포함되지 않은 문자만 추가
                allAlphabets.add(c);
            }
        }

        // 추가 문자를 랜덤하게 선택
        Collections.shuffle(allAlphabets);
        List<Character> letters = new ArrayList<>(letterCounts.keySet()); // 중복 제거된 정답 문자
        for (int i = 0; i < additionalLettersCount && i < allAlphabets.size(); i++) {
            letters.add(allAlphabets.get(i)); // 정답 제외된 랜덤 문자 추가
        }

        // 버튼 섞기
        Collections.shuffle(letters);

        // 블록 버튼 추가
        blockLayout.removeAllViews();
        blockClickCount.clear(); // 클릭 횟수 초기화
        for (char letter : letters) {
            Button blockButton = new Button(this);
            blockButton.setText(String.valueOf(letter));
            blockButton.setTextSize(18); // 텍스트 크기
            blockButton.setMinWidth(150); // 버튼 최소 너비
            blockButton.setMinHeight(150); // 버튼 최소 높이
            blockButton.setPadding(8, 8, 8, 8); // 버튼 여백
            blockButton.setAllCaps(true); // 대문자
            blockButton.setOnClickListener(this::onBlockClick);

            // 버튼 레이아웃 설정
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // GridLayout에서 열에 맞게 버튼 크기 설정
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // 열 비율 설정
            blockButton.setLayoutParams(params);

            blockLayout.addView(blockButton);

            // 블록 클릭 횟수 설정 (정답에서 등장 횟수만큼 허용)
            int clickCount = letterCounts.containsKey(letter) ? letterCounts.get(letter) : 1; // 기본값 1
            blockClickCount.put(blockButton, clickCount);
        }
    }

    private void onBlockClick(View view) {
        Button clickedButton = (Button) view;

        // 클릭 가능 횟수를 확인하고 감소
        int remainingClicks = blockClickCount.containsKey(clickedButton) ? blockClickCount.get(clickedButton) : 0;
        if (remainingClicks > 0) {
            // 클릭 가능한 경우, 정답에 글자를 추가
            currentAnswer += clickedButton.getText().toString();
            answerTextView.setText(currentAnswer);

            // 클릭 횟수 감소
            remainingClicks--;
            blockClickCount.put(clickedButton, remainingClicks);

            // 클릭 가능 횟수가 0이 되면 버튼 비활성화
            if (remainingClicks == 0) {
                clickedButton.setEnabled(false);
            }
        }
    }




    private void removeLastLetter() {
        if (!currentAnswer.isEmpty()) {
            // 마지막 글자 제거
            char lastLetter = currentAnswer.charAt(currentAnswer.length() - 1);
            currentAnswer = currentAnswer.substring(0, currentAnswer.length() - 1);
            answerTextView.setText(currentAnswer);

            // 비활성화된 블록 중 다시 활성화
            for (Map.Entry<Button, Integer> entry : blockClickCount.entrySet()) {
                Button button = entry.getKey();
                if (button.getText().toString().equals(String.valueOf(lastLetter))) {
                    int remainingClicks = entry.getValue() + 1;
                    blockClickCount.put(button, remainingClicks);
                    button.setEnabled(true); // 버튼 다시 활성화
                    break;
                }
            }
        }
    }




    private void checkAnswer() {
        if (currentAnswer.equalsIgnoreCase(correctAnswer)) {
            resultTextView.setText("정답입니다!");
            resultTextView.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(this::moveToNextQuestion, 3000);

        } else {
            resultTextView.setText("틀렸습니다. 정답은 '" + correctAnswer + "'입니다.");
            resultTextView.setVisibility(View.VISIBLE);

            // 일정 시간 후 다음 문제로 이동
            new android.os.Handler().postDelayed(this::moveToNextQuestion, 3000);
        }
    }

    private void moveToNextQuestion() {
        if (currentIndex >= quizData.size() - 1){
            resultTextView.setText("퀴즈가 끝났습니다");
            resultTextView.setVisibility(View.VISIBLE);
        } else{
            currentIndex++;
            resultTextView.setVisibility(View.GONE);
            showQuestion();
        }
    }

    private void moveToPreviousQuestion(){
        if(currentIndex > 0) {
            currentIndex--;
            resultTextView.setVisibility(View.GONE);
            showQuestion();
        }
    }
}



