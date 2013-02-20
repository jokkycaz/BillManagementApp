package com.example.billmanagement;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Bill {
	private long id;
	private String billName;
	private String billAmount;
	private String billDueDate;
	private String billAmountPaid;
	private String billNote;
	public String getBillAmountPaid() {
		return billAmountPaid;
	}
	public void setBillAmountPaid(String billAmountPaid) {
		this.billAmountPaid = billAmountPaid;
	}
	public String getBillNote() {
		return billNote;
	}
	public void setBillNote(String billNote) {
		this.billNote = billNote;
	}
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getBillDueDate() {
		return billDueDate;
	}

	public void setBillDueDate(String billDueDate) {
		this.billDueDate = billDueDate;
	}
	public String toString(){
		return billName + " " + billAmount + " " + billDueDate;
	}

}
