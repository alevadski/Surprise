package com.fistandantilus.surprise.mvp.friends;

import android.content.Context;

public interface FriendsPresenter {

    void loadFriends(Context context);

    void inviteFriends();

    void findFriends();
}
