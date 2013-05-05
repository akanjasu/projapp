package in.nammaapp.itskannada;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DictionarySearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary_search_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dictionary_search, menu);
		return true;
	}

}
