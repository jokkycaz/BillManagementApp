package com.example.database;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Comment;

import com.example.billmanagement.Bill;
import com.example.database.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BillDataSource {
	
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
	
	public BillDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	public Bill createBill(String name, String amount, String dueDate) {
		String amountPaid = "";
		String note = "";
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.COLUMN_BILL_NAME, name);
	    values.put(DatabaseHelper.COLUMN_BILL_AMOUNT, amount);
	    values.put(DatabaseHelper.COLUMN_BILL_DUE_DATE, dueDate);
	    values.put(DatabaseHelper.COLUMN_BILL_AMMOUNT_PAID, amountPaid);
	    values.put(DatabaseHelper.COLUMN_BILL_NOTE, note);
	    
	    long insertId = database.insert(DatabaseHelper.TABLE_BILLS, null,
	        values);
	    Cursor cursor = database.query(DatabaseHelper.TABLE_BILLS,
	        allBills, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Bill newBill = cursorToBill(cursor);
	    cursor.close();
	    return newBill;
	  }
	public Bill createBill(String name, String amount, String dueDate, String note) {
		String amountPaid = "";
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.COLUMN_BILL_NAME, name);
	    values.put(DatabaseHelper.COLUMN_BILL_AMOUNT, amount);
	    values.put(DatabaseHelper.COLUMN_BILL_DUE_DATE, dueDate);
	    values.put(DatabaseHelper.COLUMN_BILL_AMMOUNT_PAID, amountPaid);
	    values.put(DatabaseHelper.COLUMN_BILL_NOTE, note);
	    
	    long insertId = database.insert(DatabaseHelper.TABLE_BILLS, null,
	        values);
	    Cursor cursor = database.query(DatabaseHelper.TABLE_BILLS,
	        allBills, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Bill newBill = cursorToBill(cursor);
	    cursor.close();
	    return newBill;
	  }
	public Bill createBill(String name, String amount, String dueDate, String amountPaid, String note) {
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.COLUMN_BILL_NAME, name);
	    values.put(DatabaseHelper.COLUMN_BILL_AMOUNT, amount);
	    values.put(DatabaseHelper.COLUMN_BILL_DUE_DATE, dueDate);
	    values.put(DatabaseHelper.COLUMN_BILL_AMMOUNT_PAID, amountPaid);
	    values.put(DatabaseHelper.COLUMN_BILL_NOTE, note);
	    
	    long insertId = database.insert(DatabaseHelper.TABLE_BILLS, null,
	        values);
	    Cursor cursor = database.query(DatabaseHelper.TABLE_BILLS,
	        allBills, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Bill newBill = cursorToBill(cursor);
	    cursor.close();
	    return newBill;
	  }
	public void deleteBill(Bill bill){
		long id = bill.getId();
		System.out.println("Bill delete with id: " + id);
		database.delete(DatabaseHelper.TABLE_BILLS, DatabaseHelper.COLUMN_ID + " = " + id, null);
	}
	public List<Bill> getAllBills(){
		List<Bill> bills = new ArrayList<Bill>();
				
		Cursor cursor = database.query(DatabaseHelper.TABLE_BILLS, allBills, null, null, null, null, null);	
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Bill bill = cursorToBill(cursor);
			bills.add(bill);
			cursor.moveToNext();
		}
		cursor.close();
		return bills;
	}
	
	private Bill cursorToBill(Cursor cursor){
		Bill bill = new Bill();
		bill.setId(cursor.getLong(0));
		bill.setBillName(cursor.getString(1));
		bill.setBillAmount(cursor.getString(2));
		bill.setBillDueDate(cursor.getString(3));
		bill.setBillAmountPaid(cursor.getString(4));
		bill.setBillNote(cursor.getString(5));
		
		return bill;
	}
}
