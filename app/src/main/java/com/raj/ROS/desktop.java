package com.raj.ROS;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import org.json.*;
public class desktop extends Activity
{
	LinearLayout appsdrawer;
	LinearLayout wallpaper;
	String[] listapps;
	File app;
	TextView gravity;
	JSONObject sp;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		setTitle("Desktop");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		setContentView(R.layout.desktop);
		findlayouts();
		makeappdrawer();
		wallpaper.setBackground(getWallpaper());
	}
	private void findlayouts(){
		gravity = findViewById(R.id.desktopgravitybottom);
		appsdrawer = findViewById(R.id.desktopLinearLayoutappsdrawer);
		wallpaper = findViewById(R.id.desktopLinearLayoutwallpapper);
		sp = getJSOnUser.jo(getJSOnUser.getusername());
	}
	private void makeappdrawer(){
		try{
			app =new File(Environment.getExternalStorageDirectory()+"/.ROS/files/"+sp.getString("name")+"/system/ROS/Application/");
			listapps =  app.list();
			long appslist =  app.length();
			TextView[] tv= new TextView[Integer.parseInt(String.valueOf(app.length()))];
			for(int i = 0; i<appslist; i++){
				TextView appname = new TextView(this);
				appname.setText(listapps[i]);
				appname.setGravity(gravity.getGravity());
				File icon = new File(app.getAbsolutePath()+"/"+appname.getText()+"/"+appname.getText());
				appname.setBackgroundDrawable(Drawable.createFromPath(icon.getAbsolutePath()));
				File apppackage = new File(app.getAbsolutePath()+"/"+appname.getText()+"/Package/");
				File[] apppackagename =apppackage.listFiles();
				appname.setHeight(40);
				appname.setWidth(40);
				appname.setTextSize(10);
				appname.setTextColor(Color.BLACK);
				appname.setId(listapps[i].hashCode()+i);
				tv[i] = appname;
				System.out.println(sp.getString("cmdir"));
				appclicks(appname , listapps[i],apppackagename[0].getAbsoluteFile().toString().replace(Environment.getExternalStorageDirectory()+"/.ROS/files/raj/system/ROS/Application/"+listapps[i]+"/Package/","").replaceAll("/",""));
				appsdrawer.addView(appname);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	private void appclicks(TextView setclickon , final String appname , final String apppackagename){
		setclickon.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					try{
					Intent startapp = new Intent(Intent.ACTION_MAIN);
					if(startapp != null){
					startapp.setComponent(new ComponentName(apppackagename,apppackagename+".MainActivity"+appname));
					startapp.putExtra("name",sp.getString("name"));
					startapp.putExtra("password",sp.getString("password"));
					startapp.putExtra("system_home",Environment.getExternalStorageDirectory()+"/.ROS/files/"+sp.getString("name")+"/");
					startapp.putExtra("cmdir",sp.getString("cmdir"));
					startActivity(startapp);
					}else{
						Toast.makeText(getApplicationContext(),"App Not Found\n\r AppName -"+appname+"\n\rMake Sure You installed It Correctly or Contact Developer",Toast.LENGTH_LONG).show();
					}
					}catch(Exception e){
						Toast.makeText(getApplicationContext(),"App Not Found\n\r AppName -"+appname+"\n\rMake Sure You installed It Correctly or Contact Developer",Toast.LENGTH_LONG).show();
						System.out.println(e);
					}
				}
		});
	}
}
