package nfc.mytools.no;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class LocationItem extends Fragment implements OnItemClickListener {
	
	private Location mLocation;
	private View view;
	private ToolListAdapter mToolListAdapter;
	private MobileServiceClient mClient;
	private MobileServiceTable<Tool> mToolTable;
	
	public void setLocation(Location l){
		mLocation = l;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.location_detail, container, false);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtLocationName);
		ListView listview = (ListView) view.findViewById(R.id.listToolsOnLocation);
		getActivity().getActionBar().setTitle("Tools at " + mLocation.name);
		txtTitle.setText("Address: " + mLocation.address);
		
		mToolListAdapter = new ToolListAdapter(getActivity(), 0);
		
		listview.setAdapter(mToolListAdapter);
		listview.setOnItemClickListener(this);
		GetToolListByLocation();
		
		return view;
	}
	
	private void GetToolListByLocation(){
		
		mClient = ((MainActivity) getActivity()).GetMobileClient();
		mToolTable = mClient.getTable(Tool.class);
		mToolTable.where().field("locationid").eq(val(mLocation.id)).orderBy("statusid", QueryOrder.Ascending).execute(new TableQueryCallback<Tool>() {

			@Override
			public void onCompleted(List<Tool> result, int count, Exception exception, ServiceFilterResponse response) {
				mToolListAdapter.addAll(result);
				mToolListAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		ListItemCallback callback = ((ListItemCallback)getActivity());
		callback.listItemSelected((Tool)v.getTag());
	}
}
