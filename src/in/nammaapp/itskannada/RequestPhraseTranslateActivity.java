package in.nammaapp.itskannada;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RequestPhraseTranslateActivity extends Activity {
	String selectedRegion;
	EditText engword,kanword;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	Button submit;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_phrase_translate_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		engword = (EditText) findViewById(R.id.engPhraseContent);
		kanword = (EditText) findViewById(R.id.kanPhraseContent);
		Spinner region = (Spinner) findViewById(R.id.spnRegion4phrasereq);
		submit = (Button) findViewById(R.id.btnSubmitPhraseReq);
		region.setOnItemSelectedListener(new OnRegionSelectedListener());
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((engword.getText().toString().length() == 0) && (kanword.getText().toString().length() == 0)) {
					Toast.makeText(getBaseContext(), "Please Set Either Kannada or English Phrase", Toast.LENGTH_LONG).show();
					engword.requestFocus();
					return;
				} else if((engword.getText().toString().length() > 0) && (kanword.getText().toString().length() > 0)) {
					Toast.makeText(getBaseContext(), "Please Only Either Kannada or English Phrase", Toast.LENGTH_LONG).show();
					engword.requestFocus();
					return;
				} else {
					AsyncTask<String, String, String> submittask = new AsyncTask<String, String, String>() {
						protected String doInBackground(String... params) {
							hc = new DefaultHttpClient();
							hp = new HttpPost("http://nammaapp.in/scripts/submitphrasetranslate.php");
							ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
							postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
							postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
							if(engword.getText().toString().length() > 0)
								postparameters.add(new BasicNameValuePair("engphrase", engword.getText().toString()));
							else if(kanword.getText().toString().length() > 0)
								postparameters.add(new BasicNameValuePair("kanphrase", kanword.getText().toString()));
							postparameters.add(new BasicNameValuePair("R_ID", Integer.toString(regionIDcalc(selectedRegion))));
							try {
								hp.setEntity(new UrlEncodedFormEntity(postparameters));
								hr = hc.execute(hp);
							} catch(Exception e) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getBaseContext(), "Oops, Something went wrong", Toast.LENGTH_LONG).show();
									}
								});
								Log.e("check", "message", e);
								submit.requestFocus();
							}
							return null;
						}
						
						@Override
						protected void onPostExecute(String result) {
							Toast.makeText(getApplicationContext(), "Request Successfully Posted", Toast.LENGTH_LONG).show();
							if(pref.getString("kannada", "notSet") == "true"){
								Intent i = new Intent(RequestPhraseTranslateActivity.this, KannadaHomeActivity.class);
								startActivity(i);
							} else if(pref.getString("kannada", "notSet") == "false") {
								Intent i = new Intent(RequestPhraseTranslateActivity.this, EnglishHomeActivity.class);
								startActivity(i);
							}
						}
					};
					submittask.execute();
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request_phrase_translate, menu);
		return true;
	}
	
	private int regionIDcalc(String region) {
    	ArrayList<String> ar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.region_arrays)));
    	return ar.lastIndexOf(selectedRegion);
    }
	
	public class OnRegionSelectedListener implements OnItemSelectedListener {
    	@Override
    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
    			long arg3) {
    		selectedRegion = arg0.getItemAtPosition(arg2).toString();
    		
    	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
    }


}
