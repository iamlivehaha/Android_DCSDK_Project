package com.example.myapplication_new.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication_new.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private Context context;
    Activity activity;
    ConstraintLayout.LayoutParams layoutParams;
    OnClickItemListener onClickItemListener;
    OnClickItemDeleteListener onClickItemDeleteListener;
    boolean showDeleteIcon;


    public MyAdapter(Context context,Activity activity) {
        this.activity = activity;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recy, null,false);
        ViewHolder myViewHodler = new ViewHolder(view);
        view.setLayoutParams(layoutParams);
        return myViewHodler;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_displayName.setText("13975838645");
        String lastLoginTimeStr = String.format(activity.getString(R.string.last_login_time), "11");
        holder.tv_lastLoginTime.setText(lastLoginTimeStr);
        //holder.iv_icon.setImageDrawable(R.drawable.splash1);
        holder.cl_userItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener!=null)
                    onClickItemListener.onClick(v,position);
            }
        });

        if (showDeleteIcon){
            holder.iv_deleteRecord.setVisibility(View.VISIBLE);
            holder.iv_deleteRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemDeleteListener!=null)
                        onClickItemDeleteListener.onClick(v,position);
                }
            });

        }else {
            holder.iv_deleteRecord.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public void setLayoutParams(ConstraintLayout.LayoutParams layoutParams){
        this.layoutParams =layoutParams;

    }

    public void setShowDeleteIcon(boolean showDeleteIcon){
        this.showDeleteIcon = showDeleteIcon;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout cl_userItemLayout;
        TextView tv_displayName;
        TextView tv_lastLoginTime;
        ImageView iv_icon;
        ImageView iv_label;
        ImageView iv_deleteRecord;

        public ViewHolder(View view) {
            super(view);
            cl_userItemLayout =view.findViewById(R.id.cl_user_item_layout);
            tv_displayName = view.findViewById(R.id.tv_display_name);
            tv_lastLoginTime = view.findViewById(R.id.tv_last_login_time);
            //iv_icon = view.findViewById(R.id.iv_icon);
            iv_label = view.findViewById(R.id.iv_often_label);
            iv_deleteRecord = view.findViewById(R.id.iv_delete_record);
        }
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener{
        void onClick(View v, int position);
    }

    public void setOnClickItemDeleteListener(OnClickItemDeleteListener onClickItemDeleteListener){
        this.onClickItemDeleteListener = onClickItemDeleteListener;
    }

    public interface OnClickItemDeleteListener{
        void onClick(View v, int position);
    }

}
