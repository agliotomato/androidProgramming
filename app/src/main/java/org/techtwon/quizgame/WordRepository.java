package org.techtwon.quizgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordRepository {
    private final SQLiteDatabase db;

    public WordRepository(Context context) {
        WordDatabaseHelper dbHelper = new WordDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // 단어 삽입
    public void insertWord(String word, String meaning, String partofspeech, String example) {
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("meaning", meaning);
        values.put("partofspeech", partofspeech);
        values.put("example", example);
        db.insert("words", null, values);
    }

    // 모든 단어 가져오기
    public Cursor getAllWords() {
        return db.rawQuery("SELECT * FROM words", null); // 모든 단어를 가져오는 SQL 쿼리
    }
}


