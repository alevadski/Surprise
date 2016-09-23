package com.fistandantilus.surprise.mvp.friends;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.mvp.model.API;
import com.google.firebase.auth.FirebaseAuth;

import rx.Observable;
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
    public void findFriends(Context context) {

        Observable<String> newFriendsUidObservable = API
                .getAllContactsID(context)
                .flatMap(contactId -> API.getPhoneNumbersFromContactByID(context, contactId))
                .distinct()
                .flatMap(API::getUserUIDByPhoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable updateObservable = API.updateFriends(context, newFriendsUidObservable);

        updateObservable
                .doOnError(error -> Log.d("FRIENDS", "CANNOT FIND FRIENDS! " + error))
                .doOnCompleted(() -> loadFriends(context))
                .subscribe();

    }

    @Override
    public void inviteFriends(Context context) {
        //TODO add link to app!
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Join me in Surprise! %link%");
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.invite_friends_prompt)));
    }
}
