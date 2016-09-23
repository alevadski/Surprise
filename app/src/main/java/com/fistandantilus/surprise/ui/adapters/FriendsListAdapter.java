package com.fistandantilus.surprise.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fistandantilus.surprise.R;
import com.fistandantilus.surprise.dao.UserData;

import java.util.List;

public class FriendsListAdapter extends BaseAdapter {

    private Context context;
    private List<UserData> friends;
    private LayoutInflater inflater;

    public FriendsListAdapter(Context context, List<UserData> friends) {
        this.context = context;
        this.friends = friends;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    public UserData getFriendData(int position) {
        return (UserData) getItem(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.friends_list_item, parent, false);
            holder = new ViewHolder();

            holder.nameView = (TextView) view.findViewById(R.id.friends_list_item_name);
            holder.emailView = (TextView) view.findViewById(R.id.friends_list_item_email);
            holder.onlineView = (ImageView) view.findViewById(R.id.friends_list_item_online);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        UserData friendData = getFriendData(position);

        final ViewHolder finalHolder = holder;

        finalHolder.nameView.setText(friendData.getName());
        finalHolder.emailView.setText(friendData.getEmail());
        finalHolder.onlineView.setVisibility(friendData.isOnline() ? View.VISIBLE : View.GONE);

        return view;
    }

    class ViewHolder {
        TextView nameView;
        TextView emailView;
        ImageView onlineView;
    }

}
