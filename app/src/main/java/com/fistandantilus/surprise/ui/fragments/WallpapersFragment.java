package com.fistandantilus.surprise.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.Wallpaper;
import com.fistandantilus.surprise.mvp.model.API;
import com.fistandantilus.surprise.mvp.wallpapers.WallpapersPresenterImpl;
import com.fistandantilus.surprise.mvp.wallpapers.WallpapersView;
import com.fistandantilus.surprise.ui.adapters.WallpapersListAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WallpapersFragment extends Fragment implements View.OnClickListener, WallpapersView, AdapterView.OnItemClickListener {

    private static final String FRIEND_TO_SEND_UID_ARG = "friend_to_send_uid_arg";
    private String friendsUID;

    private Button categoryButton;
    private ListView wallpapersList;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton pickPhotoFAB;
    private FloatingActionButton makePhotoFAB;
    private FrameLayout emptyLayout;
    private FrameLayout loadingLayout;

    private WallpapersPresenterImpl presenter;
    private WallpapersListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpapers, container, false);

        presenter = new WallpapersPresenterImpl(this);

        initUI(view);

        return view;
    }

    private void initUI(View view) {

        categoryButton = (Button) view.findViewById(R.id.wallpapers_fragment_category_button);
        wallpapersList = (ListView) view.findViewById(R.id.wallpapers_fragment_list);
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.wallpapers_fragment_floating_action_menu);
        pickPhotoFAB = (FloatingActionButton) view.findViewById(R.id.wallpapers_fragment_fab_pick_photo);
        makePhotoFAB = (FloatingActionButton) view.findViewById(R.id.wallpapers_fragment_fab_make_photo);
        emptyLayout = (FrameLayout) view.findViewById(R.id.wallpapers_fragment_empty_layout);
        loadingLayout = (FrameLayout) view.findViewById(R.id.wallpapers_fragment_loading_layout);

        categoryButton.setOnClickListener(this);
        makePhotoFAB.setOnClickListener(this);
        pickPhotoFAB.setOnClickListener(this);

        wallpapersList.setOnItemClickListener(this);
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
            case R.id.wallpapers_fragment_category_button:
                pickCategory();
                break;
            case R.id.wallpapers_fragment_fab_make_photo:
                makePhoto();
                break;
            case R.id.wallpapers_fragment_fab_pick_photo:
                pickPhoto();
                break;
        }
    }

    private void pickCategory() {

        showLoading();

        API
                .getWallpapersCategories(getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(categories -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.choose_category));

                    builder.setItems(categories.toArray(new String[categories.size()]), (dialog, which) -> {
                        dialog.dismiss();
                        categoryButton.setText(categories.get(which));
                        presenter.loadWallpapers(getActivity(), categories.get(which));
                    });
                    builder.show();
                });


    }

    private void pickPhoto() {

    }

    private void makePhoto() {

    }

    @Override
    public void showWallpapers(Observable<Wallpaper> wallpaperObservable) {


        wallpaperObservable
                .toList()
                .subscribe(wallpapers -> {

                    adapter = new WallpapersListAdapter(getActivity(), wallpapers);
                    wallpapersList.setAdapter(adapter);

                    wallpapersList.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                });
    }

    @Override
    public void showEmptyView() {
        adapter = null;

        wallpapersList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);

    }

    @Override
    public void showLoading() {
        adapter = null;

        wallpapersList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();

        pickCategory();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter == null) return;

        Wallpaper wallpaper = adapter.getWallpaper(position);

        API.setWallpaper(getActivity(), wallpaper.getLink(), friendsUID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnCompleted(() -> Toast.makeText(getActivity(), "Changed!", Toast.LENGTH_SHORT).show())
                .subscribe();

    }
}
