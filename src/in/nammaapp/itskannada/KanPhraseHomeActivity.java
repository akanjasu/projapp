package in.nammaapp.itskannada;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class KanPhraseHomeActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kan_phrase_home_activity);
		TabHost tabHost  = getTabHost();
		
		TabSpec phrasesubmit = tabHost.newTabSpec("request");
		TabSpec myphrases = tabHost.newTabSpec("myphrases");
		TabSpec userphrases = tabHost.newTabSpec("userphrases");
		
		phrasesubmit.setIndicator("Add Request...");
		Intent requestintent = new Intent(KanPhraseHomeActivity.this, RequestPhraseTranslateActivity.class);
		phrasesubmit.setContent(requestintent);
		
		Intent myrequests = new Intent(KanPhraseHomeActivity.this, MyPhraseRequestsActivity.class);
		myphrases.setIndicator("My Requests...");
		myphrases.setContent(myrequests);
		
		Intent userreq = new Intent(KanPhraseHomeActivity.this, RequestListActivity.class);
		userphrases.setIndicator("User Requests...");
		userphrases.setContent(userreq);
		
		tabHost.addTab(phrasesubmit);
		tabHost.addTab(userphrases);
		tabHost.addTab(myphrases);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kan_phrase_home, menu);
		return true;
	}

}
