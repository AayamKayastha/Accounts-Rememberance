package com.example.aayamk.accountsrememberance;

import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK;

public class HomeActivity extends AppCompatActivity {

    ChatHeadService chatHeadService=new ChatHeadService();
    List<String> sites=new ArrayList<String>();
    ListView listView;
    DBHelper dbHelper;
    int isPermissionGranted=0;
    ArrayAdapter<String> adapter;
    Dialog details,newEntry;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},1);
        Intent i=getIntent();
        isPermissionGranted=i.getIntExtra("granted",0);
        listView=(ListView)findViewById(R.id.accounts);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         dbHelper=new DBHelper(this);
        initializeList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                details=new Dialog(HomeActivity.this);
                details.setContentView(R.layout.details);
                TextView tvUserName=(TextView)details.findViewById(R.id.username);
                TextView tvPassword=(TextView)details.findViewById(R.id.password);
                TextView tvemail=(TextView)details.findViewById(R.id.email);
                TextView tvExtra=(TextView)details.findViewById(R.id.extras);
                TextView tvSite=(TextView)details.findViewById(R.id.site);
                String site=sites.get(position);
                Cursor cursor=dbHelper.getData(site,position+1);
                if(cursor.getCount()>0)
                {
                    cursor.moveToFirst();
                    tvUserName.setText(tvUserName.getText()+cursor.getString(1));
                    tvPassword.setText(tvPassword.getText()+cursor.getString(2));
                    tvemail.setText(tvemail.getText()+cursor.getString(3));
                    tvExtra.setText(tvExtra.getText()+cursor.getString(4));
                    tvSite.setText(tvSite.getText()+cursor.getString(5));

                }
                details.show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addNew()
    {
        newEntry=new Dialog(this);
        newEntry.setContentView(R.layout.newentry);
        //newentry.show();
        newEntry.show();
        final Button btAdd=(Button)newEntry.findViewById(R.id.addnewentry);
        btAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {

                EditText etUserName=(EditText)newEntry.findViewById(R.id.username);
                EditText etPassword=(EditText)newEntry.findViewById(R.id.password);
                EditText etEmail=(EditText)newEntry.findViewById(R.id.email);
                EditText etExtra=(EditText)newEntry.findViewById(R.id.extras);
                EditText etSite=(EditText)newEntry.findViewById(R.id.site);

                String userName=etUserName.getText().toString(),
                        password=etPassword.getText().toString(),
                        email=etEmail.getText().toString(),
                        extra=etExtra.getText().toString(),
                        site=etSite.getText().toString();
                boolean result=dbHelper.newEntry(userName,password,email,extra,site);
                if (result)
                    Toast.makeText(HomeActivity.this, "New Data Entered", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(HomeActivity.this,"New Entry Failed",Toast.LENGTH_LONG).show();
                newEntry.dismiss();
                sites.add(site);
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void initializeList()
    {
        dbHelper=new DBHelper(this);
        Cursor listOfAccounts=dbHelper.getSites();
        if(listOfAccounts.getCount()>0)
        {
            listOfAccounts.moveToFirst();
            do{
                sites.add(listOfAccounts.getString(1));
            }while (listOfAccounts.moveToNext());
             adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sites);
            listView.setAdapter(adapter);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK || keyCode==KeyEvent.KEYCODE_HOME)
        {
            initiateService();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    public void initiateService()
    {
        startService(new Intent(getBaseContext(), ChatHeadService.class));
        finish();
    }

}


