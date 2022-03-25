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
import java.util.zip.*;
import org.json.*;

public class systemdownloader extends AsyncTask
{
	private Context context;
	String urlStr;
	String destinationFilePath;
	String username;
	String password;
	public systemdownloader(Context context,String username,String password,String url,String destpath){
		this.context = context; this.urlStr =url;  this.destinationFilePath = destpath; this.username = username; this.password = password;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		MainActivity.filedownloaddialog.setCancelable(false);
		MainActivity.filedownloaddialog.show();
	}

	@Override
	protected Object doInBackground(Object[] p1)
	{
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Log.d("downloadZipFile", "Server ResponseCode=" + connection.getResponseCode() + " ResponseMessage=" + connection.getResponseMessage());
			}
			input = connection.getInputStream();
			Log.d("downloadZipFile", "destinationFilePath=" + destinationFilePath);
			new File(destinationFilePath).createNewFile();
			output = new FileOutputStream(destinationFilePath);
			byte data[] = new byte[4096];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {
			try {
				if (output != null) output.close();
				if (input != null) input.close();
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
			if (connection != null) connection.disconnect();
		}
		Log.i("Zip Download" ,"DONE");
		try
		{
			unzip(Environment.getExternalStorageDirectory()+"/.ROS/files/system.zip", Environment.getExternalStorageDirectory()+"/.ROS/files/"+username+"/");
		}
		catch (IOException e)
		{
			System.out.println(e);
		}finally{
			File deletezip = new File(Environment.getExternalStorageDirectory()+ "/.ROS/files/system.zip");
			deletezip.delete();
		}
		return null;
	}
	public void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				extractFile(zipIn, filePath);
			} else {
				File dir = new File(filePath);
				dir.mkdirs();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
//		MainActivity.spe.putString("name",username);
//		MainActivity.spe.putString("password",password);
//		MainActivity.spe.putString("cmdir",Environment.getExternalStorageDirectory()+"/.ROS/files/"+username+"/system/ROS/Commands/");
//		MainActivity.spe.commit();
		
	}
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	@Override
	protected void onProgressUpdate(Object[] values)
	{

		super.onProgressUpdate(values);
		MainActivity.filedownloaddialog.setIndeterminate(false);
		MainActivity.filedownloaddialog.setMax(100);
		MainActivity.filedownloaddialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Object result)
	{

		super.onPostExecute(result);
		try {
			if ((MainActivity.filedownloaddialog != null) && MainActivity.filedownloaddialog.isShowing()) {
				MainActivity.filedownloaddialog.dismiss();
			}
		} catch (final IllegalArgumentException e) {
			// Handle or log or ignore
		} catch (final Exception e) {
			// Handle or log or ignore
		} finally {
			MainActivity.filedownloaddialog = null;
		}
		if (result != null){
			Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
		}else{
			try {
				JSONObject jsonwrite = new JSONObject();
				jsonwrite.put("name",username);
				jsonwrite.put("password",password);
				jsonwrite.put("cmdir",Environment.getExternalStorageDirectory()+"/.ROS/files/"+username+"/system/ROS/Commands/");
				FileWriter file = new FileWriter(Environment.getExternalStorageDirectory()+ "/.ROS/files/"+username+"/system/user/.u");
				file.write(jsonwrite.toString());
				file.flush();
				file.close();
				Toast.makeText(context,"System Has Been Downloaded\n\rPlease Restart This App", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(context,"Error While Setting Up System", Toast.LENGTH_LONG).show();
			}catch(JSONException je){
				Toast.makeText(context,"Error While Setting Up System", Toast.LENGTH_LONG).show();
				System.out.println(je);
			}
						
		}
	}				
	
	
}
