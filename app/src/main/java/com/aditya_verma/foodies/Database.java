package com.aditya_verma.foodies;

//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.io.InputStream;
//import java.sql.Blob;
//import java.util.ArrayList;

//public class Database extends SQLiteOpenHelper {
//
//    private	static final int DATABASE_VERSION =	5;
//    private	static final String	DATABASE_NAME = "food";
//    private	static final String TABLE_FOOD = "food_table";
//
//    private static final String COLUMN_ID = "_id";
//    private static final String COLUMN_FLAVOUR = "flavour";
//    private static final String COLUMN_PRICE = "price";
//    private static final String COLUMN_IMAGE = "image";
//
//
//    public Database(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String	CREATE_CONTACTS_TABLE = "CREATE	TABLE " + TABLE_FOOD+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_FLAVOUR + " TEXT," + COLUMN_PRICE + " INTEGER"  +COLUMN_IMAGE + "BLOB" + ")";
//        db.execSQL(CREATE_CONTACTS_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
//        onCreate(db);
//    }
//
//
//    public void addModel(Model model){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_FLAVOUR, model.getFlavour());
//        values.put(COLUMN_PRICE, model.getPrice());
//        values.put(COLUMN_IMAGE, model.getImage());
//         db.insert(TABLE_FOOD, null, values);
//    }
//
//
//    public ArrayList<Model> get_all_data(){
//        String sql = "select * from " + TABLE_FOOD;
//        SQLiteDatabase db = this.getReadableDatabase();
//        ArrayList<Model> storeModel = new ArrayList<>();
//       // Cursor cursor = db.rawQuery(sql, null);
//        Cursor cursor = db.rawQuery(sql,null);
//        if(cursor.moveToFirst()){
//            do {
//                int id = cursor.getInt(0);
//                String flavour = cursor.getString(1);
//                String price = cursor.getString(2);
//                byte[] image = cursor.getBlob(3);
//
//                storeModel.add(new Model(id,flavour,price,image));
//
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//        return storeModel;
//    }
//
//    public void updateModel(Model model){
//        ContentValues values = new ContentValues();
//       // values.put(COLUMN_IMAGE,model.getB());
//        values.put(COLUMN_FLAVOUR, model.getFlavour());
//        values.put(COLUMN_PRICE, model.getPrice());
//        values.put(COLUMN_IMAGE,model.getImage());
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.update(TABLE_FOOD, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(model.getId())});
//    }
//
////    public Contacts findContacts(String name){
////        String query = "Select * FROM "	+ TABLE_CONTACTS + " WHERE " + COLUMN_NAME + " = " + "name";
////        SQLiteDatabase db = this.getWritableDatabase();
////        Contacts contacts = null;
////        Cursor cursor = db.rawQuery(query,	null);
////        if	(cursor.moveToFirst()){
////            int id = Integer.parseInt(cursor.getString(0));
////            String contactsName = cursor.getString(1);
////            String contactsNo = cursor.getString(2);
////            contacts = new Contacts(id, contactsName, contactsNo);
////        }
////        cursor.close();
////        return contacts;
////    }
//
//    public void deleteModel(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_FOOD, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
//    }
//
//}

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper{

    private	static final int DATABASE_VERSION =	5;
    private	static final String	DATABASE_NAME = "food";
    private	static final String TABLE_FOOD = "food_table";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FLAVOUR = "flavour";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IMAGE = "image";

//    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
//
    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_CONTACTS_TABLE = "CREATE	TABLE " + TABLE_FOOD+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_FLAVOUR + " TEXT," + COLUMN_PRICE + " INTEGER," + COLUMN_QUANTITY + " INTEGER,"  +COLUMN_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
       onCreate(db);
    }

    public void addModel(Model model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FLAVOUR, model.getFlavour());
        values.put(COLUMN_PRICE, model.getPrice());
        values.put(COLUMN_QUANTITY ,model.getQuantity());
        values.put(COLUMN_IMAGE, model.getImage());

            db.insert(TABLE_FOOD,null, values);
    }


    public ArrayList<Model> get_all_data(){
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

    public void delete_all_Model(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD,null, null);
        db.close();
    }

//    public void delete_poora_Model(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_FOOD,null, null);
//        db.close();
//    }
}