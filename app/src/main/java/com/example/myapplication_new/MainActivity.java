package com.example.myapplication_new;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication_new.utils.DateUtil;

//import androidx.activity.result.contract.ActivityResultContracts;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;
    private Button button_public;
    private Button button;

    private EditText et_age;
    private EditText et_party;
    private EditText et_robot;
    private SharedPreferences preferences;
    private static String TAG = "MainActivity";
    private DesktopRecevier desktopRecevier;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        desktopRecevier = new DesktopRecevier();
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(desktopRecevier,filter);
        Log.d("lifecycle", "MainActivity onCreate: ");

        et_age = findViewById(R.id.et_age);
        et_party = findViewById(R.id.et_party);
        et_robot = findViewById(R.id.et_robot);



        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);//private archive
        reload();

        tv = findViewById(R.id.tv);
        tv.setText("nihao,shijie!");

        //registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),new Start)

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            //匿名类
            @Override
            public void onClick(View v) {
                //save shared preference
                String age = et_age.getText().toString();
                String party = et_party.getText().toString();
                String robot = et_robot.getText().toString();

                //commmit sharedpreference
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("age",Integer.parseInt(age));
                editor.putString("party",party);
                editor.putString("robot",robot);
                editor.commit();

                //method1: construct dirrectly
/*              Intent intent = new Intent(this,MainActivity2.class);*/
                //method2: setclass
/*              Intent intent = new Intent();  // 创建一个新意图
                intent.setClass(MainActivity.this, MainActivity2.class); // 设置意图要跳转的目标活动*/
                //method3: set component for convenience of inputting string name
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(MainActivity.this,MainActivity2.class);
                intent.setComponent(componentName);
                Bundle bundle = new Bundle();
                bundle.putString("request_time",DateUtil.getNowTime());
                bundle.putString("request_content",tv.getText().toString());
                intent.putExtras(bundle);


                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);//like singleTask: clear top activity when called repeatedly
                startActivityForResult(intent,0);
/*                registerForActivityResult();*/
                /*                startActivity(intent);*/
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new MyOnclickListener(tv));

        button_public = findViewById(R.id.button_public);
        button_public.setOnClickListener(this);

        Button button_longclick = findViewById(R.id.button_longclick_lambda);
        button_longclick.setOnLongClickListener(v ->
        {
            String desc = String.format("%s you long click the button: %s", DateUtil.getNowTime(),((Button) v).getText());
            tv.setText(desc);
            button.setEnabled(true);
            return true;
        });

        ImageButton button_img = findViewById(R.id.imagebutton_01);
        button_img.setOnClickListener(this);
    }

    private void reload() {
        int age = preferences.getInt("age", 0);
        String party = preferences.getString("party",null);
        String robot = preferences.getString("robot", null);

        if (age!=0)
        {
            et_age.setText(String.valueOf(age));
        }
        if (party!=null)
        {
            et_party.setText(String.valueOf(party));
        }
        if (robot!=null)
        {
            et_robot.setText(String.valueOf(robot));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_public)
        {
            String desc = String.format("%s you click the public button and disable jump: %s", DateUtil.getNowTime(),((Button) v).getText());
            tv.setText(desc);
            button.setEnabled(false);
        }else if (v.getId()==R.id.imagebutton_01)
        {
            //implicit  intent
/*            String phoneNo = "12345";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            Uri uri = Uri.parse("tel:"+phoneNo);
            intent.setData(uri);
            startActivity(intent);*/
            // go to login activity
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                tv.setText("the current screen is in Portrait dir");
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                tv.setText("the current screen si in landscape dir");
                break;
            default:
                break;
        }
    }

    //define a broadcast receiver
    private class DesktopRecevier extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String reason = intent.getStringExtra("reason");
            if (!TextUtils.isEmpty(reason)
                    &&(reason.equals("homekey"))||reason.equals("recentapps"))
            {
                //Android 8.0 start to supply pictureinpicture mode
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O
                        &&!isInPictureInPictureMode())
                {
                    //construct picinpic builder
                    PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
                    //setting screen radio
                    Rational ratio = new Rational(10,5);
                    builder.setAspectRatio(ratio);
                    //enter picinpic
                    enterPictureInPictureMode(builder.build());
                }
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode){
            Log.d(TAG, "onPictureInPictureModeChanged: enter pic in pic");
        }else
        {
            Log.d(TAG, "onPictureInPictureModeChanged: exit pic in pic");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //receive msg bundle from last activity
        if (intent!=null&&requestCode==0&&resultCode== Activity.RESULT_OK)
        {
            Bundle bundle = intent.getExtras(); // 从返回的意图中获取快递包裹
            // 从包裹中取出名叫response_time的字符串
            String response_time = bundle.getString("response_time");
            // 从包裹中取出名叫response_content的字符串
            String response_content = bundle.getString("response_content");
            String desc = String.format("收到返回消息：\n应答时间为：%s\n应答内容为：%s",
                    response_time, response_content);
            tv.setText(desc); // 把返回消息的详情显示在文本视图上
        }
    }

    /*    public void doClick(View view) {
        String desc = String.format("%s you click the button: %s", DateUtil.getNowTime(),((Button) view).getText());
        tv.setText(desc);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "MainActivity onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "MainActivity onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "MainActivity onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "MainActivity onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "MainActivity onDestroy: ");
        unregisterReceiver(desktopRecevier);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("lifecycle", "MainActivity: onPostResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", "MainActivity onRestart: ");
    }
}