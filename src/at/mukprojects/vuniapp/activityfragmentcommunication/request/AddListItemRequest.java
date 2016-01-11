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

package at.mukprojects.vuniapp.activityfragmentcommunication.request;

/**
 * Der ListItemRequest dient dazu, dass einem ListView ein Item hinzugef&uuml;gt
 * hast.
 * 
 * @author Mathias
 * 
 * @param <T>
 *            Gibt an, um welches ListObjekt es sich handelt.
 */
public class AddListItemRequest<T> implements Request {
    private static final long serialVersionUID = 1L;

    private T                 item;

    /**
     * Erstellt einen neuen Request.
     * 
     * @param item
     *            Item, welches einer Liste hinzugef&uuml;gt werden soll.
     */
    public AddListItemRequest(final T item) {
        this.item = item;
    }

    /**
     * Liefert das Item.
     * 
     * @return Das Item.
     */
    public final T getItem() {
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "AddListItemRequest";
    }
}
