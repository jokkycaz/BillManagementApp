package com.example.billmanagement;

import java.util.ArrayList;

import com.example.database.BillDataSource;

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
import android.widget.TextView;

public class ViewBillActivity extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Bill> m_bills = null;
	private BillAdapter m_adapter;
	private Runnable viewBills;
	private BillDataSource datasource;
	private ListView list;
	final Context context = this;
	private EditText name;
	private EditText amount;
	private EditText dueDate;
	private EditText billNote;
	private EditText editBillName;
	private EditText editBillAmount;
	private EditText editBillDate;
	private EditText editBillAmountPaid;
	private EditText editBillNote;
	private RadioButton radioButton;
	private int mSelectedPosition = -1;
	private Button cancel;
	private Button addBill;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.activity_view_bill);
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
		m_ProgressDialog = ProgressDialog.show(ViewBillActivity.this,
				"Please wait...", "Retrieving data ...", true);

		final Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				// Intent i = new Intent(ViewBillActivity.this,
				// AddBillActivity.class);
				// startActivity(i);
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.add_bill, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				alertDialogBuilder.setView(promptsView);
				//promptsView.setBackgroundResource(android.R.color.transparent);

				name = (EditText) promptsView.findViewById(R.id.newBillName);
				amount = (EditText) promptsView
						.findViewById(R.id.newBillAmount);
				dueDate = (EditText) promptsView
						.findViewById(R.id.newBillDueDate);
				billNote = (EditText) promptsView
						.findViewById(R.id.CreateBillNote);

				cancel = (Button) promptsView.findViewById(R.id.Cancel);
				cancel.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						//cancel.setBackgroundResource(R.drawable.btn_keyboard_key_pressed_on);
						Intent i = new Intent(context, ViewBillActivity.class);
						startActivity(i);
					}
				});

				addBill = (Button) promptsView
						.findViewById(R.id.addNewBill);
				addBill.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						//addBill.setBackgroundResource(R.drawable.btn_keyboard_key_pressed_on);
						datasource.createBill(name.getText().toString(), amount
								.getText().toString(), dueDate.getText()
								.toString(), billNote.getText().toString());

						Intent i = new Intent(context, ViewBillActivity.class);
						startActivity(i);
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

		public BillAdapter(Context context, int textViewResourceId,
				ArrayList<Bill> items) {
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
			}
			else{
				holder = (ViewHolder)v.getTag();
			}
			v.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,85));
			//OnClick Listener when row is clicked
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
			
			//RadioButton onClick Logic
			holder.radioBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position != mSelectedPosition && radioButton != null){
						radioButton.setChecked(false);
	                }
	                mSelectedPosition = position;
	                radioButton = (RadioButton)v;
	                
	                Bill bill = items.get(position);
					System.out.println(bill.getBillName() + " | "
							+ bill.getId());
					editBillAmountPaid = (EditText) findViewById(R.id.viewBillAmountPaid);
					editBillNote = (EditText) findViewById(R.id.viewBillNote);
					editBillName = (EditText) findViewById(R.id.editBillName);
					editBillAmount = (EditText) findViewById(R.id.editBillAmount);
					editBillDate  = (EditText) findViewById(R.id.editBillDueDate);
					
					editBillNote.setHint(bill.getBillNote().toString());
					editBillName.setHint(bill.getBillName().toString());
					editBillAmount.setHint(bill.getBillAmount().toString());
					editBillDate.setHint(bill.getBillDueDate().toString());
					editBillAmountPaid.setHint(bill.getBillAmountPaid()
							.toString());
				}
			});
			if(mSelectedPosition != position){
	            holder.radioBtn.setChecked(false);
	        }else{
	            holder.radioBtn.setChecked(true);
	            if(radioButton != null && holder.radioBtn != radioButton){
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
					bt.setText("Due: " + bill.getBillDueDate());
				}
			}
			return v;
		}
		private class ViewHolder{
			TextView name;
			RadioButton radioBtn;
		}
	}
}
