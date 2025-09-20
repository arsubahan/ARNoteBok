package com.raz.arnotebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLITEDB extends SQLiteOpenHelper {
    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /// //////////////////////////////////////////////////////////////////
    // For logging:

    // DB info: it's TITTLE, and the table we are using (just one).
    public static final String DATABASE_NAME = "ARNTBKINSDB";
    public static final String DATABASE_TABLE = "ARMASTERFL";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;

    private static final String TAG = "SQLITEDB";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITTLE = "TITTLE";
    public static final String KEY_MSG = "MSG";
    public static final String KEY_SECIND = "SECIND";


    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)

    //  public static final int COL_ROWID = 0;
    public static final int COL_TITTLE = 1;
    public static final int COL_MSG = 2;
    public static final int COL_SECIND = 3;

    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_TITTLE, KEY_MSG, KEY_SECIND};

    public SQLITEDB(@Nullable Context context) {
        super(context, DATABASE_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtbl = "CREATE TABLE " + DATABASE_TABLE
                + " (" + KEY_ROWID + " integer primary key autoincrement, "
                + KEY_TITTLE + " text not null unique, "
                + KEY_MSG + " text not null, "
                + KEY_SECIND + " text not null"
                // Rest  of creation:
                + ");";

        db.execSQL(createtbl);

    }// end of on create

    @Override
    public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading application's database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data!");
        // Destroy old database:
        _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        // Recreate new database:
        onCreate(_db);
    }// end of upgrade

    public boolean AddRecord(NoteBkModel noteBkModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITTLE, noteBkModel.getTitle());
        initialValues.put(KEY_MSG, noteBkModel.getDesc());
        initialValues.put(KEY_SECIND, noteBkModel.getSecind());

        // Insert it into the database.
        long insert = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();
        return insert != -1;
    }// end of add record

    public boolean updateRecord(long updid, NoteBkModel noteBkModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITTLE, noteBkModel.getTitle());
        initialValues.put(KEY_MSG, noteBkModel.getDesc());
        initialValues.put(KEY_SECIND, noteBkModel.getSecind());

        String whereClause = KEY_ROWID + " = " + updid;

       // " WHERE " + KEY_ROWID + " = " + noteBkModel.getId();
        // String[] whereArgs = {String.valueOf(noteBkModel.getId())};

        // Update the record where 'id' matches the provided id
        long update = db.update(DATABASE_TABLE, initialValues, whereClause,null);
       // long update = db.update(DATABASE_TABLE, initialValues, KEY_ROWID + " = ?",
         //       new String[] { String.valueOf(noteBkModel.getId()) });

        db.close();
        return update != -1;
    }



    // Change an existing row to be equal to new data.
  /*  public boolean updateRecord(long rowId, String TITTLE, String MSG, String SECIND)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         * /
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TITTLE, TITTLE);
        newValues.put(KEY_MSG, MSG);
        newValues.put(KEY_SECIND, SECIND);


        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }
    */

    public boolean deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        String queryString = "DELETE FROM " + DATABASE_TABLE ;
        Cursor cursor = db.rawQuery(queryString, null);
        if (!cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }


    public boolean deleteOne(NoteBkModel noteBkModel) {
        SQLiteDatabase db = getWritableDatabase();
        String queryString = "DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + noteBkModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if (!cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
    public List<NoteBkModel> getAll() {
        List<NoteBkModel> returnList = new ArrayList<>();
        String SelAllQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelAllQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int row_id = cursor.getInt(0);
                String row_title = cursor.getString(1);
                String row_desc = cursor.getString(2);
                String row_secind = cursor.getString(3);

                NoteBkModel noteBkModel = new NoteBkModel(row_id, row_title, row_desc, row_secind);
                returnList.add(noteBkModel);

            } while (cursor.moveToNext());

        } else {

        }// end of if movefirst
        cursor.close();
        db.close();
        return returnList;
    } // end of get all record

    // Get a specific row (by rowId)
    public String getRecord(int recid) {
        SQLiteDatabase db = this.getWritableDatabase();

        String where = KEY_ROWID + "='" + recid + "'";
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        String policynm;
        c.moveToFirst();
        if (c != null) {
            policynm = c.getString(DBAdapter.COL_TITTLE);
            //	Toast.makeText(DBAdapter.this, "Policy No =...." + policynm.toString(), Toast.LENGTH_LONG).show());
        } else {
            policynm = "";
        }
        c.close();
        return policynm;
    }


}// end of class


