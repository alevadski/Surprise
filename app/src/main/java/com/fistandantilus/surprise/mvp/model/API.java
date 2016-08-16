package com.fistandantilus.surprise.mvp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

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

    public static Observable<String> getAllContactsID(Context context) {

        if (context == null)
            return null;
        else
            return Observable.create(subscriber -> {

                ContentResolver resolver = context.getContentResolver();
                Cursor contactsCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                if (contactsCursor == null || contactsCursor.getCount() == 0) {
                    subscriber.onCompleted();
                    return;
                }

                if (contactsCursor.moveToFirst()) {
                    do {
                        String contactId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        subscriber.onNext(contactId);
                    } while (contactsCursor.moveToNext());
                }

                contactsCursor.close();
                subscriber.onCompleted();
            });
    }

    public static Observable<String> getPhoneNumbersFromContactByID(Context context, String contactId) {
        if (context == null || contactId == null || contactId.isEmpty())
            return null;
        else
            return Observable.create(subscriber -> {

                ContentResolver resolver = context.getContentResolver();
                Cursor phonesCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                if (phonesCursor == null || phonesCursor.getCount() == 0) {
                    subscriber.onCompleted();
                    return;
                }

                if (phonesCursor.moveToFirst()) {
                    do {
                        String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        subscriber.onNext(phoneNumber);
                    } while (phonesCursor.moveToNext());
                }

                phonesCursor.close();
                subscriber.onCompleted();
            });
    }

    public static Observable<String> getUserUIDByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            return null;
        else
            return Observable.create(subscriber -> {

                FirebaseDatabase.getInstance().getReference(Const.USERS_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            UserData userData = child.getValue(UserData.class);

                            if (PhoneNumberUtils.compare(userData.getPhoneNumber(), phoneNumber)) {
                                subscriber.onNext(child.getKey());
                            }
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
