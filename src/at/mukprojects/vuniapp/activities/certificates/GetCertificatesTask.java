package at.mukprojects.vuniapp.activities.certificates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.tasks.InternetBaseTask;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.exceptions.NoUniversitiesException;
import at.mukprojects.vuniapp.exceptions.NoUniversityUsersException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;

/**
 * Task zum Holen aller vorhandenen Zeugnisse.
 * 
 * @author kerrim
 * @author Mathias
 */
public class GetCertificatesTask extends
        InternetBaseTask<Void, ArrayList<CertificateEntry>> {
    private ArrayList<CertificateEntry> certificates         = null;
    private static final String         TAG                  = GetCertificatesTask.class
                                                                     .getSimpleName();

    private CertificatesActivity        certificatesActivity = null;

    /**
     * Erstellt einen neuen GetCertificatesTask.
     * 
     * @param certificatesActivity
     *            CertificatesActivity welche diesen Task aufruft.
     * @param loadingTitle
     *            Titel des Ladedialogs.
     * @param loadingText
     *            Text des Ladedialogs.
     **/
    public GetCertificatesTask(final CertificatesActivity certificatesActivity,
            final String loadingTitle, final String loadingText) {
        super(loadingTitle, loadingText, false, certificatesActivity);
        this.certificatesActivity = certificatesActivity;
    }

    @Override
    protected ArrayList<CertificateEntry> executeBackgroundMethod(
            final Void... params) {
        ArrayList<CertificateEntry> output = new ArrayList<CertificateEntry>();

        UniversityUserService userService = null;
        try {
            userService = new UniversityUserServiceStd(
                    new UniversityUserDAOAndroidDB(certificatesActivity));
        } catch (ConnectionException e1) {
            super.connectionException = e1;
        }

        HashMap<String, CertificateInterface> universities = Universities
                .getInstance().getUniversitiesWithCertificates();

        if (universities.isEmpty()) {
            super.noUniversitiesException = new NoUniversitiesException();
        } else {

            int usersFound = 0;

            for (String u : universities.keySet()) {
                List<String> userKeys = universities.get(u)
                        .getCertificateKeys();
                HashMap<String, UniversityUser> users = new HashMap<String, UniversityUser>();

                try {
                    users = userService.getUsersFromUniversity(
                            (University) universities.get(u), userKeys);
                } catch (ReadException e1) {
                    super.readException = e1;
                } catch (NotInitializedException e) {
                    SecurityHelper.initialize(certificatesActivity
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
                    for (Certificate certificate : universities.get(u)
                            .getCertificates(users)) {
                        output.add(new CertificateEntry(certificate,
                                universities.get(u)));
                    }
                } catch (IOException e) {
                    super.ioException = e;
                } catch (InvalidLoginException e) {
                    super.invalidLoginException = e;
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Zu dieser Uni (" + u
                            + ") existiert kein NUtzer");
                }
            }

            if (usersFound == 0) {
                super.noUniversityUsersException = new NoUniversityUsersException();
            }
        }
        return output;
    }

    @Override
    protected void onPostExecute(final ArrayList<CertificateEntry> result) {
        super.onPostExecute(result);
        if (super.noSuperError) {

            certificates = result;

            Log.d(TAG, "Es wurden insgesamt " + certificates.size()
                    + " Zeugnisse ausgelesen.");

            final ListView certificateView = (ListView) certificatesActivity
                    .findViewById(R.id.activity_certificates_certificatelist);
            final TextView noCertificates = (TextView) certificatesActivity
                    .findViewById(R.id.activity_certificates_nocertificates);

            if (certificates.isEmpty()) {
                certificateView.setVisibility(View.INVISIBLE);
                noCertificates.setVisibility(View.VISIBLE);

            } else {
                certificateView.setVisibility(View.VISIBLE);
                noCertificates.setVisibility(View.INVISIBLE);

                final CertificateAdapter certificateAdapter = new CertificateAdapter(
                        certificatesActivity, result);
                certificateView.setAdapter(certificateAdapter);

                certificateView
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(
                                    final AdapterView<?> parent,
                                    final View view, final int position,
                                    final long id) {
                                CertificateEntry certificate = (CertificateEntry) certificateAdapter
                                        .getItem(position);
                                Log.d(TAG, "Fach wurde ausgewählt: "
                                        + certificate.getKey().getNr());

                                CertificateDetailsFragment fragment = new CertificateDetailsFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("certificate",
                                        certificate.getKey());
                                bundle.putSerializable("university",
                                        certificate.getValue());

                                certificatesActivity.loadFragment(fragment,
                                        fragment.getClass().getName(), bundle, R.drawable.actionbar_background_blue);

                                return;
                            }
                        });

                ((Button) certificatesActivity
                        .findViewById(R.id.activity_certificates_statistics_button))
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                Log.d(TAG,
                                        "Button zum Aufrufen der Statistik wurde geklickt.");
                                CertificateStatisticsFragment fragment = new CertificateStatisticsFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("certificates",
                                        certificates);

                                certificatesActivity
                                        .loadFragment(
                                                fragment,
                                                certificatesActivity
                                                        .getResources()
                                                        .getString(
                                                                R.string.fragment_certificates_certstatistics),
                                                bundle, R.drawable.actionbar_background_blue);
                            }
                        });
            }
        }

        Log.i(TAG, "ASyncTask: GetCertificatesTask wird verlassen.");
    }

}