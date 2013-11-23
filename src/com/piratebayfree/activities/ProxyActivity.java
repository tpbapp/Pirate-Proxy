package com.piratebayfree.activities;

import com.piratebayfree.Database;
import com.piratebayfree.Proxy;
import com.piratebayfree.R;
import com.piratebayfree.Settings;
import com.piratebayfree.Status;
import com.piratebayfree.adapters.ProxyAdapter;
import com.piratebayfree.clients.ProxyClient;


import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ProxyActivity extends ListActivity implements OnClickListener {
	
	private Database db;
	private Settings settings;

	private String getpro;
	private String share;
	private String unsuspend;
	
	private String[] array;
	
	private Proxy proxy;

	private ProxyClient client;

	private ProxyAdapter adapter;
	
	private List<Proxy> proxies = new ArrayList<Proxy>();
    
    private static final int SHARE = 0;
    private static final int RATE = 1;
    private static final int SUSPEND = 2;
    private static final int DELETE = 3;

    static final String TAG = "ProxiesActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_proxies);

    	//add = getResources().getString(R.string.add);
    	getpro = getResources().getString(R.string.getpro);
    	share = getResources().getString(R.string.share);
    	unsuspend = getResources().getString(R.string.unsuspend);
    	
        array = getResources().getStringArray(R.array.proxy);
		
		loadData();
		
		loadProxies();

		setupActionBar();
		
	}
	
	private void loadData() {
		
		db = new Database(this);
		settings = new Settings(this);
		
		addLog("Proxies activity started");
		
	}
	
	private void loadProxies() {
		
		proxies = db.getProxies();
		
		if(db.getProxyCount(settings.getProxyID()) > 0) {
			
			proxy = db.getProxy(settings.getProxyID());
			
			int index = proxies.indexOf(proxy);
			
			if(index != -1 && index < proxies.size()) {
				
				proxies.get(index).setActive(true);

				addLog("Active proxy found " + proxy.getURL());
						
			} else {

				addLog("Active proxy not found in proxies");
				addLog("Active proxy URL: " + proxy.getURL());
				
			}
			
		} else {

			addLog("Active proxy not found");
			addLog("Active proxy ID: " + settings.getProxyID());
			
		}
		
		adapter = new ProxyAdapter(this, R.layout.proxy, proxies);
		
		setListAdapter(adapter);
		
		registerForContextMenu(getListView());
		
		if(settings.isNetworkAvailable()) {
		
			client = new ProxyClient(this);
		
			new ProxyThread().execute();
			
		}
		
	}
    
    // Thread
    private class ProxyThread extends AsyncTask<Void, Integer, Void> {  

		@Override  
        protected void onPreExecute() {}  
  
        @Override  
        protected Void doInBackground(Void... params) {  
        	
			synchronized(this) {
				
				int limit = 10;
				
				if(proxies.size() < 10) limit = proxies.size();
				
				for(int p = 0; p < limit; p++) {
					
					proxy = proxies.get(p);
				
					proxy = client.loadProxy(proxy);
					
					proxies.set(p, proxy);
					
					publishProgress(p);
					
				}
				
			}  
            
            return null;  
            
        }  
        
        @Override  
        protected void onProgressUpdate(Integer... values) { 
    	
        	showProxies();
    	
        }
  
        @Override  
        protected void onPostExecute(Void result) {}  
        
    }
    
    // Show
    private void showProxies() {
    	
    	adapter.notifyDataSetChanged();
    	
    }

	// Log
	private void addLog(String text) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text));
		
	}
	
	// Delete
	/*private void deleteProxy() {
		
		db.deleteProxy(proxy);
		
		showToast(deleted + " " + proxy.getName());
		
		loadProxies();
		
	}*/
	
	/*private void deleteProxies() {
		
		db.deleteProxies();
		
		showToast("Deleted all proxies");
		
		loadProxies();
		
	}*/
	
	// Suspend
	/*private void suspendProxy() {
		
		if(proxy.isSuspended()) {
		
			proxy.resetStatus();
		
			db.updateProxy(proxy);
		
			showToast(unsuspended + " " + proxy.getName());
			
		} else {
		
			proxy.setStatus(Proxy.STATUS_SUSPENDED);
		
			db.updateProxy(proxy);
		
			showToast(suspended + " " + proxy.getName());
			
		}
		
		loadProxies();
		
	}*/
	
	// Toast
	private void showToast(String text) {
		
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		
		addLog(text);
	
	}
	
	// Add
	/*private void addProxy() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final EditText input = new EditText(this);
		
		input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
		input.setText("http://");

		alert.setTitle(R.string.addproxy);
		alert.setView(input);
		alert.setCancelable(true);

		alert.setPositiveButton(add, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				
				String value = input.getText().toString();
				
				db.addProxy(new Proxy(value.toString()));
				
				loadProxies();
				
			}
			
		});

		alert.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {}
			
		});

		alert.show();

	}*/
	
	// Rate
	/*private void rateProxy() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final EditText input = new EditText(this);
		
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setText(String.valueOf(proxy.getRating()));

		alert.setTitle(rate);
		alert.setView(input);
		alert.setCancelable(true);

		alert.setPositiveButton(rate, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				
				int rating = Integer.parseInt(input.getText().toString());
				
				proxy.setRating(rating);
				
				db.updateProxy(proxy);
				
				loadProxies();
				
			}
			
		});

		alert.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {}
			
		});

		alert.show();

	}*/
	
	// Browser
	private void openBrowser() {
		
		if(settings.isDebugEnabled()) {
	
			addLog("Proxy clicked " + proxy.getName());
	
			//showToast("Source URL: " + proxy.getSourceURL());

			addLog("Search URL: " + proxy.getSearchURL());
			addLog("Twitter URL: " + proxy.getBitcoinURL());
			addLog("Bitcoin URL: " + proxy.getBitcoinURL());
			addLog("Litecoin URL: " + proxy.getLitecoinURL());
	
			//if(settings.isDebugEnabled()) deleteProxy(proxy);
			
		}
    	
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(proxy.getURL()));
		
		startActivity(intent);
		
	}
    
    // Share
	private void openShare() {
		
		addLog("Opening share");
		
		String message = Settings.getTwitterName() + " ";
		
		if(proxy.hasTwitter()) message += proxy.getTwitterName() + " ";

		message += proxy.getName() + " " + proxy.getURL();
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, message);
		
		startActivity(Intent.createChooser(intent, share));
		
	}
    
    // Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	
    	if(v.getId() == android.R.id.list) {
    		
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	    
    	    if(info.position < proxies.size()) {

    	    	proxy = db.getProxy(proxies.get(info.position).getID());
        		
        	    menu.setHeaderTitle(proxy.getName());
    	    
        	    for(int i = 0; i < array.length; i++) {
        	    	
        	    	if(i == SUSPEND) {
        	    		
        	    		if(proxy.isSuspended()) {
    	    		
        	    			menu.add(Menu.NONE, i, i, unsuspend);
        	    			
        	    		} else {
    	    		
        	    			menu.add(Menu.NONE, i, i, array[i]);
        	    			
        	    		}
        	    		
        	    	} else {
    	    		
        	    		menu.add(Menu.NONE, i, i, array[i]);
        	    		
        	    	}
    	      
        	    }
            	
            	addLog("Search context menu opened " + proxy.getName());
        	
        	}
    	    
    	}
    	
    }
    
    // Context item
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
    	int index = item.getItemId();
    	String name = array[index];
    	
    	//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

    	addLog("Proxy context menu item selected " + name);
    	
    	switch(index) {
    	
    		case SHARE:
    			
    			openShare();
    		
    			break;
			
    		case RATE:
	    		
    			//rateProxy();
    			showToast(getpro);
    		
    			break;
	    	
    		case SUSPEND:
	    		
    			//suspendProxy();
    			showToast(getpro);
    		
    			break;
			
    		case DELETE:
	    		
    			//deleteProxy();
    			showToast(getpro);
    		
    			break;
    		
    	}
		
		loadProxies();
    	
    	return true;
      
    }

    // List item
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		proxy = proxies.get(position);
		
		openBrowser();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		
			case android.R.id.home:

				NavUtils.navigateUpFromSameTask(this);
				
				return true;
		
			/*case R.id.add:

				addProxy();
				
				return true;
		
			case R.id.delete:

				deleteProxies();
				
				return true;*/
			
		}
		
		return super.onOptionsItemSelected(item);
		
	}

	@Override
	public void onClick(View view) {
		
		switch(view.getId()) {
		
			//case R.id.add:
			
				//addProxy();
		
		}
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//getMenuInflater().inflate(R.menu.proxies, menu);
		
	    //if(!settings.isDebugEnabled()) menu.removeItem(R.id.delete);
		
		return true;
		
	}

}
