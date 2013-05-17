package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

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

public class RegisterActivity extends Activity {
	EditText name,username,password,email,ed;
	Spinner region;
	Button submit;
	HttpClient hc;
	HttpPost hp;
	HttpResponse r;
	BufferedReader br;
	String checkStr;
	SharedPreferences pref;
	String selectedRegion;
	HttpGet hg;
	String html,s="";
	ArrayList<String> usernameList;
	String x;
	AsyncTask<String, Void, Void> task1;
	
	public class OnRegionSelectedListener implements OnItemSelectedListener {
    	@Override
    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
    			long arg3) {
    		selectedRegion = arg0.getItemAtPosition(arg2).toString();
    	}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		hc = new DefaultHttpClient();
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		name = (EditText) findViewById(R.id.edtName);
		username  = (EditText) findViewById(R.id.edtUsername);
		password = (EditText) findViewById(R.id.edtPassword);
		email = (EditText) findViewById(R.id.edtEmail);
		region = (Spinner) findViewById(R.id.spnRegion);
		region.setOnItemSelectedListener(new OnRegionSelectedListener());
		submit = (Button) findViewById(R.id.btnSubmit);
		task1 =  new AsyncTask<String, Void, Void>() {
			@Override
			protected Void doInBackground(String... params) {
				usernameList = new ArrayList<String>();
				hg = new HttpGet("http://nammaapp.in/scripts/usernamecheck.php?token=xtt68kjf90hs2a");
				try {
					r = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(r.getEntity().getContent(), "UTF-8"));
					while((html = br.readLine()) != null)
						s += html;
					Document userd = Jsoup.parse(s, "", Parser.xmlParser());
					for(Element e : userd.select("info"))
						usernameList.add(e.text().toString());
				} catch (Exception e) {
					runOnUiThread( new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "Oops something went wrong!", Toast.LENGTH_LONG).show();
							finish();
							startActivity(getIntent());
						}
					});
					Log.e("check for errors", "here", e);
				}
				return null;
			}
		};
		task1.execute();
		username.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					ed = (EditText) v;
					checkStr = ed.getText().toString();
					if(checkStr.length() != 0) {
						if(usernameList.contains(checkStr))
							Toast.makeText(getApplicationContext(), "Username Already Taken, Please choose a different one",Toast.LENGTH_LONG).show();
				} else {
					return;
				}
			} 
		}});
		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() < 6)
						Toast.makeText(getApplicationContext(), "Password Should be greater than 6 characters",Toast.LENGTH_LONG).show();
				} else 
					return;
			}
		});
		name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() == 0)
						Toast.makeText(getApplicationContext(), "Please enter a name",Toast.LENGTH_LONG).show();   
				} else 
					return;
			}
		});
		email.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() == 0)
						Toast.makeText(getApplicationContext(), "Please Enter an Email",Toast.LENGTH_LONG).show();
					else 
						return;
				}
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(name.getText().toString().matches("")) {
					Toast.makeText(getBaseContext(),"Please Fill the name...",Toast.LENGTH_LONG).show();
					return;
				}
				if(username.getText().toString().matches("")){
					Toast.makeText(getBaseContext(),"Please Fill the Username...",Toast.LENGTH_LONG).show();
					return;
				}
				if(usernameList.contains(username.getText().toString())) {
					Toast.makeText(getBaseContext(),"Username is already Taken, please use a different one",Toast.LENGTH_LONG).show();
					return;
				}
				if(password.getText().toString().length() < 6) {
					Toast.makeText(getBaseContext(),"Password must be atleast 6 characters long",Toast.LENGTH_LONG).show();
					return;
				}
				if(email.getText().toString().matches("")) {
					Toast.makeText(getBaseContext(),"Please Enter an Email ID for the records",Toast.LENGTH_LONG).show();
					return;
				}
				AsyncTask<String, String, String> task2 =  new AsyncTask<String, String, String>() {
					String resultinfo = null,errormsg="";
					Exception e1;
					String s = "";
					@Override
					protected String doInBackground(String... params) {
						hc = new DefaultHttpClient();
						hp = new HttpPost("http://nammaapp.in/scripts/signup.php");
						ArrayList<NameValuePair> postparameters = new ArrayList<NameValuePair>();
						postparameters.add(new BasicNameValuePair("starttoken", "f45e8v23jk5x3p917q106npuwlh94v3zpige9d80"));
						postparameters.add(new BasicNameValuePair("name", name.getText().toString()));
						postparameters.add(new BasicNameValuePair("username", username.getText().toString()));
						postparameters.add(new BasicNameValuePair("password", password.getText().toString()));
						postparameters.add(new BasicNameValuePair("mail", email.getText().toString()));
						int kn = 0;
						if(pref.getString("kannada", "notSet") == "true")
							kn = 1;
						else if(pref.getString("kannada", "notSet") == "false")	
							kn = 0;
						postparameters.add(new BasicNameValuePair("kannada", Integer.toString(kn)));
						postparameters.add(new BasicNameValuePair("regionID", Integer.toString(regionIDcalc(selectedRegion))));
						try {
							hp.setEntity(new UrlEncodedFormEntity(postparameters));
							r = hc.execute(hp);
							br = new BufferedReader(new InputStreamReader(r.getEntity().getContent(),"UTF-8"));
							String html;
							while((html = br.readLine())!= null)
								s += html;
							Document d = Jsoup.parse(s, "", Parser.xmlParser());
								SharedPreferences.Editor editor;
								editor = pref.edit();
								editor.putString("name", name.getText().toString());
								editor.putString("username", username.getText().toString());
								editor.putString("password", password.getText().toString());
								editor.putString("region", selectedRegion);
								editor.putString("email", email.getText().toString());
								editor.putString("token", d.select("token").text().toString());
								editor.putString("userID", d.select("userID").text().toString());
								editor.putString("xppoints", "0");
								editor.putString("ansupvotes", "0");
								editor.putString("quesupvotes", "0");
								editor.commit();
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "User Successfully created", Toast.LENGTH_LONG).show();
										if(pref.getString("kannada", "notSet") == "true") {
											Intent startIntent = new Intent(RegisterActivity.this, KannadaHomeActivity.class);
											startActivity(startIntent);
										} else if (pref.getString("kannada", "notSet") == "false"){
											Intent startIntent = new Intent(RegisterActivity.this, EnglishHomeActivity.class);
											startActivity(startIntent);
										}
									}
								});
						} catch(Exception e) {
							e1 = e;
							runOnUiThread(new Runnable() {
								public void run() {
									Toast toast = Toast.makeText(getApplicationContext(), "Swalpa Error, Adjust MaaDi! 1",Toast.LENGTH_LONG);
									toast.show();
									Log.e("warning", "checkhere", e1);
								}
							});
							submit.requestFocus();
						}
						return null;
					}
					protected void onPostExecute(String result) {	}
					};
					task2.execute();
				}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
    @Override
    public void onBackPressed() {
    	String name = pref.getString("name", "notSet");
		String username = pref.getString("username", "notSet");
		String password = pref.getString("password", "notSet");
		String email = pref.getString("email", "notSet");
		String region = pref.getString("region", "notSet");
		if(name == "notSet" || username == "notSet" || password == "notSet" || email == "notSet" || region == "notSet")		
			super.onBackPressed();
		else {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		}
    }
    
    private int regionIDcalc(String region) {
    	ArrayList<String> ar = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.region_arrays)));
    	return ar.lastIndexOf(selectedRegion);
    }

} 