package nfc.mytools.no;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class UpdateStatusOnTool extends Fragment implements OnClickListener, OnItemSelectedListener {

	List<String> mList;
	private TextView mTextView;
	private TextView txtToolInfo;
	private String mToolid;
	private View view;
	private MobileServiceClient mClient;
	private MobileServiceTable<Tool> mToolTable;
	private MobileServiceTable<Location> mLocationTable;
	//private MobileServiceTable<ToolLog2> mToolLogTable;
	private Button mUpdateToolLog;
	protected int mToolStatus;
	private int mAvailable = 1;
	private int mInUse = 2;
	private ImageView mImageView;
	private TextView mTxtName;
	private ArrayList<Location> mLocations;
	private int mSelectedLocationId;
	private Spinner mSpinner;
	
	public void SetList(List<String> _list) {
		mList = _list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.update_tool, container, false);
		setupTextBlocks();
		getActivity().getActionBar().setTitle("Checkout/checkin");

		mTextView.setText("loading..." + mList.get(0));
		mToolid = mList.get(0);
		getDataFromService();

		return view;
	}

	private void setupTextBlocks() {
		mTextView = (TextView) view.findViewById(R.id.txtMain);
		mTxtName = (TextView) view.findViewById(R.id.txtName);
		txtToolInfo = (TextView) view.findViewById(R.id.txtToolInfo);
		mUpdateToolLog = (Button) view.findViewById(R.id.updateToolLog);
		mImageView = (ImageView) view.findViewById(R.id.tool_image);
		mUpdateToolLog.setOnClickListener(this);
	}

	private void getDataFromService() {
		mClient = ((MainActivity) getActivity()).GetMobileClient();
		mToolTable = mClient.getTable(Tool.class);
		mLocationTable = mClient.getTable(Location.class);
		getLocationsFromService();
		refreshItemsFromTable();
	}
	
	private void getLocationsFromService(){
		mLocationTable.orderBy("name", QueryOrder.Descending).execute(new TableQueryCallback<Location>() {

			@Override
			public void onCompleted(List<Location> result, int count,
					Exception exception, ServiceFilterResponse response) {
				mLocations = (ArrayList<Location>)result;
				PopulateSpinner();
			}
		});
	}
	
	private void PopulateSpinner(){
		mSpinner = (Spinner) view.findViewById(R.id.spinnerLocation);
		
		ArrayList<String> locationStrings = new ArrayList<String>();
		
		for (Location l : mLocations) {
			locationStrings.add(l.name);
		}
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locationStrings);
		mSpinner.setAdapter(spinnerAdapter);
		mSpinner.setOnItemSelectedListener(this);
	}

	private void refreshItemsFromTable() {
		mToolTable.where().field("toolid").eq(val(mToolid)).execute(new TableQueryCallback<Tool>() {
				
			public void onCompleted(List<Tool> result, int count, Exception exception, ServiceFilterResponse response) {
					if (exception == null) {
						if (result != null && !result.isEmpty()) {
							mToolStatus = result.get(0).statusid;
							mTextView.setText("Tool ID: " + mToolid);
							mTxtName.setText(result.get(0).name);
							txtToolInfo.setText(result.get(0).description);
							
							UrlImageViewHelper.setUrlDrawable(mImageView, "http://mytool.susaeg.no/images/tools/" + result.get(0).image);
							
							if (mToolStatus == mAvailable) {
								mUpdateToolLog.setText("Checkout");
								mSpinner.setEnabled(true);
							} else if (mToolStatus == mInUse) {
								mUpdateToolLog.setText("Checkin");
								mSpinner.setEnabled(false);
							}else{
								mUpdateToolLog.setText("Not availabe");
								mUpdateToolLog.setEnabled(false);
							}
							
						} else {
							Log.d("mytools", "Result is null");
						}
					} else {
						Log.e("mytools", exception.getMessage());
					}
				}
			});
	}

	public void updateTool(View view) {
		if (mClient == null) {
			return;
		}

		if (!mToolid.isEmpty()) {

			mToolTable.where().field("toolid").eq(val(mToolid))
					.execute(new TableQueryCallback<Tool>() {

						@Override
						public void onCompleted(List<Tool> result, int count, Exception exception, ServiceFilterResponse response) {
							if (result != null) {
								Tool tool = result.get(0);
								
								mToolStatus = tool.statusid;
								
								if (mToolStatus == mInUse) {
									tool.statusid = mAvailable;
									tool.username = "";
									tool.locationid = 1;
									mUpdateToolLog.setText("Checkout");
									mSpinner.setEnabled(true);
								} else if (mToolStatus == mAvailable) {
									tool.statusid = mInUse;
									tool.username = "Bjørge Stafsnes";
									tool.locationid = mSelectedLocationId;
									mUpdateToolLog.setText("Checkin");
									mSpinner.setEnabled(false);
								} else {
									tool.statusid = mInUse;
								}
								mToolTable.update(tool, null);
							}
						}
					});
			
			if(mToolStatus != 0){
				//logToolInUse(mToolStatus);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		updateTool(v);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mSelectedLocationId = mLocations.get(position).id;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


	/*private void logToolInUse(int status) {
		
		mToolLogTable = mClient.getTable(ToolLog2.class);
		
		ToolLog2 item = new ToolLog2();

		item.toolid = mToolid;
		item.locationid = 1;
		item.userid = 1;
		item.companyid = 1;
		item.outtime = new Date();
		item.statusid = status;

		mToolLogTable.insert(item, new TableOperationCallback<ToolLog2>() {

			@Override
			public void onCompleted(ToolLog2 entity, Exception exception,
					ServiceFilterResponse response) {

				if (exception == null) {
					Log.d("MyTool", "Saved!");
				} else {
					Log.d("MyTool", "Error: " + exception.getMessage());
				}
			}
		});
	}*/

	
}
