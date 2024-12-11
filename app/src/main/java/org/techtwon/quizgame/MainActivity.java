package org.techtwon.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 각 점수대별 학습 버튼 ID와 연동
        Map<Integer, String> buttonScoreMap = new HashMap<>();
        buttonScoreMap.put(R.id.learn_600_below, "700점 이하");
        buttonScoreMap.put(R.id.learn_600_700, "700-800점");
        buttonScoreMap.put(R.id.learn_700_800, "800-900점");
        buttonScoreMap.put(R.id.learn_800_900, "990점");

        // 버튼 클릭 리스너 설정
        for (Map.Entry<Integer, String> entry : buttonScoreMap.entrySet()) {
            Integer buttonId = entry.getKey();
            String scoreRange = entry.getValue();

            Button button = findViewById(buttonId);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("scoreRange", scoreRange);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}