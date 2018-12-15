package com.webxert.listeningsouls.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webxert.listeningsouls.R;

public class ChatFragment extends Fragment {


    public static Fragment mInstance = null;

    public ChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;

    }

    public static Fragment getmInstance() {
        if (mInstance == null)
            return mInstance = new ChatFragment();
        else return mInstance;
    }
}
