package com.raj.ROS;
import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;
import com.raj.ROS.R;
import android.net.*;
import java.nio.file.*;
import java.io.*;

public class appsdrawerlists extends ArrayAdapter {
    private String[] appname;
    private String[] appicon;
    private Activity context;
	private File[] appsicons;

    public appsdrawerlists(Activity context, String[] appname, File[] appicons) {
        super(context, R.layout.appsdrawerlist, appname);
        this.context = context;
        this.appname= appname;
        this.appsicons= appicons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.appsdrawerlist, null, true);
        TextView textViewappname= (TextView) row.findViewById(R.id.appsdrawerlistappnameTextView);
         ImageView imageFlag = (ImageView) row.findViewById(R.id.appsdrawerlistappicon);
        textViewappname.setText(appname[position]);
		imageFlag.setImageURI(Uri.fromFile(appsicons[position]));
  //     imageFlag.setImageResource(appicon[position]);
        return  row;
    }
}
