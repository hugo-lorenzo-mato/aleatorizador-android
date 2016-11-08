package com.hugolm.aleatorizadorapp;

/**
 * Created by hugo on 5/11/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class DataBaseManager {
    private SQLiteDatabase sqlDB;
    // Static por si tenemos más versiones
    static final String DBName = "RandomDecisions";
    static final String TableName = "Categories";
    static final String ColIdCategories = "id";
    static final String ColNameCategories = "Category";
    static final int DBVersion = 1;
    //Create table
    static final String CreateTable = "CREATE TABLE IF NOT EXISTS " + TableName
            + "(" + ColIdCategories + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ColNameCategories + " TEXT NOT NULL);";

    // Creamos una nueva tabla para las subcategorías

    static final String TableNameSub = "Subcategories";
    static final String ColParentSubCategories = "ParentCategory";
    static final String ColNameSubCategories = "SubCategory";

    static final String CreateTableSub = "CREATE TABLE IF NOT EXISTS " + TableNameSub
            + "(" + ColNameSubCategories + " TEXT NOT NULL,"
            + ColParentSubCategories + " TEXT NOT NULL);";

    // static porque necesito una única versión de esta clase para todas las instancias
    static class DBhelper extends SQLiteOpenHelper{

        Context context;

        DBhelper(Context context){
            //Si la Base de datos no está disponible, crea una
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(CreateTable);
            sqLiteDatabase.execSQL(CreateTableSub);
            Toast.makeText(context, "DB created", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableNameSub);

            onCreate(sqLiteDatabase);

        }
    }


    public DataBaseManager(Context context) {
        DBhelper db = new DBhelper(context);
        sqlDB = db.getWritableDatabase();
    }

    public long Insert(ContentValues values){
        long id;
        id = sqlDB.insert(TableName,"",values);
        // 0 si falla
        return id;
    }

    public long InsertSub(ContentValues values){
        long id;
        id = sqlDB.insert(TableNameSub,"",values);
        // 0 si falla
        return id;
    }

    //select username,Password from Logins where ID=1
    public Cursor Query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder){

        SQLiteQueryBuilder qb= new SQLiteQueryBuilder();
        qb.setTables(TableName);

        Cursor cursor=qb.query(sqlDB,Projection,Selection,SelectionArgs,null,null,SortOrder);
        return cursor;
    }

    public Cursor QuerySub(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder){

        SQLiteQueryBuilder qb= new SQLiteQueryBuilder();
        qb.setTables(TableNameSub);

        Cursor cursor=qb.query(sqlDB,Projection,Selection,SelectionArgs,null,null,SortOrder);
        return cursor;
    }

    public int delete(String Selection, String[] SelectionArgs){
        int result = sqlDB.delete(TableName, Selection, SelectionArgs);
        return result;
    }

    public int Update(ContentValues values, String Selection, String[] SelectionArgs){
        int result =  sqlDB.update(TableName, values, Selection, SelectionArgs);
        return result;
    }

}