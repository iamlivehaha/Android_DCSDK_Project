package com.example.myapplication_new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.quicksdk.QuickSdkSplashActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends QuickSdkSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            public void run(){
                //结束闪屏，进入游戏活动
                Log.i("Splash", "startGameActivityDelay");
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        };
        timer.schedule(task,1000); //延迟1s执行
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public void onSplashStop() {

    }
}