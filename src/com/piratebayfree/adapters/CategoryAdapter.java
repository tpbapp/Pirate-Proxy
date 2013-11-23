package com.piratebayfree.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
import java.util.List;

import com.piratebayfree.Category;
import com.piratebayfree.R;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context context;

	private ImageView icon;
    private TextView name;
    
    private Category category;
    
    private List<Category> categories;
 
    public CategoryAdapter(Context context, int layout, List<Category> categories) {
    	
        super(context, layout, categories);
        
        this.context = context;
        this.categories = categories;
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	category = categories.get(position);
    	
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View row = inflater.inflate(R.layout.category, parent, false);

    	icon = (ImageView) row.findViewById(R.id.category_icon);
    	name = (TextView) row.findViewById(R.id.category_name);
    	
    	loadIcon();
    	
    	loadName();
		
    	return row;
      
    }

    // Icon
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadIcon() {
    	
		if(!category.isParent()) {
	    
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		
				icon.setAlpha(0.7f);
			
			}
			
		}
		
		if(category.isAudio()) {
			
			icon.setImageResource(R.drawable.audio);
			
		} else if(category.isVideos()) {
				
			icon.setImageResource(R.drawable.videos);
			
		} else if(category.isApps()) {
				
			icon.setImageResource(R.drawable.apps);
			
		} else if(category.isGames()) {
				
			icon.setImageResource(R.drawable.games);
		
		} else if(category.isAdult()) {
			
			icon.setImageResource(R.drawable.adult);
			
		}
		
	}
    
    // Name
    private void loadName() {
    	
		if(category.isParent()) {

			name.setText(category.getName());
			name.setTextAppearance(context, R.style.title);
    		
		} else {
	    	
			name.setText(category.getName());
    		
		}
    	
    }
    
}