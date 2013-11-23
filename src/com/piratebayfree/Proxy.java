package com.piratebayfree;

public class Proxy {

	private int id = 1;
	private int index = 0;
	private String name;
	private String url;
	private String browse;
	private String search;
	private String source;
	private String bitcoin = "";
	private String litecoin = "";
	private String twitter = "";
	private String protocol = "";
	private String news = "";
	private String title = "";
	private int rating = 3;
	private int difference = 0;
	private int status = 0;
	private long time = 0;
	private boolean active = false;
	
	public static final int RATING_MAX = 100;
	public static final int RATING_MIN = 0;
	
	public static final int STATUS_SUSPENDED = 1;
	
	public static final String PROTOCOL_HTTP = "http://";
	public static final String PROTOCOL_HTTPS = "https://";
	
	public Proxy(String url) {
		
		if(url.contains(PROTOCOL_HTTP)) {
			
			setName(url.replace(PROTOCOL_HTTP, ""));
			
			setProtocol(PROTOCOL_HTTP);
			
		} else if(url.contains(PROTOCOL_HTTPS)) {
			
			setName(url.replace(PROTOCOL_HTTPS, ""));
			
			setProtocol(PROTOCOL_HTTPS);
			
		} else {
			
			setName(url);
			
		}
		
		setURL(url);
		setSearchURL(url + "/s/");
		setBrowseURL(url + "/browse");
		setNewsURL(url + "/feeds/blog");
		
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
        this.id = id;
	}
	
	public void setIndex(int index) {
        this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getURL() {
		return url;
	}
 
    public void setURL(String url) {
		url = url.replaceAll("/$", "");
        this.url = url;
    }
	
	public String getSearchURL() {
		return search;
	}
 
    public void setSearchURL(String search) {
    	search = search.replaceAll("/$", "");
        this.search = search + "/";
    }
	
	public String getBrowseURL() {
		return browse;
	}
 
    public void setBrowseURL(String browse) {
    	browse = browse.replaceAll("/$", "");
        this.browse = browse;
    }

	public String getSourceURL() {
		return source;
	}

	public void setSourceURL(String source) {
		this.source = source;
	}
	
	public String getBitcoinURL() {
		return bitcoin;
	}

	public void setBitcoinURL(String bitcoin) {
		this.bitcoin = bitcoin;
	}
	
	public boolean hasBitcoin() {
		return bitcoin.length() > 0;
	}

	public String getLitecoinURL() {
		return litecoin;
	}

	public void setLitecoinURL(String litecoin) {
		this.litecoin = litecoin;
	}
	
	public boolean hasLitecoin() {
		return litecoin.length() > 0;
	}

	public String getTwitterURL() {
		return twitter;
	}

	public void setTwitterURL(String twitter) {
		this.twitter = twitter;
	}
	
	public boolean hasTwitter() {
		return twitter.length() > 0;
	}

	public String getTwitterName() {
		
		String url[];
		String twitter = getTwitterURL().trim();
		
		try {
		
			url = twitter.split("/");
			twitter = url[url.length - 1];
				
		} catch(Exception e) {
			
			twitter.replace("http://", "").replace("/user", "");
			twitter.replace("#", "").replace("!", "").replace("~", "").replace("/", "");
			twitter.replace("twitter.com", "");
			
		}
		
		return "@" + twitter;
		
	}

	public String getNewsURL() {
		return news;
	}

	public void setNewsURL(String news) {
		news = news.replaceAll("/$", "");
		this.news = news;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	// Title
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean hasTitle() {
		return title.length() > 0;
	}

	// Rating
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
        this.rating = rating;
	}

	public void increaseRating() {
    	
        if(rating < RATING_MAX) {
        	
        	//rating += (int) Math.ceil(rating * 0.2);
        	
        	rating++;
        	
        }
        
    }
 
    public void decreaseRating() {
    	
        if(rating > RATING_MIN) {
        	
        	//rating = (int) Math.ceil(rating / Math.ceil(rating * 0.5));
        	
        	//rating = (int) Math.ceil(rating - Math.ceil(rating / 2));
        	
        	rating--;
        	
        	//setStatus(STATUS_SUSPENDED);
        	
        }
        
    }
    
   	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}
    
   	public void resetDifference() {
		setDifference(0);
	}

	// Status
    public int getStatus() {
   		return status;
   	}

   	public void setStatus(int status) {
   		this.status = status;
   	}

   	public void resetStatus() {
   		setStatus(0);
   	}
   	
    public boolean isSuspended() {
   		return getStatus() == STATUS_SUSPENDED;
   	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
