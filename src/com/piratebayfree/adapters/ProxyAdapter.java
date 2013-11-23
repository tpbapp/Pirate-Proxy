package com.piratebayfree.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
import java.util.List;

import com.piratebayfree.Proxy;
import com.piratebayfree.R;

public class ProxyAdapter extends ArrayAdapter<Proxy> {

	private Context context;
	//private Database db;

    private ImageView icon;
    private TextView url;
    private TextView rating;
    //private ImageView delete;

	private Proxy proxy;
    
    private List<Proxy> proxies;
 
    public ProxyAdapter(Context context, int layout, List<Proxy> proxies) {
    	
        super(context, layout, proxies);
        
        this.context = context;
        this.proxies = proxies;
        
        //db = new Database(context);
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View row = inflater.inflate(R.layout.proxy, parent, false);

    	// Views
    	icon = (ImageView) row.findViewById(R.id.proxy_icon);
    	url = (TextView) row.findViewById(R.id.proxy_url);
    	rating = (TextView) row.findViewById(R.id.proxy_rating);
    	//delete = (ImageView) row.findViewById(R.id.proxydelete);
    	
    	proxy = proxies.get(position);
    	
    	url.setText(proxy.getName());
    	rating.setText(String.valueOf(proxy.getRating()));
    	
    	if(proxy.isActive()) {
    		
        	icon.setImageResource(R.drawable.success);
        	
    	} else if(proxy.isSuspended()) {
        		
            icon.setImageResource(R.drawable.warn);

    	} else if(proxy.getRating() >= 100) {
    		
        	icon.setImageResource(R.drawable.popular);

    	} else if(proxy.getRating() > 10) {
    		
        	icon.setImageResource(R.drawable.trusted);

    	} else if(proxy.getRating() > 3) {
    		
        	icon.setImageResource(R.drawable.success);
    		
    	} else if(proxy.getRating() < 1) {
    		
        	icon.setImageResource(R.drawable.error);
    		
    	}
    	
    	/*delete.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent arg1) {
				
				deleteProxy(proxy);
				
				return false;
				
			}
    		
    	});*/

    	return row;
      
    }
	
	/*private void deleteProxy(Proxy proxy) {
		
		db.deleteProxy(proxy);
		
		Log.d(TAG, "Deleted: " + proxy.getURL());
		
	}*/
    
}