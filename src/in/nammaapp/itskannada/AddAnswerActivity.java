package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import android.widget.TextView;
import android.widget.Toast;

public class AddAnswerActivity extends Activity {
	Bundle info;
	String selectedRegion;
	EditText answer,anskanword,ansengword;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	BufferedReader br;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_answer_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		Intent i = getIntent();
		info = i.getExtras();
		TextView question = (TextView) findViewById(R.id.txtQuestionAA);
		TextView engword = (TextView) findViewById(R.id.txtQEngWordAA);
		TextView kanword = (TextView) findViewById(R.id.txtQKanWordAA);
		TextView region = (TextView) findViewById(R.id.txtQRegionAA);
		TextView upvotes = (TextView) findViewById(R.id.txtQUpVotesAA);
		TextView anscount = (TextView) findViewById(R.id.txtQAnswerCountAA);
		TextView name = (TextView) findViewById(R.id.txtQUsernameAA);
		TextView xp = (TextView) findViewById(R.id.txtQXPAA);
		TextView userqup = (TextView) findViewById(R.id.txtQQUpAA);
		TextView useraup = (TextView) findViewById(R.id.txtQAUpAA);
		
		question.setText(info.getString("question"));
		engword.setText("ENG WORD: " + info.getString("engword"));
		kanword.setText("KAN WORD: " + info.getString("kanword"));
		String [] s = getResources().getStringArray(R.array.region_arrays);
		region.setText("REGION: " + s[Integer.parseInt(info.getString("regionID"))]);
		upvotes.setText("Upvotes: " + info.getString("upvotes"));
		anscount.setText("Answer Count: " + info.getString("answercount"));
		name.setText(info.getString("name"));
		xp.setText("XP: " + info.getString("userxp"));
		userqup.setText("Q Up: " + info.getString("userqupvotes"));
		useraup.setText("A Up: " + info.getString("useransupvotes"));
		
		answer = (EditText) findViewById(R.id.ansContent);
		Spinner regionspinner = (Spinner) findViewById(R.id.spnRegion4ans);
		regionspinner.setOnItemSelectedListener(new OnRegionSelectedListener());
		ansengword = (EditText) findViewById(R.id.engwordContent);
		anskanword = (EditText) findViewById(R.id.kanwordContent);
		final Button submitbutton = (Button) findViewById(R.id.buttonSubmitAnswer);
		//Button pronunce = (Button) findViewById(R.id.buttonPronunceCapture);
		answer.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				EditText ed = (EditText) v;
				if(!hasFocus) {
					if(ed.getText().toString().length() == 0)
						Toast.makeText(getBaseContext(), "Please fill in the answer..", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		submitbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(answer.getText().toString().matches("")){
					Toast.makeText(getBaseContext(), "Please fill in the answer..", Toast.LENGTH_LONG).show();
					answer.requestFocus();
				} else {
					AsyncTask<String,String,String> SubmitAnsTask =  new AsyncTask<String, String, String>() {
						String results;
						protected String doInBackground(String... params) {
							hc = new DefaultHttpClient();
							hp = new HttpPost("http://nammaapp.in/scripts/addanswer.php");
							//TODO change to prefs
							ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
							postparameters.add(new BasicNameValuePair("Q_ID", info.getString("Q_ID")));
							postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
							postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
							postparameters.add(new BasicNameValuePair("answer", answer.getText().toString()));
							if(!ansengword.getText().toString().matches(""))
								postparameters.add(new BasicNameValuePair("engword", ansengword.getText().toString()));
							if(!anskanword.getText().toString().matches(""))
								postparameters.add(new BasicNameValuePair("kanword", anskanword.getText().toString()));
							postparameters.add(new BasicNameValuePair("R_ID", Integer.toString(regionIDcalc(selectedRegion))));
							try {
								hp.setEntity(new UrlEncodedFormEntity(postparameters));
								hr = hc.execute(hp);
								br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(), "UTF-8"));
								String html; results="";
								while((html = br.readLine()) != null)
									results += html;
								} catch(Exception e) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getBaseContext(), "Oops, Something went wrong", Toast.LENGTH_LONG).show();
										}
									});
									Log.e("check", "message", e);
									submitbutton.requestFocus();
								}
							return results;
						}
						protected void onPostExecute(String result) {
								Toast.makeText(getApplicationContext(), "Answer Successfully Posted", Toast.LENGTH_LONG).show();
								Intent i = new Intent(AddAnswerActivity.this, ForumHomeActivity.class);
								startActivity(i);
						}
					};
					SubmitAnsTask.execute();
				}
			}
		});
 		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_answer, menu);
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
