package com.maxwell.caploc.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Caploc.db";


    public MyDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String productsql = "CREATE TABLE Checklist_tbl " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "checklistid TEXT,"+
                "checklist TEXT,"+
                "availableqty TEXT,"+
                "status TEXT)";
        db.execSQL(productsql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Checklist_tbl");
        onCreate(db);
    }


}
