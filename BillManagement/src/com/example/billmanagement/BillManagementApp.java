package com.example.billmanagement;

import android.app.Application;
import android.content.Context;

public class BillManagementApp extends Application {
	private static BillManagementApp INSTANCE;

	
	public void onCreate() {
		super.onCreate();
		BillManagementApp.INSTANCE = this;
	}
	
	public static Context getContext() {
		if (INSTANCE == null) {
			return null;
		}
		return INSTANCE.getApplicationContext();
	}
}
