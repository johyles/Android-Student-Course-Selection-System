package com.check_course;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 12044 on 2020/3/26.
 */

public class dbHelper extends SQLiteOpenHelper{

    String TB_Name = "userinfo";
    String TB2_Name = "courseinfo";
    String TB3_Name = "courseStudentinfo";

    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public final String Creat_User="create table if not exists "+TB_Name
            +"(uid integer primary key autoincrement,"
            +"uname varchar,"
            +"pwd varchar,"
            +"age integer"
            +")";

    public final String Creat_Course="create table if not exists "+TB2_Name
            +"(id Integer primary key autoincrement,"
            +"cid varchar,"
            +"cname varchar,"
            +"cscore Integer"
            +")";

    public final String Creat_CourseStudent="create table if not exists "+TB3_Name
            +"(id Integer primary key autoincrement,"
            +"cid varchar,"
            +"cname varchar,"
            +"cscore Integer"
            +")";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Creat_User);
        sqLiteDatabase.execSQL(Creat_Course);
        sqLiteDatabase.execSQL(Creat_CourseStudent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TB_Name);
        sqLiteDatabase.execSQL("drop table if exists "+TB2_Name);
        sqLiteDatabase.execSQL("drop table if exists "+TB3_Name);
        onCreate(sqLiteDatabase);
    }
}
