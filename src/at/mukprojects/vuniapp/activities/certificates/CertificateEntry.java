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

package at.mukprojects.vuniapp.activities.certificates;

import java.io.Serializable;
import java.util.Map.Entry;

import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;

/**
 * Objekt, welches ein Zeugnis mit der jeweiligen Universit&auml;t koppelt.
 * 
 * @author Mathias
 */
public class CertificateEntry implements
        Entry<Certificate, CertificateInterface>, Serializable,
        Comparable<CertificateEntry> {
    private static final long    serialVersionUID = 1L;
    private Certificate          certificate;
    private CertificateInterface certificateInterface;

    /**
     * Erstellt einen neuen Entry.
     * 
     * @param certificate
     *            Das Zeugnis.
     * @param certificateInterface
     *            Die Universit&auml;t.
     */
    public CertificateEntry(final Certificate certificate,
            final CertificateInterface certificateInterface) {
        this.certificate = certificate;
        this.certificateInterface = certificateInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map.Entry#setValue(java.lang.Object)
     */
    @Override
    public final CertificateInterface setValue(final CertificateInterface object) {
        certificateInterface = object;
        return certificateInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map.Entry#getValue()
     */
    @Override
    public final CertificateInterface getValue() {
        return certificateInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map.Entry#getKey()
     */
    @Override
    public final Certificate getKey() {
        return certificate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo(final CertificateEntry another) {
        return certificate.compareTo(another.getKey());
    }
}
