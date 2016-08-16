package com.fistandantilus.surprise.mvp.friends;

import com.fistandantilus.surprise.dao.UserData;

import java.util.List;

public interface FriendsView {

    public void showFriendsList(List<UserData> friends);

    public void showEmptyView();

    public void showLoading();
}
