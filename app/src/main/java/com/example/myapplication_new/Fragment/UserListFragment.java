package com.example.myapplication_new.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication_new.MainActivity2;
import com.example.myapplication_new.R;
import com.example.myapplication_new.adapter.MyAdapter;
import com.quicksdk.utility.AppConfig;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment {
    public static final String TAG="---UserListFragment";

    ConstraintLayout cl_userListHeader;
    Button btn_login;
    TextView tv_otherAccountLogin;
    TextView tv_displayName;
    TextView tv_lastLoginTime;
    ImageView iv_icon;
    ImageView iv_oftenLabel;
    RecyclerView recyclerView;
    PopupWindow popupWindow;
    View popupWindowView;
    MyAdapter userListAdapter;
    private boolean deleteModel;


    public UserListFragment() {
        super();
    }

    // TODO: Rename and change types and number of parameters
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        bindView(fragmentView);
        //setUserListHeader(displayedHerderUser);
        initPopupWindow();
        return fragmentView;
    }

    private void bindView(View fragmentView){
        cl_userListHeader = fragmentView.findViewById(R.id.cl_user_list_header);
        btn_login = fragmentView.findViewById(R.id.btn_login);
        tv_otherAccountLogin = fragmentView.findViewById(R.id.tv_other_account_login);
        tv_displayName = fragmentView.findViewById(R.id.tv_display_name);
        tv_lastLoginTime = fragmentView.findViewById(R.id.tv_last_login_time);
        iv_icon = fragmentView.findViewById(R.id.iv_icon);
        iv_oftenLabel= fragmentView.findViewById(R.id.iv_often_label);

        cl_userListHeader.setOnClickListener(this::onClickUserListHeader);
        /*btn_login.setOnClickListener(this::onClickLogin);
        tv_otherAccountLogin.setOnClickListener(this::onClickOtherLogin);*/


    }

    private void onClickUserListHeader(View v) {
        hideOtherBtn();
        Toast.makeText(getActivity(), "you click the userlist: " , Toast.LENGTH_LONG).show();
        //弹出列表
        popupWindow.setWidth(v.getWidth());//v.getWidth()
        setRecyclerViewItemRect(v);
        userListAdapter.notifyDataSetChanged();
        popupWindow.showAsDropDown(v,0,-v.getHeight());
    }

    private void setRecyclerViewItemRect(View v){

        int userCount = 6;
        int recyclerViewHeight;
        if (userCount<3){
            recyclerViewHeight = userCount * v.getHeight();
        }else {
            recyclerViewHeight = 3 * v.getHeight();
        }
        recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,recyclerViewHeight));
        ConstraintLayout.LayoutParams lytp = new ConstraintLayout.LayoutParams(v.getWidth(), v.getHeight());
        userListAdapter.setLayoutParams(lytp);//new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,v.getHeight())
        userListAdapter.setShowDeleteIcon(false);
    }

    private void hideOtherBtn() {
        btn_login.setVisibility(View.INVISIBLE);
        tv_otherAccountLogin.setVisibility(View.INVISIBLE);
    }

    private void initPopupWindow() {
        popupWindowView = View.inflate(getActivity(),R.layout.popwindows,null);
        Button btn_addNewAccount = popupWindowView.findViewById(R.id.btn_add_new_account);
        Button btn_deleteRecord = popupWindowView.findViewById(R.id.btn_delete_record);

/*        btn_addNewAccount.setOnClickListener(this::onClickAddNewAccount);
        btn_deleteRecord.setOnClickListener(this::onClickDeleteRecord);
        btn_deleteFinish.setOnClickListener(this::onClickDeleteFinish);*/

        recyclerView = popupWindowView.findViewById(R.id.rv_user_select_list);
        userListAdapter = new MyAdapter(getActivity(),getActivity());
        userListAdapter.setOnClickItemListener(this::onClickUserItem);
        //userListAdapter.setOnClickItemDeleteListener(this::onClickItemDelete);
        recyclerView.setAdapter(userListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//下拉菜单
        popupWindow.setFocusable(false);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(this::onPopupDismiss);
    }

    private void onPopupDismiss(){
        //setUserListHeader(displayedHerderUser);
        showOtherBtn();
        onClickDeleteFinish(null);
    }
    private void showOtherBtn(){
        btn_login.setVisibility(View.VISIBLE);
        tv_otherAccountLogin.setVisibility(View.VISIBLE);
    }

    private void onClickDeleteFinish(View v){
/*        group_popupWindowBtn.setVisibility(View.VISIBLE);
        btn_deleteFinish.setVisibility(View.INVISIBLE);
        deleteModel = false;*/
        userListAdapter.setShowDeleteIcon(false);
        userListAdapter.notifyDataSetChanged();
/*        UserManager.getInstance().saveAllUser();*/
    }

    private void onClickUserItem(View v, int position){
        if (deleteModel) return;
        //displayedHerderUser = historicalUsers.get(position);
        //setUserListHeader(displayedHerderUser);
        popupWindow.dismiss();
    }

    private void onClickDeleteRecord(View v){
        deleteModel = true;
/*        group_popupWindowBtn.setVisibility(View.INVISIBLE);
        btn_deleteFinish.setVisibility(View.VISIBLE);*/
        userListAdapter.setShowDeleteIcon(true);
        userListAdapter.notifyDataSetChanged();
    }

}