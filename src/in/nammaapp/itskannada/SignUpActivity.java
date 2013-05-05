package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignUpActivity extends Activity {
	Button btnReg,btnDict,btnPhrase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_activity);
		//TODO sync tables here
		btnReg = (Button) findViewById(R.id.btnSignUp);
		btnDict = (Button) findViewById(R.id.btnDict);
		btnPhrase = (Button) findViewById(R.id.btnPhrase);
		btnReg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, RegisterActivity.class);
				startActivity(i);
				i.
			}
		});
		btnDict.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, DictionarySearchActivity.class);
				startActivity(i);
			}
		});
		btnPhrase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, PhraseSearchActivity.class);
				startActivity(i);
			}
		});
//		EditText usernae
//		HttpClient hc = new DefaultHttpClient();
//		HttpPost hp = new HttpPost(getResources().getString(R.string.homeURL) + "/scripts/signup.php");
//		hp.setHeader("Connection", "keep-alive");
//		hp.setHeader("Content-Type", "application/x-www-form-urlencoded");
//		HttpResponse hr;
//		ArrayList<NameValuePair> postparameters = new ArrayList<NameValuePair>();
//		postparameters.add(new BasicNameValuePair("starttoken", "f45e8v23jk5x3p917q106npuwlh94v3zpige9d80"));
//		postparameters.add(new BasicNameValuePair("username", value))
//		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

}
