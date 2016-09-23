package com.fistandantilus.surprise.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.mvp.friends.FriendsPresenterImpl;
import com.fistandantilus.surprise.mvp.friends.FriendsView;
import com.fistandantilus.surprise.mvp.model.API;
import com.fistandantilus.surprise.tools.interactors.FriendsFragmentInteractor;
import com.fistandantilus.surprise.ui.adapters.FriendsListAdapter;

import rx.Observable;

public class FriendsFragment extends Fragment implements View.OnClickListener, FriendsView, AdapterView.OnItemClickListener {

    private ListView friendsList;
    private LinearLayout emptyLayout;
    private FrameLayout loadingLayout;
    private FrameLayout listLayout;
    private Button findFriends;
    private Button inviteFriends;
    private FloatingActionButton floatingActionButton;

    private FriendsFragmentInteractor interactor;
    private FriendsPresenterImpl friendsPresenter;

    private FriendsListAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsPresenter = new FriendsPresenterImpl(this);

        initUI(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FRIENDS", "OnStart");
        friendsPresenter.loadFriends(getActivity());
    }

    private void initUI(View view) {
        view.findViewById(R.id.friends_fragment_find_friends).setOnClickListener(this);
        view.findViewById(R.id.friends_fragment_invite_friends).setOnClickListener(this);

        friendsList = (ListView) view.findViewById(R.id.friends_fragment_list);
        emptyLayout = (LinearLayout) view.findViewById(R.id.friends_fragment_empty_list_layout);
        loadingLayout = (FrameLayout) view.findViewById(R.id.friends_fragment_loading_layout);
        listLayout = (FrameLayout) view.findViewById(R.id.friends_fragment_list_layout);
        findFriends = (Button) view.findViewById(R.id.friends_fragment_find_friends);
        inviteFriends = (Button) view.findViewById(R.id.friends_fragment_invite_friends);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.friends_fragment_fab);

        findFriends.setOnClickListener(this);
        inviteFriends.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);

        friendsList.setOnItemClickListener(this);
        interactor = (FriendsFragmentInteractor) getActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friends_fragment_find_friends:
                showLoading();
                friendsPresenter.findFriends(getActivity());
                break;
            case R.id.friends_fragment_invite_friends:
            case R.id.friends_fragment_fab:
                friendsPresenter.inviteFriends(getActivity());
                break;
        }
    }

    @Override
    public void showFriendsList(Observable<UserData> friendsObservable) {

        friendsObservable
                .toList()
                .subscribe(friendsData -> {

                    if (friendsData.isEmpty()) {
                        showEmptyView();
                        return;
                    }

                    adapter = new FriendsListAdapter(getActivity(), friendsData);
                    friendsList.setAdapter(adapter);

                    listLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                });
    }

    @Override
    public void showEmptyView() {

        adapter = null;

        listLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {

        adapter = null;

        listLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectFriend(String uid) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter == null) return;

        UserData userData = adapter.getFriendData(position);

        API.getUserUIDByPhoneNumber(userData.getPhoneNumber())
                .subscribe(interactor::onFriendSelected);

    }
}
