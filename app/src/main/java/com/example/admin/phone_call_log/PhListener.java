package com.example.admin.phone_call_log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat") public class PhListener extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context c, Intent i) {
		Log.e("inside on rec",""+i);
		// TODO Auto-generated method stub
		Bundle bundle=i.getExtras();
		int x=1;
		
		if(bundle==null)
			return;
		
		SharedPreferences sp=c.getSharedPreferences("ZnSoftech", Activity.MODE_PRIVATE);
		
		String s=bundle.getString(TelephonyManager.EXTRA_STATE);
		Log.e("value_of_Extrastate",s+"..."+i.getStringExtra(Intent.ACTION_NEW_OUTGOING_CALL));
		if(i.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			Log.e("in if",Intent.ACTION_NEW_OUTGOING_CALL+"....."+i.getAction());
			String number=i.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			sp.edit().putString("number", number).commit();
			sp.edit().putString("state", s).commit();
			Log.e("nymberrrrrrrrrrr",number);

		}

		else if(s.equals(TelephonyManager.EXTRA_STATE_RINGING))
		{
			Log.e("in first else if",s+"....."+TelephonyManager.EXTRA_STATE_RINGING);
			String number=bundle.getString("incoming_number");
			Log.e("nymberrrrrrrrrrr11",number);
			sp.edit().putString("number", number).commit();
			sp.edit().putString("state", s).commit();


		}
		
		else if(s.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
		{
			Log.e("call  ",s+"....."+TelephonyManager.EXTRA_STATE_OFFHOOK);
			x=0;
			sp.edit().putString("state", s).commit();
			//SmsManager smsManager = SmsManager.getDefault();
			//smsManager.sendTextMessage(i.getStringExtra(Intent.EXTRA_PHONE_NUMBER), null, "you called Mr.Yogesh", null, null);
		}
		
		else if(s.equals(TelephonyManager.EXTRA_STATE_IDLE))
		{

			String state=sp.getString("state", null);
			Log.e("call or cut",s+".."+state+"...");
			if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
			{
				Log.e("callllllllllll",s+".."+state+"...");

				sp.edit().putString("state", null).commit();
				History h=new History(new Handler(),c);
				c.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, h);
				/*try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(bundle.getString("incoming_number"), null, "It was Nice Talking With you Thank you for calling Me Have a nice Day Yogesh ", null, null);
					Toast.makeText(c, "SMS sent.", Toast.LENGTH_LONG).show();
				} catch (Exception e)
				{
					Toast.makeText(c, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}*/

			}
			if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
			{
				Log.e("cut---",s+".."+state+"...");
			/*try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(bundle.getString("incoming_number"), null, "Sorry I'm unable to take your call at this moment so i will call you soon Have a nice day Yogesh", null, null);
					Toast.makeText(c, "SMS sent.", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(c, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}*/
				}

			//sp.edit().putString("state", s).commit();




		}
	
	}

}
