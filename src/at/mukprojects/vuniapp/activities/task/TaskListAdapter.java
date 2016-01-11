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

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListArrayAdapter;
import at.mukprojects.vuniapp.models.Task;

/**
 * Der TaskListAdapter wird verwendet um Tasks in einer ListView anzuzeigen.
 * 
 * @author Mathias
 */
public class TaskListAdapter extends MainListArrayAdapter<Task> {
    private Context usedContex;

    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Ergebnisliste der Tasks.
     */
    public TaskListAdapter(final Context context, final List<Task> list) {
        super(context, R.layout.adapter_task_tasklist, list);
        usedContex = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View cView,
            final ViewGroup parent) {
        final ViewHolder holder;
        View convertView = cView;

        LayoutInflater inflator = LayoutInflater.from(getContext());
        convertView = inflator.inflate(R.layout.adapter_task_tasklist, parent,
                false);

        holder = new ViewHolder();
        holder.title = (TextView) convertView
                .findViewById(R.id.adapter_task_title);
        holder.description = (TextView) convertView
                .findViewById(R.id.adapter_task_description);
        holder.date = (TextView) convertView
                .findViewById(R.id.adapter_task_date);
        holder.color = convertView.findViewById(R.id.adapter_task_color);
        holder.image = (ImageView) convertView
                .findViewById(R.id.adapter_task_image);
        convertView.setTag(holder);

        Task task = getItem(position);
        holder.title.setText(task.getTitle());
        if (task.getDescription().equals("")) {
            holder.description.setText(usedContex
                    .getString(R.string.activity_task_noDescriptionText));
        } else {
            holder.description.setText(task.getDescription());
        }
        if (task.getColor() != null) {
            holder.color.setVisibility(View.VISIBLE);
            holder.color.setBackgroundColor(Color.parseColor(task.getColor()));
        } else {
            holder.color.setVisibility(View.INVISIBLE);
        }
        if (task.getDeadlineDate() != null) {
            holder.date.setText((new Date(task.getDeadlineDate()).toString()));
        } else {
            holder.date.setText(usedContex
                    .getString(R.string.activity_task_noDateText));
        }
        if (task.getImagePath() != null) {
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    /**
     * Viewholder f&uuml;r die Daten in der Ergebnisliste.
     * 
     * @author Mathias
     */
    static class ViewHolder {
        private TextView  title;
        private TextView  description;
        private TextView  date;
        private View      color;
        private ImageView image;
    }
}
