package com.example.billmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        
        final Button buttonViewBills = (Button) findViewById(R.id.buttonViewBills);
        buttonViewBills.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageActivity.this.startActivity(new Intent(MainPageActivity.this, ViewBillsActivity.class));
            }
        });
        
        final Button buttonViewGraphs = (Button) findViewById(R.id.buttonViewGraphs);
        buttonViewGraphs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageActivity.this.startActivity(new Intent(MainPageActivity.this, ViewGraphsActivity.class));
            }
        });
    }

}
