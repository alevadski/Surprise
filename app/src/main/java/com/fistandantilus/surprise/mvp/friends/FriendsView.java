package com.fistandantilus.surprise.mvp.friends;

import com.fistandantilus.surprise.dao.UserData;

import java.util.List;

import rx.Observable;

public interface FriendsView {

    void showFriendsList(Observable<UserData> friends);

    void showEmptyView();

    void showLoading();

    void selectFriend(String uid);
}
