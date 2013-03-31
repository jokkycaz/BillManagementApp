package com.example.billmanagement;

import java.util.Arrays;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

@SuppressWarnings({"deprecation"})
public class ViewGraphsActivity extends Activity {
    
    private XYPlot billGraph;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graphs);
        
        // initialize our XYPlot reference:
        billGraph = (XYPlot) findViewById(R.id.billGraph);
        
        //updateGraph(false);
    }
    
    
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateGraph(true);
    }



    private void updateGraph(boolean random) {
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

}
