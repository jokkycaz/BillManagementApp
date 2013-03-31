package com.example.billmanagement;

import java.util.ArrayList;

import com.example.database.BillDataSource;
import com.example.database.DateUtils;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SlidingDrawer;
import android.widget.TextView;

@SuppressWarnings({"deprecation","unused"})
public class ViewBillsActivity extends ListActivity {
	
    private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Bill> m_bills = null;
    private BillAdapter m_adapter;
    private Runnable viewBills;
    private BillDataSource datasource;
    private ListView list;
    final Context context = this;
    private EditText name, amount, dueDate, billNote, editBillName, editBillAmount, editBillDate,
        editBillAmountPaid, editBillNote;
    private RadioButton radioButton;
    private int mSelectedPosition = -1;
    private Button cancel;
    private Button addBill;
    private SlidingDrawer slide;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_view_bills);
        m_bills = new ArrayList<Bill>();
        list = getListView();
        this.m_adapter = new BillAdapter(this, R.layout.row, m_bills);
        setListAdapter(this.m_adapter);

        datasource = new BillDataSource(this);
        datasource.open();
        viewBills = new Runnable() {
            @Override
            public void run() {
                getBills();
            }
        };
        Thread thread = new Thread(null, viewBills, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(ViewBillsActivity.this, "Please wait...", "Retrieving data ...", true);
        
        ((Button) findViewById(R.id.buttonAdd)).setOnClickListener(new Button.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.add_bill, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                name = (EditText) promptsView.findViewById(R.id.newBillName);
                amount = (EditText) promptsView.findViewById(R.id.newBillAmount);
                dueDate = (EditText) promptsView.findViewById(R.id.newBillDueDate);
                billNote = (EditText) promptsView.findViewById(R.id.CreateBillNote);

                cancel = (Button) promptsView.findViewById(R.id.Cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivity(new Intent(context, ViewBillsActivity.class));
                    }
                });

                addBill = (Button) promptsView.findViewById(R.id.addNewBill);
                addBill.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        //Input validation
                        Boolean nameBool = false,amountBool = false,dueDateBool = false;
                        if (inputValidation.isName(name.getText().toString())
                                && name.getText().toString().length() != 0) {
                            nameBool = true;
                        } else {
                            name.setText("");
                            name.setHint("Invalid Name");
                        }
                        
                        if (inputValidation.isCurrency(amount.getText().toString())
                        		&& amount.getText().toString().length() != 0) {
                            amountBool = true;
                        } else {
                            amount.setText("");
                            amount.setHint("Invalid Input");
                        }
                        
                        if (dueDate.getText().toString().length() == 10
                        		&& inputValidation.isDate(dueDate.getText().toString())) {
                            dueDateBool = true;
                        } else {
                            dueDate.setText("");
                            dueDate.setHint("Invalid Date: mm-dd-yyyy");
                        }
                        
                        //if all input validation passed add bill to db
                        if (nameBool && amountBool && dueDateBool) {
                            datasource.createBill(
                            		name.getText().toString(),
                            		Double.parseDouble(amount.getText().toString()),
                            		DateUtils.parse(dueDate.getText().toString()),
                            		billNote.getText().toString());
                            startActivity(new Intent(context, ViewBillsActivity.class));
                        }
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if (m_bills != null && m_bills.size() > 0) {
                m_adapter.notifyDataSetChanged();
                for (int i = 0; i < m_bills.size(); i++)
                    m_adapter.add(m_bills.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };

    private void getBills() {
        try {
            m_bills = new ArrayList<Bill>();
            for (int i = 0; i < datasource.getAllBills().size(); i++) {
                m_bills.add(datasource.getAllBills().get(i));
            }

            Thread.sleep(2000);
            Log.i("ARRAY", "" + m_bills.size());
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        runOnUiThread(returnRes);
    }

    private class BillAdapter extends ArrayAdapter<Bill> {

        private ArrayList<Bill> items;

        public BillAdapter(Context context, int textViewResourceId, ArrayList<Bill> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            //used to create radioButtons
            //that only allow one to be selected at a time
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
                holder = new ViewHolder();
                holder.radioBtn = (RadioButton)v.findViewById(R.id.radioButton);
                v.setTag(holder);
            } else {
                holder = (ViewHolder)v.getTag();
            }
            //v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,85));
            //OnClick Listener when row is clicked
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    slide = (SlidingDrawer)findViewById(R.id.drawer);
                    slide.open();
                }
            });
            
            //RadioButton onClick Logic
            holder.radioBtn.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if (position != mSelectedPosition && radioButton != null) {
                        radioButton.setChecked(false);
                    }
                    mSelectedPosition = position;
                    radioButton = (RadioButton)v;
                    slide = (SlidingDrawer)findViewById(R.id.drawer);
                    slide.open();
                    
                    final Bill bill = items.get(position);
                    System.out.println(bill.getBillName() + " | " + bill.getId());
                    editBillAmountPaid = (EditText) findViewById(R.id.viewBillAmountPaid);
                    editBillNote = (EditText) findViewById(R.id.viewBillNote);
                    editBillName = (EditText) findViewById(R.id.editBillName);
                    editBillAmount = (EditText) findViewById(R.id.editBillAmount);
                    editBillDate  = (EditText) findViewById(R.id.editBillDueDate);
                    
                    editBillNote.setText(bill.getBillNote().toString());
                    editBillName.setText(bill.getBillName().toString());
                    editBillAmount.setText(bill.getBillAmount().toString());
                    editBillDate.setText(DateUtils.format(bill.getBillDueDate()));
                    editBillAmountPaid.setText(bill.getBillAmountPaid().toString());
                    
                    ((Button) findViewById(R.id.buttonDelete)).setOnClickListener(new Button.OnClickListener() {
            			@Override
            			public void onClick(View v) {
            				datasource.deleteBill(bill);
            				m_adapter.remove(bill);
            				slide.close();
            			}
                    });
                }
            });
            if (mSelectedPosition != position) {
                holder.radioBtn.setChecked(false);
            } else {
                holder.radioBtn.setChecked(true);
                if (radioButton != null && holder.radioBtn != radioButton) {
                    radioButton = holder.radioBtn;
                }
            }

            Bill bill = items.get(position);
            if (bill != null) {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView mt = (TextView) v.findViewById(R.id.middletext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                if (tt != null) {
                    tt.setText("Name: " + bill.getBillName());
                }
                if (mt != null) {
                    mt.setText("Amount: " + bill.getBillAmount());
                }
                if (bt != null) {
                    bt.setText("Due: " + DateUtils.format(bill.getBillDueDate()));
                }
            }
            return v;
        }
        
        private class ViewHolder {
            TextView name;
            RadioButton radioBtn;
        }
    }
}
