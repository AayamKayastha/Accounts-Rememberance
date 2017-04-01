package com.example.aayamk.accountsrememberance;

import android.app.Service;
import android.app.assist.AssistStructure;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ChatHeadService extends Service {

    private WindowManager windowManager;

    public ChatHeadService() {
    }
    View content;
    @Override
    public void onCreate() {
        super.onCreate();
        content=LayoutInflater.from(this).inflate(R.layout.floating_button, null);
        final WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        params.gravity= Gravity.TOP|Gravity.START;
        params.x=0;
        params.y=100;
        windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
        windowManager.addView(content,params);
        ImageView img=(ImageView)content.findViewById(R.id.floatingIcon);
        ImageView closeFloaty=(ImageView)content.findViewById(R.id.closeFloaty);
        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startApp =new Intent (ChatHeadService.this, LoginActivity.class);
                startApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startApp);
                stopSelf();
            }
        });*/
        closeFloaty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        content.findViewById(R.id.container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        initialX=params.x;
                        initialY=params.y;
                        initialTouchX=event.getRawX();
                        initialTouchY=event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (Xdiff < 10 && Ydiff < 10) {
                            Intent startApp =new Intent (ChatHeadService.this, LoginActivity.class);
                            startApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startApp);
                            stopSelf();
                        }

                            return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x= initialX+(int)(event.getRawX()-initialTouchX);
                        params.y= initialY+(int)(event.getRawY()-initialTouchY);
                        windowManager.updateViewLayout(content,params);
                        return true;


                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(content!=null)
            windowManager.removeView(content);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
