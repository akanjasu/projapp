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
import android.widget.Toast;

public class RequestListActivity extends Activity {
	ListView l;
	ArrayList<PhraseItem> items;
	ArrayList<Elements> ar;
	HttpClient hc;
	HttpGet hg;
	HttpResponse hr;
	BufferedReader br;
	String phrasepulldata;
	PhraseItem i;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_list_activity);
		items = new ArrayList<PhraseItem>();
		ar = new ArrayList<Elements>();
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		AsyncTask<String,String,String> pullphrases = new AsyncTask<String,String,String>(){
			@Override
			protected String doInBackground(String... params) {
				phrasepulldata = "";
				hc = new DefaultHttpClient();
				hg = new HttpGet("http://nammaapp.in/scripts/pullengphrasesfortranslation.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet"));
				try {
					hr = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(),"UTF-8"));
					String html;
					while((html = br.readLine())!=null)
						phrasepulldata += html;
				} catch(Exception e) {
					Log.e("message on phrase pull", "phrase pull failed", e);
				}
				return phrasepulldata;	
			}
			
			protected void onPostExecute(String result) {
				Document d = Jsoup.parse(result, "", Parser.xmlParser());
				for(Element e : d.select("phraseitem"))
					ar.add(e.getAllElements());
				Iterator<Elements> it = ar.iterator();
				while(it.hasNext()) {
					Elements e1 = it.next();
					PhraseItem e = new PhraseItem();
					e.setEngphrase(e1.get(1).text());
					e.setKanphrase(e1.get(2).text());
					e.setRegion(e1.get(3).text());
					e.setUsername(e1.get(4).text());
					e.setXp(e1.get(5).text());
					e.setQup(e1.get(6).text());
					e.setAup(e1.get(7).text());
					e.setPhraseid(e1.get(8).text());
					items.add(e);
				}
				l = (ListView) findViewById(R.id.outstandingrequestsList);
				
				if(items.size() > 0) {
					PhraseAdapter adap = new PhraseAdapter(getBaseContext(), R.layout.rowphrasekanuser, items);
					l.setAdapter(adap);
				} else {
					l.setAdapter(null);
					TextView title = (TextView) findViewById(R.id.xbfadslkf);
					title.setText("");
					Toast.makeText(getBaseContext(), "No Requests Posted...", Toast.LENGTH_LONG).show();
				}	
			}
		};
		pullphrases.execute();

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request_phrase_translate, menu);
		return true;
	}
	
	private class PhraseAdapter extends ArrayAdapter<PhraseItem> {
		private ArrayList<PhraseItem> items;
		PhraseItem item;
		Context context;
		
		public PhraseAdapter(Context context,	int textViewResourceId, ArrayList<PhraseItem> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null) {
	            LayoutInflater vi = LayoutInflater.from(context);
	            v = vi.inflate(R.layout.rowphrasekanuser, null);
	        }
	        item = items.get(position);
	        if(item != null) {
	        	TextView engphrase = (TextView) v.findViewById(R.id.txtKUPhraseEng);
	        	TextView kanphrase = (TextView) v.findViewById(R.id.txtKUPhraseKan);
	        	TextView region = (TextView) v.findViewById(R.id.txtKUPhraseRegion);
	        	TextView userxp = (TextView) v.findViewById(R.id.txtKUPXP);
	        	TextView userqup = (TextView) v.findViewById(R.id.txtKUPQUp);
	        	TextView useraup = (TextView) v.findViewById(R.id.txtKUPAUp);
	        	TextView name = (TextView) v.findViewById(R.id.txtKUPUsername);
	        	Button answer = (Button) v.findViewById(R.id.btnKUAnswer);
	        	name.setText(item.getUsername());
	        	useraup.setText("A Up: " + item.getAup());
	        	userqup.setText("Q Up: " + item.getQup());
	        	userxp.setText("XP: " + item.getXp());
	        	String [] s = getResources().getStringArray(R.array.region_arrays);
	        	region.setText("REGION: " + s[Integer.parseInt(item.getRegion())]);
	        	engphrase.setText("ENG PHRASE: " + item.getEngphrase());
	        	kanphrase.setText("KAN PHRASE: " + item.getKanphrase());
	        	
	        	answer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position2 = l.getPositionForView((View) v.getParent());
						i = items.get(position2);
						Bundle b = new Bundle();
						b.putString("phraseID", i.getPhraseid());
						b.putString("engphrase", i.getEngphrase());
						b.putString("kanphrase", i.getKanphrase());
						b.putString("regionID", i.getRegion());
						b.putString("askedname", i.getUsername());
						b.putString("askedxp", i.getXp());
						b.putString("askedqup", i.getQup());
						b.putString("askedaup", i.getAup());
						Intent i = new Intent(RequestListActivity.this, ProvideTranslateActivity.class);
						i.putExtras(b);
						startActivity(i);
					}
				});
	        }
	        return v;
		}
	}
}

