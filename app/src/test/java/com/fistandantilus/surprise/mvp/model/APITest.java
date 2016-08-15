package com.fistandantilus.surprise.mvp.model;

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
}