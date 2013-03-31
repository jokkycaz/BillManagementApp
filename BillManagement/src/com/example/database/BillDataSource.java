package com.example.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.billmanagement.Bill;
import com.example.billmanagement.BillManagementApp;
import com.example.database.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BillDataSource {
	
	private static BillDataSource INSTANCE;
    
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    
    private String[] allBills = { 
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_BILL_NAME,
            DatabaseHelper.COLUMN_BILL_AMOUNT,
            DatabaseHelper.COLUMN_BILL_DUE_DATE,
            DatabaseHelper.COLUMN_BILL_AMMOUNT_PAID,
            DatabaseHelper.COLUMN_BILL_NOTE
    };
    
    private BillDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    /** 
     * Singleton
     * Also opens the database.
     * @return The only instance
     */
    public static BillDataSource getInstance() {
    	if (INSTANCE == null) {
    		INSTANCE = new BillDataSource(BillManagementApp.getContext());
    		INSTANCE.open();
    	}
    	return INSTANCE;
    }
    
    public void open() throws SQLException{
    	if (database == null) {
    		database = dbHelper.getWritableDatabase();
    	}
    }
    
    public void close() {
        dbHelper.close();
    }
    
    public Bill createBill(String name, Double amount, Date dueDate) {
        return createBill(name, amount, dueDate, 0.0, "");
    }
    
    public Bill createBill(String name, Double amount, Date dueDate, String note) {
        return createBill(name, amount, dueDate, 0.0, note);
    }
    
    public Bill createBill(String name, Double amount, Date dueDate, Double amountPaid, String note) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BILL_NAME, name);
        values.put(DatabaseHelper.COLUMN_BILL_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_BILL_DUE_DATE, dueDate.getTime());
        values.put(DatabaseHelper.COLUMN_BILL_AMMOUNT_PAID, amountPaid);
        values.put(DatabaseHelper.COLUMN_BILL_NOTE, note);
        
        long insertId = database.insert(DatabaseHelper.TABLE_BILLS, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_BILLS,
            allBills, DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Bill newBill = cursorToBill(cursor);
        cursor.close();
        return newBill;
    }
    
    public void deleteBill(Bill bill) {
        long id = bill.getId();
        Log.d("BillDataSource","Bill delete with id: " + id);
        database.delete(DatabaseHelper.TABLE_BILLS, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }
    
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<Bill>();
                
        Cursor cursor = database.query(DatabaseHelper.TABLE_BILLS, allBills, null, null, null, null, DatabaseHelper.COLUMN_BILL_DUE_DATE);    
        
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Bill bill = cursorToBill(cursor);
            bills.add(bill);
            cursor.moveToNext();
        }
        cursor.close();
        return bills;
    }
    
    private Bill cursorToBill(Cursor cursor) {
        Bill bill = new Bill();
        bill.setId(cursor.getLong(0));
        bill.setBillName(cursor.getString(1));
        bill.setBillAmount(cursor.getDouble(2));
        bill.setBillDueDate(cursor.getLong(3));
        bill.setBillAmountPaid(cursor.getDouble(4));
        bill.setBillNote(cursor.getString(5));
        return bill;
    }
}
