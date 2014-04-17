package in.nammaapp.itskannada;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ContextKanHomeActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.context_kan_home_activity);
		TabHost tabHost  = getTabHost();
		TabSpec contextsubmit = tabHost.newTabSpec("request");
		TabSpec usercontext = tabHost.newTabSpec("userrequests");
		TabSpec systemcontext = tabHost.newTabSpec("systemrequests");
		TabSpec mycontext = tabHost.newTabSpec("myrequests");
		
		contextsubmit.setIndicator("Add Request...");
		usercontext.setIndicator("User Requests...");
		systemcontext.setIndicator("System Requests...");
		mycontext.setIndicator("My Requests...");
		
		Intent requestIntent = new Intent(ContextKanHomeActivity.this, RequestContextResolutionActivity.class);
		contextsubmit.setContent(requestIntent);
		
		Intent myrequests = new Intent(ContextKanHomeActivity.this, MyContextResolutionActivity.class);
		mycontext.setContent(myrequests);
		
		Intent usercontextIntent = new Intent(ContextKanHomeActivity.this, ResolveUserContextRequestsActivity.class);
		usercontext.setContent(usercontextIntent);
		
		Intent systemcontextIntent = new Intent(ContextKanHomeActivity.this, ResolveSystemContextRequestsActivity.class);
		systemcontext.setContent(systemcontextIntent);
		
		tabHost.addTab(contextsubmit);
		tabHost.addTab(usercontext);
		tabHost.addTab(systemcontext);
		tabHost.addTab(mycontext);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.context_kan_home, menu);
		return true;
	}

}
