package nfc.mytools.no;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ToolLogListAdapter extends ArrayAdapter<ToolLog2>{

	public ToolLogListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		final ToolLog2 currentItem = getItem(position);
		
		row.setTag(currentItem);
	
		return row;
	}
}
