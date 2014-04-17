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

public class ForumHomeActivity extends Activity {
	ArrayList<Elements> ar;
	ArrayList<QuestionItem> questions;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	HttpGet hg;
	BufferedReader br;
	String questionpulldata;
	ListView l;
	QuestionItem item2,item3,item4;
	TextView upvotes,downvotes,xp,Qupvotes,Ansupvotes;
	Button btnupvote,btndownvote,btnanswer;
	View paramv;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		ar = new ArrayList<Elements>();
		questions = new ArrayList<QuestionItem>();
		AsyncTask<String,String,String> questionpull = new AsyncTask<String,String,String>() {
			@Override
			protected String doInBackground(String... params) {
				questionpulldata="";
				hc = new DefaultHttpClient();
				String kannada = "";
				if(pref.getString("kannada", "notSet") == "true")
					kannada = "1";
				else if(pref.getString("kannada", "notSet") == "false")
					kannada = "0";
				hg = new HttpGet("http://nammaapp.in/scripts/pullquestions.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet") + "&kannada=" + kannada);
				try {
					hr = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(),"UTF-8"));
					String html;
					while((html = br.readLine())!=null)
						questionpulldata += html;
				} catch(Exception e) {
					Log.e("message on question pull", "question pull failed", e);
				}
				return questionpulldata;
			}
			
			@Override
			protected void onPostExecute(String result) {
				Document d = Jsoup.parse(result, "", Parser.xmlParser());
				for(Element e : d.select("questionitem")) {
					ar.add(e.getAllElements());
				}
				Iterator<Elements> it = ar.iterator();
				while(it.hasNext()) {
					Elements e1 = it.next();
					QuestionItem q = new QuestionItem();
					q.setQuestion(e1.get(1).text());
					q.setEngword(e1.get(2).text());
					q.setKanword(e1.get(3).text());
					q.setName(e1.get(4).text());
					q.setQuestionID(e1.get(5).text());
					q.setUpvotes(e1.get(6).text());
					q.setAnscount(e1.get(7).text());
					q.setUserID(e1.get(8).text());
					q.setXp(e1.get(9).text().toString());
					q.setQupvote(e1.get(10).text().toString());
					q.setAupvote(e1.get(11).text().toString());
					q.setRegion(e1.get(12).text().toString());
					questions.add(q);
				}
				l = (ListView) findViewById(R.id.Qlist);
				QuestionAdapter q = new QuestionAdapter(getBaseContext(), R.layout.questionrow, questions);
				l.setAdapter(q);
			}
		};
		questionpull.execute();
		
		Button btnSearch = (Button) findViewById(R.id.btnHomeSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent is = new Intent(ForumHomeActivity.this, SearchActivity.class);
				startActivity(is);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forum, menu);
		return true;
	}
	
	private class QuestionAdapter extends ArrayAdapter<QuestionItem> {
		private ArrayList<QuestionItem> items;
		QuestionItem item;
		Context context;		
		public QuestionAdapter(Context context,	int textViewResourceId, ArrayList<QuestionItem> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null) {
	            LayoutInflater vi = LayoutInflater.from(context);
	            v = vi.inflate(R.layout.questionrow, null);
	        }
	        item = items.get(position);
	        if(item != null) {
                TextView question = (TextView) v.findViewById(R.id.txtQuestion);
                TextView engword = (TextView) v.findViewById(R.id.txtQEngWord);
                TextView kanword = (TextView) v.findViewById(R.id.txtQKanWord);
                upvotes = (TextView) v.findViewById(R.id.txtQUpVotes);
                TextView answercount = (TextView) v.findViewById(R.id.txtQAnswerCount);
                TextView name = (TextView) v.findViewById(R.id.txtQUsername);
                xp = (TextView) v.findViewById(R.id.txtQXP);
                Qupvotes = (TextView) v.findViewById(R.id.txtQQUp);
                Ansupvotes = (TextView) v.findViewById(R.id.txtQAUp);
                TextView regionID = (TextView) v.findViewById(R.id.txtQRegion);
                btnanswer = (Button) v.findViewById(R.id.btnQAnswer);
                btnupvote = (Button) v.findViewById(R.id.btnQUpvote);
                btndownvote = (Button) v.findViewById(R.id.btnQDownvote);
                question.setText(item.getQuestion());
                engword.setText("ENG Word:" + item.getEngword());
                kanword.setText("KAN Word: " + item.getKanword());
                upvotes.setText("Upvotes: " + item.getUpvotes().toString());
                answercount.setText("Answers: " + item.getAnscount());
                name.setText(item.getName());
                xp.setText("");
                Qupvotes.setText("");
                Ansupvotes.setText("");
                String [] s = getResources().getStringArray(R.array.region_arrays);
                if(item.getRegion().length() > 0)
                	regionID.setText("REGION: " + s[Integer.parseInt(item.getRegion())]);
                else
                	regionID.setText("REGION: Not specified");
                
                question.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int position1 = l.getPositionForView((View) v.getParent());
						QuestionItem item1 = items.get(position1);
		                Bundle b = new Bundle();
		                b.putString("Q_ID", item1.getQuestionID());
		                b.putString("question", item1.getQuestion());
						b.putString("engword", item1.getEngword());
						b.putString("kanword", item1.getKanword());
						b.putString("upvotes", item1.getUpvotes());
						b.putString("answercount", item1.getAnscount());
						b.putString("regionID" , item1.getRegion());
						b.putString("name", item1.getName());
						b.putString("userqupvotes", item1.getQupvote());
						b.putString("useransupvotes", item1.getAupvote());
						b.putString("userxp", item1.getXp());
						Intent i = new Intent(ForumHomeActivity.this, QuestionDisplayActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});

                btnupvote.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int position2 = l.getPositionForView((View) v.getParent());
							item2 = items.get(position2);
							final Button b = (Button) v;
							AsyncTask<String,String,String> taskupvote = new AsyncTask<String, String, String>() {
								@Override
								protected String doInBackground(String... params) {
									hc = new DefaultHttpClient();
									hp = new HttpPost("http://nammaapp.in/scripts/upvotequestion.php");
									ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
									postparameters.add(new BasicNameValuePair("Q_ID", item2.getQuestionID()));
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
	                
	                	btndownvote.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int position = l.getPositionForView((View) v.getParent());
							item3 = items.get(position);
							final Button b1 = (Button) v;
							AsyncTask<String,String,String> taskdownvote = new AsyncTask<String, String, String>() {
								@Override
								protected String doInBackground(String... params) {
									hc = new DefaultHttpClient();
									hp = new HttpPost("http://nammaapp.in/scripts/downvotequestion.php");
									ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
									postparameters.add(new BasicNameValuePair("Q_ID", item3.getQuestionID()));
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
									b1.setEnabled(false);
									Toast.makeText(getBaseContext(), "Question downvoted!", Toast.LENGTH_SHORT).show();
								}
							};
							taskdownvote.execute();
						}
					});
	                
                btnanswer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position = l.getPositionForView((View) v.getParent());
						item4 = items.get(position);
						Bundle b = new Bundle();
		                b.putString("Q_ID", item4.getQuestionID());
		                b.putString("question", item4.getQuestion());
						b.putString("engword", item4.getEngword());
						b.putString("kanword", item4.getKanword());
						b.putString("upvotes", item4.getUpvotes());
						b.putString("answercount", item4.getAnscount());
						b.putString("regionID" , item4.getRegion());
						b.putString("name", item4.getName());
						b.putString("userqupvotes", item4.getQupvote());
						b.putString("useransupvotes", item4.getAupvote());
						b.putString("userxp", item4.getXp());
						Intent i = new Intent(ForumHomeActivity.this, QuestionDisplayActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});
	
	}
			return v;
		}
	}
}
