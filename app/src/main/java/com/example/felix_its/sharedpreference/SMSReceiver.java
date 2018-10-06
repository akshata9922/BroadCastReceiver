package com.example.felix_its.sharedpreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        SmsMessage[]smsm=null;
        String message=" ";
        if (bundle !=null)
        {
            Object[ ]pdus=(Object[])bundle.get("pdus");
            smsm=new SmsMessage[pdus.length];
            for (int i=0;i<smsm.length;i++)
            {
                smsm[i]=SmsMessage.createFromPdu((byte[])pdus[i]);

                message+="\r\nMessage :";
                message+=smsm[i].getMessageBody().toString();
                message+="\r\n";

                String sender=smsm[i].getOriginatingAddress();
                message+=sender;

            }
            Intent smsIntent=new Intent("sms");
            smsIntent.putExtra("sms",message);
            LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }

    }
}
