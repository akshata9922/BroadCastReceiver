package com.example.felix_its.sharedpreference;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtname, edtaddress, edtphone;
    Button btnsave;
    TextView txtmessage;
    SharedPreferences pref;
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";

    private static final int REQUEST_PERMISSION_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtname = findViewById(R.id.edtname);
        edtaddress = findViewById(R.id.edtaddress);
        edtphone = findViewById(R.id.edtphone);
        txtmessage = findViewById(R.id.txtmessage);

        btnsave = findViewById(R.id.btnsave);
        pref = getApplicationContext().getSharedPreferences("MyApp", MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, REQUEST_PERMISSION_SMS);
        }


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString().trim();
                String address = edtaddress.getText().toString().trim();
                String phone = edtphone.getText().toString().trim();

                SharedPreferences.Editor editor = pref.edit();
                editor.putString(NAME, name);
                editor.putString(ADDRESS, address);
                editor.putString(PHONE, phone);
                editor.commit();
                Toast.makeText(MainActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
                //or we can use apply in place commit
            }

        });
    }

    public void getData(View view) {
        String name = pref.getString(NAME, "");
        String address = pref.getString(ADDRESS, "");
        String phone = pref.getString(PHONE, "");

        edtname.setText(name);
        edtaddress.setText(address);
        edtphone.setText(phone);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
            }
            else {
                Toast.makeText(this,"This Permission is mandatory",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},REQUEST_PERMISSION_SMS);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver,new IntentFilter("sms"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
    }

    private BroadcastReceiver smsReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sms =intent.getStringExtra("sms");
            txtmessage.setText(sms);
        }
    };
}





