package com.aditya_verma.foodies;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.aware.PublishConfig;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class Database extends SQLiteOpenHelper{

    private	static final int DATABASE_VERSION =	5;
    private	static final String	DATABASE_NAME = "food";
    private	static final String TABLE_FOOD = "food_table";
    private	static final String TABLE_FOOD2 = "food_table_history";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FLAVOUR = "flavour";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IMAGE = "image";

    private static final String COLUMN_ID2 = "_id";
    private static final String COLUMN_FLAVOUR2 = "flavour";
    private static final String COLUMN_PRICE2 = "price";
    private static final String COLUMN_QUANTITY2 = "quantity";
    private static final String COLUMN_DATE = "date";

     public static ContentValues values;
    public static SQLiteDatabase db;


    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_CONTACTS_TABLE = "CREATE	TABLE " + TABLE_FOOD+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_FLAVOUR + " TEXT," + COLUMN_PRICE + " INTEGER," + COLUMN_QUANTITY + " INTEGER,"  +COLUMN_IMAGE + " BLOB" + ")";
        String	CREATE_CONTACTS_TABLE2 = "CREATE TABLE " + TABLE_FOOD2+ "(" + COLUMN_ID2 + " INTEGER PRIMARY KEY," + COLUMN_FLAVOUR2 + " TEXT," + COLUMN_PRICE2 + " INTEGER," +COLUMN_QUANTITY2 + " INTEGER," +COLUMN_DATE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD2);
       onCreate(db);
    }

    public void addModel(Model model){

         db = this.getWritableDatabase();
         values = new ContentValues();
        values.put(COLUMN_FLAVOUR, model.getFlavour());
        values.put(COLUMN_PRICE, model.getPrice());
        values.put(COLUMN_QUANTITY ,model.getQuantity());
        values.put(COLUMN_IMAGE, model.getImage());

        db.insert(TABLE_FOOD,null, values);
    }

    public void addModelHistory(String date){

        String sql = "select * from " + TABLE_FOOD ;
        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor = db2.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do {
                ContentValues values2 = new ContentValues();
                values2.put(COLUMN_FLAVOUR2, cursor.getString(1) );
                values2.put(COLUMN_PRICE2, cursor.getString(2));
                values2.put(COLUMN_QUANTITY2 , cursor.getString(3));
                values2.put(COLUMN_DATE ,  date);

                db2.insert(TABLE_FOOD2,null, values2);

            }

            while (cursor.moveToNext());
        }

        cursor.close();

    }


    public  ArrayList<Model> get_all_data(){

        String sql = "select * from " + TABLE_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Model> storeModel = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String flavour = cursor.getString(1);
                String price = cursor.getString(2);
                String quantity = cursor.getString(3);
                byte[] image = cursor.getBlob(4);
                storeModel.add(new Model(id,flavour,price,quantity,image));
            }

            while (cursor.moveToNext());
        }

        cursor.close();
        return storeModel;

    }


    public  ArrayList<Model> get_all_data_History(){

        String sql2 = "select * from " + TABLE_FOOD2;
        SQLiteDatabase db2 = this.getReadableDatabase();
        ArrayList<Model> storeModel2 = new ArrayList<>();
        Cursor cursor2 = db2.rawQuery(sql2,null);
        if(cursor2.moveToFirst()){
            do {
                int id = cursor2.getInt(0);
                String flavour = cursor2.getString(1);
                String price = cursor2.getString(2);
                String quantity = cursor2.getString(3);
                String date = cursor2.getString(4);
               // byte[] image = cursor2.getBlob(4);
                storeModel2.add(new Model(id,flavour,price,quantity,date));
            }

            while (cursor2.moveToNext());
        }

        cursor2.close();
        return storeModel2;

    }



    public Cursor get_poora_Data(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_FOOD, null);
        return cur;
   }


    public int get_count_cart(){
        String sql = "select * from " + TABLE_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public void updateModel(Model model){
        ContentValues values = new ContentValues();
        //values.put(COLUMN_IMAGE,model.getB());
        values.put(COLUMN_FLAVOUR, model.getFlavour());
        values.put(COLUMN_PRICE, model.getPrice());
        values.put(COLUMN_QUANTITY,model.getQuantity());
        values.put(COLUMN_IMAGE,model.getImage());
        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_FOOD, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(model.getId())});
    }

    public void deleteModel(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }

    public void deleteModelHistory(int id){
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.delete(TABLE_FOOD2, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }

    public void delete_all_Model(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD,null, null);
        db.close();
    }

}