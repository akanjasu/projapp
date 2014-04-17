package in.nammaapp.itskannada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SyncActivity extends Activity {
Button btnsync;
TextView tv;
private SQLiteDatabase db;
private DBahelper myDbHelper ;
private ArrayList<String> array_list; 
private static ArrayList<Elements> ar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		btnsync=(Button) findViewById(R.id.sync);
		tv=(TextView) findViewById(R.id.synctxt);
		myDbHelper = new DBahelper(this);
		array_list = new ArrayList<String>();
		ar = new ArrayList<Elements>();
		
		try {

	     	myDbHelper.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			myDbHelper.openDataBase();
			
			}catch(SQLException sqle){

			throw sqle;

			}
		
		
		btnsync.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ar.clear();
				// TODO Auto-generated method stub
				new AsyncTask<String,Void,String>(){

					@Override
					protected void onPreExecute()
					{
						tv.setText("checking for synchronization....");
					}
					@Override
					protected String doInBackground(String... arg0) {
						// TODO Auto-generated method stub
						db = myDbHelper.getWritableDatabase();
						String xml = httpcall();
						String resp = "";
						Document doc = Jsoup.parse(xml,"",Parser.xmlParser());
						
						for(Element e : doc.select("msg")) {
							ar.add(e.getAllElements());
							}
						
							Iterator<Elements> ite = ar.iterator();
						
						while(ite.hasNext()) {
							Elements e1 = ite.next();
							resp = e1.get(1).text().toString();
							}
						if(resp.length()>0)
							return "already";
						else
						{
							ar.clear();
							Document d = Jsoup.parse(xml,"",Parser.xmlParser());
							
							for(Element e : d.select("rowitem")) {
								ar.add(e.getAllElements());
								}
							
								Iterator<Elements> it = ar.iterator();
							
							while(it.hasNext()) {
								Elements e1 = it.next();
								String query = e1.get(1).text().toString();
								db.execSQL(query);
								
							}
							return "now";
						}
					}
					
					@Override
					protected void onPostExecute(String content)
					{
						if(content.contentEquals("already"))
							tv.setText("Already in Sync");
						else
							tv.setText("Now in Sync");
						
					}
					
				}.execute();
			}
		});
	}

	public String httpcall()
	{
		HttpClient httpc = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://nammaapp.in/scripts/SyncCheck.php");
		HttpResponse resp = null;
		BufferedReader br=null;
		
		try
		{

		httppost.setHeader("Connection", "keep-alive");
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		//query = String.format("key=%s", URLEncoder.encode(str1, "UTF-8"));
		Cursor c = db.rawQuery("SELECT  max(`C_ID`) as maxm FROM  `ClientContextTable`", null);
		String str1=null;
		if(c.moveToFirst())
		{
			str1=c.getString(c.getColumnIndex("maxm")).toString();
		}

		String query1 = String.format("key=%s", URLEncoder.encode(str1.toString(), "UTF-8"));
		httppost.setEntity(new StringEntity(query1.toString()));
		resp = httpc.execute(httppost);
		br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"UTF-8"));
		String htmltext = "";
		String str = "";
			while ((str = br.readLine()) != null) { 
							htmltext +=str;
			}	
			
			return htmltext;
			//final ArrayList<String> array_list = new ArrayList<String>();
		
		         
				
		}
		catch(Exception e)
		{
			
			return  e.toString();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sync, menu);
		return true;
	}

}
