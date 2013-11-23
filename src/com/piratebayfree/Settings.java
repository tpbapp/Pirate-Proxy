package com.piratebayfree;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Settings {

	private Context context;
	
	private SharedPreferences settings;

	private boolean history = false;
	private boolean adult = true;
	private boolean debug = false;
	
	private int proxy = 1;
	private int category = 0;
	private int search = 0;
	//private int order = 7;
    
	private int timeout = 3000;
    private String agent = "TPB Free";
	private boolean connected = false;

    private static final String URL_TPB = "http://thepiratebay.sx";
    private static final String URL_DEVELOPER = "http://tpbapp.com";
    private static final String URL_BITCOIN = "bitcoin:1KeBs4HBQzkdHC2ou3gpyGHqcL7aKzwTve";
    
    private static final String TWITTER_URL = "https://twitter.com/tpbapp";
    private static final String TWITTER_NAME = "@tpbapp";

    private static final String MARKET_TPB = "market://details?id=com.piratebaypro";
    private static final String MARKET_BITCOIN = "market://details?id=de.schildbach.wallet";
    
    private static final String SETTING_HISTORY = "history";
    private static final String SETTING_ADULT = "adult";
    private static final String SETTING_DEBUG = "debug";
    
    private static final String SETTING_PROXY_ID = "proxy";
    private static final String SETTING_SEARCH_ID = "search";
    private static final String SETTING_CATEGORY_ID = "category";
    
    private static final String SETTING_TIMEOUT = "timeout";
    private static final String SETTING_USER_AGENT = "agent";
    private static final String SETTING_CONNECTED = "connected";

    //private static final String TAG = "Settings";
	
	public Settings(Context context) {
		
		this.context = context;

		settings = PreferenceManager.getDefaultSharedPreferences(context);

		history = settings.getBoolean(SETTING_HISTORY, history);
		adult = settings.getBoolean(SETTING_ADULT, adult);
		debug = settings.getBoolean(SETTING_DEBUG, debug);
		timeout = settings.getInt(SETTING_TIMEOUT, timeout);
		
		//order = Integer.parseInt(settings.getString(SETTING_ORDER, String.valueOf(order)));
		
	}

	// Search history
	public void setHistory(boolean history) {
		
		save(SETTING_HISTORY, history);
		
		this.history = history;
		
	}
	
	public boolean isHistoryEnabled() {
		
		history = settings.getBoolean(SETTING_HISTORY, history);
		
		return history;
		
	}
	
	// Adult content
	public void setAdult(boolean adult) {
		
		//save(SETTING_ADULT, adult);
		
		this.adult = adult;
		
	}
	
	public boolean isAdultEnabled() {
		
		//adult = settings.getBoolean(SETTING_ADULT, debug);
		
		return adult;
		
	}
	
	// Debug mode
	public void setDebug(boolean debug) {
		
		//save(SETTING_DEBUG, debug);
		
		this.debug = debug;
		
	}
	
	public boolean isDebugEnabled() {
		
		debug = settings.getBoolean(SETTING_DEBUG, debug);
		
		return debug;
		
	}
	
	// Proxy ID
	public void setProxyID(int id) {
		
		save(SETTING_PROXY_ID, id);
		
		this.proxy = id;
		
	}
	
	public int getProxyID() {
		
		proxy = settings.getInt(SETTING_PROXY_ID, proxy);
		
		return proxy;
		
	}
	
	// Search ID
	public void setSearchID(int id) {
		
		save(SETTING_SEARCH_ID, id);
		
		this.search = id;
		
	}
	
	public int getSearchID() {
		
		search = settings.getInt(SETTING_SEARCH_ID, search);
		
		return search;
		
	}
	
	// Category ID
	public void setCategoryID(int id) {
		
		save(SETTING_CATEGORY_ID, id);
		
		this.category = id;
		
	}
	
	public int getCategoryID() {
		
		category = settings.getInt(SETTING_CATEGORY_ID, category);
		
		return category;
		
	}
	
	// TPB
	public static String getProxyURL() {
		return URL_TPB;
	}
	
	public static String getBitcoinURL() {
		return URL_BITCOIN;
	}
	
	// Developer
	public static String getDeveloperURL() {
		return URL_DEVELOPER;
	}
	
	public static String getTwitterURL() {
		return TWITTER_URL;
	}
	
	// Twitter
	public static String getTwitterName() {
		return TWITTER_NAME;
	}
	
	// Market
	public static String getBitcoinMarket() {
		return MARKET_BITCOIN;
	}
	
	public static String getProMarket() {
		return MARKET_TPB;
	}

	// Connection
	public void setConnected(boolean connected) {
		
		save(SETTING_CONNECTED, connected);
		
		this.connected = connected;
		
	}
	
	public boolean isConnected() {
		
		connected = settings.getBoolean(SETTING_CONNECTED, connected);
		
		return connected;
		
	}
	
	// Timeout
	public int getTimeout() {
		
		//timeout = Integer.parseInt(settings.getString(SETTING_TIMEOUT, String.valueOf(timeout)));
		timeout = settings.getInt(SETTING_TIMEOUT, timeout);
		
		return timeout;
		
	}

	public void setTimeout(int timeout) {
		
		save(SETTING_TIMEOUT, timeout);
		
		this.timeout = timeout;
		
	}

	// Agent
	public String getUserAgent() {
		
		agent = settings.getString(SETTING_USER_AGENT, agent);
		
		return agent;
		
	}

	public void setUserAgent(String agent) {
		
		save(SETTING_USER_AGENT, agent);
		
		this.agent = agent;
		
	}
	
	// Network
	public boolean isNetworkAvailable() {

    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo net = cm.getActiveNetworkInfo();
    	
        if(net != null && net.isConnectedOrConnecting()) {
        	
        	return true;
        	
        } else {
        	
            return false;
            
        }
        
    }
	
	// Save
	public void save(String key, String value) {
		
		Editor editor = settings.edit();
		
		editor.putString(key, value);
		
		editor.commit();
		
	}
	
	public void save(String key, int value) {
		
		Editor editor = settings.edit();
		
		editor.putInt(key, value);
		
		editor.commit();
		
	}
	
	public void save(String key, boolean value) {
		
		Editor editor = settings.edit();
		
		editor.putBoolean(key, value);
		
		editor.commit();
		
	}
	
	// Reset
	public void reset() {

		save(SETTING_CONNECTED, false);

		save(SETTING_SEARCH_ID, 0);
		save(SETTING_CATEGORY_ID, 0);
		
	}

}
