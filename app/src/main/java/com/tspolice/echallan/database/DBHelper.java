package com.tspolice.echallan.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.tspolice.echallan.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private String DB_PATH;
    private SQLiteDatabase mSQLiteDatabase;
    @SuppressLint("StaticFieldLeak")
    private static DBHelper mDbHelper = null;

    public static synchronized DBHelper getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DBHelper(context);
        }
        return mDbHelper;
    }

    private DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.mContext = context;
        DB_PATH = mContext.getDatabasePath(Constants.DB_NAME).toString();

        createDatabase();
    }

    private void createDatabase() {
        boolean isDbExist = checkDatabase();
        if (!isDbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error(e.getMessage());
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDatabase = null;
        try {
            checkDatabase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (checkDatabase != null) {
            checkDatabase.close();
        }
        return checkDatabase != null;
    }

    private void copyDatabase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(Constants.DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close all the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
        }
        super.close();
    }
}
