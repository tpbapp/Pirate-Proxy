package com.piratebayfree.clients;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piratebayfree.Database;
import com.piratebayfree.Proxy;
import com.piratebayfree.Settings;
import com.piratebayfree.Status;

import android.content.Context;
import android.text.format.Time;

public class ProxyClient {

	private Database db;
	private Settings settings;

	private Proxy proxy;
	
	private List<Proxy> proxies = new ArrayList<Proxy>();
	
	private String url = "";
	private boolean connected = false;
	private int count = 0;
	private int added = 0;
	
	private Document document;
	private Element titleelement;
	private Element searchelement;
	private Element twitterelement;
	//private Element footerelement;
	private Element bitcoinelement;
	private Element litecoinelement;

	static final String TITLE = "title";
	static final String SEARCH = "form[name=q]";
	//static final String TWITTER = "howdl";
	static final String TWITTER = "a[href*=twitter]";
	static final String FOOTER = "footer";
	static final String BITCOIN = "a[href^=bitcoin]";
	static final String LITECOIN = "a[href^=litecoin]";
	
	static final String TAG = "ProxyClient";

	public ProxyClient(Context context) {
		
		//super(context);
		
		db = new Database(context);
		settings = new Settings(context);

		// Default
        if(db.getProxyCount() == 0) {
        	
        	proxy = db.addProxy(new Proxy(Settings.getProxyURL()));
    		db.addProxy(new Proxy("http://pirateproxy.se"));
    		db.addProxy(new Proxy("http://pirateproxy.net"));
    		db.addProxy(new Proxy("http://tpbunblocker.com"));
    		db.addProxy(new Proxy("http://bayproxy.me"));
    		
        } else {
        	
    		proxy = new Proxy(Settings.getProxyURL());
    		
        }
		
		addLog("Proxy client started");
		
	}
	
	public void loadProxy() {
		
		proxies = db.getProxies();
		
		int p = 0;
		
		while(p < proxies.size() && !isConnected() && settings.isNetworkAvailable()) {
			
			proxy = proxies.get(p);
			
			proxy = loadProxy(proxy);
			
			if(isConnected()) break;
			
			p++;
   			
		 }
		
	}
	
