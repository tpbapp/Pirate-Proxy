package com.piratebayfree;

import android.util.Log;

public class Status {
	
	private int id = 0;
	private String tag;
	private String content;
	private long time = 0;
	private int status = 0;
	private boolean first = false;

    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int SUCCESS = 3;
	
	public Status(String tag, String content) {
		
		setTag(tag);
		setContent(content);
		
		Log.d(tag, content);
		
		setTime(System.currentTimeMillis());
		
	}
	
	public Status(String tag, String content, int status) {
		
		setTag(tag);
		setContent(content);
		setStatus(status);
		
		if(status == ERROR) {
			Log.e(tag, content);
		} else if(status == SUCCESS) {
			Log.i(tag, content);
		} else if(status == WARNING) {
			Log.w(tag, content);
		} else {
			Log.d(tag, content);
		}
		
		setTime(System.currentTimeMillis());
		
	}
	
	public Status(String tag, String content, Exception e) {
		
		setTag(tag);
		setContent(content);
		setStatus(ERROR);
			
		Log.e(tag, content, e);
		
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
        this.id = id;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
        this.tag = tag;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
        this.content = content;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
        this.status = status;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

}