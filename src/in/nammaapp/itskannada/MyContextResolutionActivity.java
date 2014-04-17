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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyContextResolutionActivity extends Activity {
	ListView l;
	ArrayList<ContextItem> items;
	ArrayList<Elements> ar;
	HttpClient hc;
	HttpGet hg;
	HttpResponse hr;
	BufferedReader br;
	String pullmycontextdata;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_context_resolution_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		items = new ArrayList<ContextItem>();
		ar = new ArrayList<Elements>();
		AsyncTask<String,String,String> pullmycontext = new AsyncTask<String,String,String>(){
			@Override
			protected String doInBackground(String... params) {
				pullmycontextdata = "";
				hc = new DefaultHttpClient();
				hg = new HttpGet("http://nammaapp.in/scripts/pullmycontext.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet"));
				try {
					hr = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(),"UTF-8"));
					String html;
					while((html = br.readLine())!=null)
						pullmycontextdata += html;
				} catch(Exception e) {
					Log.e("message on phrase pull", "phrase pull failed", e);
				}
				return pullmycontextdata;	
			}
			
			protected void onPostExecute(String result) {
				Document d = Jsoup.parse(result, "", Parser.xmlParser());
				for(Element e : d.select("contextitem"))
					ar.add(e.getAllElements());
				Iterator<Elements> it = ar.iterator();
				while(it.hasNext()) {
					Elements e1 = it.next();
					ContextItem c = new ContextItem();
					c.setEngword(e1.get(1).text());
					c.setContext(e1.get(2).text());
					c.setRegionID(e1.get(3).text());
					c.setKanword(e1.get(4).text());
					c.setUsername(e1.get(5).text());
					c.setUserxp(e1.get(6).text());
					c.setUserqup(e1.get(7).text());
					c.setUseraup(e1.get(8).text());
					items.add(c);
				}
				l = (ListView) findViewById(R.id.MyCRList);
				if(items.size() > 0) {
					ContextAdapter adap = new ContextAdapter(getBaseContext(), R.layout.engcontextrow, items);
					l.setAdapter(adap);
				} else {
					l.setAdapter(null);
					TextView title = (TextView) findViewById(R.id.mycrtitle);
					title.setText("");
					Toast.makeText(getBaseContext(), "No Requests Posted...", Toast.LENGTH_LONG).show();
				}
			}
			
		};
		pullmycontext.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_context_resolution, menu);
		return true;
	}
	private class ContextAdapter extends ArrayAdapter<ContextItem> {
		private ArrayList<ContextItem> items;
		ContextItem item;
		Context context;
		
		public ContextAdapter(Context context,	int textViewResourceId, ArrayList<ContextItem> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v==null) {
	            LayoutInflater vi = LayoutInflater.from(context);
	            v = vi.inflate(R.layout.engcontextrow, null);
	        }
	        item = items.get(position);
	        if(item != null) {
	        	TextView engword = (TextView) v.findViewById(R.id.txtEUContEng);
	        	TextView kanword = (TextView) v.findViewById(R.id.txtEUContKan);
	        	TextView region = (TextView) v.findViewById(R.id.txtEUContRegion);
	        	TextView userxp = (TextView) v.findViewById(R.id.txtEUCXP);
	        	TextView userqup = (TextView) v.findViewById(R.id.txtEUCQUp);
	        	TextView useraup = (TextView) v.findViewById(R.id.txtEUCAUp);
	        	TextView name = (TextView) v.findViewById(R.id.txtEUCUsername);
	        	TextView context = (TextView) v.findViewById(R.id.txtEUContCont);
	        	name.setText(item.getUsername());
	        	useraup.setText("A Up: " + item.getUseraup());
	        	userqup.setText("Q Up: " + item.getUserqup());
	        	userxp.setText("XP: " + item.getUserxp());
	        	String [] s = getResources().getStringArray(R.array.region_arrays);
	        	region.setText("REGION: " + s[Integer.parseInt(item.getRegionID())]);
	        	engword.setText("ENG WORD: " + item.getEngword());
	        	kanword.setText("KAN WORD: " + item.getKanword());
	        	context.setText("CONTEXT: " + item.getContext());
	        }
	        return v;
		}
	}
}
