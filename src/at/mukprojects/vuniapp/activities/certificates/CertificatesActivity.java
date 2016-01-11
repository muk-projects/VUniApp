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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivityWithFragment;
import at.mukprojects.vuniapp.dao.CertificateDAOCache;

/**
 * Activity zum Anzeigen aller verf&uuml;gbaren Zeugnisse.
 * 
 * @author kerrim
 */
public class CertificatesActivity extends VUniAppActivityWithFragment {
    private static final String    TAG          = CertificatesActivity.class
                                                        .getSimpleName();
    private static final String    PREF_FILE    = "PrefFileCertificates";

    @SuppressWarnings("unused")
    private SharedPreferences      prefFile     = null;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        setContentView(R.layout.activity_certificates);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_certificates));
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);
        super.onCreate(savedInstanceState, CertificatesActivity.class);
        prefFile = getSharedPreferences(PREF_FILE, 0);
        super.setActionbarBackground(R.drawable.actionbar_background_blue);
        
        (new GetCertificatesTask(this, getResources().getString(
                R.string.activity_certificates), getResources().getString(
                R.string.activity_certificates_loading))).execute();

        ImageButton actionbarSyncButton = (ImageButton) findViewById(R.id.actionbar_sync);
        actionbarSyncButton.setVisibility(ImageButton.VISIBLE);
        actionbarSyncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                CertificateDAOCache.removeCertificates();
                (new GetCertificatesTask((CertificatesActivity) getCurrentActivity(), getResources().getString(
                        R.string.activity_certificates), getResources().getString(
                        R.string.activity_certificates_loading))).execute();
            }
        });

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

}
