package com.piratebayfree;

public class Category {
	
	private int id = 0;
	private int index = 0;
	private int page = 0;
	private int order = 7;
	private int count = 0;
	private int status = 0;
	private String name;
	private String url;
	private Category category;
	private boolean parent = false;

	public static final int ID_AUDIO = 100;
	public static final int ID_VIDEOS = 200;
	public static final int ID_APPS = 300;
	public static final int ID_GAMES = 400;
	public static final int ID_ADULT = 500;
	public static final int ID_OTHER = 600;

	public static final int ORDERBY_SEEDS_DESC = 7;
	public static final int ORDERBY_SEEDS_ASC = 8;

	public static final int STATUS_UNSUBSCRIBED = 0;
	public static final int STATUS_SUBSCRIBED = 1;
	
	public Category(String name) {
		this.name = name;
	}
	
	public Category(String name, boolean parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public Category(String name, int id, boolean parent) {
		this.name = name;
		this.parent = parent;
		this.id = id;
		setURL("/" + id);
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
        this.id = id;
		//setURL("/" + id);
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
        this.index = index;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	// Count
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	// Status
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isSubscribed() {
		
		return getStatus() == STATUS_SUBSCRIBED;
		
	}

	public void resetStatus() {
		setStatus(0);
	}

	// Name
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
		//url = url.replaceAll("/$", "");
        this.url = url;
	}
	
	// Parent
	public Category getParent() {
		return category;
	}
	
	public void setParent(Category category) {
		//if(category.getID() > 0) setParent(true);
		this.category = category;
	}
	
	public boolean hasParent() {
		return category != null;
	}
	
	public void setParent(boolean parent) {
        this.parent = parent;
	}
	
	public boolean isParent() {
		return parent;
	}
	
	public boolean isAudio() {
		
		return getID() >= ID_AUDIO && getID() < ID_VIDEOS;
		
	}
	
	public boolean isVideos() {
		
		return getID() >= ID_VIDEOS && getID() < ID_APPS;
		
	}
	
	public boolean isApps() {
		
		return getID() >= ID_APPS && getID() < ID_GAMES;
		
	}
	
	public boolean isGames() {
		
		return getID() >= ID_GAMES && getID() < ID_ADULT;
		
	}
	
	public boolean isAdult() {
		
		return getID() >= ID_ADULT && getID() < ID_OTHER;
		
		//if(getID() >= 500 && getID() < 600) {
			
			//return true;
			
		//}
		
		//if(hasParent()) {
			
			//return getParent().getID() == 500;
			
		//} else {
		
			//return getID() == 500;
			
		//}
		
	}

}
