package com.fistandantilus.surprise.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.Wallpaper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WallpapersListAdapter extends BaseAdapter {

    private Context context;
    private List<Wallpaper> wallpapers;
    private LayoutInflater inflater;

    public WallpapersListAdapter(Context context, List<Wallpaper> wallpapers) {
        this.context = context;
        this.wallpapers = wallpapers;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wallpapers.size();
    }

    @Override
    public Object getItem(int position) {
        return wallpapers.get(position);
    }

    public Wallpaper getWallpaper(int position) {
        return (Wallpaper) getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder holder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.wallpapers_list_item, parent, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.wallpapers_list_item_image);

            holder = new ViewHolder();
            holder.imageView = imageView;

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Wallpaper wallpaper = getWallpaper(position);

        Picasso
                .with(context)
                .load(wallpaper.getLink())
                .placeholder(R.drawable.ic_image)
                .into(holder.imageView);

        return view;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
