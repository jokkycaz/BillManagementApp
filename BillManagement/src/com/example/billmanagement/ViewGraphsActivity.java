package com.example.billmanagement;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.example.database.BillDataSource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings({"deprecation"})
public class ViewGraphsActivity extends Activity {
    
    private XYPlot billGraph;
    private BillDataSource datasource;
    private Runnable listBills;
    private ProgressDialog m_ProgressDialog;
    private ArrayList<Bill> m_bills;
    
    private List<LineAndPointFormatter> formatters = new ArrayList<LineAndPointFormatter>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graphs);
        
        // initialize our XYPlot reference:
        billGraph = (XYPlot) findViewById(R.id.billGraph);
        
        
        // Red
        LineAndPointFormatter formatter = new LineAndPointFormatter(Color.rgb(200, 0, 0), Color.rgb(100, 0, 0), null);
        formatters.add(formatter);
        
        //Blue
        formatter = new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), null);
        formatters.add(formatter);
        
        // TODO Add more colors
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateGraph();
    }
    
    private void updateGraph() {
        datasource = BillDataSource.getInstance();
        listBills = new Runnable() {
            @Override
            public void run() {
                getBills();
            }
        };
        Thread thread = new Thread(null, listBills, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(ViewGraphsActivity.this, "Please wait...", "Retrieving data ...", true);
    }

    private void getBills() {
        try {
            m_bills = new ArrayList<Bill>();
            for (int i = 0; i < datasource.getAllBills().size(); i++) {
                m_bills.add(datasource.getAllBills().get(i));
            }

            Thread.sleep(500);
            Log.i("ARRAY", "" + m_bills.size());
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        runOnUiThread(returnRes);
    }

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if (m_bills != null && m_bills.size() > 0) {
                buildGraph();
            }
            m_ProgressDialog.dismiss();
        }
    };
    
    private void buildGraph() {
        
        Map<String, BillData> billMap = new HashMap<String, BillData>();
        for (Bill bill : datasource.getAllBills()) {
            Log.d("ViewGraphsActivity", "bill: " + bill);
            BillData billData;
            if (billMap.containsKey(bill.getBillName())) {
                billData = billMap.get(bill.getBillName());
            } else {
                billData = new BillData();
                billMap.put(bill.getBillName(), billData);
            }
            billData.dates.add(bill.getBillDueDate().getTime());
            billData.amounts.add(bill.getBillAmount());
        }
        
        Log.d("ViewGraphsActivity", "billDates: " + billMap);
        
        int n = 0;
        for (Entry<String, BillData> entry : billMap.entrySet()) {
            XYSeries series = new SimpleXYSeries(
                    entry.getValue().dates,
                    entry.getValue().amounts,
                    entry.getKey());
            billGraph.addSeries(series, formatters.get(n++ % formatters.size()));
        }
        
        // draw a domain tick for each year:
        //billGraph.setDomainStep(XYStepMode.SUBDIVIDE, years.length);
        
        // customize our domain/range labels
        billGraph.setDomainLabel("Due Date");
        billGraph.setRangeLabel("Bill Amount"); 
        
        billGraph.setRangeValueFormat(new DecimalFormat("0"));
        billGraph.setDomainValueFormat(new Format() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

			@Override
			public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
				//long timestamp = ((Number) object).longValue() * 1000;
				long timestamp = ((Number) object).longValue();
                Date date = new Date(timestamp);
                return dateFormat.format(date, buffer, field); 
			}

			@Override
			public Object parseObject(String string, ParsePosition position) {
				return null;
			}
        });
        
        billGraph.disableAllMarkup();
        
        billGraph.redraw();
    }

    private void updateGraphExample(boolean random) {
        // Create a couple arrays of y-values to plot:
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
        
        if (random) {
            for (int n = 0; n < series1Numbers.length; n++) {
                series1Numbers[n] = Math.round(Math.random() * 10);
            }
            for (int n = 0; n < series2Numbers.length; n++) {
                series2Numbers[n] = Math.round(Math.random() * 10);
            }
        }
        
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Series1");                             // Set the display title of the series
        // same as above
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // line color
                Color.rgb(0, 100, 0),                   // point color
                null);                                  // fill color (none)
        // add a new series' to the xyplot:
        billGraph.addSeries(series1, series1Format);
        // same as above:
        billGraph.addSeries(series2,
                new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), null));
        // reduce the number of range labels
        billGraph.setTicksPerRangeLabel(3);
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        billGraph.disableAllMarkup(); 
    }

    private class BillData {
        public BillData() {
            dates = new ArrayList<Long>();
            amounts = new ArrayList<Double>();
        }
        List<Long> dates;
        List<Double> amounts;
    }
}
