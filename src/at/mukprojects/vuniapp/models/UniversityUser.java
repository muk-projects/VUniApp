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

package at.mukprojects.vuniapp.models;

import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.models.base.User;

/**
 * Die Klasse enth&auml;t alle Attribute, die einen User auszeichnen. Jeder User
 * besitzt einen Benutzernamen und ein Passwort und eine Universit&auuml;t,
 * sowie eine eindeutige ID. Weiters kann er einen userKey zur weiteren
 * Spezifikation besitzten.
 * 
 * @author Mathias
 */
public class UniversityUser extends User {
    private static final long  serialVersionUID  = 1L;

    private Long               id;
    private University         university;
    private String             userKey;

    /**
     * Diese Variable enth&auml;t einen Userkey, welcher verwendet wird falls
     * null &uuml;bergeben wird.
     */
    public static final String USERKEY_ONLY_USER = "onlyuser";

    /**
     * Erstellt einen neuen User.
     * 
     * @param username
     *            Name des Benutzers.
     * @param password
     *            Passwort des Benutzers.
     * @param university
     *            Universit&auml;t des Benutzers.
     * @param userKey
     *            UnterKey des Users, ist null falls es nur einen user f&uuml;r
     *            diese Universit&auml;t gibt.
     */
    public UniversityUser(final String username, final String password, final University university,
            final String userKey) {
        super(username, password);
        this.university = university;
        this.userKey = userKey;
    }

    /**
     * Liefert die ID des Benutzers zur&uuml;ck.
     * 
     * @return ID des Benutzers.
     */
    public final Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Benutzers.
     * 
     * @param id
     *            ID des Benutzers.
     */
    public final void setId(final Long id) {
        this.id = id;
    }

    /**
     * Liefert die Universit&auml;t des Users.
     * 
     * @return Universit&auml;t des Users.
     */
    public final University getUniversity() {
        return university;
    }

    /**
     * Liefert den UserKey.
     * 
     * @return Der UserKey.
     */
    public final String getUserKey() {
        return userKey;
    }

    /**
     * Setzt die Universit&auml;t des Users.
     * 
     * @param university
     *            Universit&auml;t des Users.
     */
    public final void setUniversity(final University university) {
        this.university = university;
    }

    /**
     * Setzt den UserKey.
     * 
     * @param userKey
     *            Der UserKey. the userKey to set
     */
    public final void setUserKey(final String userKey) {
        this.userKey = userKey;
    }

    /**
     * Gibt an, ob es sich um den einzigen User der Universit&auml;t handelt
     * oder nicht.
     * 
     * @return Boolean der angibt ob es sich um den einzigen user handelt.
     */
    public final boolean hasUserKey() {
        return userKey != null && !userKey.equals(USERKEY_ONLY_USER);
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((university == null) ? 0 : university.hashCode());
        result = prime * result + ((userKey == null) ? 0 : userKey.hashCode());
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
        UniversityUser other = (UniversityUser) obj;
        if (university == null) {
            if (other.university != null) {
                return false;
            }
        } else if (!university.equals(other.university)) {
            return false;
        }
        if (userKey == null) {
            if (other.userKey != null) {
                return false;
            }
        } else if (!userKey.equals(other.userKey)) {
            return false;
        }
        return true;
    }

    /**
     * Kopiert einen Benutzer.
     * 
     * @return Eine Kopie des Benutzers.
     */
    public final UniversityUser copy() {
        UniversityUser retUser = new UniversityUser(this.username,
                this.password, this.university,
                this.userKey);
        retUser.setId(this.id);
        return retUser;
    }
}
