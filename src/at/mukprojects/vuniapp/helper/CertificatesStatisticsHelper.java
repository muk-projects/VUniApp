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

package at.mukprojects.vuniapp.helper;

import java.util.ArrayList;

import at.mukprojects.vuniapp.activities.certificates.CertificateEntry;

import com.jjoe64.graphview.GraphView.GraphViewData;

/**
 * Klasse mit Methoden zur statistischen Auswertung von Zeugnissen.
 * 
 * @author kerrim
 */
public class CertificatesStatisticsHelper {

    private ArrayList<CertificateEntry> certificates;

    /**
     * Erzeugt eine neue CertificatesStatisticsHelper Klasse, welche zum
     * statistischen Auswerten von Zeugnissen genutzt werden kann.
     * 
     * @param certificates
     *            Liste der auszuwertenden Zeugnissen. Diese muss zuvor nach
     *            Datum sortiert werden.
     */
    public CertificatesStatisticsHelper(
            final ArrayList<CertificateEntry> certificates) {
        super();
        this.certificates = certificates;
    }

    /**
     * Errechnet das arithmetische Mittel der &uuml;bergebenen Liste.
     * Ung&uuml;ltige Zeugnisse werden nicht beachtet.
     * 
     * @param weightedByECTS
     *            Falls auf true gesetzt, wird das arithmetische Mittel nach
     *            ECTS Punkten gewichtet ausgegeben.
     * @return Arithmetisches Mittel der &uuml;bergebenen Zeugnissen.
     */
    public final float calcAverage(final boolean weightedByECTS) {
        float sumWeight = 0F;
        float sumGradeMulEcts = 0F;
        for (CertificateEntry cert : certificates) {
            float weight = weightedByECTS ? cert.getKey().getEcts() : 1;
            if (cert.getKey().getActive()
                    && cert.getValue()
                            .getValueToGrade(cert.getKey().getGrade()) != null) {
                sumGradeMulEcts += (weight * (float) cert.getValue()
                        .getValueToGrade(cert.getKey().getGrade()));
                sumWeight += weight;
            }
        }
        return (sumGradeMulEcts / sumWeight);
    }

    /**
     * Diese Methode liefert eine Liste von zweidimensionalen Daten mit Datum
     * des Zeugniserhaltes und der Note zur&uuml;ck. Diese Daten sind zum
     * Zeichnen von Graphen gedacht.
     * 
     * @return Array mit zweidimensionalen Daten mit Datum des Zeugniserhaltes
     *         und der Note
     */
    public final GraphViewData[] createGraphViewData() {
        ArrayList<GraphViewData> data = new ArrayList<GraphViewData>();

        for (CertificateEntry cert : certificates) {
            if (cert.getValue().getValueToGrade(cert.getKey().getGrade()) != null) {
                data.add(new GraphViewData(cert.getKey().getExamDate()
                        .getTime(), cert.getValue().getValueToGrade(
                        cert.getKey().getGrade())));
            }
        }

        GraphViewData[] output = new GraphViewData[data.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = data.get(i);
        }

        return output;
    }
}
