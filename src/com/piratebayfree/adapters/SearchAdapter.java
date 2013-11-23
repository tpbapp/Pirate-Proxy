package com.piratebayfree.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
import java.util.List;

import com.piratebayfree.R;
import com.piratebayfree.Search;

public class SearchAdapter extends ArrayAdapter<Search> {

    private Context context;

    private TextView name;
    
    private Search search;
    
    private List<Search> searches;
 
    public SearchAdapter(Context context, int layout, List<Search> searches) {
    	
        super(context, layout, searches);
        
        this.context = context;
        this.searches = searches;
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	search = searches.get(position);
    	
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View row = inflater.inflate(R.layout.search, parent, false);

    	name = (TextView) row.findViewById(R.id.search_name);

		name.setText(search.getName());

    	return row;
      
    }
    
}