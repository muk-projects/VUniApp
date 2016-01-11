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
 * Basisklasse aller User Models.
 * 
 * @author Mathias
 */
// CHECKSTYLE OFF
public abstract class User implements Serializable {
    private static final long  serialVersionUID = 1L;
    public static final String USER_EXTRA       = "user";

    protected String           username;
    protected String           password;

    /**
     * Erstellt einen User.
     * 
     * @param username
     *            Name des Benutzers.
     * @param password
     *            Passwort des Benutzers.
     */
    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Liefert den Benutzernamen des Benutzers zur&uuml;ck.
     * 
     * @return Name des Benutzers.
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Setzt den Namen des Benutzers.
     * 
     * @param username
     *            Name des Benutzers.
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Liefert das Passwort des Benutzers zur&uuml;ck.
     * 
     * @return Das Passwort des Benutzers.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort des Benutzers.
     * 
     * @param password
     *            Das Passwort des Benutzers.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }
}
