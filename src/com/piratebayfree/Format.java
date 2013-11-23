package com.piratebayfree;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

public class Format {
	
	public static final String TORRENT_DATE = "yyyy-MM-dd kk:mm:ss zzz";
	
    public final static long ONE_SECOND = 1000;
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long ONE_DAY = ONE_HOUR * 24;
    public final static long ONE_MONTH = ONE_DAY * 30;
    public final static long ONE_YEAR = ONE_MONTH * 12;
	
	private static final String TAG = "Format";

	public Format() {}
	
	public String getName(String name) {
		
		if(name.length() > 0) {
		
			// Strip
			name = name.replace(".", " ")
					.replace("-", " ")
					.replace("_", " ")
					.replace("~", " ")
					//.replace("(", "]")
					//.replace(")", "]")
					//.replace("[", "(")
					//.replace("]", ")")
					.replace("*", "");
		
			// Uppercase words
			String[] words = name.split(" ");
		
			name = "";
				
			for(String word : words) {
			
				if(word.length() > 0) {
			
					String character = String.valueOf(word.charAt(0)).toUpperCase(Locale.getDefault());
			
					name += character + word.substring(1).trim();
					name += " ";
				
				}
			
			}
			
		}

		// Uppercase first
		//String character = String.valueOf(name.charAt(0)).toUpperCase();
		
		//name = character + name.substring(1).trim();
		
		return name.trim();
		
	}

    public String getQuery(String query) {
		
		try {
			
			query = URLEncoder.encode(query, "UTF_8");
			
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
			
		}

    	return query;
    	
    }
	
	public String getNumberText(int number) {
		
		float decimal = number;
		String text = "";
	
		// > 1M
		if(number >= 1000000) {
		
			decimal = (number + 99999) / 100000 * 100000;
			number = Math.round(decimal / 1000000);
			text = number + "M";

		// > 100K
		} else if(number >= 100000) {
		
			decimal = (number + 9999) / 10000 * 10000;
			number = Math.round(decimal / 1000);
			text = number + "K";

		// > 10K
		} else if(number >= 10000) {
		
			decimal = (number + 999) / 1000 * 1000;
			number = Math.round(decimal / 1000);
			text = number + "K";

		// > 1K
		} else if(number >= 1000) {
		
			decimal = (number + 99) / 100 * 100;
			decimal = Math.round(decimal / 1000);
			text = decimal + "K";
			text = text.replaceAll("([0-9])\\.0+([^0-9]|$)", "$1$2");

		// > 100
		} else if(number >= 100) {
		
			number = (int) (number + 9) / 10 * 10;
			text = number + text;

		// > 50
		} else if(number >= 50) {
		
			number = (int) (number + 4) / 5 * 5;
			text = number + text;

		// < 10
		} else {
		
			//number = (int) (number + 1) / 2 * 2;
			text = number + text;
		
		}
	
		return text;
	
	}
	
	// Date
	public String getDate(String date, String format) {
		
		Date now = new Date();
		Date then = new Date();
	
		Time time = new Time();
		time.setToNow();
		now.setTime(time.toMillis(false));
	
		SimpleDateFormat sdf = new SimpleDateFormat(TORRENT_DATE, Locale.ENGLISH);
    
    	try {
    	
    		then = sdf.parse(date);
        
    	} catch (ParseException e) {
    	
        	Log.w(TAG, e.toString());
        
    	}
    	
    	if(then.getTime() < now.getTime()) {
    
    		return getRelativeTimespan(then.getTime(), now.getTime());
    		
    	} else {
    
    		return "";
    		
    	}
    
	}
	
    // Timespan
	public String getRelativeTimespan(long then, long now) {

        String text = "";
        
        text = (String) DateUtils.getRelativeTimeSpanString(then, now, 0);
        
        String first = text.substring(0,1).toUpperCase(Locale.getDefault());
        
        text = first + text.substring(1);
		
		return text;
		
	}

	// Bytes to size
	public String bytesToSize(long bytes) {
		
		String size = "0B";
		
	    if(bytes <= 0) return size;
	    
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    
	    int digits = (int) (Math.log10(bytes) / Math.log10(1024));
	    
	    size = new DecimalFormat("#,##0.#")
	    	.format(bytes / Math.pow(1024, digits)) + " " + units[digits];
		
		return size;
		
	}
	
	// Blob to bitmap
	public Bitmap blobToBitmap(byte[] blob) {
		
		Bitmap bitmap = null;
		
		try {
		
			bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
		
		} catch(Exception e) {
		
			e.printStackTrace();
			
		}
		
		return bitmap;
	
	}
	
	// Bitmap to blob
	public byte[] bitmapToBlob(Bitmap bitmap) {
		
		byte[] blob = null;

		try {
				
			if(bitmap.getWidth() > 10) {
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
				
				blob = stream.toByteArray();
				
			}
		
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return blob;
			
	}

	//private String plural(long interval) {
		//return interval > 1 ? "s" : "";
	//}
	//long duration = now - then;
    //long interval = 0;
    //StringBuffer buffer = new StringBuffer();
    
    /*if(duration >= ONE_SECOND) {
    	
    	interval = duration / ONE_DAY;
    	
    	if(interval > 0) {
    		
    		duration -= interval * ONE_DAY;
    		
    		buffer.append(interval)
    				.append(" day")
    				.append(interval > 1 ? "s" : "")
    				.append(duration >= ONE_MINUTE ? ", " : "");
    		
    	}

    	interval = duration / ONE_HOUR;
    		
    	if(interval > 0) {
    		
    		duration -= interval * ONE_HOUR;
    		
    		buffer.append(interval)
    				.append(" hour")
    				.append(interval > 1 ? "s" : "")
    				.append(duration >= ONE_MINUTE ? ", " : "");
    		
    	}

        interval = duration / ONE_MINUTE;
        	
        if(interval > 0) {
        	
        	duration -= interval * ONE_MINUTE;
        	
        	buffer.append(interval)
        			.append(" minute")
        			.append(interval > 1 ? "s" : "");
        	
        }

        if(!buffer.toString().equals("") && duration >= ONE_SECOND) {
        	
        	buffer.append(" and ");
        	
        }

        interval = duration / ONE_SECOND;
        	
        if(interval > 0) {
        	
        	buffer.append(interval)
        			.append(" second")
        			.append(interval > 1 ? "s" : "");
        	
        }
        	
        text = buffer.toString() + " ago";
        	
    } else {
    	
    	text = "0 seconds ago";
    	
    }*/

    /*duration = (long) Math.floor(duration / ONE_SECOND);
    
    interval = (long) Math.floor(duration / ONE_YEAR);
    if(interval > 0) return interval + " year" + plural(interval) + " ago";
    
    interval = (long) Math.floor(duration / ONE_MONTH);
    if(interval > 0) return interval + " month" + plural(interval) + " ago";

    interval = (long) Math.floor(duration / ONE_DAY);
    if(interval > 0) return interval + " day" + plural(interval) + " ago";

    interval = (long) Math.floor(duration / ONE_HOUR);
    if(interval > 0) return interval + " hour" + plural(interval) + " ago";
        
    interval = (long) Math.floor(duration / ONE_MINUTE);
    if(interval > 0) return interval + " minute" + plural(interval) + " ago";
        
    return Math.floor(duration) + " seconds ago";*/

}
