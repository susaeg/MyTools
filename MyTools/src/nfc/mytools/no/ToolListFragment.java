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

public class ToolListFragment extends ListFragment {

	private MobileServiceClient mClient;
	private MobileServiceTable<Tool> mToolTable;
	private ToolListAdapter mToolListAdpter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setTitle("Tools");
		mToolListAdpter = new ToolListAdapter(getActivity(), 0);
		setListAdapter(mToolListAdpter);
		GetToolListFromService();
	}

	private void GetToolListFromService(){
		
		mClient = ((MainActivity) getActivity()).GetMobileClient();
		mToolTable = mClient.getTable(Tool.class);
		mToolTable.orderBy("statusid", QueryOrder.Ascending).execute(new TableQueryCallback<Tool>() {

			@Override
			public void onCompleted(List<Tool> result, int count, Exception exception, ServiceFilterResponse response) {
				mToolListAdpter.addAll(result);
				mToolListAdpter.notifyDataSetChanged();
			}
		});
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		ListItemCallback callback = ((ListItemCallback)getActivity());
		callback.listItemSelected((Tool)v.getTag());
	}
}