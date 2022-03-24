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
import com.raj.ROS.*;

public class terminalfiledownloader extends AsyncTask
{
	private Context context;
	String urlStr;
	String getPackageName;
	String destinationFilePath;
	String username;
	String packagename;
	public terminalfiledownloader(Context context,String username,String packagename,String apppackagename){
		this.context = context; this.getPackageName= apppackagename; this.username = username; this.packagename = packagename;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		urlStr = "";
		destinationFilePath = null;//MainActivity.sp.getString("cmdir","");
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
			e.printStackTrace();

		} finally {
			try {
				if (output != null) output.close();
				if (input != null) input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (connection != null) connection.disconnect();
		}
		File f = new File(destinationFilePath);
		//Log.d("downloadZipFile", "f.getName()=" + f.getName().replace(".zip", ""));
		Log.i("Zip Download" ,"DONE");
		try
		{
			unzip(Environment.getExternalStorageDirectory()+"/Android/data/" + getPackageName+"/files/system.zip", "/data/data/" + getPackageName + "/files/"+username+"/");
		}
		catch (IOException e)
		{}finally{
			File deletezip = new File(Environment.getExternalStoragePublicDirectory("")+"/Android/data/" +getPackageName + "/files/system.zip");
			deletezip.delete();
			/*
			 //extractSystem.run = false;*/
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
		/*	filedownloaddialog.setIndeterminate(false);
		 filedownloaddialog.setMax(100);
		 filedownloaddialog.setProgress(values[0]);*/
	}

	@Override
	protected void onPostExecute(Object result)
	{

		super.onPostExecute(result);
		try {
			/*	if ((filedownloaddialog != null) && filedownloaddialog.isShowing()) {
			 filedownloaddialog.dismiss();
			 }*/
		} catch (final IllegalArgumentException e) {
			// Handle or log or ignore
		} catch (final Exception e) {
			// Handle or log or ignore
		} finally {
			//	filedownloaddialog = null;
		}
		/*	if (result != null){
		 //Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
		 }else{
		 /*MainActivity.spe.putString("firsttime","false");
		 MainActivity.spe.putString("name",username);
		 MainActivity.spe.putString("password",password);
		 MainActivity.spe.putString("cmdir","/data/data"+getPackageName+"/files/"+username+"/system/ROS/Commands/");
		 MainActivity.spe.commit();
		 Toast.makeText(context,"System Has Been Downloaded", Toast.LENGTH_LONG).show();
	 */	}


}
