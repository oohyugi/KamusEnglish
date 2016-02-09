package com.kamus.kamusenglish;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by koba on 11/23/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbkamusenglish";
    public static String TABLE_NAME = "kamus";
    public static String FIELD_KATA = "kata";
    public static String FIELD_ARTI = "arti";
    public static String FIELD_ID = "_id";
    public static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE = "create table " + TABLE_NAME + " (_id integer primary key autoincrement, " +
            "kata text not null, " +
            "arti text not null)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXIST " +TABLE_NAME);
        onCreate(db);
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS kamus");
        db.execSQL("CREATE TABLE if not exists kamus (_id INTEGER PRIMARY KEY AUTOINCREMENT, kata TEXT, arti TEXT);");

    }
}
