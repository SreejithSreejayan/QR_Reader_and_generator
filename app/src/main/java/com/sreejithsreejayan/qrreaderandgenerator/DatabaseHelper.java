package com.sreejithsreejayan.qrreaderandgenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="QRCodeReaderandGenerator";
    private static final int DB_VERSION=1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLCommand="CREATE TABLE scannerhistory(id integer primary key autoincrement,qr_encoded_data text,date_and_time text);";
        db.execSQL(SQLCommand);
//        String SQLCommand2="CREATE TABLE generatorhistory(id integer primary key autoincrement,qr_encoder_data text,date_and_time integer);";
//        db.execSQL(SQLCommand2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
