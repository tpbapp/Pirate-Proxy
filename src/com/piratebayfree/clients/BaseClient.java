package com.piratebayfree.clients;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.piratebayfree.Database;
import com.piratebayfree.Settings;
import com.piratebayfree.Status;

import android.content.Context;

public class BaseClient {

	private Database database;
	private Settings settings;
	
	private Document document;
	
	private boolean connected = false;
	private int count = 0;
	private String data = "";
	private String url = "";
	
	private static final String TAG = "BaseClient";
	
	public BaseClient(Context context) {
		
		setDatabase(new Database(context));
		setSettings(new Settings(context));
		
	}

	// Data
	public void loadData() {
		
		addLog("Connecting to " + getURL());
		
		try {
		
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, getSettings().getTimeout());
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(getURL());
        	HttpResponse response = client.execute(request);
        
        	setData(EntityUtils.toString(response.getEntity()));
        
        } catch (Exception e) {
        	
        	addLog("Could not connect to " + getURL(), Status.WARNING);
        	addLog(e.toString(), Status.WARNING);
        	
        	setConnected(false);
        	
        }
		
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	// Document
	public void loadDocument() {
		
		addLog("Connecting to " + getURL());
		
		try {
		
			document = Jsoup.connect(getURL())
					.userAgent(settings.getUserAgent())
					.timeout(settings.getTimeout())
					//.data("q", search.getQuery())
					//.data("category", String.valueOf(category.getID()))
					//.data("page", String.valueOf(getPage()))
					//.data("orderby", String.valueOf(getOrder()))
					.get();
        
        } catch (Exception e) {
        	
        	addLog("Could not connect to " + getURL(), Status.WARNING);
        	addLog(e.toString(), Status.WARNING);
        	
        	setConnected(false);
        	
        }
		
	}
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
        
	// Log
	public void addLog(String text) {
		
		if(settings.isDebugEnabled()) database.addLog(new Status(TAG, text));
		
	}
	
	public void addLog(String text, int status) {
		
		if(settings.isDebugEnabled()) database.addLog(new Status(TAG, text, status));
		
	}

	// Connection
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	// Count
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void increaseCount() {
		count++;
	}

	// URL
	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	// Settings
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	// Database
	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}