package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	private static ArrayList<Elements> ar;		
	private ArrayList<QuestionItem> questions;
	private EditText etxt;
	private Button sbtn;
	ListView lv;
	public static int i=0,j=0,k=0;
	HttpClient hc;
	HttpPost hp;
	HttpResponse hr;
	HttpGet hg;
	BufferedReader br;
	TextView upvotes,downvotes,xp,Qupvotes,Ansupvotes;
	Button btnupvote,btndownvote,btnanswer;
	QuestionItem item2,item3,item4;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_forum_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		ar = new ArrayList<Elements>();
		questions = new ArrayList<QuestionItem>();
		etxt=(EditText) findViewById(R.id.editText1);
		sbtn=(Button) findViewById(R.id.btnsearch);
		lv = (ListView) findViewById(R.id.Qlist);
		
		sbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				questions.clear();
				ar.clear();
				i=0;
				j=0;
				k=0;
				lv.setAdapter(null);
				if(etxt.getText().toString().length()==0)
					Toast.makeText(getApplicationContext(),
						    "Please enter the question", Toast.LENGTH_SHORT).show();
				else
				{
					new AsyncTask<String,Void,String>(){

						@Override
						protected void onPreExecute()
						{
							
						}
						@Override
						protected String doInBackground(String... arg0) {
							String xml = httpcall(arg0[0]);
							//String[] strarray =null;
							Document d4 = Jsoup.parse(xml,"",Parser.xmlParser());
							String resp = "";
							for(Element e : d4.select("message")) {
								ar.add(e.getAllElements());
								}
							
								Iterator<Elements> ite = ar.iterator();
							
							while(ite.hasNext()) {
								Elements e1 = ite.next();
								resp = e1.get(1).text().toString();
								}
							if(resp.length()>0)
								return "no";
							
							else
							{//final ArrayList<String> strarray = new ArrayList<String>();
								ar.clear();
							StringTokenizer st = new StringTokenizer(xml, "||");
							String[] s1= {null,null,null};
							
							Integer x=0;
							while (st.hasMoreTokens())
							{
							s1[x]=st.nextToken();
							x++;
							}
							s1[1]="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+s1[1];
							s1[2]="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+s1[2];
							//String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><questionarray><questionitem><question>hfdkfsdlklfklakfldkfkflkalkfdl fjldklfdkl kfjakljello</question><engword>hello</engword><kanword>hello</kanword><name>jayanth</name><questionID>h1</questionID><upvotes>3</upvotes><anscount>3</anscount><userID>78</userID><xp>8</xp><qupvote>3</qupvote><aupvote>3</aupvote><regionID>1</regionID></questionitem><questionitem><question>hello1</question><engword>hello1</engword><kanword>hello1</kanword><name>jayanth1</name><questionID>h11</questionID><upvotes>31</upvotes><anscount>31</anscount><userID>781</userID><xp>81</xp><qupvote>31</qupvote><aupvote>31</aupvote><regionID>1</regionID></questionitem></questionarray>";
						
							Document d = Jsoup.parse(s1[0],"",Parser.xmlParser());
							//,"",Parser.xmlParser());
								for(Element e : d.select("questionitem")) {
									ar.add(e.getAllElements());
									i++;
								}
							Document d1 = Jsoup.parse(s1[1],"",Parser.xmlParser());
								//,"",Parser.xmlParser());
									for(Element e : d1.select("questionitem")) {
										ar.add(e.getAllElements());
										j++;
									}
							Document d2 = Jsoup.parse(s1[2],"",Parser.xmlParser());
									//,"",Parser.xmlParser());
										for(Element e : d2.select("questionitem")) {
											ar.add(e.getAllElements());
											k++;
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
								q.setXp(e1.get(9).text());
								q.setQupvote(e1.get(10).text());
								q.setAupvote(e1.get(11).text());
								q.setRegion(e1.get(12).text());
								questions.add(q);
							}
							return "yes";
						}	
							
						}
						
						@Override
						protected void onPostExecute(String content)
						{
							if(content.contentEquals("yes"))
							{
							QuestionAdapter q = new QuestionAdapter(SearchActivity.this, R.layout.questionrow, questions){
								@Override
								  public View getView(int position, View convertView, ViewGroup parent) {
									  View view = super.getView(position, convertView, parent); 
									  
									  if (position < i ) {
									      view.setBackgroundColor(Color.GREEN);  
									  } else if(position <= (i+j-1))
									  {
									      view.setBackgroundColor(Color.YELLOW);  
									  }
									  else
										  view.setBackgroundColor(Color.RED);
									  return view;
								    
								  }
							};
							lv.setAdapter(q);
							}
							else
							{
							lv.setAdapter(null);
							Toast.makeText(getApplicationContext(),
								    "No relevant Questions found", Toast.LENGTH_SHORT).show();
							}
					
						}
						
					}.execute(etxt.getText().toString());
					
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_forum, menu);
		return true;
	}

	public String httpcall(String query)
	{
		
		HttpClient httpc = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://nammaapp.in/scripts/forumquestionsearch.php");
		HttpResponse resp = null;
		BufferedReader br=null;
		try
		{

		httppost.setHeader("Connection", "keep-alive");
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		//query = String.format("key=%s", URLEncoder.encode(str1, "UTF-8"));
		String query1 = String.format("query=%s", URLEncoder.encode(query, "UTF-8"));
		httppost.setEntity(new StringEntity(query1.toString()));
		resp = httpc.execute(httppost);
		br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"UTF-8"));
		String htmltext = "";
		String str = "";
			while ((str = br.readLine()) != null) { 
							htmltext +=str;
			}	
			return htmltext;
		}
		catch(Exception e)
		{
			return e.toString();
		}
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
                upvotes.setText("Upvotes: " + item.getUpvotes());
                answercount.setText("Answers: " + item.getAnscount());
                name.setText(item.getName());
                xp.setText("XP: " + item.getXp());
                Qupvotes.setText("Q Up: "+ item.getQupvote());
                Ansupvotes.setText("A Up: " + item.getAupvote());
                String [] s = getResources().getStringArray(R.array.region_arrays);
                regionID.setText("REGION: " + s[Integer.parseInt(item.getRegion())]);
                
                question.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int position1 = lv.getPositionForView((View) v.getParent());
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
						Intent i = new Intent(SearchActivity.this, QuestionDisplayActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});

                btnupvote.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int position2 = lv.getPositionForView((View) v.getParent());
							item2 = items.get(position2);
							final Button b = (Button) v;
							AsyncTask<String,String,String> taskupvote = new AsyncTask<String, String, String>() {
								@Override
								protected String doInBackground(String... params) {
									hc = new DefaultHttpClient();
									hp = new HttpPost("http://nammaapp.in/scripts/upvotequestion.php");
									ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
									postparameters.add(new BasicNameValuePair("Q_ID", item2.getQuestionID()));
									postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID","notSet")));
									postparameters.add(new BasicNameValuePair("token", pref.getString("token","notSet")));
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
								}
							};
							taskupvote.execute();
						}
					});
	                
	                	btndownvote.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int position = lv.getPositionForView((View) v.getParent());
							item3 = items.get(position);
							final Button b1 = (Button) v;
							AsyncTask<String,String,String> taskdownvote = new AsyncTask<String, String, String>() {
								@Override
								protected String doInBackground(String... params) {
									hc = new DefaultHttpClient();
									hp = new HttpPost("http://nammaapp.in/scripts/downvotequestion.php");
									ArrayList<BasicNameValuePair> postparameters = new ArrayList<BasicNameValuePair>();
									//TODO change to values when deployed
									postparameters.add(new BasicNameValuePair("Q_ID", item3.getQuestionID()));
									postparameters.add(new BasicNameValuePair("U_ID", pref.getString("userID", "notSet")));
									postparameters.add(new BasicNameValuePair("token", pref.getString("token","notSet")));
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
								}
							};
							taskdownvote.execute();
						}
					});
	                
                btnanswer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position = lv.getPositionForView((View) v.getParent());
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
						Intent i = new Intent(SearchActivity.this, QuestionDisplayActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});
	        }
			return v;
		}
	}
}
