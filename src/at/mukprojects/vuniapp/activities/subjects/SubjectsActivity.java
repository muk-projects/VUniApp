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

import java.io.IOException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivityWithFragment;
import at.mukprojects.vuniapp.dao.SubjectDAOCache;
import at.mukprojects.vuniapp.dao.SubjectDAOFilterDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.services.SubjectService;
import at.mukprojects.vuniapp.services.SubjectServiceStd;

/**
 * Activity zum Anzeigen der abonnierten Lehrveranstaltungen.
 * 
 * @author kerrim
 * @author Mathias
 */
public class SubjectsActivity extends VUniAppActivityWithFragment {
    private static final String TAG       = SubjectsActivity.class
                                                  .getSimpleName();
    private static final String PREF_FILE = "PrefFileSubjects";

    @SuppressWarnings("unused")
    private SharedPreferences   prefFile  = null;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        setContentView(R.layout.activity_subjects);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_subjects));
        super.setCurrentActivity(this);
        super.onCreate(savedInstanceState, SubjectsActivity.class);
        prefFile = getSharedPreferences(PREF_FILE, 0);
        super.setActionbarBackground(R.drawable.actionbar_background_blue);

        (new GetSubjectsTask(this, getResources().getString(
                R.string.activity_subjects), getResources().getString(
                R.string.activity_subjects_loading))).execute();

        ImageButton actionbarSyncButton = (ImageButton) findViewById(R.id.actionbar_sync);
        actionbarSyncButton.setVisibility(ImageButton.VISIBLE);
        actionbarSyncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                SubjectDAOCache.removeSubjects();
                (new GetSubjectsTask((SubjectsActivity) getCurrentActivity(),
                        getResources().getString(R.string.activity_subjects),
                        getResources().getString(
                                R.string.activity_subjects_loading))).execute();
            }
        });

        ListView subjectsList = (ListView) findViewById(R.id.activity_subjects_subjectlist);
        registerForContextMenu(subjectsList);

        Button addSubjectButton = (Button) findViewById(R.id.activity_subjects_add);
        addSubjectButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                SubjectAddFragment fragment = new SubjectAddFragment();
                loadFragment(fragment, fragment.getClass().getName(),
                        new Bundle(), R.drawable.actionbar_background_blue);

            }
        });

        Button filteredSubjectsButton = (Button) findViewById(R.id.activity_subjects_filtered);
        filteredSubjectsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                SubjectFilterFragment fragment = new SubjectFilterFragment();
                loadFragment(fragment, fragment.getClass().getName(),
                        new Bundle(), R.drawable.actionbar_background_blue);
            }
        });

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu
     * , android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public final void onCreateContextMenu(final ContextMenu menu, final View v,
            final ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(getString(R.string.activity_subjects_contextmenu_remove));
        menu.add(getString(R.string.activity_subjects_contextmenu_edit));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem
     * )
     */
    @Override
    public final boolean onContextItemSelected(final MenuItem item) {
        ListView subjectsList = (ListView) findViewById(R.id.activity_subjects_subjectlist);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        int index = info.position;
        SubjectAdapter subjectAdapter = (SubjectAdapter) subjectsList
                .getAdapter();
        Subject subject = (Subject) subjectAdapter.getItem(index);
        if (item.getTitle().equals(
                getString(R.string.activity_subjects_contextmenu_remove))) {
            Log.d(TAG, "Folgendes Fach wird entfernt: " + subject.getName());
            try {
                SubjectService subjectService;
                subjectService = new SubjectServiceStd(new SubjectDAOFilterDB(
                        this));
                ArrayList<Subject> subjectsToFilter = new ArrayList<Subject>();
                subjectsToFilter.add(subject);
                subjectService.writeSubjects(subjectsToFilter);
            } catch (ConnectionException e) {
                Log.e(TAG,
                        "Beim Filtern des Faches ist ein Fehler aufgetreten.");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG,
                        "Beim Filtern des Faches ist ein Fehler aufgetreten.");
                e.printStackTrace();
            }

            SubjectDAOCache.removeSubjects();
            (new GetSubjectsTask((SubjectsActivity) getCurrentActivity(),
                    getResources().getString(R.string.activity_subjects),
                    getResources()
                            .getString(R.string.activity_subjects_loading)))
                    .execute();
        } else if (item.getTitle().equals(
                getString(R.string.activity_subjects_contextmenu_edit))) {

            SubjectAddFragment fragment = new SubjectAddFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Subject.SUBJECT, subject);

            loadFragment(fragment, fragment.getClass().getName(), bundle,
                    R.drawable.actionbar_background_blue);
        }
        return true;
    }
}
