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

package at.mukprojects.vuniapp.universities.interfaces.base;

import java.io.IOException;

import at.mukprojects.vuniapp.models.UniversityUser;

/**
 * Das Interface LoginInterface wird von allen Universit&auml;ten implementiert,
 * welche einenen Login implementiert haben.
 * 
 * Das Interface ist nicht daf&uuml;r gedacht alleine implementiert zu werden,
 * sondern wird von anderen Interfaces verwendet.
 * 
 * @author Mathias
 */
public interface LoginInterface extends UserInterface {
    /**
     * Pr&uuml;ft, ob es sich um g&uuml;ltige Logindaten handelt.
     * 
     * @param user
     *            Der zu pr&uuml;fende User.
     * @return Es wird True zur&uuml;ckgeliefert, wenn der Test erfolgreich war
     *         und anderenfalls False.
     * @throws IOException
     *             Wird geworfen, falls beim Anmelden ein Fehler aufgetreten
     *             ist.
     */
    boolean checkLoginData(final UniversityUser user) throws IOException;
}
