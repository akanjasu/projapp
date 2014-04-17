package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
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
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionDisplayActivity extends Activity {
	ArrayList<Elements> ar;
	ArrayList<AnswerItem> answers;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	HttpGet hg;
	BufferedReader br;
	String answerpulldata;
	ListView l;
	AnswerItem item2,item3,item4;
	TextView upvotes,xp,Userqupvotes,Useransupvotes;
	Button btnupvote,btndownvote,btnanswer;
	Bundle info;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_display_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		Intent i = getIntent();
		info = i.getExtras();
		TextView question = (TextView) findViewById(R.id.txtQuestionA);
		TextView qengword = (TextView) findViewById(R.id.txtQEngWordA);
		TextView qkanword = (TextView) findViewById(R.id.txtQKanWordA);
		TextView qregion = (TextView) findViewById(R.id.txtQRegionA);
		TextView qupvotes = (TextView) findViewById(R.id.txtQUpVotesA);
		TextView qanscount = (TextView) findViewById(R.id.txtQAnswerCountA);
		TextView qname = (TextView) findViewById(R.id.txtQUsernameA);
		TextView qusrxp = (TextView) findViewById(R.id.txtQXPA);
		TextView qusrqup = (TextView) findViewById(R.id.txtQQUpA);
		TextView quseraup = (TextView) findViewById(R.id.txtQAUpA);
		Button btnupvoteq = (Button) findViewById(R.id.btnQUpvoteA);
		Button btndownvoteq = (Button) findViewById(R.id.btnQDownvoteA);
		Button btnanswerq = (Button) findViewById(R.id.btnQAnswerA);
		
		question.setText(info.getString("question"));
		qengword.setText("ENG WORD: " + info.getString("engword"));
		qkanword.setText("KAN WORD: " + info.getString("kanword"));
		String [] s = getResources().getStringArray(R.array.region_arrays);
		if(info.getString("regionID").length() > 0)
			qregion.setText("REGION: " + s[Integer.parseInt(info.getString("regionID"))]);
		else
			qregion.setText("REGION: Not specified");
        qupvotes.setText("Upvotes: " + info.getString("upvotes"));
        qanscount.setText("Answers: " + info.getString("answercount"));
        qname.setText(info.getString("name"));
        qusrxp.setText("");
        qusrqup.setText("");
        quseraup.setText("");
        
        btnupvoteq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Button b = (Button) v;
				AsyncTask<String,String,String> taskupvote = new AsyncTask<String, String, String>() {
					@Override
					protected String doInBackground(String... params) {
						hc = new DefaultHttpClient();
						hp = new HttpPost("http://nammaapp.in/scripts/upvotequestion.php");
						ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
						postparameters.add(new BasicNameValuePair("Q_ID", info.getString("Q_ID")));
						postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
						postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
						try {
							hp.setEntity(new UrlEncodedFormEntity(postparameters));
							hr = hc.execute(hp);
						} catch (Exception e) {
							Log.e("message","care",e);
						}
						return null;
					}
					
					@Override
					protected void onPostExecute(String result) {
						b.setEnabled(false);
						Toast.makeText(getBaseContext(), "Question upvoted!", Toast.LENGTH_SHORT).show();
					}
				};
				taskupvote.execute();
			}
		});
        
        btndownvoteq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Button b1 = (Button) v;
				AsyncTask<String,String,String> taskdownvote = new AsyncTask<String, String, String>() {
					@Override
					protected String doInBackground(String... params) {
						hc = new DefaultHttpClient();
						hp = new HttpPost("http://nammaapp.in/scripts/downvotequestion.php");
						ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
						postparameters.add(new BasicNameValuePair("Q_ID", info.getString("Q_ID")));
						postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
						postparameters.add(new BasicNameValuePair("token", pref.getString("token" , "notSet")));
						try {
							hp.setEntity(new UrlEncodedFormEntity(postparameters));
							hr = hc.execute(hp);
						} catch (Exception e) {
							Log.e("message","care",e);
						}
						return null;
					}
					
					@Override
					protected void onPostExecute(String result) {
						b1.setEnabled(false);
						Toast.makeText(getBaseContext(), "Question downvoted!", Toast.LENGTH_SHORT).show();
					}
				};
				taskdownvote.execute();
				
			}
		});
        
        btnanswerq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i1 = new Intent(QuestionDisplayActivity.this, AddAnswerActivity.class);
				i1.putExtras(info);
				startActivity(i1);
			}
		});
		ar = new ArrayList<Elements>();
		answers = new ArrayList<AnswerItem>();
		AsyncTask<String,String,String> answerpull = new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				answerpulldata = "";
				hc = new DefaultHttpClient();
				hg = new HttpGet("http://nammaapp.in/scripts/pullanswers.php?Q_ID=" + info.getString("Q_ID") + "&U_ID=" + pref.getString("userID", "notSet") + "&token=" + pref.getString("token", "notSet"));
				try {
					hr = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(),"UTF-8"));
					String html;
					while((html = br.readLine())!=null)
						answerpulldata += html;
				} catch(Exception e) {
					Log.e("message on question pull", "question pull failed", e);
				}
				return answerpulldata;	
			}
			
			protected void onPostExecute(String result) {
				Document d = Jsoup.parse(result, "", Parser.xmlParser());
				for(Element e : d.select("answeritem"))
					ar.add(e.getAllElements());
				Iterator<Elements> it = ar.iterator();
				while(it.hasNext()) {
					Elements e1 = it.next();
					AnswerItem a = new AnswerItem();
					a.setAnswer(e1.get(1).text());
					a.setEngword(e1.get(2).text());
					a.setKanword(e1.get(3).text());
					a.setName(e1.get(4).text());
					a.setAnswerID(e1.get(5).text());
					a.setUpvotes(e1.get(6).text());
					a.setXp(e1.get(7).text());
					a.setUserqup(e1.get(8).text());
					a.setUseraup(e1.get(9).text());
					a.setRegionID(e1.get(10).text());
					a.setAccepted(e1.get(11).text());
					answers.add(a);
				}
				l = (ListView) findViewById(R.id.Alist);
				
				if(answers.size() > 0) {
					AnswerAdapter ansadap = new AnswerAdapter(getBaseContext(), R.layout.answerrow, answers);
					l.setAdapter(ansadap);
				} else {
					l.setAdapter(null);
					TextView title = (TextView) findViewById(R.id.textView1x);
					title.setText("");
					Toast.makeText(getBaseContext(), "No Answers Posted...", Toast.LENGTH_LONG).show();
				}
			}
		};
		answerpull.execute();
	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_display, menu);
		return true;
	}
	
	private class AnswerAdapter extends ArrayAdapter<AnswerItem> {
		private ArrayList<AnswerItem> items;
		AnswerItem item;
		Context context;
		public AnswerAdapter(Context context,	int textViewResourceId, ArrayList<AnswerItem> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null) {
	            LayoutInflater vi = LayoutInflater.from(context);
	            v = vi.inflate(R.layout.answerrow, null);
	        }
	        item = items.get(position);
	        if(item != null) {
	        	TextView answer = (TextView) v.findViewById(R.id.txtAnswer);
	        	TextView engword = (TextView) v.findViewById(R.id.txtAEngWord);
	        	TextView kanword = (TextView) v.findViewById(R.id.txtAKanWord);
	        	TextView region = (TextView) v.findViewById(R.id.txtARegion);
	        	TextView upvotes = (TextView) v.findViewById(R.id.txtAUpVotes);
	        	TextView userxp = (TextView) v.findViewById(R.id.txtAUXP);
	        	TextView userqup = (TextView) v.findViewById(R.id.txtAUQUp);
	        	TextView useraup = (TextView) v.findViewById(R.id.txtAUAUp);
	        	TextView name = (TextView) v.findViewById(R.id.txtAUsername);
	        	TextView Accepted = (TextView) v.findViewById(R.id.txtAAccepted);
	        	Button btnupvote = (Button) v.findViewById(R.id.btnAUpvote);
	        	Button btndownvote = (Button) v.findViewById(R.id.btnADownvote);
	        	//Button pronunce = (Button) v.findViewById(R.id.btnpronunceWord);
	        	
	        	answer.setText(item.getAnswer());
	        	engword.setText("ENG WORD: " + item.getEngword());
	        	kanword.setText("KAN WORD: " + item.getKanword());
	        	String [] s = getResources().getStringArray(R.array.region_arrays);
	        	if(item.getRegionID().length() > 0)
	        		region.setText("REGION: " + s[Integer.parseInt(item.getRegionID())]);
	        	else
	        		region.setText("REGION: Not specified");
                upvotes.setText("Upvotes: " + item.getUpvotes());
                userxp.setText("XP: " + item.getXp());
                userqup.setText("Q Up: " + item.getUserqup());
                useraup.setText("A Up: " + item.getUseraup());
                name.setText(item.getName());
                if(Integer.parseInt(item.getAccepted()) == 0)
                	Accepted.setText("");
                else
                	Accepted.setText("ACCEPTED ANSWER");
                btnupvote.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int positionup = l.getPositionForView((View) v.getParent());
						final AnswerItem item1 = items.get(positionup);
						final Button b2 = (Button) v;
						AsyncTask<String,String,String> aupvotetask = new AsyncTask<String, String, String>() {
							@Override
							protected String doInBackground(String... params) {
								hc = new DefaultHttpClient();
								hp = new HttpPost("http://nammaapp.in/scripts/upvoteanswer.php");
								ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
								postparameters.add(new BasicNameValuePair("A_ID", item1.getAnswerID()));
								postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
								postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
								try {
									hp.setEntity(new UrlEncodedFormEntity(postparameters));
									hr = hc.execute(hp);
								} catch (Exception e) {
									Log.e("message","care",e);
								}
								return null;
							}
							
							@Override
							protected void onPostExecute(String result) {
								b2.setEnabled(false);
								Toast.makeText(getBaseContext(), "Answer upvoted!", Toast.LENGTH_SHORT).show();
							}
						};
						aupvotetask.execute();
					}
				});
                
                btndownvote.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int positionfordown = l.getPositionForView((View) v.getParent());
						final AnswerItem item1 = items.get(positionfordown);
						final Button b3 = (Button) v;
						AsyncTask<String,String,String> adownvotetask = new AsyncTask<String, String, String>() {
							@Override
							protected String doInBackground(String... params) {
								hc = new DefaultHttpClient();
								hp = new HttpPost("http://nammaapp.in/scripts/downvoteanswer.php");
								ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
								postparameters.add(new BasicNameValuePair("A_ID", item1.getAnswerID()));
								postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
								postparameters.add(new BasicNameValuePair("token", pref.getString("token", "notSet")));
								try {
									hp.setEntity(new UrlEncodedFormEntity(postparameters));
									hr = hc.execute(hp);
								} catch (Exception e) {
									Log.e("message","care",e);
								}
								return null;
							}
							
							@Override
							protected void onPostExecute(String result) {
								b3.setEnabled(false);
								Toast.makeText(getBaseContext(), "Answer downvoted!", Toast.LENGTH_SHORT).show();
							}
						};
						adownvotetask.execute();
					}
				});
	        }
        return v;
		}

	}
}