package at.sw2017.pocketdiary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import at.sw2017.pocketdiary.FriendItem;

public class FriendListAdapter extends ArrayAdapter<FriendItem> {

    Context context;
    int layoutResourceId;
    FriendItem data[] = null;

    public FriendListAdapter(Context context, int layoutResourceId, FriendItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriendHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FriendHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (FriendHolder)row.getTag();
        }

        FriendItem friendItem = data[position];
        holder.txtTitle.setText(friendItem.title);

        return row;
    }

    static class FriendHolder
    {
        TextView txtTitle;
    }
}