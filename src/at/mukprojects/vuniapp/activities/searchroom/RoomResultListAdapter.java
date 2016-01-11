/**
 * Copyright (C) 2013 by
 * Mathias Markl and Kerrim Abd El Hamed
 *
 * This program is free software!
 * You are allowed to redistribute it and/or modify it
 * under the terms of the GNU General Public License, version 2.
 * For details of the GNU General Public License see
 * http://www.gnu.org/licenses/gpl-2.0.html
 */

package at.mukprojects.vuniapp.activities.searchroom;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.models.Room;

/**
 * Von VUniAppListBaseAdapter erbender Adapter der die Ergebnisliste der Suche
 * verwaltet.
 * 
 * @author Mathias
 */
class RoomResultListAdapter extends MainListBaseAdapter<Room> {
    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Ergebnisliste der R&auml;me.
     */
    public RoomResultListAdapter(final Context context, final ArrayList<Room> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, final View cView,
            final ViewGroup parent) {
        final ViewHolder holder;
        View convertView = cView;

        convertView = inflator.inflate(R.layout.adapter_searchroom_resultlist,
                parent, false);

        holder = new ViewHolder();
        holder.roomName = (TextView) convertView
                .findViewById(R.id.adapter_searchroom_roomName);
        holder.address = (TextView) convertView
                .findViewById(R.id.adapter_searchroom_address);
        holder.info = (TextView) convertView
                .findViewById(R.id.adapter_searchroom_info);

        convertView.setTag(holder);

        Room insertRoom = (Room) getItem(position);
        holder.roomName.setText(insertRoom.getRoomName());
        if (insertRoom.getAddress() != null) {
            holder.address.setText(insertRoom.getAddress());
        }
        if (insertRoom.getDetailInfo() != null) {
            holder.info.setText(insertRoom.getDetailInfo());
        }
        return convertView;
    }

    /**
     * Viewholder f&uuml;r die Daten in der Ergebnisliste.
     * 
     * @author Mathias
     */
    static class ViewHolder {
        private TextView roomName;
        private TextView address;
        private TextView info;
    }
}
