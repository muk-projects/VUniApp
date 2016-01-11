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

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivityWithFragment;
import at.mukprojects.vuniapp.baseclasses.adapters.MainListBaseAdapter;
import at.mukprojects.vuniapp.daemons.certificatenotifier.CertificateNotifier;

/**
 * Activity zum Aktivieren von hilfreichen Werkzeugen.
 * 
 * @author kerrim
 */
public class ServicesActivity extends VUniAppActivityWithFragment {
    private static final String TAG       = ServicesActivity.class
                                                  .getSimpleName();
    private static final String PREF_FILE = "PrefFileTools";

    @SuppressWarnings("unused")
    private SharedPreferences   prefFile  = null;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");

        setContentView(R.layout.activity_services);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_services));
        super.setCurrentActivity(this);
        super.onCreate(savedInstanceState, ServicesActivity.class);
        prefFile = getSharedPreferences(PREF_FILE, 0);

        ListView serviceList = (ListView) findViewById(R.id.activity_services_servicelist);

        ArrayList<ServiceControl> services = new ArrayList<ServiceControl>();
        services.add(new ServiceControl(getResources().getString(
                R.string.activity_services_certificatenotifier),
                new CertificateNotifierFragment(), CertificateNotifier.class,
                this));

        final ServiceListAdapter serviceListAdapter = new ServiceListAdapter(
                super.getCurrentActivity(), services);

        serviceList.setAdapter(serviceListAdapter);

        serviceList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                ServiceControl service = (ServiceControl) serviceListAdapter
                        .getItem(position);

                loadFragment(service.getSettingsFragment());
            }
        });

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /**
     * Adapter zum Anzeigen der Daemons in einer ListView.
     * 
     * @author kerrim
     */
    class ServiceListAdapter extends MainListBaseAdapter<ServiceControl> {

        /**
         * Erzeugt einen neuen Adapter.
         * 
         * @param context
         *            Context welcher vom Adapter genutzt werden soll.
         * @param list
         *            Liste aller Daemons.
         */
        public ServiceListAdapter(final Context context,
                final ArrayList<ServiceControl> list) {
            super(context, list);
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(final int position, final View cView,
                final ViewGroup parent) {
            final ViewHolder holder;
            View convertView = cView;

            convertView = inflator.inflate(
                    R.layout.adapter_services_servicelist, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.adapter_service_servicelist_toolname);
            holder.status = (ToggleButton) convertView
                    .findViewById(R.id.adapter_services_servicelist_activate);

            convertView.setTag(holder);

            final ServiceControl service = (ServiceControl) getItem(position);

            holder.name.setText(service.getName());
            holder.status.setChecked(service.isRunning());

            holder.status
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(final CompoundButton buttonView,
                                final boolean isChecked) {
                            if (isChecked) {
                                service.start();
                            } else {
                                service.end();
                            }
                        }

                    });

            return convertView;
        }
    }

    /**
     * ViewHolder f&uuml;r die anzuzeigenden F&auml;cher.
     * 
     * @author kerrim
     */
    static class ViewHolder {
        private TextView     name;
        private ToggleButton status;
    }
}
