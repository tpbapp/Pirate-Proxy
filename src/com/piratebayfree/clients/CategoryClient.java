package com.piratebayfree.clients;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.piratebayfree.Category;
import com.piratebayfree.Database;
import com.piratebayfree.Proxy;
import com.piratebayfree.Settings;
import com.piratebayfree.Status;

import android.content.Context;

public class CategoryClient {

	private Context context;
	private Database db;
	private Settings settings;

	private Proxy proxy;
	private Category category;

	private boolean connected = false;
	private int count = 0;
	private int added = 0;
	private String url;
	
	private Document document;
	private Elements categoryelements;

	private List<Category> categories = new ArrayList<Category>();
	
	private static final String CATEGORIES = "#categoriesTable a[href*=browse]";
	
	private static final String TAG = "CategoriesClient";

	public CategoryClient(Context context, Proxy proxy) {
		
		this.context = context;
		this.proxy = proxy;
		
		setURL(proxy.getBrowseURL());
		
		loadClient();

	}
	
	public void loadClient() {
		
		db = new Database(context);
		settings = new Settings(context);
		
        if(db.getCategoryCount() == 0) {
        	
        	db.addCategory(new Category("All", 0, true));
        	db.addCategory(new Category("Audio", 100, true));
        	db.addCategory(new Category("Video", 200, true));
        	db.addCategory(new Category("Applications", 300, true));
        	db.addCategory(new Category("Games", 400, true));
        	db.addCategory(new Category("Adult", 500, true));
        	db.addCategory(new Category("Other", 600, true));
    		
        }
		
	}
	
	public void loadCategories() {
		
		if(db.getCategoryCount() < 10) {
			
			addLog("Loading categories from " + getURL());
		
			try {
			
				// Connect
				document = Jsoup.connect(getURL())
						.userAgent(settings.getUserAgent())
						.timeout(settings.getTimeout())
						.get();
	    	
				setURL(document.baseUri());
	    	
				categoryelements = document.select(CATEGORIES);
			
				setCount(categoryelements.size());
	    	
				addLog(categoryelements.size() + " categories found");
	    	
				for(Element categoryelement : categoryelements) { 
	    		
					// Category
					String[] categoryarray = categoryelement.attr("href").split("/");
	    		
					int categoryid = Integer.parseInt(categoryarray[categoryarray.length - 1]);
	    		
					category = new Category(categoryelement.text());
	    		
					category.setID(categoryid);
	    			category.setURL(categoryelement.attr("href"));

	    			// Parent
	    			if(category.getID() > 100 && category.getID() < 200) {
					
	    				category.setParent(db.getCategory(100));
    				
	    			} else if(category.getID() > 200 && category.getID() < 300) {
					
    					category.setParent(db.getCategory(200));
    				
	    			} else if(category.getID() > 300 && category.getID() < 400) {
					
	    				category.setParent(db.getCategory(300));
    				
	    			} else if(category.getID() > 400 && category.getID() < 500) {
					
	    				category.setParent(db.getCategory(400));
    				
	    			} else if(category.getID() > 500 && category.getID() < 600) {
					
	    				category.setParent(db.getCategory(500));
    				
	    			} else if(category.getID() > 600) {
					
	    				category.setParent(db.getCategory(600));
    				
	    			} else {
					
	    				category.setParent(true);
					
	    			}
	    		
	    			// Save
	    			if(db.getCategoryCount(category) == 0) {
	    		
	    				//category = db.addCategory(category);
	    				
	    				db.addCategory(category);
	    			
	    				increaseAdded();
	    			
	    				if(category.hasParent()) {
	    			
	    					addLog("Category added " + category.getParent().getName()
	    							+ " > " + category.getName()
	    							+ " " + category.getID()
	    							+ " " + category.getURL());
	    				
	    				} else {
	    			
	    					addLog("Category added " + category.getName()
	    							+ " " + category.getID()
	    							+ " " + category.getURL());
	    				
	    				}
	    			
	    			} else {
			    		
			    		category = db.getCategory(category.getID());
	    			
	    				if(category.isParent()) {
	    			
	    					addLog("Category found " + category.getName()
	    							+ " " + category.getID()
	    							+ " " + category.getURL());
	    				
	    				} else {
	    			
	    					addLog("Category found " + category.getParent().getName()
	    							+ " > " + category.getName()
	    							+ " " + category.getID()
	    							+ " " + category.getURL());
	    				
	    				}
		    		
	    			}
    			
    			/*if(settings.isAdultEnabled() && category.isAdult()) {
	    			
    				if(category.isParent()) {
    	    			
	    				addLog("Category removed " + category.getName()
	    						+ " " + category.getURL());
	    				
	    			} else {
	    			
	    				addLog("Category removed " + category.getParent().getName()
	    						+ " > " + category.getName()
	    						+ " " + category.getURL());
	    				
	    			}
    				
    				categories.remove(category);
    				
    			} else {
		    	
    				categories.add(category);
    				
    			}*/
		    	
					categories.add(category);
	      		
	    		}
	    	
	    		if(categories.size() > 0) {
	    	
	    			proxy.increaseRating();
			
					addLog("Proxy rating increased " + proxy.getName(), Status.SUCCESS);
    		
					setConnected(true);
				
	    		}
				
			} catch (Exception e) {
	    	
    		//if(settings.isNetworkAvailable()) {
			
				//addLog("Proxy rating decreased " + proxy.getName());
    			
    			//proxy.decreaseRating();
    			
    		//}
	    		
	    		addLog("Error loading categories", Status.WARNING);
	    		addLog(e.toString(), Status.WARNING);
	    	
	    		//if(settings.isDebugEnabled()) e.printStackTrace();
		
				setConnected(false);
				
			}
    	
			db.updateProxy(proxy);
		
		}
		
	}

	// Log
	private void addLog(String text) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text));
		
	}
	
	private void addLog(String text, int status) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text, status));
		
	}

	public boolean isConnected() {
		//connected = settings.isConnected();
		return connected;
	}

	public void setConnected(boolean connected) {
		//settings.setConnected(connected);
		this.connected = connected;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAdded() {
		return added;
	}

	public void setAdded(int added) {
		this.added = added;
	}
	
	private void increaseAdded() {
		added++;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public List<Category> getCategories() {
		return categories;
	}

}
