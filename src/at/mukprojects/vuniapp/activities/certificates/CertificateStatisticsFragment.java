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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.helper.CertificatesStatisticsHelper;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

/**
 * Dieses Fragment zeigt eine Statistik f&uuml;r im Bundle unter certificates
 * &uuml;bergebene Zeugnisse an.
 * 
 * @author kerrim
 */
public class CertificateStatisticsFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;
    private static final String TAG              = CertificateStatisticsFragment.class
                                                         .getSimpleName();

    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_certificates_certificatestatistics,
                container, false);
    }

    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();

        Bundle bundle = getArguments();
        @SuppressWarnings("unchecked")
        ArrayList<CertificateEntry> certificates = (ArrayList<CertificateEntry>) bundle
                .getSerializable("certificates");
        Collections.sort(certificates);
        
        CertificatesStatisticsHelper csh = new CertificatesStatisticsHelper(
                certificates);

        TextView avgByEctsTextView = (TextView) getActivity().findViewById(
                R.id.fragment_certificates_certstatistics_avgects);
        avgByEctsTextView
                .setText(convertFloatToString(csh.calcAverage(true), 2));

        TextView avgUnweightedTextView = (TextView) getActivity().findViewById(
                R.id.fragment_certificates_certstatistics_avgunweighted);
        avgUnweightedTextView.setText(convertFloatToString(
                csh.calcAverage(false), 2));

        GraphViewSeries graphViewSeries = new GraphViewSeries(
                csh.createGraphViewData());
        GraphView graphView = new BarGraphView(getActivity(), "");
        graphView.addSeries(graphViewSeries);
        graphView.getGraphViewStyle().setNumVerticalLabels(6);
        graphView.getGraphViewStyle().setNumHorizontalLabels(3);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView.setManualYAxisBounds(5, 0);
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(final double value, final boolean isValueX) {
                if (isValueX) {
                    Date date = new Date((long) value);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy",
                            Locale.getDefault());
                    return sdf.format(date);
                }
                return null;
            }
        });

        LinearLayout graph = (LinearLayout) getActivity().findViewById(
                R.id.fragment_certificates_certstatistics_graph);
        graph.addView(graphView);

        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

    /**
     * Erzeugt aus einer Fließkommazahl einen String mit einer vorgegebenen
     * Genauigkeit.
     * 
     * @param input
     *            Fließkommazahl zum Umwandeln in einen String.
     * @param precision
     *            Genauigkeit mit der die Zahl angegeben werden soll.
     * @return String mit der Fließkommazahl in der angegebenen Genauigkeit.
     */
    private String convertFloatToString(final float input, final int precision) {
        float precMultiplier = (float) Math.pow(10, precision);
        int result = Math.round(input * precMultiplier);
        if (result == 0) {
            return "0,00";
        }
        String output = "" + result;
        output = output.substring(0, output.length() - precision)
                + ","
                + output.substring(output.length() - precision, output.length());
        return output;

    }
}
