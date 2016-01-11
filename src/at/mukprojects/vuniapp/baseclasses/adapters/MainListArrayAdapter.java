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

package at.mukprojects.vuniapp.baseclasses.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Die Klasse MainListArrayAdapter dient dazu eigene Projekt relevante Methoden
 * f&uuml;r einen Arrayadapter zu implementieren.
 * 
 * @author Mathias
 * @param <T>
 *            Typ der Listitems.
 */
public abstract class MainListArrayAdapter<T> extends ArrayAdapter<T> {

    /**
     * Erstellt einen neuen MainListArrayAdapter.
     * 
     * @param context
     *            Der Context.
     * @param textViewResourceId
     *            Die ResourceID welche inflatet werden soll.
     * @param list
     *            Die Liste an Items, welche angezeigt werden soll.
     */
    public MainListArrayAdapter(final Context context,
            final int textViewResourceId, final List<T> list) {
        super(context, textViewResourceId, list);
    }

    /**
     * Liefert eine Liste aller Items. Die Liste ist eine schwache Kopie. Die
     * Liste wird kopiert die Items jedoch nicht.
     * 
     * @return Liste der Items.
     */
    public final List<T> getItemList() {
        ArrayList<T> output = new ArrayList<T>();
        for (int i = 0; i < getCount(); i++) {
            output.add(getItem(i));
        }
        return output;
    }
}
