package com.fistandantilus.surprise.mvp.model;

import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;

public class UsersManager {

    public static Observable<UserData> getUserDataByUid(final String userUID) {

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
}
