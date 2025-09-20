package com.raz.arnotebook;
// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_TITTLE = "TITTLE";
    public static final String KEY_MSG = "MSG";
    public static final String KEY_SECIND = "SECIND";


    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_TITTLE = 1;
    public static final int COL_MSG= 2;
    public static final int COL_SECIND = 3;

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_TITTLE, KEY_MSG, KEY_SECIND};

    // DB info: it's TITTLE, and the table we are using (just one).
    public static final String DATABASE_NAME = "ARNTBKINSDB";
    public static final String DATABASE_TABLE = "ARMASTERFL";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SQL =
            /*
             * CHANGE 2:
             */
            // TODO: Place your fields here!
            // + KEY_{...} + " {type} not null"
            //	- Key is the column TITTLE you created above.
            //	- {type} is one of: text, integer, real, blob
            //		(http://www.sqlite.org/datatype3.html)
            //  - "not null" means it is a required field (must be given a value).
            // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITTLE + " text not null unique, "
                    + KEY_MSG + " text not null, "
                    + KEY_SECIND + " text not null"

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private final DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow( String txtittle, String txtmsg, String  txtsecind)
    {
        /*
         * CHANGE 3:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITTLE, txtittle);
        initialValues.put(KEY_MSG, txtmsg);
        initialValues.put(KEY_SECIND, txtsecind);


        // Insert it into the database.
        return db.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String TITTLE, String MSG, String SECIND)
    {
        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         */
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

    // Get a specific row (by rowId)
    public String getRecord(String polid)
    {
        String where = KEY_TITTLE + "='" + polid+"'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        String policynm ;
        c.moveToFirst();
        if (c != null)
        {
            policynm = c.getString(DBAdapter.COL_TITTLE);
            //	Toast.makeText(DBAdapter.this, "Policy No =...." + policynm.toString(), Toast.LENGTH_LONG).show());
        }
        else
        {
            policynm = "";
        }
        c.close();
        return policynm;
    }

	/*	public void exportDB(){
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source=null;
		FileChannel destination=null;
		String currentDBPath = "/data/"+ " com.raz.arnotebook" +"/databases/"+ DATABASE_NAME;
		String backupDBPath = DATABASE_NAME + "bk";
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
		} catch(IOException e) {
			e.printStackTrace();
			Toast.makeText(context, "DB Not Exported!" + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
*/

    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }



}