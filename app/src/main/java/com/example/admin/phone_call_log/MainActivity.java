package com.example.admin.phone_call_log;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{
	
	DBHelper db;
	TextView tv;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},0);
        tv=(TextView)findViewById(R.id.text);
        tv.setText("");
        
        db=new DBHelper(this, "ZnSoftech.db", null, 2);
        
        Cursor c=db.getData();
        
        if(c.getCount()>0)
        {
        	c.moveToFirst();
        	 do
        	   {
        	    String number=c.getString(0);
        	    String date=c.getString(1);
        	    String time=c.getString(2);
        	    String duration=c.getString(3);
        	    String type=c.getString(4);
                   String name=c.getString(5);
        	    
        	    tv.append("NAME:"+name+"\nNumber:"+number+"\nDate:"+date+"\nTime:"+time+"\nDuration:"+duration+"\nCall Type:"+type+"\n\n");
        	   }while(c.moveToNext());
        }
        else
        {
        	tv.setText("No Incoming and Outgoing call history exists!!!");
        }
       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	db.deleteTable();
        	tv.setText("No Incoming and Outgoing call history exists!!!");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
