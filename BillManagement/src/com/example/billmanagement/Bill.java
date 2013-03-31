package com.example.billmanagement;

import java.util.Date;

public class Bill {
    private long id;
    private String billName;
    private Double billAmount;
    private Date billDueDate;
    private Double billAmountPaid;
    private String billNote;
    
    public Bill() { }
    
    public Bill(long id) {
    	this.id = id;
    }
    
    public Double getBillAmountPaid() {
        return billAmountPaid;
    }
    
    public void setBillAmountPaid(Double billAmountPaid) {
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

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Date getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(Date billDueDate) {
        this.billDueDate = billDueDate;
    }
    
    public void setBillDueDate(long milliseconds) {
        this.billDueDate = new Date(milliseconds);
    }
    
    public String toString(){
        return billName + " " + billAmount + " " + billDueDate;
    }

}
