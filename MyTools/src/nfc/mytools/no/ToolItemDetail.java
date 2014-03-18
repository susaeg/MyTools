package nfc.mytools.no;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ToolItemDetail extends Fragment {

	private View view;
	private Tool mTool;
	private TextView txtID;
	private TextView txtTitle;
	private TextView txtInfo;
	private TextView txtLocationAndUse;
	private MobileServiceClient mClient;
	private MobileServiceTable<Location> mLocationTable;
	private String mLocation = "";
	private TextView txtInUseBy;
	private ImageView imgTool; 
	
	public void setTool(Tool t){
		mTool = t;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tool_detail, container, false);
		GetToolListByLocation();
		SetTextViews();
		
		getActivity().getActionBar().setTitle(mTool.name);
		
		UrlImageViewHelper.setUrlDrawable(imgTool, "http://mytool.susaeg.no/images/tools/" + mTool.image);
		
		txtID.setText("Tool ID: " + mTool.toolid);
		txtTitle.setText(mTool.name);
		txtInfo.setText(mTool.description);
		txtLocationAndUse.setText("Current location: " + mLocation);
		
		if(mTool.statusid == 2){
			txtInUseBy.setText("In use by: " + mTool.username);
		}else{
			txtInUseBy.setText(getInUseByText(mTool.statusid));
		}
		
		txtInUseBy.setTextColor(Color.parseColor(findTextColor(mTool.statusid)));
	
		return view;
	}
	
	private String getInUseByText(int statusid) {
		if(statusid == 1){
			return "Tool is Available";
		}else if(statusid == 3){
			return "Tool is at service";
		} else{
			return "Tool is broken";
		}
	}

	private String findTextColor(int statusid){
		if(statusid == 1){
			return "#34b010";
		}else if(statusid == 2){
			return "#e9940d";
		} else if(statusid == 3){
			return "#1e44c3";
		} else{
			return "#ff0000";
		}
	}
	
	public void SetTextViews(){
		imgTool = (ImageView) view.findViewById(R.id.tool_image_detail);
		txtID = (TextView) view.findViewById(R.id.txtid_detail);
		txtTitle = (TextView) view.findViewById(R.id.txtName_detail);
		txtInfo = (TextView) view.findViewById(R.id.txtToolInfo_detail);
		txtLocationAndUse = (TextView) view.findViewById(R.id.txtLocation_Detail);
		txtInUseBy = (TextView) view.findViewById(R.id.txtInUseBy_Detail);
	}
	
	public void GetToolListByLocation(){
		
		mClient = ((MainActivity) getActivity()).GetMobileClient();
		mLocationTable = mClient.getTable(Location.class);
		mLocationTable.where().field("id").eq(val(mTool.locationid)).execute(new TableQueryCallback<Location>() {

			@Override
			public void onCompleted(List<Location> result, int count, Exception exception, ServiceFilterResponse response) {
				if(result != null){
					txtLocationAndUse.setText("Current location: " + result.get(0).name);
				}
			}
		});
	}
}
