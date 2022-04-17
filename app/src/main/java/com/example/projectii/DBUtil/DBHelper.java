package com.example.projectii.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projectii.model.LearnerModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_project2";
    SQLiteDatabase db = getWritableDatabase();

    public DBHelper(Context c) {
        super(c, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" +
                " learners" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,fullname VARCHAR(255),email VARCHAR(255)," +
                " address VARCHAR(255),phone VARCHAR(255),password VARCHAR(255),reg_date DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS learners");
        onCreate(sqLiteDatabase);
    }
    public void addLearner(LearnerModel l) {
        ContentValues cv = new ContentValues();
        cv.put("fullname",l.getFullName());
        cv.put("email",l.getEmail());
        cv.put("address",l.getAddress());
        cv.put("phone",l.getPhone());
        cv.put("password",l.getPassword());
        db.insert("learners",null,cv);
    }
    public ArrayList<LearnerModel> retrieveLearner() {
        ArrayList<LearnerModel> data = new ArrayList<>();
        String sql = "SELECT * FROM Learners";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                LearnerModel st = new LearnerModel();
                st.setFullName(cursor.getString(1));
                st.setEmail(cursor.getString(2));
                st.setAddress(cursor.getString(3));
                st.setPhone(cursor.getString(4));
                st.setPassword(cursor.getString(4));
                data.add(st);
            }while (cursor.moveToNext());
        }
        return data;
    }
}
