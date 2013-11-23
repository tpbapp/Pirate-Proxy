package com.piratebayfree.activities;

import com.newrelic.agent.android.NewRelic;

import com.google.analytics.tracking.android.EasyTracker;

import com.google.android.gms.ads.*;
import com.piratebayfree.Category;
import com.piratebayfree.Database;
import com.piratebayfree.Proxy;
import com.piratebayfree.R;
import com.piratebayfree.Search;
import com.piratebayfree.Settings;
import com.piratebayfree.Status;
import com.piratebayfree.adapters.CategoryAdapter;
import com.piratebayfree.clients.CategoryClient;
import com.piratebayfree.clients.ProxyClient;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity implements TextWatcher, OnKeyListener, OnClickListener, OnFocusChangeListener, OnItemClickListener, OnItemSelectedListener {
    
	private Database db;
	private Settings settings;
	//private Filter filter;

	private ImageView logo;
	private AutoCompleteTextView searchfield;
	private ImageButton searchbutton;
	private Button browse;
	private ImageView statusicon;
	private TextView statustext;
	private ImageView advert;
	private AdView admob;
	private ProgressBar loading;
	private DrawerLayout drawer;
	private ListView categorylist;
    private ActionBarDrawerToggle toggle;

	private String getpro;
	private String connecting;
	private String searching;
	private String nonetwork;
	private String noconnection;
	//private String connect;
	//private String cancel;
	private String pirate;
	private String share;
	private String torrents;
	private String[] array;

	private ProxyThread proxythread;
    private ProxyClient proxyclient;
    
	private CategoriesThread categoriesthread;
    private CategoryClient categoriesclient;
	
	private Proxy proxy;
	private Search search;
	private Category category;

	private List<Search> searches = new ArrayList<Search>();
	private List<Category> categories = new ArrayList<Category>();
	private List<String> searchqueries = new ArrayList<String>();

	private CategoryAdapter categoryadapter;
	private ArrayAdapter<String> searchadapter;
	
	//private boolean popup = true;

	private static final int INDEX_SUBSCRIBE = 0;
	private static final int INDEX_SHARE = 1;

	private static final String STATUS_SUCCESS = "success";
	private static final String STATUS_ERROR = "error";
	
	private static final String ADMOB = "ca-app-pub-7296622233197315/3074943589";
	private static final String NEWRELIC = "AAbb8f04e183dbe96eaa12a17c523c977200092e7e";
	
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

        // Views
    	logo = (ImageView) findViewById(R.id.main_logo);
    	searchfield = (AutoCompleteTextView) findViewById(R.id.main_search_editor);
    	searchbutton = (ImageButton) findViewById(R.id.main_search_button);
    	browse = (Button) findViewById(R.id.main_browse);
    	statusicon = (ImageView) findViewById(R.id.main_status_icon);
		statustext = (TextView) findViewById(R.id.main_status_text);
    	advert = (ImageView) findViewById(R.id.main_advert);
    	//admob = (AdView) findViewById(R.id.main_admob);
		loading = (ProgressBar) findViewById(R.id.main_loading);
        drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        categorylist = (ListView) findViewById(R.id.main_drawer_categories);
        
        // Strings
        getpro  = getResources().getString(R.string.getpro);
        connecting  = getResources().getString(R.string.connecting);
        searching  = getResources().getString(R.string.searching);
        nonetwork = getResources().getString(R.string.nonetwork);
        noconnection = getResources().getString(R.string.noconnection);
        //connect = getResources().getString(R.string.connect);
        //cancel = getResources().getString(R.string.cancel);
        pirate = getResources().getString(R.string.search);
        share = getResources().getString(R.string.share);
        torrents = getResources().getString(R.string.torrents);
        array = getResources().getStringArray(R.array.category);
        
        logo.setOnClickListener(this);
		browse.setOnClickListener(this);
        advert.setOnClickListener(this);

        db = new Database(this);
		settings = new Settings(this);

        addLog("TPB Proxy started");
        
        addLog(db.getProxyCount() + " proxies, " + db.getCategoryCount() + " categories");
    	
        db.deleteLogs();
		
		loadAds();
		
		loadTracking();
        
	}
	  
	@Override
	protected void onResume() {
	        	
		super.onResume();
		
		showLoading();
		
		loadThreads();
		
		loadSettings();
		
		loadSearch();
		
		loadProxy();
		
		loadDrawer();
		
		showCategories();
	                
	}

	// Ads
	private void loadAds() {
		
	    admob = new AdView(this);
	    
	    admob.setAdUnitId(ADMOB);
	    admob.setAdSize(AdSize.BANNER);
	    
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_activity);
	    
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT);
	    
	    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

	    layout.addView(admob, params);

	    AdRequest request = new AdRequest.Builder().build();

	    admob.loadAd(request);


	}
	
	// Tracking
	private void loadTracking() {
	
		NewRelic.withApplicationToken(NEWRELIC).start(this.getApplication());

	}

	@Override
	public void onStart() {
		 
	    super.onStart();
	    
	    EasyTracker.getInstance(this).activityStart(this);
	    
	}
	
	@Override
	public void onStop() {
		
	    super.onStop();
	    
	    EasyTracker.getInstance(this).activityStop(this);
	    
	}
	
	// Threads
	private void loadThreads() {

		proxythread = new ProxyThread();
		
		categoriesthread = new CategoriesThread();
		
		//popup = true;
		
	}
	
	// Settings
	private void loadSettings() {
	
		settings = new Settings(this);

		settings.reset();
		
	}

	// Search
	private void loadSearch() {
		
		searches = db.getSearches();
		
		searchqueries.clear();
		
		for(int i = 0 ; i < searches.size(); i++) {
			
			search = searches.get(i);
			
			if(search.isSubscribed() || settings.isHistoryEnabled()) {
			
				searchqueries.add(search.getName());
				
			} else {
				
				db.deleteSearch(search);
				
			}
			
		}
		
		if(settings.isHistoryEnabled()) {
				
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		
				searchadapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, searchqueries);

				searchfield.setAdapter(searchadapter);
				
				searchfield.setThreshold(1);
				
			}
			
		}
		
		searchfield.setOnKeyListener(this);
		//searchfield.setOnClickListener(this);
		searchfield.setOnFocusChangeListener(this);
		//searchfield.addTextChangedListener(this);
		//searchfield.setOnItemClickListener(this);
    	
		//searchfield.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		searchfield.setCursorVisible(true);
		//searchfield.setSelected(false);
		searchfield.clearFocus();
		
		//if(db.getSearchCount(settings.getSearchID()) > 0) {
			
			//search = db.getSearch(settings.getSearchID());
			
			//searchfield.setText(search.getName());
			
		//}
		
		if(searchfield.getText().toString().length() == 0)
				searchfield.setText(pirate);
		
		if(searchfield.getText().toString().contains(pirate))
				searchfield.setTextColor(Color.GRAY);
		
		searchbutton.setOnClickListener(this);
	
	}
	
	// Proxy
    private void loadProxy() {
			
		loadThreads();
			
		proxyclient = new ProxyClient(this);
			
		proxythread.execute();
			
		timeoutProxy();
    	
    }
	
	// Proxy timeout
	private void timeoutProxy() {
        
	    int timeout = (int) Math.round(settings.getTimeout() * 10);
	        
	    Handler handler = new Handler();
	        
	    handler.postDelayed(new Runnable() {
	        	
	    	@Override
	    	public void run() {
	      		
	    		if(!proxyclient.isConnected() && settings.isNetworkAvailable()) {
	        			
	    			loadThreads();
	                	
	    			hideLoading();
	      			
	    			showConnect(noconnection);
	      		
	    		}
	      		
	        }
	        	
	    }, timeout);
	    
	}
    
    // Proxy thread
    private class ProxyThread extends AsyncTask<Void, Integer, Void> {  

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
        protected void onPostExecute(Void result) {  
	    	
            if(proxyclient.isConnected()) {
            		
            	setStatus("Connected to " + proxyclient.getURL(), STATUS_SUCCESS);
            	
            	proxy = proxyclient.getProxy();
            	
            } else {
            	
            	if(!settings.isNetworkAvailable()) {
            
            		setStatus(nonetwork);
                	
                	showStatus();
            	
            	//} else {
            
            		//setStatus(noproxy);
            		//setStatus(noconnection);
            		
            		//showConnect(noconnection);
            		
            	}
            	
            }
        	
        	hideLoading();
            
            loadCategories();
        
        }  
        
    }

	// Categories
    private void loadCategories() {
    	
    	loadThreads();
		
		categories = db.getCategories();
		
		//categories = filter.filterCategories(categories);
		
		if(proxyclient.isConnected()) {
    	
			categoriesclient = new CategoryClient(this, proxy);
    	
			categoriesclient.loadClient();
    	
			categoriesthread.execute();
			
		} else {
    	
			showCategories();
			
		}
    	
    }
    
    // Categories thread
    private class CategoriesThread extends AsyncTask<Void, Integer, Void> {  

		@Override  
        protected void onPreExecute() {
			
			//categories = filter.filterCategories(categories);
        	
        }  
  
        @Override  
        protected Void doInBackground(Void... params) {  
        	
			synchronized(this) {
				
				categoriesclient.loadCategories();
				
			}  
            
            return null;  
            
        }  
  
        @Override  
        protected void onPostExecute(Void result) { 
	    	
            if(categoriesclient.isConnected()) {
            	
            	//categories = filter.filterCategories(categoriesclient.getCategories());
            	
            	categories = categoriesclient.getCategories();
            	
            }
            
        	showCategories();
        
        }  
        
    }
	
	// Show
	private void showCategories() {
		
		if(categories.size() == 0) categories = db.getCategories();
		
		//categories = filter.filterCategories(categories);
		
		for(int i = 0; i < categories.size(); i++) {
			
			category = categories.get(i);
			
			//if(category.getID() == 0 || (!settings.isAdultEnabled() && category.isAdult())) {
			if(category.getID() == 0) {
				
				categories.remove(i);
				
				addLog("Category removed " + category.getName());
					
			}
			
		}
		
		categoryadapter = new CategoryAdapter(this, R.layout.category, categories);
		
		categorylist.setAdapter(categoryadapter);
		
		categorylist.setOnItemClickListener(this);

        drawer.setDrawerListener(toggle);
        
        toggle.syncState();
	
		registerForContextMenu(categorylist);
        
        setupActionBar();
		
	}

    // Drawer
	private void loadDrawer() {

        toggle = new ActionBarDrawerToggle(this, drawer,
        		R.drawable.drawer, R.string.open, R.string.close) {

            // Open drawer
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerOpened(View drawerView) {
        		
        		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            	
        			getActionBar().setTitle(getResources().getString(R.string.browse));
                    
                    invalidateOptionsMenu();
                
        		}
        		
        		searchfield.clearFocus();

        	    InputMethodManager osk = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        	    
        	    osk.hideSoftInputFromWindow(searchfield.getWindowToken(), 0);
                
            	addLog("Categories drawer opened");
            	
            	//loadCategories();
            	
            	//showCategories();
            	
            }

            // Close drawer
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerClosed(View view) {
        		
        		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            	
        			getActionBar().setTitle(getResources().getString(R.string.tpb));
                    
                    invalidateOptionsMenu();
                
        		}
                
            	addLog("Categories drawer closed");
            	
            }
            
        };
		
	}

	// Log
	private void addLog(String text) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text));
		
	}
  
	//private void addLog(String text, int status) {
		
		//if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text, status));
		
	//}
	
	// Toast
	private void showToast(String text) {
		
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		
		addLog(text);
	
	}
	
    // Status
	public void setStatus(String text) {
		
		statusicon.setImageResource(R.drawable.neutral);
	
		statustext.setText(text);
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text));
		
	}
	
	public void setStatus(String text, String key) {
		
		int logstatus = 0;
		
		if(key == STATUS_SUCCESS) {
			
			statusicon.setImageResource(R.drawable.success);
			logstatus = Status.SUCCESS;
			
		} else if(key == STATUS_ERROR) {
			
			statusicon.setImageResource(R.drawable.error);
			logstatus = Status.ERROR;
			
		} else {
			
			statusicon.setImageResource(R.drawable.neutral);
			
		}
		
		statustext.setText(text);
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text, logstatus));
		
		
	}
	
	private void showStatus() {
		
		//statusicon.setVisibility(View.VISIBLE);
		statustext.setVisibility(View.VISIBLE);
		
	}
	
	// Loading
	private void showLoading() {
		
		loading.setVisibility(View.VISIBLE);

		logo.setVisibility(View.GONE);
		searchfield.setVisibility(View.GONE);
		searchbutton.setVisibility(View.GONE);
		statusicon.setVisibility(View.GONE);
		statustext.setVisibility(View.GONE);
		browse.setVisibility(View.GONE);
		
	}
	
	private void hideLoading() {
		
		loading.setVisibility(View.GONE);

		logo.setVisibility(View.VISIBLE);
		searchfield.setVisibility(View.VISIBLE);
		searchbutton.setVisibility(View.VISIBLE);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			
			browse.setVisibility(View.VISIBLE);
			
		}
		
		if(settings.isDebugEnabled()) {
				
			statusicon.setVisibility(View.VISIBLE);
			statustext.setVisibility(View.VISIBLE);
			
		}
		
	}

	// Search
	public void openSearch() {
		
		loadThreads();
		
		String searchtext = searchfield.getText().toString();
		
		if(searchtext.length() > 0 && !searchtext.equals(pirate)) {
			
			setStatus(searching + "...");

			search = new Search(searchtext);
		
			if(db.getSearchCount(search) > 0) {
				
				search = db.getSearch(search.getName());
				
				addLog(search.getName() + " search exists in database");
				
			} else {
				
				search = db.addSearch(search);
				
				searches.add(search);
				searchqueries.add(search.getName());
				
				addLog("Search for " + search.getName() + " added to database");
				
			}
			
			settings.setSearchID(search.getID());
			
    		openTorrents();
			
		}
		
	}
	
	// Category
	private void openCategory(Category category) {
	
		if(!category.isParent()) {
	
			addLog("Category selected " + category.getName());
	
			settings.setCategoryID(category.getID());
	
			openTorrents();
	
		}
	
	}
    
    // Torrents
    private void openTorrents() {
    	
    	String url = "";
    	
    	if(db.getCategoryCount(settings.getCategoryID()) > 0 && settings.getCategoryID() > 0) {
    		
    		category = db.getCategory(settings.getCategoryID());
    		
    		url = proxy.getURL() + category.getURL() + "/0/7/0";
    		
    	} else if(db.getSearchCount(settings.getSearchID()) > 0) {
    		
    		search = db.getSearch(settings.getSearchID());
    		
    		url = proxy.getSearchURL() + "?q=" + search.getQuery();
    		
    	}
    	
    	loadSettings();
    	
    	loadProxy();
    	
    	openBrowser(url);
    	
    }
    
    // Share
	private void openShare() {
		
		addLog("Opening share");
		
		String message = Settings.getTwitterName();
		
		if(proxy.hasTwitter()) message += " " + proxy.getTwitterName();
		
		if(category.isParent()) {

			message += " " + category.getName() + " " + torrents + " " + categoriesclient.getURL();
			
		} else {

			message += " " + category.getParent().getName() + " " + category.getName()
					+ " " + torrents + " " + categoriesclient.getURL();
			
		}
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, message);
		
		startActivity(Intent.createChooser(intent, share));
		
	}
    
    // Browser
    private void openBrowser(String url) {
    	
    	try {
    	
    		addLog("Opening browser " + url);
    	
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    	
    		startActivity(intent);
    		
    	} catch (Exception e) {
    		
    		addLog("Error opening browser");
    		//e.printStackTrace();
    		
    	}
    	
    }
    
	// Connect
	private void showConnect(String text) {
		
		/*try {
		
		if(popup) {
		
		addLog("Showing connect dialog");
	
    	AlertDialog.Builder alert  = new AlertDialog.Builder(this);

		alert.setTitle(text);
	
		alert.setPositiveButton(connect, new DialogInterface.OnClickListener() {
			
        	public void onClick(DialogInterface dialog, int which) {
        		
        		loadThreads();

        		loadProxy();
        		
        	}
        	
    	});

    	// Cancel
		alert.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
			
            public void onClick(DialogInterface dialog, int which) {
            	
    			loadCategories();
            	
            }
            
    	});
	
		alert.setCancelable(true);
		
		alert.setIcon(R.drawable.proxies);
		
		alert.create().show();
		
		}
		
		} catch(Exception e) {}*/
	
    }
	
	// Clear search
	public void clearSearch() {
		
		searchfield.setTextColor(Color.DKGRAY);
		
		if(searchfield.getText().toString().contains(pirate)) {
			
			searchfield.setText("");
		
			addLog("Search field cleared");
		
		}
		
	}
	
	// Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	
    	// Categories drawer
    	if(v.getId() == R.id.main_drawer_categories) {
    		
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	    
    	    if(info.position < categories.size()) {

        		category = db.getCategory(categories.get(info.position).getID());
            	
            	addLog("Category context menu opened " + category.getName());
        		
        	    menu.setHeaderTitle(category.getName());
    	    
        	    for(int i = 0; i < array.length; i++) {
    	    	
        	    	if(i == INDEX_SUBSCRIBE) {
        	    		
        	    		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    	    		
        	    			if(category.isSubscribed()) {
            	    		
        	    				menu.add(Menu.NONE, i, i, R.string.unsubscribe);
    	    			
        	    			} else {
            	    		
        	    				menu.add(Menu.NONE, i, i, array[i]);
        	    			
        	    			}
    	    				
    	    				addLog("Context menu item added " + array[i]);
        	    			
        	    		} else {
        	    			
    	    				addLog("Context menu item excluded " + array[i]);
    	    				
        	    		}
    	    	
        	    	} else {
    	    		
        	    		menu.add(Menu.NONE, i, i, array[i]);
	    				
	    				addLog("Context menu item added " + array[i]);
    	    		
        	    	}
    	      
        	    }
        	
        	}
    	    
    	}
    	
    }
    
    // Context item
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
    	int index = item.getItemId();
    	String name = array[index];
    	
    	addLog("Category context menu item selected " + name);
    	
    	if(index == INDEX_SUBSCRIBE) {
    		
    		showToast(getpro);
    		
    	} else if(index == INDEX_SHARE) {
    		
    		openShare();
    		
    	}
		
		loadCategories();
    	
    	return true;
      
    }

    // Text change events
	@Override
	public void afterTextChanged(Editable view) {}

	@Override
	public void beforeTextChanged(CharSequence text, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
		
		//setSetting(SETTING_SEARCH_QUERY, text.toString());
		
	}

	// Key events
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		
		if(keyCode == EditorInfo.IME_ACTION_SEARCH ||
			keyCode == EditorInfo.IME_ACTION_DONE ||
			keyCode == KeyEvent.KEYCODE_ENTER &&
			event.getAction() == KeyEvent.ACTION_DOWN) {    
			
			openSearch();
			
		    return super.onKeyDown(keyCode, event);
			
		}
	
		return false;
		
	}

	// Clicks
	@Override
	public void onClick(View view) {
		
		switch(view.getId()) {
		
			case R.id.main_search_button:
				
				openSearch();
				
				break;
		
			case R.id.main_logo:
				
				openBrowser(proxy.getURL());
				
				break;
		
			case R.id.main_browse:
				
				 drawer.openDrawer(categorylist);
				
				break;
		
			case R.id.main_advert:
				
				openBrowser(Settings.getProMarket());
				
				break;
			
		}
		
	} 

	//@Override
	public void onFocusChange(View view, boolean focus) {
		
		if(focus) {
			
			clearSearch();
			
			new Runnable() {
				
				@Override
				public void run() {
					
					final InputMethodManager imm = (InputMethodManager)
							getSystemService(INPUT_METHOD_SERVICE);
					
					imm.showSoftInput(searchfield, InputMethodManager.SHOW_IMPLICIT);
					
				}
				
			};
			
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {

		adapter.setSelection(position);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapter) {}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		
		switch(adapter.getId()) {
		
			case R.id.main_search_editor:

				openSearch();
				
				break;
		
			case R.id.main_drawer_categories:
				
				openCategory(categories.get(position));
				
				break;
				
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    MenuInflater inflater = getMenuInflater();
	    
	    inflater.inflate(R.menu.main, menu);
	    
	    //if(!settings.isDebugEnabled()) menu.removeItem(R.id.logs);
	    
	    return true;
	    
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(toggle.onOptionsItemSelected(item)) return true;
		
	    switch(item.getItemId()) {
	    
	        //case R.id.settings:
	        	
        		//loadThreads();
	        	
	        	//Intent settingsintent = new Intent(this, SettingsActivity.class);
	        	
	        	//startActivity(settingsintent); 
	            
	    	    //return true;
		    	
		    case R.id.about:
			    	
	        	loadThreads();
		        	
		        Intent aboutintent = new Intent(this, AboutActivity.class);
		        
		        startActivity(aboutintent);
			    			
			    return true;
	            
	        default:
	        	
	            return super.onOptionsItemSelected(item); 
	            
	    }
	    
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		
		//popup = false;
		
		super.onNewIntent(intent);
		
	}
	
	@Override
	public void onDestroy() {
		
		loadThreads();
		
		super.onDestroy();
		
	}
    
}
