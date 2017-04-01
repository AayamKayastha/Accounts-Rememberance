package com.example.aayamk.accountsrememberance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aayamk on 1/8/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context,"db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table details(sn integer primary key autoincrement, username text, password text, email text, extra text, site text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getData(String site,int position)
    {
        SQLiteDatabase db=getReadableDatabase();
        return db.query("details",null,"site =? and sn=?",new String[]{site,String.valueOf(position)},null,null,null);
    }
    public boolean newEntry(String userName, String password, String email,String extras,String site)
    {
        ContentValues contentValues=new ContentValues();
        SQLiteDatabase db=getWritableDatabase();
        contentValues.put("username",userName);
        contentValues.put("password",password);
        contentValues.put("email",email);
        contentValues.put("extra",extras);
        contentValues.put("site",site);
        long result=db.insert("details",null,contentValues);
        if (result!=-1)
            return true;
        else
            return false;
    }
    public Cursor getSites()
    {
        SQLiteDatabase db=getReadableDatabase();
        return db.query("details",new String[]{"sn","site"},null,null,null,null,null);
    }
}
