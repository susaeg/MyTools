package nfc.mytools.no;

import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class LocationListFragment extends ListFragment {

	private MobileServiceClient mClient;
	private MobileServiceTable<Location> mLocationTable;
	private LocationListAdapter mLocationListAdpter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setTitle("Locations");
		mLocationListAdpter = new LocationListAdapter(getActivity(), 0);
		setListAdapter(mLocationListAdpter);
		GetToolListFromService();
	}

	private void GetToolListFromService(){
		
		mClient = ((MainActivity) getActivity()).GetMobileClient();
		mLocationTable = mClient.getTable(Location.class);
		mLocationTable.orderBy("name", QueryOrder.Ascending).execute(new TableQueryCallback<Location>() {

			@Override
			public void onCompleted(List<Location> result, int count, Exception exception, ServiceFilterResponse response) {
				mLocationListAdpter.addAll(result);
				mLocationListAdpter.notifyDataSetChanged();
			}
		});
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		ListItemCallback callback = ((ListItemCallback)getActivity());
		callback.listItemSelected((Location)v.getTag());
	}
}
