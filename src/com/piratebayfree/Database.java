package com.piratebayfree;

import java.util.ArrayList;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class Database extends SQLiteOpenHelper {
 
    // Database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tpbfree";
 
    // Table name
    private static final String TABLE_PROXIES = "proxies";
    private static final String TABLE_SEARCHES = "searches";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_LOGS = "logs";
 
    // Proxies table
    public static final String PROXY_ID = "proxy_id";
    public static final String PROXY_URL = "proxy_url";
    public static final String PROXY_SEARCH = "proxy_search";
    public static final String PROXY_SOURCE = "proxy_source";
    public static final String PROXY_BITCOIN = "proxy_bitcoin";
    public static final String PROXY_LITECOIN = "proxy_litecoin";
    public static final String PROXY_TWITTER = "proxy_twitter";
    public static final String PROXY_TITLE = "proxy_title";
    public static final String PROXY_RATING = "proxy_rating";
    public static final String PROXY_STATUS = "proxy_status";
    public static final String PROXY_TIME = "proxy_time";
    public static final int PROXY_LIMIT = 120;
 
    // Searches table
    public static final String SEARCH_ID = "search_id";
    public static final String SEARCH_NAME = "search_name";
    public static final String SEARCH_QUERY = "search_query";
    public static final String SEARCH_STATUS = "search_status";
    public static final String SEARCH_TIME = "search_time";
    public static final int SEARCH_LIMIT = 120;
    
    // Categories table
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_URL = "category_url";
    public static final String CATEGORY_STATUS = "category_status";
    public static final String CATEGORY_PARENT = "category_parent";
    public static final int CATEGORY_LIMIT = 60;
    
    // Logs table
    public static final String LOG_ID = "log_id";
    public static final String LOG_TAG = "log_tag";
    public static final String LOG_CONTENT = "log_content";
    public static final String LOG_STATUS = "log_status";
    public static final String LOG_TIME = "log_time";
    public static final int LOG_LIMIT = 120;
 
    public Database(Context context) {
    	
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
 
    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Proxies
        String CREATE_TABLE_PROXIES = "CREATE TABLE " + TABLE_PROXIES + "("
        		+ PROXY_ID + " INTEGER PRIMARY KEY, "
                + PROXY_URL + " TEXT, "
                + PROXY_SEARCH + " TEXT, "
                + PROXY_SOURCE + " TEXT, "
                + PROXY_BITCOIN + " TEXT, "
                + PROXY_LITECOIN + " TEXT, "
                + PROXY_TWITTER + " TEXT, "
                + PROXY_TITLE + " TEXT, "
        		+ PROXY_RATING + " INTEGER, "
                + PROXY_STATUS + " INTEGER, "
                + PROXY_TIME + " LONG)";
    	
        // Searches
        String CREATE_TABLE_SEARCHES = "CREATE TABLE " + TABLE_SEARCHES + "("
        		+ SEARCH_ID + " INTEGER PRIMARY KEY, "
                + SEARCH_NAME + " TEXT UNIQUE, "
                + SEARCH_QUERY + " TEXT, "
                + SEARCH_STATUS + " INTEGER, "
                + SEARCH_TIME + " INTEGER)";

        // Categories
        String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
        		+ CATEGORY_ID + " INTEGER, "
                + CATEGORY_NAME + " TEXT, "
                + CATEGORY_URL + " TEXT, "
                + CATEGORY_STATUS + " INTEGER, "
                + CATEGORY_PARENT + " INTEGER)";
    	
        // Logs
        String CREATE_TABLE_LOGS = "CREATE TABLE " + TABLE_LOGS + "("
        		+ LOG_ID + " INTEGER PRIMARY KEY, "
                + LOG_TAG + " TEXT, "
                + LOG_CONTENT + " TEXT, "
                + LOG_TIME + " INTEGER, "
                + LOG_STATUS + " INTEGER)";
        
        db.execSQL(CREATE_TABLE_PROXIES);
        db.execSQL(CREATE_TABLE_SEARCHES);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_LOGS);
        
        //reset(db);
        
    }
 
    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    	reset(db);
        
    }
    
    // Reset Database
    public void reset(SQLiteDatabase db) {
    	
        // Drop tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROXIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
 
        // Recreate tables
        onCreate(db);
    	
    }
 	
    
	/*
	 * Proxies
	 */
    
    // Add proxy
    public Proxy addProxy(Proxy proxy) {
       	
    	SQLiteDatabase db = this.getWritableDatabase();
        
    	ContentValues values = new ContentValues();

    	values.put(PROXY_URL, proxy.getURL());
    	values.put(PROXY_SEARCH, proxy.getSearchURL());
    	values.put(PROXY_SOURCE, proxy.getSourceURL());
    	values.put(PROXY_BITCOIN, proxy.getBitcoinURL());
    	values.put(PROXY_LITECOIN, proxy.getLitecoinURL());
    	values.put(PROXY_TWITTER, proxy.getTwitterURL());
    	values.put(PROXY_TITLE, proxy.getTitle());
    	values.put(PROXY_RATING, proxy.getRating());
    	values.put(PROXY_STATUS, proxy.getStatus());
    	values.put(PROXY_TIME, proxy.getTime());

    	int id = (int) db.insert(TABLE_PROXIES, null, values);
        
    	db.close();
        
    	proxy.setID(id);
        
        return proxy;
        
    }

    // Update proxy
	public void updateProxy(Proxy proxy) {
		
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();

        values.put(PROXY_SEARCH, proxy.getSearchURL());
		values.put(PROXY_SOURCE, proxy.getSourceURL());
		values.put(PROXY_BITCOIN, proxy.getBitcoinURL());
		values.put(PROXY_LITECOIN, proxy.getLitecoinURL());
		values.put(PROXY_TWITTER, proxy.getTwitterURL());
		values.put(PROXY_TITLE, proxy.getTitle());
        values.put(PROXY_RATING, proxy.getRating());
    	values.put(PROXY_STATUS, proxy.getStatus());
    	values.put(PROXY_TIME, proxy.getTime());
 
        db.update(TABLE_PROXIES, values, PROXY_ID + " = ?",
        		new String[] { String.valueOf(proxy.getID())});
        
        db.close();
        
    }
    
    // Get proxy
    public Proxy getProxy(int id) {
    	
    	Proxy proxy = null;

        String query = "SELECT * FROM " + TABLE_PROXIES
        		+ " WHERE " + PROXY_ID + "='" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
           	
        	proxy = new Proxy(cursor.getString(cursor.getColumnIndex(PROXY_URL)));

     	    proxy.setID(cursor.getInt(cursor.getColumnIndex(PROXY_ID)));
     	    proxy.setSearchURL(cursor.getString(cursor.getColumnIndex(PROXY_SEARCH)));
     	    proxy.setSourceURL(cursor.getString(cursor.getColumnIndex(PROXY_SOURCE)));
     	    proxy.setBitcoinURL(cursor.getString(cursor.getColumnIndex(PROXY_BITCOIN)));
     	    proxy.setLitecoinURL(cursor.getString(cursor.getColumnIndex(PROXY_LITECOIN)));
     	    proxy.setTwitterURL(cursor.getString(cursor.getColumnIndex(PROXY_TWITTER)));
     	    proxy.setTitle(cursor.getString(cursor.getColumnIndex(PROXY_TITLE)));
     	    proxy.setRating(cursor.getInt(cursor.getColumnIndex(PROXY_RATING)));
     	    proxy.setStatus(cursor.getInt(cursor.getColumnIndex(PROXY_STATUS)));
     	    proxy.setTime(cursor.getLong(cursor.getColumnIndex(PROXY_TIME)));
           
        }
        
        cursor.close();
        
        db.close();

        return proxy;
       
    }
    
    // Get top proxy
    public Proxy getTopProxy() {
    	
    	Proxy proxy = null;

        String select = "SELECT * FROM " + TABLE_PROXIES
        		+ " ORDER BY " + PROXY_RATING + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()) {
           	
        	proxy = new Proxy(cursor.getString(cursor.getColumnIndex(PROXY_URL)));

     	    proxy.setID(cursor.getInt(cursor.getColumnIndex(PROXY_ID)));
     	    proxy.setSearchURL(cursor.getString(cursor.getColumnIndex(PROXY_SEARCH)));
     	    proxy.setSourceURL(cursor.getString(cursor.getColumnIndex(PROXY_SOURCE)));
     	    proxy.setBitcoinURL(cursor.getString(cursor.getColumnIndex(PROXY_BITCOIN)));
     	    proxy.setLitecoinURL(cursor.getString(cursor.getColumnIndex(PROXY_LITECOIN)));
     	    proxy.setTwitterURL(cursor.getString(cursor.getColumnIndex(PROXY_TWITTER)));
     	    proxy.setTitle(cursor.getString(cursor.getColumnIndex(PROXY_TITLE)));
     	    proxy.setRating(cursor.getInt(cursor.getColumnIndex(PROXY_RATING)));
     	    proxy.setStatus(cursor.getInt(cursor.getColumnIndex(PROXY_STATUS)));
     	    proxy.setTime(cursor.getLong(cursor.getColumnIndex(PROXY_TIME)));
           
        }
        
        cursor.close();
        
        db.close();

        return proxy;
       
    }
    
    // Get proxies
    public ArrayList<Proxy> getProxies() {
   	
    	ArrayList<Proxy> proxies = new ArrayList<Proxy>();

        String selectQuery = "SELECT * FROM " + TABLE_PROXIES
        		+ " ORDER BY " + PROXY_RATING + " DESC LIMIT " + PROXY_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        if(cursor.moveToFirst()) {
        	
        	int i = 0;
       	
            do {
               
        	    Proxy proxy = new Proxy(cursor.getString(cursor.getColumnIndex(PROXY_URL)));

        	    proxy.setID(cursor.getInt(cursor.getColumnIndex(PROXY_ID)));
        	    proxy.setSearchURL(cursor.getString(cursor.getColumnIndex(PROXY_SEARCH)));
         	    proxy.setSourceURL(cursor.getString(cursor.getColumnIndex(PROXY_SOURCE)));
         	    proxy.setBitcoinURL(cursor.getString(cursor.getColumnIndex(PROXY_BITCOIN)));
         	    proxy.setLitecoinURL(cursor.getString(cursor.getColumnIndex(PROXY_LITECOIN)));
         	    proxy.setTwitterURL(cursor.getString(cursor.getColumnIndex(PROXY_TWITTER)));
         	    proxy.setTitle(cursor.getString(cursor.getColumnIndex(PROXY_TITLE)));
        	    proxy.setRating(cursor.getInt(cursor.getColumnIndex(PROXY_RATING)));
         	    proxy.setStatus(cursor.getInt(cursor.getColumnIndex(PROXY_STATUS)));
         	    proxy.setTime(cursor.getLong(cursor.getColumnIndex(PROXY_TIME)));
        	    proxy.setIndex(i);
        	   
        	    proxies.add(proxy);
        	    
        	    i++;
               
            } while(cursor.moveToNext());
           
        }
        
        cursor.close();
        
        db.close();

        return proxies;
       
    }
    
    // Get proxies
    public ArrayList<Proxy> getTopProxies() {
   	
    	ArrayList<Proxy> proxies = new ArrayList<Proxy>();

        String query = "SELECT * FROM " + TABLE_PROXIES
        		+ " WHERE " + PROXY_STATUS + " != " + Proxy.STATUS_SUSPENDED
        		+ " ORDER BY " + PROXY_RATING + " DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
 
        if(cursor.moveToFirst()) {
        	
        	int i = 0;
       	
            do {
               
        	    Proxy proxy = new Proxy(cursor.getString(cursor.getColumnIndex(PROXY_URL)));

        	    proxy.setID(cursor.getInt(cursor.getColumnIndex(PROXY_ID)));
        	    proxy.setSearchURL(cursor.getString(cursor.getColumnIndex(PROXY_SEARCH)));
         	    proxy.setSourceURL(cursor.getString(cursor.getColumnIndex(PROXY_SOURCE)));
         	    proxy.setBitcoinURL(cursor.getString(cursor.getColumnIndex(PROXY_BITCOIN)));
         	    proxy.setLitecoinURL(cursor.getString(cursor.getColumnIndex(PROXY_LITECOIN)));
         	    proxy.setTwitterURL(cursor.getString(cursor.getColumnIndex(PROXY_TWITTER)));
         	    proxy.setTitle(cursor.getString(cursor.getColumnIndex(PROXY_TITLE)));
        	    proxy.setRating(cursor.getInt(cursor.getColumnIndex(PROXY_RATING)));
         	    proxy.setStatus(cursor.getInt(cursor.getColumnIndex(PROXY_STATUS)));
         	    proxy.setTime(cursor.getLong(cursor.getColumnIndex(PROXY_TIME)));
        	    proxy.setIndex(i);
        	   
        	    proxies.add(proxy);
        	    
        	    i++;
               
            } while(cursor.moveToNext());
           
        }
        
        cursor.close();
        
        db.close();

        return proxies;
       
    }
    
    // Get proxy count
    public int getProxyCount() {
    	
        String query = "SELECT * FROM " + TABLE_PROXIES;
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
    public int getProxyCount(int id) {
    	
        String query = "SELECT * FROM " + TABLE_PROXIES
        		+ " WHERE " + PROXY_ID + "='" + id + "'"
				+ " LIMIT 1";
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
    public int getProxyCount(Proxy proxy) {
    	
        String query = "SELECT * FROM " + TABLE_PROXIES
        		+ " WHERE " + PROXY_URL + " = ?"
        		+ " LIMIT 1";
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, new String[] { proxy.getURL() });
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
 	// Delete proxy
 	public void deleteProxy(Proxy proxy) {
 	   
 		SQLiteDatabase db = this.getWritableDatabase();
        
 		db.delete(TABLE_PROXIES, PROXY_URL + " = ?",
 				new String[] { String.valueOf(proxy.getURL()) });
        
 		db.close();
        
     }
	   
	// Delete all proxies
	public void deleteProxies() {
		   
		SQLiteDatabase db = this.getWritableDatabase();
	       
		db.delete(TABLE_PROXIES, null, null);
	       
		db.close();
	       
	}
 	
 	
	/*
	 * Searches
	 */
   
    // Add search
    public Search addSearch(Search search) {
   	
        SQLiteDatabase db = this.getWritableDatabase();
       
        ContentValues values = new ContentValues();

        values.put(SEARCH_NAME, search.getName());
        values.put(SEARCH_QUERY, search.getQuery());
        values.put(SEARCH_STATUS, search.getStatus());

        search.setID((int) db.insert(TABLE_SEARCHES, null, values));
       
        db.close();
        
        return search;
       
    }

    // Update search
	public void updateSearch(Search search) {
		
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();

        values.put(SEARCH_NAME, search.getName());
        values.put(SEARCH_QUERY, search.getQuery());
        values.put(SEARCH_STATUS, search.getStatus());
 
        db.update(TABLE_SEARCHES, values, SEARCH_ID + " = ?",
        		new String[] { String.valueOf(search.getID())});
        
        db.close();
        
    }
    
    // Get search
    public Search getSearch(int id) {
    	
    	Search search = null;

        String select = "SELECT * FROM " + TABLE_SEARCHES
        		+ " WHERE " + SEARCH_ID + "='" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()) {
           	
        	search = new Search(cursor.getString(cursor.getColumnIndex(SEARCH_NAME)));
        	
        	search.setID(cursor.getInt(cursor.getColumnIndex(SEARCH_ID)));
        	search.setQuery(cursor.getString(cursor.getColumnIndex(SEARCH_QUERY)));
        	search.setStatus(cursor.getInt(cursor.getColumnIndex(SEARCH_STATUS)));
           
        }
        
        cursor.close();
        
        db.close();

        return search;
       
    }
    
    public Search getSearch(String name) {
    	
    	Search search = null;

        String query = "SELECT * FROM " + TABLE_SEARCHES
        		+ " WHERE " + SEARCH_NAME + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(query, new String[] { name });

        if(cursor.moveToFirst()) {
           	
        	search = new Search(cursor.getString(cursor.getColumnIndex(SEARCH_NAME)));
        	
        	search.setID(cursor.getInt(cursor.getColumnIndex(SEARCH_ID)));
        	search.setQuery(cursor.getString(cursor.getColumnIndex(SEARCH_QUERY)));
        	search.setStatus(cursor.getInt(cursor.getColumnIndex(SEARCH_STATUS)));
           
        }
        
        cursor.close();
        
        db.close();

        return search;
       
    }
    
    // Get searches
    public ArrayList<Search> getSearches() {
   	
    	ArrayList<Search> searches = new ArrayList<Search>();

        String query = "SELECT * FROM " + TABLE_SEARCHES;

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
       	
            do {
               	
            	Search search = new Search(cursor.getString(cursor.getColumnIndex(SEARCH_NAME)));
            	
            	search.setID(cursor.getInt(cursor.getColumnIndex(SEARCH_ID)));
            	search.setQuery(cursor.getString(cursor.getColumnIndex(SEARCH_QUERY)));
            	search.setStatus(cursor.getInt(cursor.getColumnIndex(SEARCH_STATUS)));
            	
                searches.add(search);
               
            } while(cursor.moveToNext());
           
        }
        
        cursor.close();
        
        db.close();

        return searches;
       
    } 

    // Get search count
    public int getSearchCount() {
    	
        String query = "SELECT * FROM " + TABLE_SEARCHES;
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
    public int getSearchCount(int id) {
    	
        String query = "SELECT * FROM " + TABLE_SEARCHES
        		+ " WHERE " + SEARCH_ID + "='" + id + "'";
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
    public int getSearchCount(String name) {
    	
        String query = "SELECT * FROM " + TABLE_SEARCHES
        		+ " WHERE " + SEARCH_NAME + " = ?";
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, new String[] { name });
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
    public int getSearchCount(Search search) {
    	
        String query = "SELECT * FROM " + TABLE_SEARCHES
        		+ " WHERE " + SEARCH_NAME + " = ?";
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, new String[] { search.getName() });
        
        int searchcount = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return searchcount;
        
    }
   
	// Delete search
	public void deleteSearch(Search search) {
	   
		SQLiteDatabase db = this.getWritableDatabase();
       
		db.delete(TABLE_SEARCHES, SEARCH_ID + " = ?",
				new String[] { String.valueOf(search.getID()) });
       
		db.close();
       
    }
   
	// Delete all searches
	public void deleteSearches() {
	   
		SQLiteDatabase db = this.getWritableDatabase();
       
		db.delete(TABLE_SEARCHES, null, null);
       
		db.close();
       
    }

	
	/*
	 * Categories
	 */
	
	// Category values
	private ContentValues getCategoryValues(Category category) {
       
        ContentValues values = new ContentValues();

        values.put(CATEGORY_ID, category.getID());
        values.put(CATEGORY_NAME, category.getName());
        values.put(CATEGORY_URL, category.getURL());
        values.put(CATEGORY_STATUS, category.getStatus());
        
        if(!category.isParent()) {
        	
            values.put(CATEGORY_PARENT, category.getParent().getID());
            
        } else {
        	
            values.put(CATEGORY_PARENT, 0);
        	
        }
        
        return values;
		
	}
   
    // Add category
    public void addCategory(Category category) {
   	
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CATEGORIES, null, getCategoryValues(category));
       
        db.close();
       
    }

    // Update category
	public void updateCategory(Category category) {
		
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.update(TABLE_CATEGORIES, getCategoryValues(category),
        		CATEGORY_ID + " = ?", new String[] { String.valueOf(category.getID()) });
        
        db.close();
        
    }
    
    // Get category
    public Category getCategory(int id) {
    	
    	Category category = null;

        String query = "SELECT * FROM " + TABLE_CATEGORIES
        		+ " WHERE " + CATEGORY_ID + "='" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
        	
        	boolean isparent = false;
        	
        	if(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT)) == 0) isparent = true;
           	
        	category = new Category(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)), isparent);

        	category.setID(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
        	category.setURL(cursor.getString(cursor.getColumnIndex(CATEGORY_URL)));
        	category.setStatus(cursor.getInt(cursor.getColumnIndex(CATEGORY_STATUS)));
        	
        	if(!isparent) category.setParent(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT))));
           
        }
        
        cursor.close();
        
        db.close();

        return category;
       
    }
    
    // Get categories
    public ArrayList<Category> getCategories() {
   	
    	ArrayList<Category> categories = new ArrayList<Category>();
    	
        String query = "SELECT * FROM " + TABLE_CATEGORIES
        		+ " ORDER BY " + CATEGORY_ID + " LIMIT " + CATEGORY_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
       	
            do {
            	
            	boolean isparent = false;
            	
            	if(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT)) == 0) isparent = true;
               	
            	Category category = new Category(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)), isparent);
            	
            	category.setID(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
            	category.setURL(cursor.getString(cursor.getColumnIndex(CATEGORY_URL)));
            	category.setStatus(cursor.getInt(cursor.getColumnIndex(CATEGORY_STATUS)));
            		
            	if(isparent) category.setParent(getCategory(cursor.getInt(cursor.getColumnIndex(CATEGORY_PARENT))));
            	
                categories.add(category);
               
            } while(cursor.moveToNext());
           
        }
        
        cursor.close();
        
        db.close();

        return categories;
       
    } 
    
    // Get categories
    public ArrayList<Category> getParentCategories() {
   	
    	ArrayList<Category> categories = new ArrayList<Category>();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES
        		+ " WHERE " + CATEGORY_PARENT + "='0'"
        		+ " ORDER BY " + CATEGORY_ID + " LIMIT " + CATEGORY_LIMIT;


        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
       	
            do {
               	
            	Category category = new Category(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)), true);
            	
            	category.setID(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)));
            	category.setURL(cursor.getString(cursor.getColumnIndex(CATEGORY_URL)));
            	category.setStatus(cursor.getInt(cursor.getColumnIndex(CATEGORY_STATUS)));
            	
                categories.add(category);
               
            } while(cursor.moveToNext());
           
        }
        
        cursor.close();
        
        db.close();

        return categories;
       
    } 
    
    // Get category count
    public int getCategoryCount() {
    	
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(query, null);
        
        int count = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return count;
        
    }
    
    public int getCategoryCount(int id) {
    	
        String countQuery = "SELECT * FROM " + TABLE_CATEGORIES
        		+ " WHERE " + CATEGORY_ID + "='" + id + "'"
                + " ORDER BY " + CATEGORY_ID + " LIMIT " + CATEGORY_LIMIT;
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int categorycount = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return categorycount;
        
    }
    
    public int getCategoryCount(String name) {
    	
        String countQuery = "SELECT * FROM " + TABLE_CATEGORIES
        		+ " WHERE " + CATEGORY_NAME + "='" + name + "'"
                + " ORDER BY " + CATEGORY_ID + " LIMIT " + CATEGORY_LIMIT;
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int categorycount = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return categorycount;
        
    }
    
    public int getCategoryCount(Category category) {
    	
        String countQuery = "SELECT * FROM " + TABLE_CATEGORIES
        		+ " WHERE " + CATEGORY_ID + "='" + category.getID() + "'"
                + " ORDER BY " + CATEGORY_ID + " LIMIT " + CATEGORY_LIMIT;
        
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int categorycount = cursor.getCount();
        
        cursor.close();
        
        db.close();
 
        return categorycount;
        
    }
   
	// Delete category
	public void deleteCategory(Category category) {
	   
		SQLiteDatabase db = this.getWritableDatabase();
       
		db.delete(TABLE_CATEGORIES, CATEGORY_ID + " = ?",
				new String[] { String.valueOf(category) });
       
		db.close();
       
    }
   
	// Delete all categories
	public void deleteCategories() {
	   
		SQLiteDatabase db = this.getWritableDatabase();
       
		db.delete(TABLE_CATEGORIES, null, null);
       
		db.close();
       
    }
 	
 	
	/*
	 * Logs
	 */
   
    // Add log
    public Status addLog(Status log) {
   	
        SQLiteDatabase db = this.getWritableDatabase();
       
        ContentValues values = new ContentValues();

        values.put(LOG_TAG, log.getTag());
        values.put(LOG_CONTENT, log.getContent());
        values.put(LOG_STATUS, log.getStatus());
        values.put(LOG_TIME, log.getTime());

        int id = (int) db.insert(TABLE_LOGS, null, values);
       
        db.close();
        
        log.setID(id);
        
        return log;
       
    }
    
    // Get logs
    public ArrayList<Status> getLogs() {
   	
    	ArrayList<Status> logs = new ArrayList<Status>();

        String select = "SELECT * FROM " + TABLE_LOGS
                + " ORDER BY " + LOG_ID; // + " LIMIT " + LOG_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()) {
       	
            do {
           	
        		Status log = new Status(
        			cursor.getString(cursor.getColumnIndex(LOG_TAG)),
        			cursor.getString(cursor.getColumnIndex(LOG_CONTENT)),
        			cursor.getInt(cursor.getColumnIndex(LOG_STATUS))
        		);
        	
        		log.setID(cursor.getInt(cursor.getColumnIndex(LOG_ID)));
        		log.setTime(cursor.getInt(cursor.getColumnIndex(LOG_TIME)));
        		
        		logs.add(log);
               
            } while(cursor.moveToNext());
           
        }
        
        cursor.close();
        
        db.close();

        return logs;
       
    }
   
	// Delete all logs
	public void deleteLogs() {
	   
		SQLiteDatabase db = this.getWritableDatabase();
       
		db.delete(TABLE_LOGS, null, null);
       
		db.close();
       
    }
	
	
	/*
	 * Utils
	 */

	// String to time
	//public int formatDate(String date) {
		
	//}
	
	// Escape string
	public String escape(String str) {
		//str = str.replace("'", "\'");
		str = str.replace("'", "''");
		//str = str.replace('"', '\"');
		//str = str.replace("UN-iQUE", "UNiQUE");
		//str = DatabaseUtils.sqlEscapeString(str);
		return str;
	}
	
	// Unescape string
	public String unescape(String str) {
		//str = str.replace("\'", "'");
		str = str.replace("''", "'");
		//str = str.replace('\"', '"');
		//str = str.replace("UNiQUE", "UN-iQUE");
		return str;
	}
	
 
}
