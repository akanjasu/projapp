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

public class ResolveSystemContextRequestsActivity extends Activity {
	ListView l;
	ArrayList<ContextItem> items;
	ArrayList<Elements> ar;
	HttpClient hc;
	HttpGet hg;
	HttpResponse hr;
	BufferedReader br;
	String contextpulldata;
	ContextItem i;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resolve_system_context_requests_activity);
		items = new ArrayList<ContextItem>();
		ar = new ArrayList<Elements>();
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		AsyncTask<String,String,String> pullcontexts = new AsyncTask<String,String,String>(){
			@Override
			protected String doInBackground(String... params) {
				contextpulldata = "";
				hc = new DefaultHttpClient();
				
				hg = new HttpGet("http://nammaapp.in/scripts/pullsyscontextfortranslation.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet"));
				try {
					hr = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(),"UTF-8"));
					String html;
					while((html = br.readLine())!=null)
						contextpulldata += html;
				} catch(Exception e) {
					Log.e("message on context pull", "context pull failed", e);
				}
				return contextpulldata;	
			}
			
			protected void onPostExecute(String result) {
				Document d = Jsoup.parse(result, "", Parser.xmlParser());
				for(Element e : d.select("contextitem"))
					ar.add(e.getAllElements());
				Iterator<Elements> it = ar.iterator();
				while(it.hasNext()) {
					Elements e1 = it.next();
					ContextItem c = new ContextItem();
					c.setID(e1.get(1).text());
					c.setEngword(e1.get(2).text());
					c.setKanword(e1.get(3).text());
					items.add(c);
				}
				l = (ListView) findViewById(R.id.kansysCRList);
				if(items.size() > 0) {
					ContextAdapter adap = new ContextAdapter(getBaseContext(), R.layout.kansystemcontextrow, items);
					l.setAdapter(adap);
				} else {
					l.setAdapter(null);
					TextView title = (TextView) findViewById(R.id.kansyscrtitle);
					title.setText("");
					Toast.makeText(getBaseContext(), "No Requests Posted...", Toast.LENGTH_LONG).show();
				}
			}
		};
		pullcontexts.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resolve_system_context_requests, menu);
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
	            v = vi.inflate(R.layout.kansystemcontextrow, null);
	        }
	        item = items.get(position);
	        if(item != null) {
	        	TextView engword = (TextView) v.findViewById(R.id.txtKUSysContEng);
	        	TextView kanword = (TextView) v.findViewById(R.id.txtKUSysContKan);
	        	engword.setText("ENG WORD: " + item.getEngword());
	        	kanword.setText("KAN WORD: " + item.getKanword());
	        	Button resolve = (Button) v.findViewById(R.id.btnKUSysContResolve);
	        	
	        	resolve.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position1 = l.getPositionForView((View) v.getParent());
						ContextItem current = items.get(position1);
						Bundle b = new Bundle();
						b.putString("option", "systemresolve");
						b.putString("ID", current.getID());
						b.putString("engword", current.getEngword());
						b.putString("kanword", current.getKanword());
						Intent i = new Intent(ResolveSystemContextRequestsActivity.this, ResolveContextFormActivity.class);
						i.putExtras(b);
						startActivity(i);	
					}
				});
	        }
	        return v;
		}
	}

}
