package in.nammaapp.itskannada;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ResponsePhraseTranslateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phrase_request_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phrase_request, menu);
		return true;
	}

}
