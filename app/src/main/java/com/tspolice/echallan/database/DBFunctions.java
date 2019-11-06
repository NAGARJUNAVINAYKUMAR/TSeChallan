package com.tspolice.echallan.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBFunctions {

    private static DBFunctions mDbFunctions;
    private DBHelper mDbHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public static DBFunctions getInstance(final Context context) {
        if (mDbFunctions == null) {
            mDbFunctions = new DBFunctions(context);
        }
        return mDbFunctions;
    }

    private DBFunctions(Context context) {
        mDbHelper = DBHelper.getInstance(context);
    }

    public Cursor getWheelerMaster() {
        mSqLiteDatabase = mDbHelper.getReadableDatabase();
        return mSqLiteDatabase.rawQuery("SELECT WHEELER_CODE, WHEELER_NAME FROM WHEELER_TABLE ", null);
    }
}
