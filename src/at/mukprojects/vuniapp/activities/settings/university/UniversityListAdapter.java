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

package at.mukprojects.vuniapp.activities.settings.university;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.models.Room;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.UniversitySettings;

/**
 * Von VUniAppListBaseAdapter erbender Adapter der die Liste an
 * Universit&auml;ten verwaltet.
 * 
 * @author Mathias
 */
public class UniversityListAdapter extends MainListBaseAdapter<University> {
    private SharedPreferences preferences;

    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Liste an Universit&auml;ten.
     * @param preferences
     *            Preference in welcher alle aktiven Universit&auml;ten
     *            gespeichert sind.
     */
    public UniversityListAdapter(final Context context,
            final ArrayList<University> list,
            final SharedPreferences preferences) {
        super(context, list);
        this.preferences = preferences;
    }

    @Override
    public final View getView(final int position, final View cView,
            final ViewGroup parent) {
        final ViewHolder holder;
        View convertView = cView;

        convertView = inflator.inflate(
                R.layout.adapter_universitysettings_universitylist, parent,
                false);

        holder = new ViewHolder();
        holder.text = (TextView) convertView
                .findViewById(R.id.adapter_universitysettings_text);
        holder.checkBox = (CheckBox) convertView
                .findViewById(R.id.adapter_universitysettings_checkbox);

        convertView.setTag(holder);

        final University university = (University) getItem(position);
        holder.text.setText(university.getName());
        holder.checkBox.setChecked(preferences.getBoolean(
                university.getKeyName(), false));
        holder.checkBox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            final CompoundButton buttonView,
                            final boolean isChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        UniversitySettings settings = null;
                        try {
                            settings = UniversitySettings.getInstance();
                        } catch (NotInitializedException e) {
                            settings = UniversitySettings
                                    .initialize(preferences);
                        }
                        if (isChecked) {
                            editor.putBoolean(university.getKeyName(), true);
                            editor.commit();
                            settings.addUniversityToActiv(university);
                        } else {
                            editor.remove(university.getKeyName());
                            editor.commit();
                            settings.removeUniversityFromActiv(university);
                        }
                    }
                });

        return convertView;
    }

    /**
     * Viewholder f&uuml;r die Daten in der Ergebnisliste.
     * 
     * @author Mathias
     */
    static class ViewHolder {
        private TextView text;
        private CheckBox checkBox;
    }
}
