package in.nammaapp.itskannada;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ContextRequestEngHomeActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.context_request_eng_home_activity);
		TabHost tabHost  = getTabHost();
		
		TabSpec contextsubmit = tabHost.newTabSpec("request");
		TabSpec mycontext = tabHost.newTabSpec("myphrases");
		
		contextsubmit.setIndicator("Request...");
		mycontext.setIndicator("My Requests...");
		
		Intent requestIntent = new Intent(ContextRequestEngHomeActivity.this, RequestContextResolutionActivity.class);
		contextsubmit.setContent(requestIntent);
		
		Intent mycontentIntent = new Intent(ContextRequestEngHomeActivity.this, MyContextResolutionActivity.class);
		mycontext.setContent(mycontentIntent);
		
		tabHost.addTab(contextsubmit);
		tabHost.addTab(mycontext);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_context_res, menu);
		return true;
	}

}
