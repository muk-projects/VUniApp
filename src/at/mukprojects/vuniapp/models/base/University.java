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

package at.mukprojects.vuniapp.models.base;

import java.io.Serializable;

/**
 * Die Klasse University enth&auml;t alle Attribute einer Universit&auml;t.
 * 
 * @author Mathias
 * @author kerrim
 */
// CHECKSTYLE OFF
public abstract class University implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            name;
    private String            keyName;

    /**
     * Erstellt eine University.
     * 
     * @param name
     *            Name der Universit&auml;t. Der verwendete Name einer
     *            Universit&auuml;t muss einzigartig sein.
     * @param keyName
     *            KeyName der Universit&auml;t. Dieser wird vom System verwendet
     *            und darf daher keine Umlaute oder Sonderzeichen enthalten.
     *            Wichtig ist auch, dass der verwendete keyName einzigartig ist
     *            und somit nur f&uuml;r eine Universit&auml;t verwendet wird.
     */
    protected University(final String name, final String keyName) {
        this.name = name;
        this.keyName = keyName;
    }

    /**
     * Liefert den Namen der Universit&auml;t.
     * 
     * @return Namen der Universit&auml;t.
     */
    public final String getName() {
        return name;
    }

    /**
     * Liefert den KeyNamen der Universit&auml;t.
     * 
     * @return KeyNamen der Universit&auml;t.
     */
    public final String getKeyName() {
        return keyName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        University other = (University) obj;
        if (keyName == null) {
            if (other.keyName != null) {
                return false;
            }
        } else if (!keyName.equals(other.keyName)) {
            return false;
        }
        return true;
    }
}
