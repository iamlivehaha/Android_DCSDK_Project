package com.example.myapplication_new;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_new.Fragment.UserListFragment;
import com.example.myapplication_new.adapter.MyAdapter;
import com.example.myapplication_new.newclass.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_msg;
    private Thread thread = new Thread();
    private static final String HTTP_ADDRESS = "http://www.baidu.com";
    private TextView webShow;
    private WebView webView;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;

    List<Book> mBookList = new ArrayList<>();
    private int[] images = {R.drawable.splash1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.iv_back).setOnClickListener(this);
/*        findViewById(R.id.bt_response).setOnClickListener(this);
        findViewById(R.id.bt_webrequest).setOnClickListener(this);*/
/*        webView = findViewById(R.id.wv_webview);
        webShow = findViewById(R.id.tv_webShow);*/
        //tv_msg = findViewById(R.id.tv_msg);

        //dynamic fragment register
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
/*        DynamicFragment dynamicFragment = DynamicFragment.newInstance(R.drawable.zagu2, "Zagu2 is coming!");
        if (dynamicFragment!=null)
        {
            transaction.add(R.id.fragment_container,dynamicFragment);
        }else
        {
            transaction.show(dynamicFragment);
        }*/
        transaction.replace(R.id.ll_content, UserListFragment.newInstance());
        transaction.setReorderingAllowed(true);
        transaction.commit();






        //get msg from last activity
        Bundle bundle = getIntent().getExtras();
        String request_time = bundle.getString("request_time");
        String request_content = bundle.getString("request_content");
        String desc = String.format("receive msg:\n requested time = %s\n requested content = %s",request_time,request_content);
        //tv_msg.setText(desc );

    }



    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_back)
        {
            finish();
        }
/*        else if (v.getId()==R.id.bt_response)
        {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("response_time", DateUtil.getNowTime());
            //put string to string.xmp saving compiling process
            bundle.putString("response_content", String.valueOf(R.string.response_msg));
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }else if(v.getId()==R.id.bt_webrequest)
        {
            //2. useing webview to show web
            webView.loadUrl(HTTP_ADDRESS);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            //1. use http request to get text
            // start a http request to show web text
            // be careful child thread is not able to update UI elements
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String str = NetworkUtil.getHttpText(HTTP_ADDRESS);
*//*                    Log.d("NetWork", "onClick http request: "+str);*//*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webShow.setText(str);
                        }
                    });
                }
            }).start();
        }*/
    }
}