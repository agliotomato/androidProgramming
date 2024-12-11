package org.techtwon.quizgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDatabaseHelper2 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz.db2";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE =
            "CREATE TABLE questions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "word TEXT, " +
                    "option1 TEXT, " +
                    "option2 TEXT, " +
                    "option3 TEXT, " +
                    "option4 TEXT, " +
                    "correctOption INTEGER)";

    public WordDatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

        // 샘플 데이터 삽입
        db.execSQL("INSERT INTO questions (word, option1, option2, option3, option4, correctOption) VALUES " +
                "('reluctantly', '마지못해', '유감스럽게도', '제대로', '고의로', 1)");
        db.execSQL("INSERT INTO questions (word, option1, option2, option3, option4, correctOption) VALUES " +
                "('competent', '결함이 있는', '자선의', '경쟁력 있는', '유능한', 4)");
        db.execSQL("INSERT INTO questions (word, option1, option2, option3, option4, correctOption) VALUES " +
                "('affordable', '조절 가능한', '가격이 적당한', '이용 가능한', '추가의', 2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }
}
