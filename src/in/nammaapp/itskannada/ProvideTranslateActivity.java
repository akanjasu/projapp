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
import android.widget.TextView;
import android.widget.Toast;

public class ProvideTranslateActivity extends Activity {
	Boolean flag;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	EditText engphrase,kanphrase;
	String selectedRegion;
	Button submit;
	Bundle b;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provide_translate_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		b = getIntent().getExtras();
		engphrase = (EditText) findViewById(R.id.engRAPhraseContent);
		kanphrase = (EditText) findViewById(R.id.kanRAPhraseContent);
		Spinner spn = (Spinner) findViewById(R.id.spnRegion4phrasereqA);
		submit = (Button) findViewById(R.id.btnSubmitPhraseResp);
		TextView username = (TextView) findViewById(R.id.txtRAUPUsername);
		TextView userqup = (TextView) findViewById(R.id.txtRAUPQUp);
		TextView useraup = (TextView) findViewById(R.id.txtRAUPAUp);
		TextView userxp = (TextView) findViewById(R.id.txtRAUPXP);
		
		username.setText(b.getString("askedname"));
		userqup.setText("Q Up: " + b.getString("askedqup"));
		useraup.setText("A Up: " + b.getString("askedaup"));
		userxp.setText("XP: " + b.getString("askedxp"));
		if(!b.getString("engphrase").equalsIgnoreCase("NULL")){
			engphrase.setText(b.getString("engphrase"));
			engphrase.setFocusable(false);
			flag = true;
		}
		if(!b.getString("kanphrase").equalsIgnoreCase("NULL")) {
			kanphrase.setText(b.getString("kanphrase"));
			kanphrase.setFocusable(false);
			flag = false;
		}
		spn.setSelection(Integer.parseInt(b.getString("regionID")));
		spn.setOnItemSelectedListener(new OnRegionSelectedListener());
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AsyncTask<String,String,String> postresponse = new AsyncTask<String, String, String>() {
					@Override
					protected String doInBackground(String... params) {
						hc = new DefaultHttpClient();
						hp = new HttpPost("http://nammaapp.in/scripts/postresponseforphrase.php");
						ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
						if(flag)
							postparameters.add(new BasicNameValuePair("kanphrase", kanphrase.getText().toString()));
						else if(!flag)
							postparameters.add(new BasicNameValuePair("engphrase", engphrase.getText().toString()));
						postparameters.add(new BasicNameValuePair("R_ID", Integer.toString(regionIDcalc(selectedRegion))));
						postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
						postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
						postparameters.add(new BasicNameValuePair("PR_ID", b.getString("phraseID")));
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
						Toast.makeText(getBaseContext(), "Response Posted Successfully, you have earned 5 XP!", Toast.LENGTH_LONG).show();
						int xp = Integer.parseInt(pref.getString("xppoints", "0"));
						xp += 5;
						SharedPreferences.Editor edit = pref.edit();
						edit.putString("xppoints", Integer.toString(xp));
						edit.commit();
						Intent i = new Intent(ProvideTranslateActivity.this, RequestListActivity.class);
						startActivity(i);
					}
				};
				postresponse.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.provide_translate, menu);
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
