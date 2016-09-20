package com.fistandantilus.surprise.mvp.friends;

import android.content.Context;

import com.fistandantilus.surprise.mvp.model.API;

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

        friendsView.showFriendsList(API
                .getAllContactsID(context)
                .flatMap(contactID -> API.getPhoneNumbersFromContactByID(context, contactID))
                .flatMap(API::getUserUIDByPhoneNumber)
                .filter(userUID -> userUID != null && !userUID.isEmpty())
                .flatMap(API::getUserDataByUid)
                .filter(userData -> userData != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()));

    }

    @Override
    public void inviteFriends() {

    }

    @Override
    public void findFriends() {

    }
}
