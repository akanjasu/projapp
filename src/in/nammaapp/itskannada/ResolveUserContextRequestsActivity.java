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

public class ResolveUserContextRequestsActivity extends Activity {
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
		setContentView(R.layout.resolve_user_context_requests_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		items = new ArrayList<ContextItem>();
		ar = new ArrayList<Elements>();
		
		AsyncTask<String,String,String> pullcontexts = new AsyncTask<String,String,String>(){
			@Override
			protected String doInBackground(String... params) {
				contextpulldata = "";
				hc = new DefaultHttpClient();
				hg = new HttpGet("http://nammaapp.in/scripts/pullkancontextfortranslation.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet"));
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
					c.setContext(e1.get(4).text());
					c.setRegionID(e1.get(5).text());
					c.setUsername(e1.get(6).text());
					c.setUserxp(e1.get(7).text());
					c.setUserqup(e1.get(8).text());
					c.setUseraup(e1.get(9).text());
					items.add(c);
				}
				l = (ListView) findViewById(R.id.kanuserCRList);
				if(items.size() > 0) {
					ContextAdapter adap = new ContextAdapter(getBaseContext(), R.layout.kancontextrow, items);
					l.setAdapter(adap);
				} else {
					l.setAdapter(null);
					TextView title = (TextView) findViewById(R.id.kanusercrtitle);
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
		getMenuInflater().inflate(R.menu.resolve_user_context_requests, menu);
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
	            v = vi.inflate(R.layout.kancontextrow, null);
	        }
	        item = items.get(position);
	        if(item != null) {
	        	TextView engword = (TextView) v.findViewById(R.id.txtKUContEng);
	        	TextView kanword = (TextView) v.findViewById(R.id.txtKUContKan);
	        	TextView region = (TextView) v.findViewById(R.id.txtKUContRegion);
	        	TextView userxp = (TextView) v.findViewById(R.id.txtKUCXP);
	        	TextView userqup = (TextView) v.findViewById(R.id.txtKUCQUp);
	        	TextView useraup = (TextView) v.findViewById(R.id.txtKUCAUp);
	        	TextView name = (TextView) v.findViewById(R.id.txtKUCUsername);
	        	TextView context = (TextView) v.findViewById(R.id.txtKUContCont);
	        	name.setText(item.getUsername());
	        	useraup.setText("A Up: " + item.getUseraup());
	        	userqup.setText("Q Up: " + item.getUserqup());
	        	userxp.setText("XP: " + item.getUserxp());
	        	String [] s = getResources().getStringArray(R.array.region_arrays);
	        	region.setText("REGION: " + s[Integer.parseInt(item.getRegionID())]);
	        	engword.setText("ENG WORD: " + item.getEngword());
	        	kanword.setText("KAN WORD: " + item.getKanword());
	        	context.setText("CONTEXT: " + item.getContext());
	        	Button resolve = (Button) v.findViewById(R.id.btnKUContResolve);
	        	
	        	resolve.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position1 = l.getPositionForView((View) v.getParent());
						ContextItem current = items.get(position1);
						Bundle b = new Bundle();
						b.putString("option", "userresolve");
						b.putString("ID", current.getID());
						b.putString("engword", current.getEngword());
						b.putString("kanword", current.getKanword());
						b.putString("context", current.getContext());
						b.putString("regionID", current.getRegionID());
						b.putString("username", current.getUsername());
						b.putString("userxp", current.getUserxp());
						b.putString("userqup", current.getUserqup());
						b.putString("useraup", current.getUseraup());
						Intent i = new Intent(ResolveUserContextRequestsActivity.this, ResolveContextFormActivity.class);
						i.putExtras(b);
						startActivity(i);	
					}
				});
	        }
	        return v;
		}
	}
}
