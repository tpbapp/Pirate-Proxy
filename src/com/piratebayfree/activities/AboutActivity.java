package com.piratebayfree.activities;

import com.piratebayfree.Database;
import com.piratebayfree.Proxy;
import com.piratebayfree.R;
import com.piratebayfree.Settings;
import com.piratebayfree.Status;
import com.piratebayfree.clients.ProxyClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class AboutActivity extends Activity implements OnClickListener {

	private Database db;
	private Settings settings;
	
	private Proxy proxy;
	
	private ProxyClient proxyclient;

	private ImageView tpbapp;
	private TextView developer;
	private TextView twitter;
	private TextView title;
	private ImageView icon;
	private TextView status;
	private Button donate;

	private String connecting;
	//private String noproxy;
	private String nonetwork;
	private String share;
	
	private static final String TAG = "AboutActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);

		// Views
		tpbapp = (ImageView) findViewById(R.id.tpbapp);
		developer = (TextView) findViewById(R.id.about_developer);
		twitter = (TextView) findViewById(R.id.about_twitter);
		title = (TextView) findViewById(R.id.about_title);
		icon = (ImageView) findViewById(R.id.about_status_icon);
		status = (TextView) findViewById(R.id.about_status_text);
		donate = (Button) findViewById(R.id.about_donate);
		
		// Listeners
		tpbapp.setOnClickListener(this);
		developer.setOnClickListener(this);
		twitter.setOnClickListener(this);
		donate.setOnClickListener(this);
		status.setOnClickListener(this);
		
		// Strings
        connecting = getResources().getString(R.string.connecting);
        //noproxy = getResources().getString(R.string.noproxy);
        nonetwork = getResources().getString(R.string.nonetwork);

		db = new Database(this);
		settings = new Settings(this);
		
		twitter.setText(Settings.getTwitterName());
		
		loadProxy();

		setupActionBar();
		
	}
	
	// Proxy
	private void loadProxy() {
		
		if(settings.isConnected()) {
			
			showProxy();
			
		} else {
			
			proxyclient = new ProxyClient(this);
		
			new LoadProxy().execute();
			
		}
	
	}
	
	private class LoadProxy extends AsyncTask<Void, Integer, Void> {  

		@Override  
    	protected void onPreExecute() {
		
			setStatus(connecting + "...");
		
		}  

		@Override  
		protected Void doInBackground(Void... params) {  
    	
			synchronized(this) {
			
				proxyclient.loadProxy();
			
			}
		
			return null;
        
		}  

		@Override  
		protected void onProgressUpdate(Integer... values) {}  
		
		@Override  
    	protected void onPostExecute(Void result) {
			
			showProxy();
    	
		}
    
	}
	
	// Show
	private void showProxy() {
    	
		//if(proxyclient != null) {
    		
			//proxy = proxyclient.getProxy();
			
			//setStatus(proxy.getName(), Status.SUCCESS);
		
		//} else if(db.getProxyCount(settings.getProxyID()) > 0) {
	
			proxy = db.getProxy(settings.getProxyID());
			
			if(settings.isNetworkAvailable()) {
			
				setStatus(proxy.getName(), Status.SUCCESS);
				
			} else {
			
				setStatus(nonetwork);
				
			}
			
			if(proxy.hasTitle()) title.setText(proxy.getTitle());
			
			if(proxy.hasTwitter()) twitter.setText(proxy.getTwitterName());
			
		//} else {
			
			//setStatus(noproxy, Status.ERROR);
			
		//}
		
	}
	
	// Log
	private void addLog(String text) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text));
		
	}

	private void addLog(String text, int status) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text, status));
		
	}

	// Status
	public void setStatus(String text) {
		
		status.setText(text);
		
		addLog(text);
		
	}
	
	public void setStatus(String text, int status) {
		
		this.status.setText(text);
		
		if(status == Status.SUCCESS) {
			
			icon.setImageResource(R.drawable.success);
			
		} else if(status == Status.ERROR) {
			
			icon.setImageResource(R.drawable.error);
			
		} else if(status == Status.WARNING) {
			
			icon.setImageResource(R.drawable.warn);
			
		}
		
		addLog(text, status);
		
	}
	
	// Developer
	private void openWebsite() {
		
		addLog("Opening developer website " + Settings.getDeveloperURL());
    	
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.getDeveloperURL()));
		
		startActivity(intent);
		
	}
	
	// Twitter
	private void openTwitter() {
		
		if(proxy.hasTwitter()) {
		
			addLog("Opening twitter " + proxy.getTwitterURL());
    	
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(proxy.getTwitterURL()));
			
			startActivity(intent);
			
		} else {
		
			addLog("Opening twitter " + Settings.getTwitterURL());
    	
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.getTwitterURL()));
			
			startActivity(intent);
		
		}
		
	}
	
	// Donate
	private void openBitcoin() {
		
		String url = Settings.getBitcoinURL();
		
		if(proxy.hasBitcoin()) url = proxy.getBitcoinURL();
		
		addLog("Opening Bitcoin donate " + url);
		
		try {
    	
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			
			startActivity(intent);
			
		} catch(Exception e) {
			
			addLog("Bitcoin app not found");
			 
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.getBitcoinMarket()));
			
			startActivity(intent);
			
			addLog("Opening " + url);
			
		}
		
	}
	
	/*private void openLitecoin() {
		
		addLog("Opening Litecoin donate");
		
		String url = "litecoin:LiYp3Dg11N5BgV8qKW42ubSZXFmjDByjoV";
		
		if(proxy.hasLitecoin()) url = proxy.getLitecoinURL();
    	
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
		
	}*/
	
	// Share
	private void openShare() {
		
		addLog("Opening share");
		
		String message = Settings.getTwitterName();
		
		if(proxy.hasTwitter()) message += " " + proxy.getTwitterName();
		
		message += " " + proxy.getURL() + " " + Settings.getDeveloperURL();
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, message);
		
		startActivity(Intent.createChooser(intent, share));
		
	}
	
	// Proxy
	private void openBrowser() {
		
		addLog("Opening proxy website " + proxy.getURL());
    	
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(proxy.getURL()));
		
		startActivity(intent);
		
	}

	// Click
	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		
			case R.id.tpbapp:
				
				openWebsite();
				
				break;
		
			case R.id.about_developer:
				
				openWebsite();
				
				break;
		
			case R.id.about_twitter:
				
				openTwitter();
				
				break;
		
			case R.id.about_donate:
				
				openBitcoin();
				
				break;
		
			case R.id.about_status_text:
	    	    
				Intent proxiesintent = new Intent(this, ProxyActivity.class);
	        
				startActivity(proxiesintent);
			
				break;
		
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.about, menu);
	    
	    //if(!settings.isDebugEnabled()) menu.removeItem(R.id.logs);
	    
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
			case android.R.id.home:
				
				NavUtils.navigateUpFromSameTask(this);
			
				return true;
			
			//case R.id.search:

				//NavUtils.navigateUpFromSameTask(this);
    
				//return true;
		
			case R.id.share:
				
				openShare();
	            
				return true;
		
			case R.id.browser:
				
				openBrowser();
	            
				return true;
			    
		    case R.id.proxies:
		    	    
		        Intent proxiesintent = new Intent(this, ProxyActivity.class);
		        startActivity(proxiesintent);
		            
		    	return true;
			    
		    //case R.id.searches:
		    	    
		        //Intent searchesintent = new Intent(this, SearchesActivity.class);
		        //startActivity(searchesintent);
		            
		    	//return true;

			//case R.id.settings:
        	
				//Intent settingsintent = new Intent(this, SettingsActivity.class);
				//startActivity(settingsintent);
    
				//return true;
		        
		    //case R.id.logs:
	        	
	        	//Intent logsintent = new Intent(this, LogsActivity.class);
	        	//startActivity(logsintent);
		    			
		    	//return true;
				
		}
		
		return super.onOptionsItemSelected(item);
	}

}
