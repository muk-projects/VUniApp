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

package at.mukprojects.vuniapp.activities.services;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.daemons.certificatenotifier.CertificateNotifier;

/**
 * Einstellungsactivity f&uuml;r das Zeugnisbenachrichtigungstool.
 * 
 * @author kerrim
 */
public class CertificateNotifierFragment extends VUniAppFragment {
    private static final long   serialVersionUID           = 1L;

    private static final String TAG                        = CertificateNotifierFragment.class
                                                                   .getSimpleName();

    private static final String PREF_FILE                  = "PrefFileCertificateNotifier";
    private static final String PREF_FILE_INTERVALL_HOUR   = "certificatenotifier_intervall_hour";
    private static final String PREF_FILE_INTERVALL_MINUTE = "certificatenotifier_intervall_minute";
    private static final String PREF_FILE_ONLYWLAN         = "certificatenotifier_onlywlan";

    private SharedPreferences   prefFile                   = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services_certificatenotifier,
                container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();

        prefFile = getActivity().getSharedPreferences(PREF_FILE, 0);

        int hour = prefFile.getInt(PREF_FILE_INTERVALL_HOUR, 0);
        int minute = prefFile.getInt(PREF_FILE_INTERVALL_MINUTE, 0);
        boolean onlywlan = prefFile.getBoolean(PREF_FILE_ONLYWLAN, true);

        TimePicker timePicker = (TimePicker) getView().findViewById(
                R.id.fragment_services_certificatenotifier_checkingintervall);
        timePicker.setIs24HourView(true);

        setSettings(hour, minute, onlywlan);

        ((Button) getView().findViewById(
                R.id.fragment_services_certificatenotifier_setting_daily))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSettings(0, 0, true);

                    }
                });

        ((Button) getView().findViewById(
                R.id.fragment_services_certificatenotifier_setting_important))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSettings(1, 0, false);

                    }
                });

        ((Button) getView().findViewById(
                R.id.fragment_services_certificatenotifier_setting_hardcore))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSettings(0, 5, false);

                    }
                });
        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public final void onPause() {
        super.onPause();
        saveSettings();
        ServiceControl certificateNotifier = new ServiceControl(getResources()
                .getString(R.string.activity_services_certificatenotifier),
                null, CertificateNotifier.class,
                (VUniAppActivity) getActivity());
        if (certificateNotifier.isRunning()) {
            certificateNotifier.restart();
        }
    }

    /**
     * Setzt die Einstellungselemente f&uuml;r die Zeugnisbenachrichtigungen.
     * 
     * @param hour
     *            Stunden des Abfrageintervalls.
     * @param minute
     *            Minuten des Abfrageintervalls.
     * @param onlywlan
     *            Falls true werden die Zeugnisse nur abgefragt falls man sich
     *            im WLAN befindet, sonst werden sie immer falls eine
     *            Internetverbindung vorhanden ist abgefragt.
     */
    private void setSettings(final int hour, final int minute,
            final boolean onlywlan) {
        Log.i(TAG, "Methode: setSettings wird gestartet.");

        TimePicker checkingintervall = (TimePicker) getView().findViewById(
                R.id.fragment_services_certificatenotifier_checkingintervall);
        checkingintervall.setCurrentHour(hour);
        checkingintervall.setCurrentMinute(minute);
        CheckBox checkBox = (CheckBox) getView().findViewById(
                R.id.fragment_services_certificatenotifier_onlywlan);
        checkBox.setChecked(onlywlan);

        Log.i(TAG, "Methode: setSettings wird verlassen.");
    }

    /**
     * Speichert die gesetzten Einstellungen.
     */
    private void saveSettings() {
        Log.i(TAG, "Methode: saveSettings wird gestartet.");

        TimePicker checkingintervall = (TimePicker) getView().findViewById(
                R.id.fragment_services_certificatenotifier_checkingintervall);
        CheckBox checkBox = (CheckBox) getView().findViewById(
                R.id.fragment_services_certificatenotifier_onlywlan);

        SharedPreferences.Editor prefs = prefFile.edit();

        prefs.putInt(PREF_FILE_INTERVALL_HOUR, (checkingintervall
                .getCurrentHour() != 0 ? checkingintervall.getCurrentHour()
                : 24));
        prefs.putInt(PREF_FILE_INTERVALL_MINUTE,
                checkingintervall.getCurrentMinute());
        prefs.putBoolean(PREF_FILE_ONLYWLAN, checkBox.isChecked());

        prefs.apply();

        Log.i(TAG, "Methode: saveSettings wird verlassen.");
    }
}
