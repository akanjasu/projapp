package in.nammaapp.itskannada;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ContextSearchHome extends Activity {
	private static final int REQUEST_CODE = 1234;
	private ListView wordsList;
	private EditText etext;
	private Button searchbtn,context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.context_search_home);
		Button speakButton = (Button) findViewById(R.id.spkbtn);
		   searchbtn = (Button) findViewById(R.id.searchbtn);
	      wordsList = (ListView) findViewById(R.id.list);
	      etext=(EditText)findViewById(R.id.etext);
			context =(Button)findViewById(R.id.button1);
			
	       // Disable button if no recognition service is present
	       PackageManager pm = getPackageManager();
	       List<ResolveInfo> activities = pm.queryIntentActivities(
	               new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	       if (activities.size() == 0)
	       {
	           speakButton.setEnabled(false);
	           speakButton.setText("no");
	       }	
	       searchbtn.setOnClickListener(new OnClickListener() {
	   		public void onClick(View arg)
	   		{
	   			if(etext.getText().toString().length()!=0)
	   			{
	   		Intent i = new Intent(ContextSearchHome.this,ContextDisplayMeaningActivity.class);
	   		i.putExtra("searchword",etext.getText().toString());
	   		startActivity(i);
	   		}}
	   			
	   	});

	   }
	          



	       
	   public void speakButtonClicked(View v)
	      {
	          startVoiceRecognitionActivity();
	      }

	      /**
	       * Fire an intent to start the voice recognition activity.
	       */
	      private void startVoiceRecognitionActivity()
	      {
	          Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	          intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition");
	          startActivityForResult(intent, REQUEST_CODE);
	      }

	      /**
	       * Handle the results from the voice recognition activity.
	       */
	      @Override
	      protected void onActivityResult(int requestCode, int resultCode, Intent data)
	      {
	          if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
	          {
	              // Populate the wordsList with the String values the recognition engine thought it heard
	              ArrayList<String> matches = data.getStringArrayListExtra(
	                      RecognizerIntent.EXTRA_RESULTS);
	              ArrayAdapter<String> words = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
	                      matches);
	              wordsList.setVisibility(View.VISIBLE);
	              wordsList.setAdapter(words);
	              wordsList.setOnItemClickListener(new OnItemClickListener() {
	                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                      // Build the Intent used to open WordActivity with a specific word Uri
	                  	 final String item = (String) parent.getItemAtPosition(position);
	                       etext.setText(item);
	                       wordsList.setVisibility(View.INVISIBLE);
	                  }
	              });
	          }
	          super.onActivityResult(requestCode, resultCode, data);
	          
	      }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.context_search_home, menu);
		return true;
	}

}
