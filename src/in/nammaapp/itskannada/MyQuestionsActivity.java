package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class MyQuestionsActivity extends Activity {
	ArrayList<QuestionItem> questions;
	QuestionItem item,item1,item2,item3,item4;
	ListView l;
	TextView upvotes;
	Button delete,edit,btnanswer;
	ArrayList<Elements> ar;
	String questionpulldata;
	HttpClient hc;
	HttpGet hg;
	HttpResponse hr;
	BufferedReader br;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_questions_activity);
		TextView title = (TextView) findViewById(R.id.txtMyHometitle);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		title.setText("MY QUESTIONS");
		l = (ListView) findViewById(R.id.myQuesList);
		questions = new ArrayList<QuestionItem>();
		ar = new ArrayList<Elements>();
		
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
				hg = new HttpGet("http://nammaapp.in/scripts/userpullquestion.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet") + "&kannada=" + kannada);
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
					q.setXp(e1.get(9).text());
					q.setQupvote(e1.get(10).text());
					q.setAupvote(e1.get(11).text());
					q.setRegion(e1.get(12).text());
					questions.add(q);
				}
				QuestionAdapter q = new QuestionAdapter(getBaseContext(), R.layout.myquestionrow, questions);
				l.setAdapter(q);
			}
		};
		questionpull.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_questions, menu);
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
	            v = vi.inflate(R.layout.myquestionrow, null);
	        }
	        item = items.get(position);
	        if(item != null) {
                TextView question = (TextView) v.findViewById(R.id.txtMyQuestion);
                TextView engword = (TextView) v.findViewById(R.id.txtMyQEngWord);
                TextView kanword = (TextView) v.findViewById(R.id.txtMyQKanWord);
                upvotes = (TextView) v.findViewById(R.id.txtMyQUpVotes);
                TextView answercount = (TextView) v.findViewById(R.id.txtMyQAnswerCount);
                TextView regionID = (TextView) v.findViewById(R.id.txtMyQRegion);
                btnanswer = (Button) v.findViewById(R.id.btnQAnswer);
                delete = (Button) v.findViewById(R.id.btnMyQDelete);
                edit = (Button) v.findViewById(R.id.btnMyQEdit);
                question.setText(item.getQuestion());
                engword.setText("ENG Word:" + item.getEngword());
                kanword.setText("KAN Word: " + item.getKanword());
                upvotes.setText("Upvotes: " + item.getUpvotes());
                answercount.setText("Answers: " + item.getAnscount());
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
						Intent i = new Intent(MyQuestionsActivity.this, MyQuestionDisplayActivity.class);
						i.putExtras(b);
						startActivity(i);
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
						Intent i = new Intent(MyQuestionsActivity.this, MyQuestionDisplayActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});
	
	}
			return v;
		}
	}


}
