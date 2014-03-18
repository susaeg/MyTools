package nfc.mytools.no;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToolListAdapter extends ArrayAdapter<Tool>{

	public ToolListAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		final Tool currentItem = getItem(position);
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			row = inflater.inflate(R.layout.tool_listitem, parent, false);
		}

		row.setTag(currentItem);
		TextView titleView = (TextView) row.findViewById(R.id.tool_title);
		ImageView toolimageView = (ImageView) row.findViewById(R.id.tool_image);
		titleView.setText(currentItem.name);
		
		if(currentItem.image != null && !currentItem.image.isEmpty())
		{
			UrlImageViewHelper.setUrlDrawable(toolimageView, "http://mytool.susaeg.no/images/tools/" + currentItem.image);
		}else {
			UrlImageViewHelper.setUrlDrawable(toolimageView, "http://mytool.susaeg.no/images/tools/icon_unknown.png");
		}
		
		ImageView statusImage = (ImageView) row.findViewById(R.id.tool_status);
		
		if(currentItem.statusid == 1){
			statusImage.setImageResource(R.drawable.status_icon_green);
		}else if(currentItem.statusid == 2){
			statusImage.setImageResource(R.drawable.status_icon_orange);
		}else if(currentItem.statusid == 3){
			statusImage.setImageResource(R.drawable.status_icon_blue);
		}else if(currentItem.statusid == 4){
			statusImage.setImageResource(R.drawable.status_icon_red);
		}else{
			statusImage.setImageResource(R.drawable.status_icon_unknown);
		}
		
		return row;
	}
}