	public Proxy loadProxy(Proxy proxy) {
		
		int rating = proxy.getRating();
		
		setURL(proxy.getURL());
		
		addLog("Connecting to proxy " + proxy.getURL()
				+ " timeout @ " + settings.getTimeout() + "ms");
			
		try {
			
			document = Jsoup.connect(getURL())
					.userAgent(settings.getUserAgent())
					.timeout(settings.getTimeout())
					.get();
			
			setURL(document.baseUri());
			
		} catch (Exception e) {
			
			setConnected(false);

			if(settings.isNetworkAvailable()) proxy.decreaseRating();

			addLog("Could not connect to " + url, Status.WARNING);
			addLog(e.toString(), Status.WARNING);
		
			//if(settings.isDebugEnabled()) e.printStackTrace();
			
		}
			
		try {
				
			titleelement = document.getElementsByTag(TITLE).first();
				
		} catch(Exception e) {
				
			addLog("Proxy has no title");
					
		}
			
		try {
				
			searchelement = document.select(SEARCH).first();

			//proxy.increaseRating();
				
		} catch(Exception e) {
				
			//addLog("Proxy has no search");
					
		}
			
		try {
				
			twitterelement = document.select(TWITTER).first();
			//twitterelement = document.getElementById(TWITTER).getElementsByTag("a").first();
				
		} catch(Exception e) {

			proxy.increaseRating();
					
			//addLog("Proxy has no Twitter URL");
					
		}
			
		try {
			
			bitcoinelement = document.select(BITCOIN).first();
			litecoinelement = document.select(LITECOIN).first();

			//proxy.increaseRating();
			
		} catch(Exception e) {
				
			//addLog("Proxy has no donate URL");
				
		}
				
		try {

			proxy.setTitle(titleelement.text());
			proxy.setSearchURL(searchelement.attr("abs:action"));
			
		} catch(Exception e) {

			//addLog("Proxy has no title");
					
		}
				
		try {
				
			proxy.setTwitterURL(twitterelement.attr("href"));
				
		} catch(Exception e) {

			//addLog("Proxy has no Twitter");
					
		}
				
		try {
					
			proxy.setBitcoinURL(bitcoinelement.attr("href"));
			proxy.setLitecoinURL(litecoinelement.attr("href"));
					
		} catch(Exception e) {

			//addLog("Proxy has no donate");
					
		}
			
		try {
			
			if(searchelement.hasAttr("action")) {

				settings.setProxyID(proxy.getID());

				proxy.setURL(document.baseUri());
		        
		        Time time = new Time();
		        time.setToNow();
				proxy.setTime(time.toMillis(false));

				proxy.increaseRating();
				
				setConnected(true);
				
				if(settings.isDebugEnabled()) {
					
					addLog("Connected to " + proxy.getURL(), Status.SUCCESS);
					if(rating > Proxy.RATING_MIN && rating < Proxy.RATING_MAX)
							addLog("Rating increased from " + rating
									+ " to " + proxy.getRating());
					
					//addLog("Search: " + searchelement.getElementById("inp").html());
					addLog("Search: " + proxy.getSearchURL());
					addLog("Title: " + proxy.getTitle());
					
					if(proxy.hasTwitter()) addLog("Twitter: " + proxy.getTwitterName() + " " + proxy.getTwitterURL());
					if(proxy.hasBitcoin()) addLog("Bitcoin: " + proxy.getBitcoinURL());
					if(proxy.hasLitecoin()) addLog("Litecoin: " + proxy.getLitecoinURL());
				
				}
				
				db.updateProxy(proxy);
					
				return proxy;
		            		
			} else {

				if(settings.isNetworkAvailable()) proxy.decreaseRating();
    			
				setConnected(false);

				addLog("Invalid page data at " + getURL());
				
				proxy.setDifference(proxy.getRating() - rating);
				
				if(proxy.getDifference() != 0) {
					
					addLog("Proxy rating changed " + proxy.getDifference());
					
				} else {
					
					addLog("Proxy rating unchanged " + proxy.getDifference());
					
				}
				
				addLog("Title: " + titleelement.text());
				//addLog("Search: " + searchelement.getElementById("inp").html());
				addLog("Search: " + proxy.getSearchURL());
				
				if(proxy.hasTwitter()) addLog("Twitter: " + twitterelement.attr("href"));
				if(proxy.hasBitcoin()) addLog("Bitcoin: " + bitcoinelement.attr("href"));
				if(proxy.hasLitecoin()) addLog("Litecoin: " + litecoinelement.attr("href"));
            				
			}
			
		} catch (Exception e) {

			if(settings.isNetworkAvailable()) proxy.decreaseRating();
			
			setConnected(false);

			addLog("Invalid page data at " + getURL());
			
			proxy.setDifference(proxy.getRating() - rating);
			
			if(proxy.getDifference() != 0) {
				
				addLog("Proxy rating changed " + proxy.getDifference());
				
			} else {
				
				addLog("Proxy rating unchanged " + proxy.getDifference());
				
			}
			
			//addLog("Title: " + titleelement.text());
			//addLog("Search: " + searchelement.getElementById("inp").html());
			//addLog("Search: " + proxy.getSearchURL());
			
			//if(proxy.hasTwitter()) addLog("Twitter: " + twitterelement.attr("href"));
			//if(proxy.hasBitcoin()) addLog("Bitcoin: " + bitcoinelement.attr("href"));
			//if(proxy.hasLitecoin()) addLog("Litecoin: " + litecoinelement.attr("href"));
			
		}
		
		db.updateProxy(proxy);
		
		return proxy;
		
	}
	
