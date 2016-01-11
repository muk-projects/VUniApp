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
import java.util.Locale;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;

/**
 * Fragment zum Anzeigen eines Zeugnisses. Diesem Fragment wird das Zeugnis als
 * Certificate Objekt mitgegeben.
 * 
 * @author kerrim
 */
public class CertificateDetailsFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;

    private static final String TAG              = CertificateDetailsFragment.class
                                                         .getSimpleName();

    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_certificates_certificatedetails, container,
                false);
    }

    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();
        Bundle bundle = getArguments();
        Certificate certificate = (Certificate) bundle
                .getSerializable("certificate");
        CertificateInterface universityInterface = (CertificateInterface) bundle
                .getSerializable("university");

        TextView course = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_course);
        course.setText(certificate.getType() + " " + certificate.getTitle());

        TextView university = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_university);
        university.setText(certificate.getUniversity().getName());

        TextView coursenr = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_coursenr);
        coursenr.setText((certificate.getNr() != null) ? certificate.getNr()
                : "nicht bekannt");

        TextView stnr = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_stnr);
        stnr.setText(certificate.getStNr());

        TextView date = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        date.setText(sdf.format(certificate.getExamDate()));

        TextView professor = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_professor);
        professor.setText(certificate.getProfessor());

        TextView hours = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_hours);
        hours.setText(String.valueOf(certificate.getHours()));

        TextView ects = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_ects);
        ects.setText(String.valueOf(certificate.getEcts()));

        TextView grade = (TextView) getView().findViewById(
                R.id.fragment_certificates_certdetails_grade);
        grade.setText(universityInterface.getDisplayedNameToGrade(certificate
                .getGrade()));
        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

}
