package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class mydatabase extends SQLiteOpenHelper {
    //variable to store database name
    private static final String dbname = "Expense";
    private  static  final Integer dbversion = 1;
    private static  final  String tbname = "registration";
    private  static  final  String reg_id = "u_id";
    private  static  final  String reg_email = "email";
    private  static  final  String reg_password = "password";
    private  static  final  String reg_occupation = "occupation";







    //constructor
    public mydatabase( Context context) {
        super(context, dbname, null, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + tbname +
                "(" + reg_id + "INTEGER PRIMARY KEY AUTOINCREMENT," + reg_email + "TEXT," + reg_password + "TEXT," + reg_occupation + "TEXT" + ")");

        //sqLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+tbname );
        onCreate(sqLiteDatabase);

    }
    public void  addregistration (String email , String password , String occupation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(reg_email , email);
        values.put(reg_password , password);
        values.put(reg_occupation , occupation);


        db.insert(tbname , null, values);
    }
}
