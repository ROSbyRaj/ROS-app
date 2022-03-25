package com.raj.ROS;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import java.io.*;
import java.net.*;
import com.raj.ROS.R;
//mport android.widget.Toolbar.*;

public class MainActivityTerminal extends Activity 
{
	public static Bundle user;
	private EditText command;
	static TextView commandoutput;
	public static String cmdout;
	public static boolean internet;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainterminal);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		findlayout(); setll();
		user =getIntent().getExtras();
		keydone();
		checkinternetconnection cic = new checkinternetconnection();
		cic.execute();
	}
	private void keydone(){
		command.setOnEditorActionListener(new TextView.OnEditorActionListener(){
				@Override
				public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
				{
					if(p2 == EditorInfo.IME_ACTION_DONE){
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(command.getWindowToken(),0);
						terminal t = new terminal(command.getText().toString(),user);
						t.execute();
						command.setText("");
						return true;
					}
					return false;
				}
		});
	}
	private void findlayout(){
		command = findViewById(R.id.mainEditTextcommand);
		commandoutput = findViewById(R.id.mainTextViewoutput);
	}
	private void setll(){
		Display d = getWindowManager().getDefaultDisplay(); Point size = new Point(); d.getSize(size);
		LinearLayout ll =findViewById(R.id.mainLinearLayouttermoutput);
		LayoutParams params = (LayoutParams) ll.getLayoutParams();
		params.height = size.x / 2 -10;
		ll.setLayoutParams(params);
	}
	private class terminal extends AsyncTask
	{
		private String command;
		private Bundle Extras;
		private String[] cmdlines;
		public terminal(String command,Bundle extras){
			this.command = command; this.Extras = extras;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			checkinternetconnection cic = new checkinternetconnection();
			cic.execute();
		}
		@Override
		protected Object doInBackground(Object[] p1)
		{
			cmdlines = new String[4095];
			String response = "";
			File commands = new File(Extras.getString("cmdir"));//Environment.getExternalStorageDirectory()+"/.ROS/files/"+Extras.getString("name")+"/system/ROS/Commands/");
			File[] cmdlists = commands.listFiles();
			if(command.toLowerCase().startsWith("install")){
					try{
						FileWriter file = new FileWriter(commands.getAbsolutePath()+"/install");
						file.write(command.replaceAll("install ",""));
						String a = command.replaceAll("install ","");
						file.flush();
						file.close();
						command = command.replaceAll(a,"").replaceAll(" ","");
					}catch(IOException e){
						response ="\n\r"+e.toString();
					}
			}
			if(cmdlists[0] != null){
			int i =0;
			try{
				while(true){
					if(command.equals(cmdlists[i].getName())){
						break;
					}else{i++;}
				}
					File f = new File(cmdlists[i].getAbsolutePath());
					FileReader fr = new FileReader(f);
					try(BufferedReader br = new BufferedReader(fr)){
						int a = 0;
						while((cmdlines[a]=br.readLine())!=null){
							a++;
						}
					}
				if(cmdlists[i].getName().toLowerCase().equals("install")){
					if(internet){
					if(findpackage(cmdlines[0])){
						
						response="\n\rInstalling";
					}else{
						response ="\n\rPackage not Found";
					}
					}else{response = "\n\rNo Internet Connection";
					}
				}
				if(cmdlists[i].getName().toLowerCase().equals("update")){
					if(internet){
					response ="\n\rUpdating";
					}else{response="\n\rNo Internet Connection";}
				}
				
				try{
					FileWriter file = new FileWriter(commands.getAbsolutePath()+"/install");
					file.write("");
					file.flush();
					file.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}catch(ArrayIndexOutOfBoundsException ae){
				response=("\n\rCommand Not Found");
			}catch(IOException e){
				Log.e("Terminal BackGround",e.toString());
				response=e.toString();
			}
		}
			return response;
		}
		private boolean findpackage(String packagename){
			boolean isthere = false;

			return isthere;
		}
		@Override
		protected void onPostExecute(Object result)
		{
			super.onPostExecute(result);
			MainActivityTerminal.commandoutput.append(result.toString());
			
		}
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
					MainActivityTerminal.internet = true;
					testurlconntion.disconnect();
				}
				catch (MalformedURLException e)
				{
					MainActivityTerminal.internet = false;
				}
			}catch (IOException e){
				MainActivityTerminal.internet = false;
			}
			return null;
		}
}}
