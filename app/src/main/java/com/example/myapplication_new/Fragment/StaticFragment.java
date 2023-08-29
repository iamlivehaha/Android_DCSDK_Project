package com.example.myapplication_new.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication_new.R;

public class StaticFragment extends Fragment {

    private static final String TAG = "static fragment";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "fragment onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "fragment onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "fragment onCreateView: ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_static, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "fragment onActivityViewCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "fragment onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "fragment onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "fragment onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "fragment onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "fragment onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "fragment onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "fragment onDetach: ");
    }
}