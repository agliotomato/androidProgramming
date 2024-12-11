package org.techtwon.quizgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private LinearLayout lcSection, part5Section, part6Section, part7Section;
    private TextView scoreRangeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lcSection = (LinearLayout) findViewById(R.id.LcSection);
        part5Section = findViewById(R.id.Part5Section);
        part6Section = findViewById(R.id.Part6Section);
        part7Section = findViewById(R.id.Part7Section);

        scoreRangeTextView = findViewById(R.id.scoreRangeTextView);

        // MainActivity에서 전달된 scoreRange 값 받기
        Intent intent = getIntent();
        String scoreRange = intent.getStringExtra("scoreRange");

        // 전달된 점수를 TextView에 표시
        if (scoreRange != null) {
            scoreRangeTextView.setText("선택한 점수 범위: " + scoreRange);
        } else {
            scoreRangeTextView.setText("점수 범위를 선택하지 않았습니다.");
        }

        TextView lcTextView = findViewById(R.id.LCTextView);
        lcTextView.setOnClickListener(v -> showSection(lcSection));

        TextView part5TextView = findViewById(R.id.part5TextView);
        part5TextView.setOnClickListener(v -> showSection(part5Section));

        TextView part6TextView = findViewById(R.id.part6TextView);
        part6TextView.setOnClickListener(v -> showSection(part6Section));

        TextView part7TextView = findViewById(R.id.part7TextView);
        part7TextView.setOnClickListener(v -> showSection(part7Section));

        ImageView quiz1Button = findViewById(R.id.Quiz5_1); // XML에 정의된 ID
        quiz1Button.setOnClickListener(v -> {
            Intent quiz1Intent = new Intent(SecondActivity.this, Quiz1.class);
            startActivity(quiz1Intent);
        });

        ImageView quiz2Button = findViewById(R.id.Quiz5_2); // XML에 정의된 ID
        quiz2Button.setOnClickListener(v -> {
            Intent quiz2Intent = new Intent(SecondActivity.this, Quiz2.class);
            startActivity(quiz2Intent);
        });

        ImageView quiz3Button = findViewById(R.id.Quiz5_3); // XML에 정의된 ID
        quiz3Button.setOnClickListener(v -> {
            Intent quiz3Intent = new Intent(SecondActivity.this, Quiz3.class);
            startActivity(quiz3Intent);
        });

        ImageView quiz4Button = findViewById(R.id.Quiz5_4); // XML에 정의된 ID
        quiz4Button.setOnClickListener(v -> {
            Intent quiz4Intent = new Intent(SecondActivity.this, Quiz4.class);
            startActivity(quiz4Intent);
        });


    }

    private void showSection(LinearLayout selectedSection){
        lcSection.setVisibility(View.GONE);
        part5Section.setVisibility(View.GONE);
        part6Section.setVisibility(View.GONE);
        part7Section.setVisibility(View.GONE);

        selectedSection.setVisibility(View.VISIBLE);
    }
}