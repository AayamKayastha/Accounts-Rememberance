package com.example.aayamk.accountsrememberance;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import static android.view.KeyEvent.KEYCODE_HOME;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int i=0;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btLogin=(Button)findViewById(R.id.login);
        btLogin.setEnabled(false);
        progressBar=(ProgressBar)findViewById(R.id.loading);
        sharedPreferences=getSharedPreferences("settings",0);
        editor=LoginActivity.this.getSharedPreferences("settings",0).edit();
        if(sharedPreferences.getBoolean("logged_in",true))
        {
            login();
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 2084);
        }
            CountDownTimer countDownTimer=new CountDownTimer(1000,20) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressBar.getProgress()+1);
            }

            @Override
            public void onFinish() {
                btLogin.setEnabled(true);
            }
        };
        countDownTimer.start();
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            login();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode!=2084)
            super.onActivityResult(requestCode, resultCode, data);
        else
        {
            if(resultCode== RESULT_OK)
               i=1;

        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK || keyCode==KEYCODE_HOME)
        {
            initiateService();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void login()
    {
        Intent login=new Intent(LoginActivity.this,HomeActivity.class);
        login.putExtra("granted",i);
        editor=LoginActivity.this.getSharedPreferences("settings",0).edit();
        editor.putBoolean("logged_in",true);
        startActivity(login);
        finish();

    }

    public void initiateService()
    {
        startService(new Intent(getBaseContext(), ChatHeadService.class));
        finish();
    }

}
