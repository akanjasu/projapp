package in.nammaapp.itskannada;

import java.io.BufferedReader;
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

public class QuestionActivity extends Activity {
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	BufferedReader br;
	String selectedRegion;
	EditText question,engword,kanword;
	Button questionsubmit;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		question = (EditText) findViewById(R.id.quesContent);
		engword = (EditText) findViewById(R.id.QuesengwordContent);
		kanword = (EditText) findViewById(R.id.QueskanwordContent);
		Spinner regionspinner = (Spinner) findViewById(R.id.spnRegion4ques);
		questionsubmit = (Button) findViewById(R.id.buttonSubmitQuestion);
		
		regionspinner.setOnItemSelectedListener(new OnRegionSelectedListener());
		
		question.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				EditText ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() == 0) {
						Toast.makeText(getBaseContext(),"Please enter the Question...", Toast.LENGTH_LONG).show();
					}
				}
				
			}
		});
		
		questionsubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(question.getText().toString().length() == 0) {
					Toast.makeText(getBaseContext(), "Please fill in the question...", Toast.LENGTH_LONG).show();
					question.requestFocus();
				} else {
					AsyncTask<String,String,String> postquestiontask = new AsyncTask<String,String,String>() {
						@Override
						protected String doInBackground(String... params) {							
							hc = new DefaultHttpClient();
							hp = new HttpPost("http://nammaapp.in/scripts/addquestion.php");
							ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
							postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
							postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
							postparameters.add(new BasicNameValuePair("question", question.getText().toString()));
							postparameters.add(new BasicNameValuePair("R_ID", Integer.toString(regionIDcalc(selectedRegion))));
							if(!engword.getText().toString().matches(""))
								postparameters.add(new BasicNameValuePair("engword", engword.getText().toString()));
							if(!kanword.getText().toString().matches(""))
								postparameters.add(new BasicNameValuePair("kanword", kanword.getText().toString()));
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
								questionsubmit.requestFocus();
							}
							return null;
						}
						
						@Override
						protected void onPostExecute(String result) {
							Toast.makeText(getApplicationContext(), "Question Successfully Posted", Toast.LENGTH_LONG).show();
							if(pref.getString("kannada", "notSet") == "true"){
								Intent i = new Intent(QuestionActivity.this, KannadaHomeActivity.class);
								startActivity(i);
							} else if(pref.getString("kannada", "notSet") == "false") {
								Intent i = new Intent(QuestionActivity.this, EnglishHomeActivity.class);
								startActivity(i);
							}
						}
					};
					postquestiontask.execute();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
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
