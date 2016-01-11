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

package at.mukprojects.vuniapp.activities.searchprofessor;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.models.Professor;

/**
 * Von BaseAdapter erbender Adapter der die Ergebnisliste der Suche verwaltet.
 * 
 * @author Mathias
 */
class ProfessorResultListAdapter extends MainListBaseAdapter<Professor> {
    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param list
     *            Ergebnisliste der Professoren.
     */
    public ProfessorResultListAdapter(final Context context,
            final ArrayList<Professor> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, final View convertView,
            final ViewGroup parent) {
        final ViewHolder holder;

        View tConvertView = inflator.inflate(
                R.layout.adapter_searchprofessor_resultlist, parent, false);

        holder = new ViewHolder();
        holder.professor = (TextView) tConvertView
                .findViewById(R.id.adapter_searchprofessor_resultlist_text);

        tConvertView.setTag(holder);

        holder.professor.setText(((Professor) getItem(position)).getName());

        return tConvertView;
    }

    /**
     * Viewholder f&uuml;r die Daten in der Ergebnisliste.
     * 
     * @author Mathias
     */
    static class ViewHolder {
        private TextView professor;
    }
}
