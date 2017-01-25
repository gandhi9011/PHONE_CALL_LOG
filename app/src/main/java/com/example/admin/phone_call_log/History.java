package com.example.admin.phone_call_log;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;

@SuppressLint("SimpleDateFormat")
public class History extends ContentObserver {

	Context c;

	public History(Handler handler, Context cc) {
		// TODO Auto-generated constructor stub
		super(handler);
		c=cc;
	}

	@Override
    public boolean deliverSelfNotifications() {
        return true;
    }

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		SharedPreferences sp=c.getSharedPreferences("ZnSoftech", Activity.MODE_PRIVATE);
		String number=sp.getString("number", null);

		if(number!=null)
		{
			getCalldetailsNow();
			sp.edit().putString("number", null).commit();


		}
	}

	private void getCalldetailsNow() {
		// TODO Auto-generated method stub

		 Cursor managedCursor=c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
		
		int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int duration1 = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        int type1=managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date1=managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int name=managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

        
        if( managedCursor.moveToFirst() == true ) {
            String phNumber = managedCursor.getString(number);
            String callDuration = managedCursor.getString(duration1);
            
            String type=managedCursor.getString(type1);
            String date=managedCursor.getString(date1);
            String prname=managedCursor.getString(name);
			if(prname.equals(null))
			{
				prname=getContactName(c,phNumber);
			}
            String dir = null;
		    int dircode = Integer.parseInt(type);
		    switch (dircode)
		    { 
		    case CallLog.Calls.OUTGOING_TYPE:
		    	dir = "OUTGOING";
		    	break;
		    case CallLog.Calls.INCOMING_TYPE:
		    	dir = "INCOMING";
		    	break;
		    case CallLog.Calls.MISSED_TYPE:
		    	dir = "MISSED";
		    	break;
		    default: 
		    	dir = "MISSED";
		    	break;
		    }
		    
		    SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
		    SimpleDateFormat sdf_time = new SimpleDateFormat("h:mm a");
		   // SimpleDateFormat sdf_dur = new SimpleDateFormat("KK:mm:ss");
		    
		    String dateString = sdf_date.format(new Date(Long.parseLong(date)));
		    String timeString = sdf_time.format(new Date(Long.parseLong(date)));
		  //  String duration_new=sdf_dur.format(new Date(Long.parseLong(callDuration)));

           DBHelper db=new DBHelper(c, "ZnSoftech.db", null, 2);
           db.insertdata(phNumber, dateString, timeString, callDuration, dir,prname);
           
        }
        
        managedCursor.close();
	}



	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if(cursor.moveToFirst()) {
			contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}

		if(cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;
	}

}
