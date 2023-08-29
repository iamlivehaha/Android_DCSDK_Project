package com.example.myapplication_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication_new.utils.DateUtil;
import com.quicksdk.Extend;
import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.User;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;
import com.quicksdk.utility.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RoleActivity extends AppCompatActivity implements  View.OnClickListener {

    private static final int REQUESTCODE = 111;

    private Button btn_roleCreate;
    private Button btn_roleUp;
    private Button btn_exitRC;
    private Button btn_startGame;
    private TextView tv_debugInfo;
    private EditText et_gameRoleLevel;
    private EditText et_gameRoleName;
    private EditText et_gameRoleID;
    private EditText et_partyName;
    private EditText et_vipLevel;
    private EditText et_gameRoleBalance;
    private Context mContext;
    private boolean isLandscape = false;

    private SharedPreferences preferences;
    private List<EditText> roleInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        this.mContext = this.getApplicationContext();
        this.initView();
        QuickSDK.getInstance().setIsLandScape(this.isLandscape);
        this.initQkNotifiers();
        //初始化接口，应在Activity中onCreate方法中调用
        //targetVersion大于等于23时，需要在初始化之前申请权限（QuickSDK没有要求任何权限，游戏按自己的需要进行申请便可，如不用可以不做申请）
        try {
            //check权限
            if ((ContextCompat.checkSelfPermission(RoleActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(RoleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                //没有,申请权限  权限数组
                ActivityCompat.requestPermissions(RoleActivity.this, new String[] { Manifest.permission.READ_PHONE_STATE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
            } else {
                // 有则执行初始化
                Sdk.getInstance().init(this, "89818740640522515940966715727986", "87874271");
            }
        } catch (Exception e) {
            //异常  继续申请
            ActivityCompat.requestPermissions(RoleActivity.this, new String[] { Manifest.permission.READ_PHONE_STATE ,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUESTCODE);
        }
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);//private archive
        reloadRoleInfo();

        //sdk lifecyle
        Sdk.getInstance().onCreate(this);
    }

    private void reloadRoleInfo() {
        Vector<String> strs = new Vector<>();
        strs.add(preferences.getString(String.valueOf(R.string.RoleName),null));
        strs.add(preferences.getString(String.valueOf(R.string.RoleLevel),null));
        strs.add(preferences.getString(String.valueOf(R.string.RoleID),null));
        strs.add(preferences.getString(String.valueOf(R.string.RoleBalance),null));
        strs.add(preferences.getString(String.valueOf(R.string.VipLevel),null));
        strs.add(preferences.getString(String.valueOf(R.string.PartyName),null));


        int i =0;
        for(EditText editText:roleInfoList)
        {
            editText.setText(strs.get(i++));
        }
    }

    //example use the func return value to multi-Init Notifier
    private void initQkNotifiers() {
        QuickSDK.getInstance().setInitNotifier(new InitNotifier() {
            public void onSuccess() {
                RoleActivity.this.tv_debugInfo.setText("初始化成功: " + "product code :89818740640522515940966715727986");
            }

            public void onFailed(String message, String trace) {
                RoleActivity.this.tv_debugInfo.setText("初始化失败:" + message);
            }
        })
/*                .setLoginNotifier(new LoginNotifier() {
            public void onSuccess(UserInfo userInfo) {
                if (userInfo != null) {
*//*                   登录成功，获取到用户信息userInfo
                   通过userInfo中的UID、token做服务器登录认证*//*
*//*                    接入要求：
                    1）启动游戏，在登录界面中获取到登录成功的通知，跳转到进入游戏的界面；
                    2）游戏以自动登录的方式调用登录时，获取到登录失败和登录取消的通知，应再次调用登录的功能；
                    3）不同渠道可能返回相同的UID，游戏应使用渠道ID+UID作为用户的唯一标识。渠道ID通过“public int getChannelType()”获取。*//*
                    int channel_ID = Extend.getInstance().getChannelType();
                    RoleActivity.this.tv_debugInfo.setText("登陆成功\n\rchannel_ID:" +channel_ID+ "\n\r"
                            + "UserID:  " + userInfo.getUID() + "\n\r"
                            + "UserName:  " + userInfo.getUserName() + "\n\r"
                            + "Token:  " + userInfo.getToken());
                    Log.d("quicksdk", "登陆成功\n\r channel_ID:" + channel_ID+ "\n\r"
                            +"UserID:  " + userInfo.getUID() + "\n\r"
                            + "UserName:  " + userInfo.getUserName() + "\n\r"
                            + "Token:  " + userInfo.getToken());
                }

            }

            public void onCancel() {
                RoleActivity.this.tv_debugInfo.setText("取消登陆");
            }

            public void onFailed(String message, String trace) {
                RoleActivity.this.tv_debugInfo.setText("登陆失败:" + message);
            }
        }).setLogoutNotifier(new LogoutNotifier() {
            public void onSuccess() {
                *//*                接入要求： 获取到注销成功的通知，游戏应回到登录界面并重新调用登录方法。*//*
                RoleActivity.this.tv_debugInfo.setText("注销成功");
            }

            public void onFailed(String message, String trace) {
                RoleActivity.this.tv_debugInfo.setText("注销失败:" + message);
            }
        }).setSwitchAccountNotifier(new SwitchAccountNotifier() {
            public void onSuccess(UserInfo userInfo) {
                if (userInfo != null) {
                    int channel_ID = Extend.getInstance().getChannelType();
                    RoleActivity.this.tv_debugInfo.setText("切换账号成功\n\rchannel_ID:" +channel_ID+ "\n\r"
                            + "UserID:  " + userInfo.getUID() + "\n\r"
                            + "UserName:  " + userInfo.getUserName() + "\n\r"
                            + "Token:  " + userInfo.getToken());
                }

            }

            public void onFailed(String message, String trace) {
                RoleActivity.this.tv_debugInfo.setText("切换账号失败:" + message);
            }

            public void onCancel() {
                RoleActivity.this.tv_debugInfo.setText("取消切换账号");
            }
        }).setPayNotifier(new PayNotifier() {
            *//*说明：游戏充值是否成功到账，只能以服务器的通知为准，而不是客户端的通知，因为部分渠道SDK自身有时不会发送正确的通知。*//*
            public void onSuccess(String sdkOrderID, String cpOrderID, String extrasParams) {
                RoleActivity.this.tv_debugInfo.setText("支付成功，sdkOrderID:" + sdkOrderID + ",cpOrderID:" + cpOrderID);
                //do not deal the logic of pay success here!
            }

            public void onCancel(String cpOrderID) {
                RoleActivity.this.tv_debugInfo.setText("支付取消，cpOrderID:" + cpOrderID);
            }

            public void onFailed(String cpOrderID, String message, String trace) {
                RoleActivity.this.tv_debugInfo.setText("支付失败:pay failed,cpOrderID:" + cpOrderID + ",message:" + message);
            }
        }).setExitNotifier(new ExitNotifier() {
            public void onSuccess() {
                //退出成功，游戏在此做自身的退出逻辑处理
                RoleActivity.this.finish();
            }

            public void onFailed(String message, String trace) {
                RoleActivity.this.tv_debugInfo.setText("退出失败：" + message);
            }
        })*/
        ;
    }

    private void initView() {
        //Role info Edittext
        et_gameRoleName = (EditText)this.findViewById(this.getResId("et_gameRoleName", "id"));
        et_gameRoleLevel = (EditText)this.findViewById(this.getResId("et_gameRoleLevel", "id"));
        et_gameRoleID = (EditText)this.findViewById(this.getResId("et_gameRoleID", "id"));
        et_gameRoleBalance = (EditText)this.findViewById(this.getResId("et_gameRoleBalance", "id"));
        et_vipLevel = (EditText)this.findViewById(this.getResId("et_vipLevel", "id"));
        et_partyName = (EditText)this.findViewById(this.getResId("et_partyName", "id"));
        roleInfoList.add(et_gameRoleName);
        roleInfoList.add(et_gameRoleLevel);
        roleInfoList.add(et_gameRoleID);
        roleInfoList.add(et_gameRoleBalance);
        roleInfoList.add(et_vipLevel);
        roleInfoList.add(et_partyName);

        //Button
        btn_roleCreate = (Button)this.findViewById(this.getResId("btn_roleCreate", "id"));
        btn_roleCreate.setOnClickListener(this);
        btn_roleUp = (Button)this.findViewById(this.getResId("btn_roleUp", "id"));
        btn_roleUp.setOnClickListener(this);
        btn_exitRC = (Button)this.findViewById(this.getResId("btn_exitRC", "id"));
        btn_exitRC.setOnClickListener(this);
        btn_startGame = (Button)this.findViewById(this.getResId("btn_startGame", "id"));
        btn_startGame.setOnClickListener(this);
        tv_debugInfo = (TextView)this.findViewById(this.getResId("tv_debugInfo", "id"));
    }

    public int getResId(String name, String defType) {
        return this.mContext.getResources().getIdentifier(name, defType, this.mContext.getPackageName());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == this.getResId("btn_roleCreate", "id")) {
            setUserInfo(true);
            tv_debugInfo.setText("create role!");
        }

        if (id == this.getResId("btn_roleUp", "id")) {
            this.setUserInfo(false);
            tv_debugInfo.setText("Role Up!!!");
        }

        if (id == this.getResId("btn_exitRC", "id")) {
            this.finish();
        }

        if (id == this.getResId("btn_startGame", "id")) {
            this.setUserInfo(false);//enter game set roleinfo for sure

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            //put string to string.xmp saving compiling process
            bundle.putString(getString(R.string.RoleName), String.valueOf(et_gameRoleName.getText()));
            bundle.putString(getString(R.string.RoleLevel), String.valueOf(et_gameRoleLevel.getText()));
            bundle.putString(getString(R.string.RoleID), String.valueOf(et_gameRoleID.getText()));
            bundle.putString(getString(R.string.RoleBalance), String.valueOf(et_gameRoleBalance.getText()));
            bundle.putString(getString(R.string.VipLevel), String.valueOf(et_vipLevel.getText()));
            bundle.putString(getString(R.string.PartyName), String.valueOf(et_partyName.getText()));
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    /**
     *  在创建游戏角色、进入游戏和角色升级3个地方调用此接口，当创建角色时createRole值为true，其他两种情况为false。true & false  均需要调用一遍
     * @param IsCreateRole   //创建角色 IsCreateRole set to true
     *         //进入游戏及角色升级 IsCreateRole set to false
     */

    public void setUserInfo(boolean IsCreateRole) {
        GameRoleInfo roleInfo = new GameRoleInfo();
        roleInfo.setServerID("1");//数字字符串，不能含有中文字符
        roleInfo.setServerName("火星服务器");
        roleInfo.setGameRoleName(String.valueOf(et_gameRoleName.getText()));
        roleInfo.setGameRoleID(String.valueOf(et_gameRoleID.getText()));
        roleInfo.setGameUserLevel(String.valueOf(et_gameRoleLevel.getText()));//设置游戏角色等级
        roleInfo.setVipLevel(String.valueOf(et_vipLevel.getText()));//设置当前用户vip等级，必须为数字整型字符串,请勿传"vip1"等类似字符串
        roleInfo.setGameBalance(String.valueOf(et_gameRoleBalance.getText()));//角色用户余额
        roleInfo.setPartyName(String.valueOf(et_partyName.getText()));//设置帮派名称
        roleInfo.setRoleCreateTime("1473141432");//UC，当乐与1881，TT渠道必传，值为10位数时间戳
        roleInfo.setPartyId("1100");//360渠道参数，设置帮派id，必须为整型字符串
        roleInfo.setGameRoleGender("男");//360渠道参数
        roleInfo.setGameRolePower("38");//360,TT语音渠道参数，设置角色战力，必须为整型字符串
        roleInfo.setPartyRoleId("11");//360渠道参数，设置角色在帮派中的id
        roleInfo.setPartyRoleName("chairman");//360渠道参数，设置角色在帮派中的名称
        roleInfo.setProfessionId("38");//360渠道参数，设置角色职业id，必须为整型字符串
        roleInfo.setProfession("pilot");//360渠道参数，设置角色职业名称
        roleInfo.setFriendlist("无");//360渠道参数，设置好友关系列表，格式请参考：http://open.quicksdk.net/help/detail/aid/190

        User.getInstance().setGameRoleInfo(this, roleInfo, IsCreateRole);
        User.getInstance().getUserInfo().getUID();

        // save sharepreference
        //commmit sharedpreference

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(R.string.RoleName),roleInfo.getGameRoleName());
        editor.putString(String.valueOf(R.string.RoleLevel),roleInfo.getGameRoleLevel());
        editor.putString(String.valueOf(R.string.RoleID),roleInfo.getGameRoleID());
        editor.putString(String.valueOf(R.string.RoleBalance),roleInfo.getGameBalance());
        editor.putString(String.valueOf(R.string.VipLevel),roleInfo.getVipLevel());
        editor.putString(String.valueOf(R.string.PartyName),roleInfo.getPartyName());

        editor.commit();
    }

    protected void onStart() {
        super.onStart();
        Sdk.getInstance().onStart(this);
    }

    protected void onRestart() {
        super.onRestart();
        Sdk.getInstance().onRestart(this);
    }

    protected void onPause() {
        super.onPause();
        Sdk.getInstance().onPause(this);
    }

    protected void onResume() {
        super.onResume();
        Sdk.getInstance().onResume(this);
    }

    protected void onStop() {
        super.onStop();
        Sdk.getInstance().onStop(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        Sdk.getInstance().onDestroy(this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Sdk.getInstance().onNewIntent(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Sdk.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }
}