package in.nammaapp.itskannada;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class EngPhraseRequestHomeActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eng_phrase_request_home_activity);

		TabHost tabHost  = getTabHost();
		
		TabSpec phrasesubmit = tabHost.newTabSpec("request");
		TabSpec myphrases = tabHost.newTabSpec("myphrases");
		
		phrasesubmit.setIndicator("Add Request...");
		Intent requestintent = new Intent(EngPhraseRequestHomeActivity.this, RequestPhraseTranslateActivity.class);
		phrasesubmit.setContent(requestintent);
		
		Intent myrequests = new Intent(EngPhraseRequestHomeActivity.this, MyPhraseRequestsActivity.class);
		myphrases.setIndicator("My Requests");
		myphrases.setContent(myrequests);
		
		tabHost.addTab(phrasesubmit);
		tabHost.addTab(myphrases);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request_phrase_translate, menu);
		return true;
	}

}
