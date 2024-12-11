package org.techtwon.quizgame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 3;

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "question TEXT," +
                "option1 TEXT," +
                "option2 TEXT," +
                "option3 TEXT," +
                "option4 TEXT," +
                "correctOption INTEGER)");

        // 샘플 데이터 삽입
        db.execSQL("INSERT INTO questions (question, option1, option2, option3, option4, correctOption) VALUES " +
                "('유의어가 아닌 것은?', 'important', 'significant', 'essential', 'minute', 4)," +
                "('유의어가 아닌 것은?', 'manipulate', 'control', 'obfuscate', 'shape', 3)," +
                "('유의어가 아닌 것은?', 'affordable', 'abundant', 'available', 'appropriate', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }

    public Cursor getQuestionById(int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM questions WHERE id = ?", new String[]{String.valueOf(questionId)});
    }
}
