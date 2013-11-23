package com.piratebayfree.activities;

import java.util.ArrayList;
import java.util.List;

import com.piratebayfree.Database;
import com.piratebayfree.R;
import com.piratebayfree.Status;
import com.piratebayfree.adapters.LogAdapter;


import android.os.Bundle;
import android.app.ListActivity;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

public class LogsActivity extends ListActivity {
	
	private Database db;
	
	private Status log;
	private String tag = "";
	private int previous = 0;
	private long time = 0;

	private LogAdapter adapter;
	
	private List<Status> logs = new ArrayList<Status>();
	
    static final String TAG = "LogsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_logs);
		
		Log.d(TAG, "Logs activity started");
		
		db = new Database(this);
		
		loadLogs();

		setupActionBar();
		
	}
	
	// Load logs
	private void loadLogs() {
	
		logs = db.getLogs();
	
		Log.d(TAG, logs.size() + " logs found");
	
		for(int i = 0; i < logs.size(); i++) {
		
			log = logs.get(i);
					
			// Time
			time = log.getTime() - previous; 
			
			previous = (int) log.getTime();
			
			log.setTime(time);
			
			// Tag
			if(!tag.contains(log.getTag())) {
				
				tag = log.getTag();
				
				log.setFirst(true);
				
			} else {
				
				log.setFirst(false);
				
			}
		
		}
	
		showLogs();
	
	}
	
	// Show logs
	private void showLogs() {
	
		adapter = new LogAdapter(this, R.layout.log, logs);
	
		setListAdapter(adapter);
	
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logs, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case android.R.id.home:
			
			NavUtils.navigateUpFromSameTask(this);
			
			return true;
			
		}
		
		return super.onOptionsItemSelected(item);
		
	}

}
