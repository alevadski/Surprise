package com.fistandantilus.surprise.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.mvp.friends.FriendsPresenterImpl;
import com.fistandantilus.surprise.mvp.friends.FriendsView;
import com.fistandantilus.surprise.tools.interactors.FriendsFragmentInteractor;

import rx.Observable;

public class FriendsFragment extends Fragment implements View.OnClickListener, FriendsView {

    private ListView friendsList;
    private LinearLayout emptyLayout;
    private FrameLayout loadingLayout;

    private FriendsFragmentInteractor interactor;
    private FriendsPresenterImpl friendsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsPresenter = new FriendsPresenterImpl(this);

        initUI(view);

        friendsPresenter.loadFriends(getActivity());
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
    public void showFriendsList(Observable<UserData> friendsObservable) {

        friendsObservable
                .map(UserData::getName)
                .toList()
                .subscribe(friendsNames -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friendsNames);
                    friendsList.setAdapter(adapter);

                    friendsList.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                });
    }

    @Override
    public void showEmptyView() {
        friendsList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        friendsList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectFriend(String uid) {

    }
}
