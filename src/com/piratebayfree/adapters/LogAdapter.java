package com.piratebayfree.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
import java.util.List;

import com.piratebayfree.R;
import com.piratebayfree.Status;

public class LogAdapter extends ArrayAdapter<Status> {

    private Context context;

	private ImageView icon;
    private TextView tag;
    private TextView time;
    private TextView content;
    
    private Status log;
    
    private List<Status> logs;

 
    public LogAdapter(Context context, int layout, List<Status> logs) {
    	
        super(context, layout, logs);
        
        this.context = context;
        this.logs = logs;
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	log = logs.get(position);
    	
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View row = inflater.inflate(R.layout.log, parent, false);

    	icon = (ImageView) row.findViewById(R.id.log_icon);
    	tag = (TextView) row.findViewById(R.id.log_tag);
    	time = (TextView) row.findViewById(R.id.log_time);
    	content = (TextView) row.findViewById(R.id.log_content);
	    	
    	tag.setText(log.getTag());
    	content.setText(log.getContent());
    	
    	if(log.isFirst()) {

        	// Tag
    		tag.setTypeface(null, Typeface.BOLD);
    		content.setTypeface(null, Typeface.BOLD);
    		time.setVisibility(View.GONE);
    		
    	} else {
    	
    		// Time
    		if(log.getTime() > 100) {
		
    			float seconds = ((int) log.getTime() + 99) / 100 * 100;
			
    			time.setText("+" + (seconds / 1000) + "s");

    			if(log.getTime() > 2500) time.setTextColor(content.getResources().getColor(R.color.warn));
    			if(log.getTime() > 5000) time.setTextColor(content.getResources().getColor(R.color.error));
    	
    		} else if(log.getTime() > 0) {
		
    			float seconds = ((int) log.getTime() + 9) / 10 * 10;
			
    			time.setText("+" + (seconds / 1000) + "s");
    		
    		}
    		
    	}
    	
    	if(log.getStatus() == Status.SUCCESS) {
    		
    		tag.setTextColor(context.getResources().getColor(R.color.success));
    		content.setTextColor(context.getResources().getColor(R.color.success));
        	icon.setImageResource(R.drawable.success);
        	
    	} else if(log.getStatus() == Status.WARNING) {

    		tag.setTextColor(context.getResources().getColor(R.color.warn));
    		content.setTextColor(context.getResources().getColor(R.color.warn));
        	icon.setImageResource(R.drawable.warn);
    
		} else if(log.getStatus() == Status.ERROR) {

    		tag.setTextColor(context.getResources().getColor(R.color.error));
    		content.setTextColor(context.getResources().getColor(R.color.error));
        	icon.setImageResource(R.drawable.error);
    		
    	}

    	return row;
      
    }
    
}