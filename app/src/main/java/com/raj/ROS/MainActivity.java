package com.raj.ROS;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class MainActivity extends Activity
{
	public static boolean internet;
	public static ProgressDialog filedownloaddialog;
	EditText name;
	EditText password;
	Button login;
	File f;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
       setTitle("ROS Login");
		setContentView(R.layout.main);
		findlayout();
		System.out.println(Environment.getExternalStorageDirectory());
		 	checkinternetconnection ic = new checkinternetconnection();
		ic.execute();
		login.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if(!(name.getText().length()==0)){
						if(!(password.getText().length()==0)){
					String n = name.getText().toString();
					String p = password.getText().toString();
						f = new File(Environment.getExternalStorageDirectory() + "/.ROS/files/"+n+"/system/user/.u");
					if(isuempty()){
						if(internet){
							File FI = new File(Environment.getExternalStorageDirectory()+"/.ROS/files/");
							FI.mkdirs();
							filedownloaddialog = new ProgressDialog(MainActivity.this);
							filedownloaddialog.setTitle("Downloading System");
							filedownloaddialog.setMessage("Please Wait\n\r Downloading System");
							systemdownloader fdown = new systemdownloader(MainActivity.this, n, p, "https://github.com/ROSbyRaj/ROSSystem/releases/download/ROS/system.zip",Environment.getExternalStorageDirectory()+"/.ROS/files/system.zip");
							fdown.execute();
						}else{
							Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
						}
						}else{
							try{
							final JSONObject jo = getJSOnUser.jo(n);
							if(jo.getString("name").equals(n)){
								if(jo.getString("password").equals(p)){
							getJSOnUser.setUsername(n);
									Intent i = new Intent(MainActivity.this, desktop.class);
									startActivity(i);
						finish();
						}else{makeText("Worng Password",Toast.LENGTH_SHORT);}
					}else{makeText("Worng Username",Toast.LENGTH_SHORT);}
				}catch(JSONException e){ System.out.println(e);}
							
				}
				}else{
					Toast.makeText(MainActivity.this,"PLEASE FILL PASSWORD",Toast.LENGTH_SHORT).show();
				}
				}else{
					Toast.makeText(MainActivity.this,"PLEASE FILL USERNAME",Toast.LENGTH_SHORT).show();
				}
				}
		});
		}
		private Boolean isuempty(){
			Boolean isempty = true;
				File f = new File(Environment.getExternalStorageDirectory()+"/.ROS/files/"+name.getText()+"/system/user/.u");
				isempty = !f.exists();
			return isempty;
		}
		private void findlayout(){
			name = findViewById(R.id.mainEditTextName);
			password = findViewById(R.id.mainEditTextpassword);
			login = findViewById(R.id.mainButtonlogin);
		}
		public  void makeText(String message,int ToastLength){
			Toast.makeText(this,message,ToastLength).show();
		}
	public class checkinternetconnection extends AsyncTask
	{
		@Override
		protected Object doInBackground(Object[] p1)
		{
			HttpURLConnection testurlconntion = null;
			try		{
				try
				{
					URL url = new URL("https://google.com");
					testurlconntion = (HttpURLConnection) url.openConnection();
					testurlconntion.connect();
					MainActivity.internet = true;
					testurlconntion.disconnect();
				}
				catch (MalformedURLException e)
				{
					System.out.println(e);
					MainActivity.internet = false;
				}
				}catch (IOException e){
				System.out.println(e);
				MainActivity.internet = false;
		}
			return null;
		}}}
		
	
