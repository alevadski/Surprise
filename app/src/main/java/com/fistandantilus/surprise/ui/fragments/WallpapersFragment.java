package com.fistandantilus.surprise.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.Wallpaper;
import com.fistandantilus.surprise.mvp.wallpapers.WallpapersView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import rx.Observable;

public class WallpapersFragment extends Fragment implements View.OnClickListener, WallpapersView {

    private static final String FRIEND_TO_SEND_UID_ARG = "friend_to_send_uid_arg";
    private String friendsUID;

    private Spinner categorySpinner;
    private RecyclerView wallpapersRecycler;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton pickPhotoFAB;
    private FloatingActionButton makePhotoFAB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpapers, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {

        categorySpinner = (Spinner) view.findViewById(R.id.wallpapers_fragment_category_spinner);
        wallpapersRecycler = (RecyclerView) view.findViewById(R.id.wallpapers_fragment_recycler);
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.wallpapers_fragment_floating_action_menu);
        pickPhotoFAB = (FloatingActionButton) view.findViewById(R.id.wallpapers_fragment_fab_pick_photo);
        makePhotoFAB = (FloatingActionButton) view.findViewById(R.id.wallpapers_fragment_fab_make_photo);

        makePhotoFAB.setOnClickListener(this);
        pickPhotoFAB.setOnClickListener(this);

        //TODO TEMPORARY SPINNER ADAPTER!
        categorySpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, new String[]{"one", "two", "three"}));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        friendsUID = args.getString(FRIEND_TO_SEND_UID_ARG);

        if (friendsUID == null || friendsUID.isEmpty()) {
            throw new IllegalStateException("Friend UID is null or empty");
        }
    }

    public static WallpapersFragment newInstance(String friendUID) {
        WallpapersFragment fragment = new WallpapersFragment();

        Bundle args = new Bundle();
        args.putString(FRIEND_TO_SEND_UID_ARG, friendUID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallpapers_fragment_fab_make_photo:
                makePhoto();
                break;
            case R.id.wallpapers_fragment_fab_pick_photo:
                pickPhoto();
                break;
        }
    }

    private void pickPhoto() {

    }

    private void makePhoto() {

    }

    @Override
    public void showWallpapers(Observable<Wallpaper> wallpapers) {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showLoading() {

    }
}
