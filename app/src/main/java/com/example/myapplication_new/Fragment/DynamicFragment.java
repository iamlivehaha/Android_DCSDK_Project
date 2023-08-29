 package com.example.myapplication_new.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication_new.R;


public class DynamicFragment extends Fragment {

    private static final String TAG = "dynamic fragment";
    private FragmentActivity mContext;
    private int mImageID;
    private String mDesc;
    private View mView;

    // TODO: Rename and change types and number of parameters
    public static DynamicFragment newInstance(int imgage_id, String desc) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("image_id", imgage_id);
        args.putString("desc", desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        if (getArguments() != null) {
            mImageID = getArguments().getInt("image_id", 0);
            mDesc = getArguments().getString("desc");
        }
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dynamic, container, false);
        // 根据布局文件fragment_dynamic.xml设置视图对象
        ImageView iv_pic = mView.findViewById(R.id.iv_pic);
        TextView tv_desc = mView.findViewById(R.id.tv_desc);
        iv_pic.setImageResource(mImageID);
        tv_desc.setText(mDesc);
        Log.d(TAG, "onCreateView: ");
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}