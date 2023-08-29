package com.example.myapplication_new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.quicksdk.BaseCallBack;
import com.quicksdk.Extend;
import com.quicksdk.Payment;
import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.User;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;
import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;
import com.quicksdk.utility.AppConfig;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int REQUESTCODE = 121;
    private static final String TAGE = "SDKERROR";
    private Button btnLogin;
    private Button btnPay;
    private Button btnLogout;
    private Button btnFinish;

    private TextView infoTv;

    private EditText et_gameRoleName;
    private EditText et_gameRoleID;
    private EditText et_partyName;
    private EditText et_vipLevel;
    private EditText et_gameRoleBalance;
    private EditText et_gameRoleLevel;

    private Button btn_roleCreate;
    private Button btn_roleUp;
    private Button btn_exitRC;
    private Button btn_startGame;

    private SharedPreferences preferences;
    private List<EditText> roleInfoList = new ArrayList<>();
    private List<Button> roleButtonList = new ArrayList<>();

    private EditText input_orderid;
    private Context mContext;
    boolean isLandscape = false;
    private boolean isLogin = false;
    private int activityRequestCode = 1;
    private Map<String, String> roleMsgMap= new HashMap<>();;
    private boolean isSelectRole = false;


    public LoginActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this.getApplicationContext();
        setContentView(R.layout.activity_login);
        this.initView();
        QuickSDK.getInstance().setIsLandScape(this.isLandscape);
        this.initQkNotifiers();
        //初始化接口，应在Activity中onCreate方法中调用
        //targetVersion大于等于23时，需要在初始化之前申请权限（QuickSDK没有要求任何权限，游戏按自己的需要进行申请便可，如不用可以不做申请）
        try {
            //check权限
            if ((ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                //没有,申请权限  权限数组
                ActivityCompat.requestPermissions(LoginActivity.this, new String[] { Manifest.permission.READ_PHONE_STATE ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
            } else {
                // 有则执行初始化
                Sdk.getInstance().init(this, "89818740640522515940966715727986", "87874271");
            }
        } catch (Exception e) {
            //异常  继续申请
            ActivityCompat.requestPermissions(LoginActivity.this, new String[] { Manifest.permission.READ_PHONE_STATE ,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUESTCODE);
        }
        //targetVersion小于等于22时，无申请权限
/*        Sdk.getInstance().init(this, "89818740640522515940966715727986", "87874271");*/

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //如果requestCode不是游戏自己申请时传的，则不作处理
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUESTCODE) {
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //申请成功
            Sdk.getInstance().init(this, "89818740640522515940966715727986", "87874271");
        } else {
            //失败  这里逻辑以游戏为准 这里只是模拟申请失败 cp方可改为继续正常初始化调登录然后进游戏 或者继续申请权限 或者退出游戏 或者其他逻辑
            Sdk.getInstance().init(this, "89818740640522515940966715727986", "87874271");
            Log.e(TAGE, "onRequestPermissionsResult: request failed!" );
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        if (id == this.getResId("btn_roleCreate", "id")) {
            setUserInfo(true);
            isSelectRole = true;
            infoTv.setText("create role!");
        }

        if (id == this.getResId("btn_roleUp", "id")) {
            this.setUserInfo(false);
            isSelectRole = true;
            infoTv.setText("Role Up!!!");
        }

        if (id == this.getResId("btn_exitRC", "id")) {
            //// TODO: 2023/8/17 disable create role button
            isSelectRole = false;
        }

        if (id == this.getResId("btn_login", "id")) {
            //Toast 是一个 View 视图，快速的为用户显示少量的信息。
            Toast.makeText(this, "oaid: " + AppConfig.getInstance().getOaid(), Toast.LENGTH_LONG).show();
            //进入到登录界面，自动调用此接口，须保证初始化成功后再调用登录接口。
            //此项目使用点击按钮都登陆，如果是自动登陆设置则使用上述方法
            if (!isLogin)
            {
                User.getInstance().login(this);
            }else
            {
                Toast.makeText(this, "you has login in: " + AppConfig.getInstance().getOaid(), Toast.LENGTH_LONG).show();
            }

        }
        // start game!
        if (id == this.getResId("btn_startGame", "id")) {
            /*this.setUserInfo();*/
            if (isLogin)
            {
                this.setUserInfo(false);//enter game set roleinfo for sure
                infoTv.setText("成功进入游戏！");
            }else
            {
                Toast.makeText(this, "Please Login in to select role! ", Toast.LENGTH_LONG).show();
            }
            Log.d("quicksdk", "parentChannel start game!:  " + Extend.getInstance().getParentChannelType());
        }

        if (id == this.getResId("btn_logout", "id")) {
            User.getInstance().logout(this);
        }

        if (id == this.getResId("btn_pay", "id")) {
            this.pay();
        }

        if (id == this.getResId("btn_finish", "id")) {
            this.exit();
        }

    }

    @SuppressLint({"NewApi"})
    private void pay() {
        final GameRoleInfo roleInfo = new GameRoleInfo();
        roleInfo.setServerID("1");
        roleInfo.setServerName("火星服务器");
        if (isLogin&& isSelectRole&&preferences.getString(String.valueOf(R.string.RoleName),null)!=null)
        {
            roleInfo.setGameRoleName(preferences.getString(String.valueOf(R.string.RoleName),null));
            roleInfo.setGameRoleID(preferences.getString(String.valueOf(R.string.RoleID),null));
            roleInfo.setGameUserLevel(preferences.getString(String.valueOf(R.string.RoleLevel),null));
            roleInfo.setVipLevel(preferences.getString(String.valueOf(R.string.VipLevel),null));
            roleInfo.setGameBalance(preferences.getString(String.valueOf(R.string.RoleBalance),null));
            roleInfo.setPartyName(preferences.getString(String.valueOf(R.string.PartyName),null));
        }else if (!isLogin)
        {
            Toast.makeText(this, "Please Login to pay! ", Toast.LENGTH_LONG).show();
            return;
        }else if (!isSelectRole)
        {
            Toast.makeText(this, "Please select the role to pay! ", Toast.LENGTH_LONG).show();
            return;
        }else
        {
            Toast.makeText(this, "SharedPreference is lost! ", Toast.LENGTH_LONG).show();
            return;
        }
        roleInfo.setRoleCreateTime("1473141432");
        final OrderInfo orderInfo = new OrderInfo();
        String orderID = UUID.randomUUID().toString().replace("-", "");
        orderInfo.setCpOrderID(orderID);
        orderInfo.setGoodsName("元宝");
        orderInfo.setCount(60);//表示游戏币数量
        //setAmount 总金额（元）
        orderInfo.setAmount(this.input_orderid.getText().toString().isEmpty() ? 1.0D : Double.valueOf(this.input_orderid.getText().toString()));
        orderInfo.setGoodsID("1");
        orderInfo.setGoodsDesc("Diamond_60");
        orderInfo.setExtrasParams("透传参数"+orderID);
        this.runOnUiThread(new Runnable() {
            public void run() {
                Payment.getInstance().pay(LoginActivity.this, orderInfo, roleInfo);
            }
        });
    }

    private void exit() {
/*      渠道SDK退出，游戏做退出逻辑时，先通过isShowExitDialog接口判断渠道是否有退出框，
        如果渠道有退出框，直接调用QuickSDK的exit接口；
        如果渠道没有退出框，则调用游戏自身的退出框，退出框点击“确定”后，调用QuickSDK的exit接口。*/
        if (QuickSDK.getInstance().isShowExitDialog()) {
            Sdk.getInstance().exit(this);
        } else {
            (new AlertDialog.Builder(this)).setTitle("退出").setMessage("是否退出游戏?").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Sdk.getInstance().exit(LoginActivity.this);
                }
            }).setNegativeButton("取消", (android.content.DialogInterface.OnClickListener)null).show();
        }

    }

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



    private void initView() {
        et_gameRoleName = (EditText)this.findViewById(this.getResId("et_gameRoleName", "id"));
        et_gameRoleLevel = (EditText) this.findViewById(this.getResId("et_gameRoleLevel", "id"));
        et_gameRoleID = (EditText)this.findViewById(this.getResId("et_gameRoleID", "id"));
        et_gameRoleBalance = (EditText)this.findViewById(this.getResId("et_gameRoleBalance", "id"));
        et_vipLevel = (EditText)this.findViewById(this.getResId("et_vipLevel", "id"));
        et_partyName = (EditText)this.findViewById(this.getResId("et_partyName", "id"));


        btn_roleCreate = (Button)this.findViewById(this.getResId("btn_roleCreate", "id"));
        btn_roleCreate.setOnClickListener(this);
        btn_roleUp = (Button)this.findViewById(this.getResId("btn_roleUp", "id"));
        btn_roleUp.setOnClickListener(this);
        btn_exitRC = (Button)this.findViewById(this.getResId("btn_exitRC", "id"));
        btn_exitRC.setOnClickListener(this);

        this.btnLogin = (Button)this.findViewById(this.getResId("btn_login", "id"));
        this.btnLogin.setOnClickListener(this);
        this.btnPay = (Button)this.findViewById(this.getResId("btn_pay", "id"));
        this.btnPay.setOnClickListener(this);
        this.btnLogout = (Button)this.findViewById(this.getResId("btn_logout", "id"));
        this.btnLogout.setOnClickListener(this);
        this.btnFinish = (Button)this.findViewById(this.getResId("btn_finish", "id"));
        this.btnFinish.setOnClickListener(this);
        this.btn_startGame = (Button)this.findViewById(this.getResId("btn_startGame", "id"));
        this.btn_startGame.setOnClickListener(this);
        this.input_orderid = (EditText)this.findViewById(this.getResId("input_orderid", "id"));
        this.infoTv = (TextView)this.findViewById(this.getResId("tv_userInfo", "id"));

        roleInfoList.add(et_gameRoleName);
        roleInfoList.add(et_gameRoleLevel);
        roleInfoList.add(et_gameRoleID);
        roleInfoList.add(et_gameRoleBalance);
        roleInfoList.add(et_vipLevel);
        roleInfoList.add(et_partyName);

        roleButtonList.add(btn_roleCreate);
        roleButtonList.add(btn_roleUp);
        roleButtonList.add(btn_exitRC);
        roleButtonList.add(btn_startGame);
        roleButtonList.add(btnPay);

        setVisibaleRoleInfo(false);

    }

    public int getResId(String name, String defType) {
        return this.mContext.getResources().getIdentifier(name, defType, this.mContext.getPackageName());
    }

    //example use the func return value to multi-Init Notifier
    private void initQkNotifiers() {
        QuickSDK.getInstance().setInitNotifier(new InitNotifier() {
            public void onSuccess() {
                //// TODO: 2023/8/16  getQuickGameProductCode does not work?
                //获取QuickSDK定义的设备号 
                //说明：此设备号仅以QuickSDK算法计算得来，与渠道或cp设备号不一致； 
                String deviceID = Extend.getInstance().getDeviceID(LoginActivity.this);
                LoginActivity.this.infoTv.setText("初始化成功: DeviceID = "+deviceID + LoginActivity.getQuickGameProductCode());

            }

            public void onFailed(String message, String trace) {
                LoginActivity.this.infoTv.setText("初始化失败:" + message);
            }
        }).setLoginNotifier(new LoginNotifier() {
            public void onSuccess(UserInfo userInfo) {
                if (userInfo != null) {
/*                   登录成功，获取到用户信息userInfo
                   通过userInfo中的UID、token做服务器登录认证*/
/*                    接入要求：
                    1）启动游戏，在登录界面中获取到登录成功的通知，跳转到进入游戏的界面；
                    2）游戏以自动登录的方式调用登录时，获取到登录失败和登录取消的通知，应再次调用登录的功能；
                    3）不同渠道可能返回相同的UID，游戏应使用渠道ID+UID作为用户的唯一标识。渠道ID通过“public int getChannelType()”获取。*/
                    isLogin = true;
                    setVisibaleRoleInfo(true);
                    btnLogin.setVisibility(View.INVISIBLE);

                    int channel_ID = Extend.getInstance().getChannelType();
                    LoginActivity.this.infoTv.setText("登陆成功\n\rchannel_ID:" +channel_ID+ "\n\r"
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
                isLogin = false;
                LoginActivity.this.infoTv.setText("取消登陆");

            }

            public void onFailed(String message, String trace) {
                isLogin = false;
                LoginActivity.this.infoTv.setText("登陆失败:" + message);
            }
        }).setLogoutNotifier(new LogoutNotifier() {
            public void onSuccess() {
/*                接入要求： 获取到注销成功的通知，游戏应回到登录界面并重新调用登录方法。*/
                isLogin = false;
                btnLogin.setVisibility(View.VISIBLE);
                LoginActivity.this.infoTv.setText("注销成功");
            }

            public void onFailed(String message, String trace) {
                LoginActivity.this.infoTv.setText("注销失败:" + message);
            }
        }).setSwitchAccountNotifier(new SwitchAccountNotifier() {
            public void onSuccess(UserInfo userInfo) {
                if (userInfo != null) {
                    //切换账号成功的回调，返回新账号的userInfo
                    //注意：针对渠道SDK在游戏界面中通过悬浮框进行账号的切换 在此回调内无需调用登录接口
                    int channel_ID = Extend.getInstance().getChannelType();
                    LoginActivity.this.infoTv.setText("切换账号成功\n\rchannel_ID:" +channel_ID+ "\n\r"
                            + "UserID:  " + userInfo.getUID() + "\n\r"
                            + "UserName:  " + userInfo.getUserName() + "\n\r"
                            + "Token:  " + userInfo.getToken());
                }

            }

            public void onFailed(String message, String trace) {
                LoginActivity.this.infoTv.setText("切换账号失败:" + message);
            }

            public void onCancel() {
                LoginActivity.this.infoTv.setText("取消切换账号");
            }
        }).setPayNotifier(new PayNotifier() {
            /*说明：游戏充值是否成功到账，只能以服务器的通知为准，而不是客户端的通知，因为部分渠道SDK自身有时不会发送正确的通知。*/
            public void onSuccess(String sdkOrderID, String cpOrderID, String extrasParams) {
                LoginActivity.this.infoTv.setText("支付成功，sdkOrderID:" + sdkOrderID + ",cpOrderID:" + cpOrderID);
                //do not deal the logic of pay success here!
            }

            public void onCancel(String cpOrderID) {
                LoginActivity.this.infoTv.setText("支付取消，cpOrderID:" + cpOrderID);
            }

            public void onFailed(String cpOrderID, String message, String trace) {
                LoginActivity.this.infoTv.setText("支付失败:pay failed,cpOrderID:" + cpOrderID + ",message:" + message);
            }
        }).setExitNotifier(new ExitNotifier() {
            public void onSuccess() {
                //退出成功，游戏在此做自身的退出逻辑处理
                LoginActivity.this.finish();
            }

            public void onFailed(String message, String trace) {
                LoginActivity.this.infoTv.setText("退出失败：" + message);
            }
        });
    }

    private void setVisibaleRoleInfo(boolean isenable) {
        if (isenable)
        {
            for (EditText view :roleInfoList)
            {
                view.setVisibility(View.VISIBLE);
            }
            for (Button button:roleButtonList)
            {
                button.setVisibility(View.VISIBLE);
            }
        }else
        {
            for (EditText view:roleInfoList)
            {
                view.setVisibility(View.INVISIBLE);
            }

            for (Button button:roleButtonList)
            {
                button.setVisibility(View.INVISIBLE);
            }
        }

    }

    public static String getQuickGameProductCode() {
        String code = "";

        try {
            Class<?> qkExtendClass = Class.forName("com.quickgamesdk.QGManager");
            Method getProductCode = qkExtendClass.getDeclaredMethod("getProductCode");
            getProductCode.setAccessible(true);
            code = String.valueOf(getProductCode.invoke(qkExtendClass));
        } catch (Exception var3) {
            var3.printStackTrace();
            code = "";
        }

        if (TextUtils.isEmpty(code)) {
            code = "";
        }

        return code;
    }

    private void verifyRealName() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (Extend.getInstance().isFunctionSupported(105)) {
                    Extend.getInstance().callFunctionWithParamsCallBack(LoginActivity.this, 105, new BaseCallBack() {
                        public void onSuccess(Object... arg0) {
                            if (arg0 != null && arg0.length > 0) {
                                JSONObject jsonObject = (JSONObject)arg0[0];
                                Log.d("json", "==========" + jsonObject.toString());
                            }

                        }

                        public void onFailed(Object... arg0) {
                        }
                    }, new Object[0]);
                }

            }
        });
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
        //receive msg bundle from Role select activity
