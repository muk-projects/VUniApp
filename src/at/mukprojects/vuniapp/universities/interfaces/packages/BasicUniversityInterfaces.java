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

package at.mukprojects.vuniapp.universities.interfaces.packages;

import at.mukprojects.vuniapp.universities.interfaces.CanteenInterface;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;
import at.mukprojects.vuniapp.universities.interfaces.DatesInterface;
import at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface;
import at.mukprojects.vuniapp.universities.interfaces.RoomInterface;
import at.mukprojects.vuniapp.universities.interfaces.SubjectInterface;

/**
 * Zusammenfassung der Basis- Universit&auml;ts Funktionen.
 * 
 * @author Mathias
 */
public interface BasicUniversityInterfaces extends CanteenInterface,
        CertificateInterface, ProfessorInterface, RoomInterface,
        SubjectInterface, DatesInterface {
}
