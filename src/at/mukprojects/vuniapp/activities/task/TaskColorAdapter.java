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

package at.mukprojects.vuniapp.activities.task;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListArrayAdapter;
import at.mukprojects.vuniapp.enumeration.TaskColor;

/**
 * Der ColorAdapter wird verwendet um Farben in einem Spinner anzuzeigen.
 * 
 * @author Mathias
 */
public class TaskColorAdapter extends MainListArrayAdapter<TaskColor> {

    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Liste an Farben.
     */
    public TaskColorAdapter(final Context context, final List<TaskColor> list) {
        super(context, R.layout.adapter_task_colorlist, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public final View getDropDownView(final int position,
            final View convertView, final ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View convertView,
            final ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Diese Methode erzeigt den individuellen View.
     */
    public final View getCustomView(final int position, final View cView,
            final ViewGroup parent) {
        final ViewHolder holder;
        View convertView = cView;

        LayoutInflater inflator = LayoutInflater.from(getContext());
        convertView = inflator.inflate(R.layout.adapter_task_colorlist, parent,
                false);

        holder = new ViewHolder();
        holder.color = convertView.findViewById(R.id.adpater_task_colorList);

        TaskColor taskColor = getItem(position);
        holder.color.setBackgroundColor(Color.parseColor(taskColor
                .displayValue()));

        return convertView;
    }

    /**
     * Viewholder f&uuml;r die Daten in der Ergebnisliste.
     * 
     * @author Mathias
     */
    static class ViewHolder {
        private View color;
    }
}
