package com.fistandantilus.surprise.mvp.friends;

import android.content.Context;

import com.fistandantilus.surprise.mvp.model.API;
import com.google.firebase.auth.FirebaseAuth;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FriendsPresenterImpl implements FriendsPresenter {

    private FriendsView friendsView;

    public FriendsPresenterImpl(FriendsView friendsView) {
        this.friendsView = friendsView;
    }

    @Override
    public void loadFriends(Context context) {

        friendsView.showLoading();

        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        friendsView.showFriendsList(
                API
                        .getUserFriendsUIDList(userUID)
                .flatMap(API::getUserDataByUid)
                .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
        );


    }

    @Override
    public void inviteFriends() {

    }

    @Override
    public void findFriends() {

    }
}
