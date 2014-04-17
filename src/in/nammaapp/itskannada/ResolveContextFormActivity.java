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
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ResolveContextFormActivity extends Activity {
	EditText engword,kanword,context;
	Button submit;
	Spinner spn;
	String selectedRegion;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	Bundle b;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resolve_context_form_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		engword = (EditText) findViewById(R.id.contextResEngWordcontent);
		kanword = (EditText) findViewById(R.id.contextResKanWordcontent);
		context = (EditText) findViewById(R.id.contextContextcontentRes);
		submit = (Button) findViewById(R.id.btnSubmitContRes);
		spn = (Spinner) findViewById(R.id.spn4contextRes);
		spn.setOnItemSelectedListener(new OnRegionSelectedListener());
		b = getIntent().getExtras();
		if(b.getString("option").equalsIgnoreCase("systemresolve")) {
			engword.setText(b.getString("engword"));
			engword.setFocusable(false);
			kanword.setText(b.getString("kanword"));
			kanword.setFocusable(false);
			context.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					EditText ed = (EditText) v;
					if(!hasFocus){
						if(ed.getText().toString().length() == 0)
							Toast.makeText(getBaseContext(), "Please Fill in the context before proceeding", Toast.LENGTH_SHORT).show();
					}	
				}
			});
			
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(context.getText().toString().length() == 0) {
						Toast.makeText(getBaseContext(), "Please Fill in the context before proceeding", Toast.LENGTH_SHORT).show();
						return;
					}
					AsyncTask<String,String,String> postsysresp = new AsyncTask<String,String,String>() {
						@Override
						protected String doInBackground(String... params) {
							hc = new DefaultHttpClient();
							hp = new HttpPost("http://nammaapp.in/scripts/postsyscontresp.php");
							ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
							postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID","notSet")));
							postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
							postparameters.add(new BasicNameValuePair("ID", b.getString("ID")));
							postparameters.add(new BasicNameValuePair("engword", b.getString("engword")));
							postparameters.add(new BasicNameValuePair("kanword", b.getString("kanword")));
							postparameters.add(new BasicNameValuePair("context", context.getText().toString()));
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
						
						protected void onPostExecute(String result) {
							Toast.makeText(getApplicationContext(), "Response Successfully Posted. You have earned 5 XP", Toast.LENGTH_LONG).show();
							int xp = Integer.parseInt(pref.getString("xppoints", "0"));
							xp += 5;
							SharedPreferences.Editor edit = pref.edit();
							edit.putString("xppoints", Integer.toString(xp));
							edit.commit();
							Intent i = new Intent(ResolveContextFormActivity.this , ContextKanHomeActivity.class);
							startActivity(i);
						}
					};
					postsysresp.execute();
				}
			});
		} else if (b.getString("option").equalsIgnoreCase("userresolve")) {
			engword.setText(b.getString("engword"));
			engword.setFocusable(false);
			context.setText(b.getString("context"));
			context.setFocusable(false);
			kanword.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					EditText ed = (EditText) v;
					if(!hasFocus){
						if(ed.getText().toString().length() == 0)
							Toast.makeText(getBaseContext(), "Please Fill in the kannada word before proceeding", Toast.LENGTH_SHORT).show();
					}	
				}
			});
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(kanword.getText().toString().length() == 0) {
						Toast.makeText(getBaseContext(), "Please Fill in the kannada word before proceeding", Toast.LENGTH_SHORT).show();
						return;
					}
					AsyncTask<String,String,String> postsysresp = new AsyncTask<String,String,String>() {
						@Override
						protected String doInBackground(String... params) {
							hc = new DefaultHttpClient();
							hp = new HttpPost("http://nammaapp.in/scripts/postusercontresp.php");
							ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
							postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
							postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
							postparameters.add(new BasicNameValuePair("ID", b.getString("ID")));
							postparameters.add(new BasicNameValuePair("engword", b.getString("engword")));
							postparameters.add(new BasicNameValuePair("kanword", kanword.getText().toString()));
							postparameters.add(new BasicNameValuePair("context", b.getString("context")));
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
						
						protected void onPostExecute(String result) {
							Toast.makeText(getApplicationContext(), "Response Successfully Posted, You have earned 5 XP!", Toast.LENGTH_LONG).show();
							int xp = Integer.parseInt(pref.getString("xppoints", "0"));
							xp += 5;
							SharedPreferences.Editor edit = pref.edit();
							edit.putString("xppoints", Integer.toString(xp));
							edit.commit();
							Intent i = new Intent(ResolveContextFormActivity.this , ContextKanHomeActivity.class);
							startActivity(i);
						}
					};
					postsysresp.execute();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resolve_context_form, menu);
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
