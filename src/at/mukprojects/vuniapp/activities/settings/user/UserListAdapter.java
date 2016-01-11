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

package at.mukprojects.vuniapp.activities.settings.user;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListArrayAdapter;
import at.mukprojects.vuniapp.models.UniversityUser;

/**
 * Der UserListAdapter wird verwendet um User in einer ListView anzuzeigen.
 * 
 * @author Mathias
 */
public class UserListAdapter extends MainListArrayAdapter<UniversityUser> {

    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Ergebnisliste der UniversityUsers.
     */
    public UserListAdapter(final Context context,
            final List<UniversityUser> list) {
        super(context, R.layout.adapter_user_userlist, list);
    }

    @Override
    public final View getView(final int position, final View cView,
            final ViewGroup parent) {
        final ViewHolder holder;
        View convertView = cView;

        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.adapter_user_userlist,
                    parent, false);

            holder = new ViewHolder();
            holder.university = (TextView) convertView
                    .findViewById(R.id.adapter_user_university);
            holder.user = (TextView) convertView
                    .findViewById(R.id.adapter_user_user);
            holder.password = (TextView) convertView
                    .findViewById(R.id.adapter_user_password);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UniversityUser universityUser = (UniversityUser) getItem(position);
        String universityText = universityUser.getUniversity().getName();
        if (universityUser.hasUserKey()) {
            // TODO M: Key ist sinnvollen Namen umwandeln und in 2 Zeilen
            // schreiben
            universityText += " (" + universityUser.getUserKey() + ")";
        }
        holder.university.setText(universityText);
        holder.user.setText(universityUser.getUsername());
        holder.password.setText(universityUser.getPassword());
        return convertView;
    }

    /**
     * Viewholder f&uuml;r die Daten in der Ergebnisliste.
     * 
     * @author Mathias
     */
    static class ViewHolder {
        private TextView university;
        private TextView user;
        private TextView password;
    }
}
