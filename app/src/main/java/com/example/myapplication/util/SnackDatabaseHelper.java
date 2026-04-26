package com.example.myapplication.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.model.Snack;

import java.util.ArrayList;

public class SnackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cinefast_snacks.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "snacks";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image";
    private static final String COL_DESCRIPTION = "description";

    public SnackDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PRICE + " INTEGER NOT NULL, " +
                COL_IMAGE + " TEXT NOT NULL, " +
                COL_DESCRIPTION + " TEXT NOT NULL)";
        db.execSQL(createTable);

        // Insert initial snack data
        insertSnack(db, "Popcorn", "Large / Buttered", 8, "snack_popcorn");
        insertSnack(db, "Nachos", "With Cheese Dip", 8, "snack_nachos");
        insertSnack(db, "Soft Drink", "Large / Any Flavor", 6, "snack_drink");
        insertSnack(db, "Candy Mix", "Assorted Candies", 6, "snack_candy");
        insertSnack(db, "Hot Dog", "With Mustard", 7, "snack_hotdog");
    }

    private void insertSnack(SQLiteDatabase db, String name, String description, int price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        db.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Snack> getAllSnacks() {
        ArrayList<Snack> snacks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRICE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));
                snacks.add(new Snack(name, description, price, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return snacks;
    }
}
