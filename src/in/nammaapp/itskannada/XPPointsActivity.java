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
import android.view.Menu;
import android.widget.ListView;

public class XPPointsActivity extends Activity {
	HttpClient hc;
	HttpGet hg;
	HttpResponse hr;
	BufferedReader br;
	PointsItem item;
	ArrayList<PointsItem> items;
	SharedPreferences pref;
	String pointspulldata;
	ArrayList<Elements> ar;
	ListView l;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xppoints_activity);
		ar = new ArrayList<Elements>();
		items = new ArrayList<PointsItem>();
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		AsyncTask<String,String,String> xppointspull = new AsyncTask<String,String,String>() {
			@Override
			protected String doInBackground(String... params) {
				pointspulldata = "";
				hc = new DefaultHttpClient();
				String kannada = "";
				if(pref.getString("kannada", "notSet") == "true")
					kannada = "1";
				else if(pref.getString("kannada", "notSet") == "false")
					kannada = "0";
				hg = new HttpGet("http://nammaapp.in/scripts/pullxppoints.php?token=" + pref.getString("token", "notSet") + "&U_ID=" + pref.getString("userID", "notSet") + "&kannada=" +  kannada);
				try {
					hr = hc.execute(hg);
					br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent(), "UTF-8"));
					String html;
					while((html = br.readLine()) != null)
						pointspulldata += html;
				} catch(Exception e) {
					Log.e("message on points pull", "points pull failed", e);
				}
				return pointspulldata;
			}
			
			@Override
			protected void onPostExecute(String result) {
				Document d = Jsoup.parse(result, "", Parser.xmlParser());
				for(Element e : d.select("xppointitem")) {
					ar.add(e.getAllElements());
				}
				Iterator<Elements> it = ar.iterator();
				while(it.hasNext()) {
					Elements e1 = it.next();
					PointsItem p = new PointsItem();
					p.setRank(e1.get(1).text());
					p.setName(e1.get(2).text());
					p.setPoints(e1.get(3).text());
					items.add(p);
				}
				//TODO add adapter
					
			}
		};
		xppointspull.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xppoints, menu);
		return true;
	}

}
