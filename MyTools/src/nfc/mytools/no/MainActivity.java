package nfc.mytools.no;

import java.net.MalformedURLException;
import java.util.ArrayList;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements OnItemClickListener, ListItemCallback {

	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "MyTools";
	private MobileServiceClient mClient;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private FragmentTransaction transaction;
	private String mTag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        ArrayList<String> menuItems = new ArrayList<String>() {{
        	add("Tool list");
        	add("Locations");
        	add("Unused tools");
        }};
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, menuItems));
        mDrawerList.setOnItemClickListener(this);
	
		replaceContentFragment(new ToolListFragment());
	
		Log.d("MyTool", getIntent().getAction());
	}
	
	public void replaceContentFragment(Fragment fragment){
		
		mTag = "";
		
		if(fragment instanceof ToolListFragment){
			mTag = "toollist";
		}
		if(fragment instanceof UpdateStatusOnTool){
			mTag = "updatestatusontool";
		}
		if(fragment instanceof ToolItemDetail){
			mTag = "toolitemdetail";
		}
		if(fragment instanceof LocationItem){
			mTag = "locationitem";
		}
		
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, fragment, mTag);
		transaction.addToBackStack(mTag);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		readDataOnTag(intent);
	}
	
	protected void readDataOnTag(Intent intent) {
		if(intent.getAction() != null && intent.getData() != null){
			UpdateStatusOnTool updateToolStatus = new UpdateStatusOnTool();
			updateToolStatus.SetList(intent.getData().getPathSegments());
			MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ping);
			mp.setLooping(false);
			mp.start();
			mp.release();
			mp = null;
			replaceContentFragment(updateToolStatus);
		}
	}
	
	public MobileServiceClient GetMobileClient() {
		try {
			if(mClient == null)
			{
				mClient = new MobileServiceClient("https://nfc.azure-mobile.net/", "ZsETLqwcglaPdjKPpVOJwLTTvgiJAg23", this);
			}
		} catch (MalformedURLException e) {
			Log.e("MyTool", e.getMessage());
		}
		return mClient;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(position == 0){
			replaceContentFragment(new ToolListFragment());
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		else if(position == 1)
		{
			replaceContentFragment(new LocationListFragment());
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	public void listItemSelected(Object o) {
		Fragment frag = null;
		
		if(o instanceof Tool)
		{
			frag = new ToolItemDetail();
			((ToolItemDetail) frag).setTool((Tool)o);
		}
		else if(o instanceof Location)
		{
			frag = new LocationItem();
			((LocationItem) frag).setLocation((Location)o);
		}
		
		replaceContentFragment(frag);
	}
}