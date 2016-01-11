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

import java.io.Serializable;
import java.util.HashMap;

/**
 * Dieses Interface wird von alles Universit&auml;ten implementiert, welche
 * einen Benutzer ben&ouml;tigen.
 * 
 * @author Mathias
 */
public interface UserInterface extends Serializable {
    /**
     * Diese Methode liefert eine Map mit allen UserKeys zur&uuml;ck. Die Map
     * besteht aus dem Key und einem Namen, welcher von der Benutzerverwaltung
     * angezeigt werden soll. Gibt es nur einen Benutzer f&uuml;r alle
     * Anwendungen der Universit&auml;t, dann wird eine leere Map
     * zur&uuml;ckgeliefert oder eine Map mit nur einem Eintrag, wenn
     * gew&uuml;nscht ist, dass dennoch eine Unterkategorie angezeigt wird.
     * 
     * Das Interface ist nicht daf&uuml;r gedacht alleine implementiert zu
     * werden, sondern wird von anderen Interfaces verwendet.
     * 
     * @return Map mit allen UserKeys.
     */
    HashMap<String, String> getUserKeys();
}
