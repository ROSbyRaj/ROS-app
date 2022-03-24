package com.raj.ROS;
import android.os.*;
import java.io.*;
import org.json.*;

public class getJSOnUser
{
	private static String username;
	public static JSONObject jo(String name){
		JSONObject js =null;
		try {
			File f = new File(Environment.getExternalStorageDirectory()+"/.ROS/files/"+name+"/system/user/.u");
			FileInputStream is = new FileInputStream(f);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String mResponse = new String(buffer);
			js = new JSONObject(mResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}catch(JSONException je){ System.out.println(je);
		}
		return js;
	}
	
	public static void setUsername(String name){
		username = name;
	}
	public static String getusername(){
		return username;
	}
}