/*        if (data!=null&&requestCode==activityRequestCode&&resultCode== Activity.RESULT_OK)
        {
            Bundle bundle = data.getExtras(); // 从返回的意图中获取快递包裹
            // 从包裹中取出名叫response_time的字符串
            roleMsgMap.put(getString(R.string.RoleName),bundle.getString(getString(R.string.RoleName))) ;
            roleMsgMap.put(getString(R.string.RoleLevel),bundle.getString(getString(R.string.RoleLevel))) ;
            roleMsgMap.put(getString(R.string.RoleID),bundle.getString(getString(R.string.RoleID)));
            roleMsgMap.put(getString(R.string.RoleBalance),bundle.getString(getString(R.string.RoleBalance)));
            roleMsgMap.put(getString(R.string.VipLevel),bundle.getString(getString(R.string.VipLevel)));
            roleMsgMap.put(getString(R.string.PartyName),bundle.getString(getString(R.string.PartyName)));

*//*            String desc = String.format("收到返回消息：\n应答时间为：%s\n应答内容为：%s",
                    response_time, response_content);*//*
            String desc = "";
            for (String key: roleMsgMap.keySet()) {
                desc+= (key+": "+ roleMsgMap.get(key)+"\n");
            }

            infoTv.setText(desc); // 把返回消息的详情显示在文本视图上
        }*/
    }

}