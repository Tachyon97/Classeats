package com.sya.classeats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {
    private Context context;
    private ArrayList<Group> groups;
    private LayoutInflater inflater;

    public GroupAdapter(Context context, ArrayList<Group> groups) {
        super(context, -1, groups);
        this.context = context;
        this.groups = groups;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        final View items = inflater.inflate(R.layout.group_adapter, parent, false);
        final TextView name = items.findViewById(R.id.groupName);
        final TextView connected = items.findViewById(R.id.connected);
        name.setText(groups.get(position).getGroup_Name());
        return items;
    }


}
