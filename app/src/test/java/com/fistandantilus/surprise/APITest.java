package com.fistandantilus.surprise;

import android.test.mock.MockContext;

import com.fistandantilus.surprise.mvp.model.API;

import org.junit.Assert;
import org.junit.Test;

public class APITest {
    @Test
    public void getUserDataByUid() throws Exception {
        Assert.assertNull(API.getUserDataByUid(null));
        Assert.assertNull(API.getUserDataByUid(""));
    }

    @Test
    public void getUserFriendsUIDList() throws Exception {
        Assert.assertNull(API.getUserFriendsUIDList(null));
        Assert.assertNull(API.getUserFriendsUIDList(""));
    }

    @Test
    public void getAllContactsID() throws Exception {
        Assert.assertNull(API.getAllContactsID(null));
    }

    @Test
    public void getPhoneNumbersFromContactByID() throws Exception {
        Assert.assertNull(API.getPhoneNumbersFromContactByID(null, null));
        Assert.assertNull(API.getPhoneNumbersFromContactByID(null, ""));
        Assert.assertNull(API.getPhoneNumbersFromContactByID(new MockContext(), null));
        Assert.assertNull(API.getPhoneNumbersFromContactByID(new MockContext(), ""));
    }

    @Test
    public void getUserUIDByPhoneNumber() throws Exception {
        Assert.assertNull(API.getUserUIDByPhoneNumber(null));
        Assert.assertNull(API.getUserUIDByPhoneNumber(""));
    }

}