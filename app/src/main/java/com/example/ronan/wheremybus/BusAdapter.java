package com.example.ronan.wheremybus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Ronan on 22/12/2016.
 */

public class BusAdapter extends ArrayAdapter<BusData> {

    //constructor
    public BusAdapter(Context context, List<BusData> buses) {
        super(context, 0, buses);
    }

/**
 * Returns a list item view that displays information
 * about the due buses
 */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Check if there is an existing list item view ,  that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        BusData currentBus = getItem(position);

        if(currentBus.getRoute().equals("150")) {

            TextView routeVew = (TextView) listItemView.findViewById(R.id.route);
            TextView destination = (TextView) listItemView.findViewById(R.id.destination);
            TextView due = (TextView) listItemView.findViewById(R.id.dueTime);

            routeVew.setText(currentBus.getRoute());
            destination.setText(currentBus.getDestination());
            String dueTime = currentBus.getDuetime();
            if (dueTime.equals("Due")) {
                due.setText(currentBus.getDuetime());
            } else {
                due.setText(currentBus.getDuetime() + " Min");
            }

        }




        return listItemView;
    }
}
