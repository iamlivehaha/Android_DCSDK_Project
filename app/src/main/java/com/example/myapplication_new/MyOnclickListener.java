package com.example.myapplication_new;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication_new.utils.DateUtil;

public class MyOnclickListener implements View.OnClickListener {
    private final TextView tv_result;
    public MyOnclickListener(TextView tv_result) {
        this.tv_result = tv_result;
    }

    //override
    public void onClick(View view)
    {
        String desc = String.format("%s you click the button: %s", DateUtil.getNowTime(),((Button) view).getText());
        tv_result.setText(desc);
    }
}
