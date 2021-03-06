package com.fistandantilus.surprise.mvp.main;

import com.fistandantilus.surprise.mvp.model.API;

public class MainPresenterImpl implements MainPresenter {

    private final MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void logout() {
        API.logout();
    }
}
