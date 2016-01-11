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

package at.mukprojects.vuniapp.activities.subjects;

import java.util.Calendar;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.models.Subject;

/**
 * Fragment zum Anzeigen eines Faches. Dieses bekommt ein Subject Objekt und
 * zeigt dieses an.
 * 
 * @author kerrim
 */
public class SubjectAddFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;

    private static final String TAG              = SubjectAddFragment.class
                                                         .getSimpleName();

    private Subject             subject;

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
        return inflater.inflate(R.layout.fragment_subjects_add, container,
                false);
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
        Bundle bundle = getArguments();
        subject = (Subject) bundle.getSerializable(Subject.SUBJECT);
        
        // TODO K: Spinner beladen
        
        // TODO K: subject zum Beschreiben der Textfelder nutzen

        Button save = (Button) getView().findViewById(
                R.id.fragment_subjects_add_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (subject == null) {
                    subject = new Subject("CUSTOMSUBJECT-"
                            + Calendar.getInstance().getTimeInMillis() + "-"
                            + (100000 + (int) (Math.random() * 100000)));
                }
                
                // TODO K: Sachen aus den Textfeldern in subject schreiben
                
                // TODO K: Service zum Speichern von Subject verwenden.
                
                // TODO K: Was passiert nach dem Speichern

            }

        });

        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

}
