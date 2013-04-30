package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*public static final String TABLE_COMMENTS = "comments";
public static final String COLUMN_ID = "_id";
public static final String COLUMN_COMMENT = "comment";

private static final String DATABASE_NAME = "commments.db";
private static final int DATABASE_VERSION = 1;
*/

public class DatabaseHelper extends SQLiteOpenHelper {
      public static final String TABLE_BILLS = "bill";
      public static final String COLUMN_ID = "_id";
      public static final String COLUMN_BILL_NAME = "billName";
      public static final String COLUMN_BILL_AMOUNT = "billAmount";
      public static final String COLUMN_BILL_DUE_DATE = "billDueDate";
      public static final String COLUMN_BILL_AMMOUNT_PAID = "billAmoundPaid";
      public static final String COLUMN_BILL_NOTE = "billNote";

      private static final String DATABASE_NAME = "bills.db";
      private static final int DATABASE_VERSION = 1;
      
      /*public static final String TABLE_COMMENTS = "comments";
      public static final String COLUMN_ID = "_id";
      public static final String COLUMN_COMMENT = "comment";

      private static final String DATABASE_NAME = "commments.db";
      private static final int DATABASE_VERSION = 1;
      */
      
      private static final String DATABASE_CREATE = "CREATE TABLE "
              + TABLE_BILLS + " (" 
              + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
              + COLUMN_BILL_NAME + " TEXT NOT NULL, "
              + COLUMN_BILL_AMOUNT + " REAL NOT NULL, "
              + COLUMN_BILL_DUE_DATE + " INTEGER NOT NULL, "
              + COLUMN_BILL_AMMOUNT_PAID + " TEXT, "
              + COLUMN_BILL_NOTE + " TEXT);";
    
    public DatabaseHelper(Context context) {
    	super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
              "Upgrading database from version " + oldVersion + " to " + newVersion
              + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }  

}
