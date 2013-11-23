package com.piratebayfree;

public class Search {
	
	private int id = 0;
	private int page = 0;
	private int order = 7;
	private int count = 0;
	private int status = 0;
	private String name = "";
	private String query = "";
	
	private Format format;

	static final int ORDERBY_SEEDS_DESC = 7;
	static final int ORDERBY_SEEDS_ASC = 8;

	public static final int STATUS_UNSUBSCRIBED = 1;
	public static final int STATUS_SUBSCRIBED = 1;
	
	public Search(String name) {
		
		format = new Format();
		
		setName(name);
		setQuery(name);
		
	}
	
	// ID
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
        this.id = id;
	}
	
	// Name
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
        this.name = format.getName(name);
	}
	
	// Query
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
        this.query = format.getQuery(query);
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void resetStatus() {
		setStatus(0);
	}

	public boolean isSubscribed() {
		
		return getStatus() == STATUS_SUBSCRIBED;
		
	}

}