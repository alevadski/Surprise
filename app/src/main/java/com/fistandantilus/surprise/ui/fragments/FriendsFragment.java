package com.fistandantilus.surprise.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.mvp.friends.FriendsView;
import com.fistandantilus.surprise.tools.interactors.FriendsFragmentInteractor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendsFragment extends Fragment implements View.OnClickListener, FriendsView {

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private ListView friendsList;
    private LinearLayout emptyLayout;
    private FrameLayout loadingLayout;

    private FriendsFragmentInteractor interactor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {
        view.findViewById(R.id.friends_fragment_find_friends).setOnClickListener(this);
        view.findViewById(R.id.friends_fragment_invite_friends).setOnClickListener(this);

        friendsList = (ListView) view.findViewById(R.id.friends_fragment_list);
        emptyLayout = (LinearLayout) view.findViewById(R.id.friends_fragment_empty_list_layout);
        loadingLayout = (FrameLayout) view.findViewById(R.id.friends_fragment_loading_layout);

        interactor = (FriendsFragmentInteractor) getActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friends_fragment_find_friends:

                break;
            case R.id.friends_fragment_invite_friends:

                break;
        }
    }

    @Override
    public void showFriendsList(List<UserData> friends) {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showLoading() {

    }
}
