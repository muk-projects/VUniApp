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

package at.mukprojects.vuniapp.activityfragmentcommunication;

import at.mukprojects.vuniapp.activityfragmentcommunication.request.Request;
import at.mukprojects.vuniapp.activityfragmentcommunication.response.Response;

/**
 * Das Interface gibt Fragments die M&ouml;glichkeit mit der Activity zu
 * kommunizieren.
 * 
 * @author Mathias
 */
public interface FragmentListener {

    /**
     * Die Methode verarbeitet einen Request und sendet einen Response.
     * 
     * @param request
     *            Der Request gibt an welche Aufgabe zu verf&uuml;llen ist.
     * @return Die Antwort der Anfrage, wenn es keine Antwort gibt so wird null
     *         zur&uuml;ckgeliefert. Es wird ebenso null zur&uuml;ckgegeben,
     *         wenn der Request nicht verarbeitet werden konnte.
     */
    Response invoke(Request request);
}