	/*public Proxy loadProxy(Proxy proxy) {
		
		int rating = proxy.getRating();
		
		setURL(proxy.getURL());
		
		addLog("Connecting to proxy " + proxy.getURL()
				+ " timeout @ " + settings.getTimeout() + "ms");
			
		try {
			
			document = Jsoup.connect(getURL())
					.userAgent(settings.getUserAgent())
					.timeout(settings.getTimeout())
					.get();
			
			setURL(document.baseUri());
			
		} catch (Exception e) {
			
			setConnected(false);

			if(settings.isNetworkAvailable()) proxy.decreaseRating();

			addLog("Could not connect to " + url, Status.WARNING);
			addLog(e.toString(), Status.WARNING);
		
			//if(settings.isDebugEnabled()) e.printStackTrace();
			
		}
			
		try {
				
			titleelement = document.getElementsByTag(TITLE).first();
				
		} catch(Exception e) {
				
			addLog("Proxy has no title", Status.WARNING);
					
		}
			
		try {
				
			searchelement = document.select(SEARCH).first();

			//proxy.increaseRating();
				
		} catch(Exception e) {

			if(settings.isNetworkAvailable()) proxy.decreaseRating();
				
			addLog("Proxy has no search form", Status.WARNING);
					
		}
			
		try {
				
			twitterelement = document.select(TWITTER).first();
			//twitterelement = document.getElementById(TWITTER).getElementsByTag("a").first();
				
		} catch(Exception e) {
					
			//addLog("Proxy has no Twitter URL");
					
		}
			
		try {
				
			//footerelement = document.getElementById(FOOTER);
			//bitcoinelement = footerelement.select(BITCOIN).first();
			//litecoinelement = footerelement.select(LITECOIN).first();
			bitcoinelement = document.select(BITCOIN).first();
			litecoinelement = document.select(LITECOIN).first();

			proxy.increaseRating();
			
		} catch(Exception e) {
				
			//addLog("Proxy has no donate URL");
				
		}
				
		try {

			proxy.setTitle(titleelement.text());
			proxy.setSearchURL(searchelement.attr("abs:action"));
			
		} catch(Exception e) {

			//addLog("Proxy has no title");
					
		}
				
		try {
				
			proxy.setTwitterURL(twitterelement.attr("href"));
				
		} catch(Exception e) {

			//addLog("Proxy has no Twitter");
					
		}
				
		try {
					
			proxy.setBitcoinURL(bitcoinelement.attr("href"));
			proxy.setLitecoinURL(litecoinelement.attr("href"));
					
		} catch(Exception e) {

			//addLog("Proxy has no donate");
					
		}
			
		try {
			
			if(searchelement.hasAttr("abs:action")) {

				settings.setProxyID(proxy.getID());

				proxy.setURL(document.baseUri());
		        
		        Time time = new Time();
		        time.setToNow();
				proxy.setTime(time.toMillis(false));

				proxy.increaseRating();
				
				setConnected(true);
				
				if(settings.isDebugEnabled()) {
					
					addLog("Connected to " + proxy.getURL(), Status.SUCCESS);
					if(rating > Proxy.RATING_MIN && rating < Proxy.RATING_MAX)
							addLog("Rating increased from " + rating
									+ " to " + proxy.getRating());

					addLog("Title: " + proxy.getTitle());
					addLog("Search: " + proxy.getSearchURL());
					
					if(proxy.hasTwitter()) addLog("Twitter: " + proxy.getTwitterName() + " " + proxy.getTwitterURL());
					if(proxy.hasBitcoin()) addLog("Bitcoin: " + proxy.getBitcoinURL());
					if(proxy.hasLitecoin()) addLog("Litecoin: " + proxy.getLitecoinURL());
				
				}
		            		
			} else {

				proxy.decreaseRating();
    			
				setConnected(false);

				addLog("Search field action attribute not found " + getURL());
				addLog("Rating decreased from " + rating + " to " + proxy.getRating());
				
				addLog("Title: " + titleelement.text());
				addLog("Search URL: " + proxy.getSearchURL());
				addLog("Search HTML: " + searchelement.getElementById("inp").html());
				addLog("Search Action: " + searchelement.attr("abs:action"));
				
				if(proxy.hasTwitter()) addLog("Twitter: " + twitterelement.attr("href"));
				if(proxy.hasBitcoin()) addLog("Bitcoin: " + bitcoinelement.attr("href"));
				if(proxy.hasLitecoin()) addLog("Litecoin: " + litecoinelement.attr("href"));
            				
			}
			
		} catch (Exception e) {

			if(settings.isNetworkAvailable()) proxy.decreaseRating();
			
			setConnected(false);

			addLog("Invalid page data at " + getURL());
			addLog("Rating decreased from " + rating + " to " + proxy.getRating());
			
			//addLog("Title: " + titleelement.text());
			//addLog("Search: " + searchelement.getElementById("inp").html());
			//addLog("Search: " + proxy.getSearchURL());
			
			if(proxy.hasTwitter()) addLog("Twitter: " + twitterelement.attr("href"));
			if(proxy.hasBitcoin()) addLog("Bitcoin: " + bitcoinelement.attr("href"));
			if(proxy.hasLitecoin()) addLog("Litecoin: " + litecoinelement.attr("href"));
			
		}
		
		proxy.setDifference(proxy.getRating() - rating);
		
		db.updateProxy(proxy);
		
		return proxy;
		
	}*/
	
	// Log
	private void addLog(String text) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text));
		
	}
	
	// Log
	private void addLog(String text, int status) {
		
		if(settings.isDebugEnabled()) db.addLog(new Status(TAG, text, status));
		
	}

	// Connection
	public boolean isConnected() {
		//connected = settings.isConnected();
		return connected;
	}

	public void setConnected(boolean connected) {
		settings.setConnected(connected);
		this.connected = connected;
	}

	// Count
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

	// URL
	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	// Proxy
	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	// Proxies
	public List<Proxy> getProxies() {
		return proxies;
	}

	public void setProxies(List<Proxy> proxies) {
		this.proxies = proxies;
	}
	
}
