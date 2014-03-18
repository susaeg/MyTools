package nfc.mytools.no;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LocationListAdapter extends ArrayAdapter<Location> {

	public LocationListAdapter(Context context, int resource) {
		super(context, resource);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater li = LayoutInflater.from(getContext());
		TextView view = (TextView) li.inflate(R.layout.location_listitem, null);
		
		final Location currentItem = getItem(position);
		
		view.setText(currentItem.name);
		view.setTag(currentItem);
		
		return view;
	}
	
}
