package at.mukprojects.vuniapp.activities.subjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.exceptions.NoUniversitiesException;
import at.mukprojects.vuniapp.exceptions.NoUniversityUsersException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.models.Subject;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.SubjectInterface;

/**
 * Task zum Holen aller abonnierten Lehrveranstaltungen.
 * 
 * @author kerrim
 */
public class GetSubjectsTask extends InternetBaseTask<Void, ArrayList<Subject>> {
    private static final String TAG              = GetSubjectsTask.class
                                                         .getSimpleName();

    private SubjectsActivity    subjectsActivity = null;

    /**
     * Erstellt einen neuen GetSubjectsTask.
     * 
     * @param subjectsActivity
     *            SubjectsActivity welche diesen Task aufruft.
     * @param loadingTitle
     *            Titel des Ladedialogs.
     * @param loadingText
     *            Text des Ladedialogs.
     */
    public GetSubjectsTask(final SubjectsActivity subjectsActivity,
            final String loadingTitle, final String loadingText) {
        super(loadingTitle, loadingText, false, subjectsActivity);
        this.subjectsActivity = subjectsActivity;
    }

    @Override
    protected final ArrayList<Subject> executeBackgroundMethod(
            final Void... params) {
        ArrayList<Subject> output = new ArrayList<Subject>();

        UniversityUserService userService = null;
        try {
            userService = new UniversityUserServiceStd(
                    new UniversityUserDAOAndroidDB(subjectsActivity));
        } catch (ConnectionException e1) {
            super.connectionException = e1;
        }

        HashMap<String, SubjectInterface> universities = Universities
                .getInstance().getUniversitiesWithSubjects();

        if (universities.isEmpty()) {
            super.noUniversitiesException = new NoUniversitiesException();
        } else {
            int usersFound = 0;

            for (String u : universities.keySet()) {
                List<String> userKeys = universities.get(u).getSubjectKeys();
                HashMap<String, UniversityUser> users = new HashMap<String, UniversityUser>();

                try {
                    users = userService.getUsersFromUniversity(
                            (University) universities.get(u), userKeys);
                } catch (ReadException e1) {
                    e1.printStackTrace();
                } catch (NotInitializedException e) {
                    SecurityHelper.initialize(subjectsActivity
                            .getPrefFileConfig());
                    try {
                        users = userService.getUsersFromUniversity(
                                (University) universities.get(u), userKeys);
                    } catch (ReadException readException) {
                        super.readException = readException;
                    } catch (NotInitializedException notInitializedException) {
                        super.notInitializedException = notInitializedException;
                    }
                }

                try {
                    usersFound++;
                    output.addAll(universities.get(u).getSubjects(users, activity));
                } catch (IOException e) {
                    super.ioException = e;
                } catch (InvalidLoginException e) {
                    super.invalidLoginException = e;
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Zu dieser Uni (" + u
                            + ") existiert kein NUtzer");
                } catch (ConnectionException e) {
                    super.connectionException = e;
                }
            }
            
            

            if (usersFound == 0) {
                super.noUniversityUsersException = new NoUniversityUsersException();
            }
        }

        return output;
    }

    @Override
    protected final void onPostExecute(final ArrayList<Subject> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {
            final ListView subjectView = (ListView) subjectsActivity
                    .findViewById(R.id.activity_subjects_subjectlist);
            final TextView noSubjects = (TextView) subjectsActivity
                    .findViewById(R.id.activity_subjects_nosubjects);

            if (result.isEmpty()) {
                subjectView.setVisibility(View.INVISIBLE);
                noSubjects.setVisibility(View.VISIBLE);

            } else {
                subjectView.setVisibility(View.VISIBLE);
                noSubjects.setVisibility(View.INVISIBLE);

                final SubjectAdapter subjectAdapter = new SubjectAdapter(
                        subjectsActivity, result);
                subjectView.setAdapter(subjectAdapter);

                subjectView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent,
                            final View view, final int position, final long id) {
                        Subject subject = (Subject) subjectAdapter
                                .getItem(position);
                        Log.d(TAG,
                                "Fach wurde ausgewählt: " + subject.getName());

                        SubjectDetailsFragment fragment = new SubjectDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("subject", subject);

                        subjectsActivity.loadFragment(fragment, null, bundle,
                                R.drawable.actionbar_background_blue);
                    }
                });
            }
        }
        Log.i(TAG, "ASyncTask: GetSubjectsTask wird verlassen.");
    }

}
