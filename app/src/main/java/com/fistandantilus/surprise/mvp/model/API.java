package com.fistandantilus.surprise.mvp.model;

import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;

public class API {

    public static Observable<UserData> getUserDataByUid(final String userUID) {

        if (userUID == null || userUID.isEmpty())
            return null;
        else
            return Observable.create(subscriber -> {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.USERS_PATH).child(userUID);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subscriber.onNext(dataSnapshot.getValue(UserData.class));
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new Throwable(databaseError.getMessage()));
                    }
                });
            });
    }

    public static Observable<String> getUserFriendsUIDList(String userUID) {

        if (userUID == null || userUID.isEmpty())
            return null;
        else
            return Observable.create(subscriber -> {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.USERS_PATH).child(userUID).child(Const.FRIENDS_PATH);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childData : dataSnapshot.getChildren()) {
                            String friendUID = childData.getValue(String.class);
                            subscriber.onNext(friendUID);
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new Throwable(databaseError.getMessage()));
                    }
                });
            });
    }

    public static void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }
}
