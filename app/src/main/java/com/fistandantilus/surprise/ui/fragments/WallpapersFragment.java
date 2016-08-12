package com.fistandantilus.surprise.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fistandantilus.surprise.R;

public class WallpapersFragment extends Fragment {

    private static final String FRIEND_TO_SEND_UID_ARG = "friend_to_send_uid_arg";
    private String friendToSend;

    public static WallpapersFragment newInstance(String friendUID) {
        WallpapersFragment fragment = new WallpapersFragment();

        Bundle args = new Bundle();
        args.putString(FRIEND_TO_SEND_UID_ARG, friendUID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        friendToSend = args.getString(FRIEND_TO_SEND_UID_ARG);

        if (friendToSend == null || friendToSend.isEmpty()) {
            throw new IllegalStateException("Friend UID is null or empty");
        }

        Toast.makeText(getActivity(), "FriendUID is " + friendToSend, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallpapers, container, false);
    }
}
