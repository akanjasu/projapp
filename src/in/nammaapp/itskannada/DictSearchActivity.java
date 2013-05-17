package in.nammaapp.itskannada;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class DictSearchActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dict_search);
		
		 TabHost tabHost = getTabHost();
		 
		 TabSpec dictspec = tabHost.newTabSpec("EngKanDict");
		 dictspec.setIndicator("English Kannada Dictionary");
		 Intent dictIntent = new Intent(DictSearchActivity.this, EngKanDictSearch.class);
		 dictspec.setContent(dictIntent);
		 
		 TabSpec contextspec = tabHost.newTabSpec("ContextDict");
		 contextspec.setIndicator("Context Based Dictionary");
		 Intent contIntent = new Intent(DictSearchActivity.this, ContextSearchHome.class);
		 contextspec.setContent(contIntent);
		 
		 tabHost.addTab(dictspec);
		 tabHost.addTab(contextspec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dict_search, menu);
		return true;
	}

}
