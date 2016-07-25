package com.fistandantilus.surprise.ui.fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;
import com.fistandantilus.surprise.tools.Const;
import com.fistandantilus.surprise.ui.adapters.FriendsListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private ListView friendsList;
    private LinearLayout emptyLayout;
    private FrameLayout loadingLayout;

    public static final int MODE_LOADING = 0;
    public static final int MODE_RESULT = 1;
    public static final int MODE_EMPTY_LIST = 2;

    private String userUID = null;
    private UserData userData = null;

    private int currentMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        ((Button) view.findViewById(R.id.friends_fragment_find_friends)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.friends_fragment_invite_friends)).setOnClickListener(this);

        friendsList = (ListView) view.findViewById(R.id.friends_fragment_list);
        emptyLayout = (LinearLayout) view.findViewById(R.id.friends_fragment_empty_list_layout);
        loadingLayout = (FrameLayout) view.findViewById(R.id.friends_fragment_loading_layout);

        setMode(MODE_LOADING);

        initFirebaseFeatures();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        database.getReference(Const.USERS_PATH).addChildEventListener(childChangedListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        database.getReference(Const.USERS_PATH).removeEventListener(childChangedListener);
    }

    private void initFirebaseFeatures() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            throw new IllegalStateException("Not logged in...");
        }

        userUID = user.getUid();

        database.getReference(Const.USERS_PATH).child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);

                if (userData != null) {
                    loadFriends();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadFriends() {
        if (userData.getFriends() == null) {
            setMode(MODE_EMPTY_LIST);
        } else {
            setMode(MODE_RESULT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friends_fragment_find_friends:
                userData.setFriends(new ArrayList<String>());
                findFriends();
                break;
            case R.id.friends_fragment_invite_friends:
                inviteFriends();
                break;
        }
    }

    private void inviteFriends() {
        Toast.makeText(getActivity(), "Invite friends", Toast.LENGTH_SHORT).show();
    }

    private void findFriends() {
        new FriendsFinder().execute();
    }

    private void setMode(int newMode) {
        switch (newMode) {
            case MODE_LOADING:
                loadingLayout.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                friendsList.setVisibility(View.GONE);
                break;
            case MODE_RESULT:
                friendsList.setAdapter(new FriendsListAdapter(getActivity(), userData.getFriends()));

                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                friendsList.setVisibility(View.VISIBLE);
                break;
            case MODE_EMPTY_LIST:
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                friendsList.setVisibility(View.GONE);
                break;
        }
        currentMode = newMode;
    }

    private Set<String> getAllPhonesFromContacts() {
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor == null) {
            Log.d("PHONES", "CURSOR IS NULL!");
            return null;
        }

        Set<String> phonesList = new HashSet<>();
        cursor.moveToFirst();

        while (cursor.moveToNext()) {

            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (!TextUtils.isEmpty(phoneNumber)) {
                    phonesList.add(phoneNumber.replace(" ", ""));
                    continue;
                }
            }
            phones.close();
        }
        cursor.close();
        return phonesList;
    }

    class FriendsFinder extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            setMode(MODE_LOADING);
        }

        @Override
        protected Void doInBackground(Void... params) {

            final Set<String> phonesList = getAllPhonesFromContacts();

            if (phonesList == null || phonesList.isEmpty()) {
                setMode(MODE_EMPTY_LIST);
                return null;
            }

            database.getReference(Const.USERS_PATH).child(userUID).child("friends").setValue(null);
            userData.getFriends().clear();

            database.getReference(Const.USERS_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        UserData user = child.getValue(UserData.class);

                        for (String phone : phonesList) {

                            if (PhoneNumberUtils.compare(phone, user.getPhoneNumber())) {
                                userData.getFriends().add(child.getKey());
                                database.getReference(Const.USERS_PATH).child(userUID).setValue(userData);
                            }
                        }
                    }

                    loadFriends();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            return null;
        }
    }

    ChildEventListener childChangedListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            loadFriends();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
