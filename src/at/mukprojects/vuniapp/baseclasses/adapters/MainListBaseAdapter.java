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
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

//CHECKSTYLE OFF

/**
 * Die Basis Klasse dient dazu den Umgang mit ListView Adaptern zu vereinfachen.
 * 
 * @author Mathias
 * @param <T>
 *            Typ der Liste, welche in der ListView verwendet wird.
 */
public abstract class MainListBaseAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflator;
    protected ArrayList<T>   list = null;

    /**
     * Erzeugt einen neuen Adapter.
     * 
     * @param context
     *            Context welcher vom Adapter genutzt werden soll.
     * @param searchresult
     *            Ergebnisliste.
     */
    public MainListBaseAdapter(final Context context, final ArrayList<T> list) {
        inflator = LayoutInflater.from(context);
        this.list = list;
    }

    /**
     * Liefert eine Liste aller Items. Die Liste ist eine schwache Kopie. Die
     * Liste wird kopiert die Items jedoch nicht.
     * 
     * @return Liste der Items.
     */
    public List<T> getItemList() {
        ArrayList<T> output = new ArrayList<T>();
        for (T item : list) {
            output.add(item);
        }
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(final int position) {
        return list.get(position);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(final int position) {
        return position;
    }
}
