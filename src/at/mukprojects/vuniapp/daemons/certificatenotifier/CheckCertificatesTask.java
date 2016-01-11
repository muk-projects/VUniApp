package at.mukprojects.vuniapp.daemons.certificatenotifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.activities.StartActivity;
import at.mukprojects.vuniapp.activities.certificates.CertificatesActivity;
import at.mukprojects.vuniapp.dao.UniversityUserDAOAndroidDB;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.InvalidLoginException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.models.Certificate;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.services.UniversityUserService;
import at.mukprojects.vuniapp.services.UniversityUserServiceStd;
import at.mukprojects.vuniapp.storages.Universities;
import at.mukprojects.vuniapp.universities.interfaces.CertificateInterface;

/**
 * Task zum Abrufen von neuen Zeugnissen. Dabei werden immer alle Zeugnisse
 * geholt und diese mit den alten verglichen.
 * 
 * @author kerrim
 */
public class CheckCertificatesTask extends TimerTask {
    private static final String                     TAG             = CheckCertificatesTask.class
                                                                            .getSimpleName();
    private static final int                        NOTIFIERID      = 00001;

    private Context                                 context         = null;

    private boolean                                 onlywlan;

    private HashMap<String, ArrayList<Certificate>> oldCertificates = null;
    private HashMap<String, ArrayList<Certificate>> certificates    = null;

    private HashMap<String, CertificateInterface>   universities    = null;
    private UniversityUserService                   userService     = null;

    /**
     * Erstellt einen neuen Zeugnisse Abruf Task.
     * 
     * @param onlywlan
     *            Falls ja wird nur gesucht falls man sich im WLAN befindet.
     * @param context
     *            Context des Aufrufers.
     */
    public CheckCertificatesTask(final boolean onlywlan, final Context context) {
        this.onlywlan = onlywlan;
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.TimerTask#run()
     */
    @Override
    public final void run() {
        Log.i(TAG, "CertificateNotifier check für neue Zeugnisse gestartet...");
        try {

            // TODO K: Fehlerbehandlungen

            ConnectivityManager connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!onlywlan || mWifi.isConnected()) {

                certificates = new HashMap<String, ArrayList<Certificate>>();

                userService = new UniversityUserServiceStd(
                        new UniversityUserDAOAndroidDB(context));

                universities = Universities.getInstance()
                        .getUniversitiesWithCertificates();

                for (String universityName : universities.keySet()) {
                    try {
                        addCertificatesFromUniversity(universityName);
                    } catch (ReadException e) {
                        Log.e(TAG, e.toString());
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    } catch (InvalidLoginException e) {
                        Log.e(TAG, e.toString());
                    } catch (NotInitializedException e) {
                        Log.e(TAG, e.toString());
                    }
                }

                if (oldCertificates != null) {
                    compareCertificates();
                }

                oldCertificates = certificates;
            }

        } catch (ConnectionException e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "CertificateNotifier check für neue Zeugnisse beendet!");

    }

    /**
     * F&uuml;gt alle Zeugnisse von einer Universit&aumlt zur Zeugnisse liste
     * hinzu.
     * 
     * @param universityName
     *            Universit&auml;t die hinzugefuuml;gt werden soll.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es beim lesen der Benutzer
     *             einer Universit&auml;t zu Fehler kam.
     * @throws InvalidLoginException
     *             Wird geworfen falls Benutzername und Passwort nicht korrekt
     *             waren.
     * @throws IOException
     *             Wird geworfen, falls beim Holen der Daten ein Fehler
     *             aufgetreten ist.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    private void addCertificatesFromUniversity(final String universityName)
            throws ReadException, IOException, InvalidLoginException,
            NotInitializedException {

        List<String> userKeys = universities.get(universityName)
                .getCertificateKeys();
        HashMap<String, UniversityUser> users = new HashMap<String, UniversityUser>();

        users = userService.getUsersFromUniversity(
                (University) universities.get(universityName), userKeys);

        try {
            ArrayList<Certificate> foundCertificates = universities.get(
                    universityName).getCertificates(users);
            Log.d(TAG, "Zeugnisse gefunden: " + foundCertificates.size());
            certificates.put(universityName, foundCertificates);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Keine passenden Benutzer übergeben.");
        }
    }

    /**
     * Vergleicht die neu bekommenen Zeugnisse mit den alten und zeigt
     * gegebenfalls eine Notification an.
     */
    private void compareCertificates() {
        HashMap<String, ArrayList<Certificate>> newCertificates = new HashMap<String, ArrayList<Certificate>>();
        int countNewCertificates = 0;

        for (String universityName : oldCertificates.keySet()) {
            if (certificates.containsKey(universityName)
                    && certificates.get(universityName).size() > oldCertificates
                            .get(universityName).size()) {
                ArrayList<Certificate> certificateList = certificates
                        .get(universityName);
                ArrayList<Certificate> oldCertificateList = oldCertificates
                        .get(universityName);
                ArrayList<Certificate> newCertificateList = new ArrayList<Certificate>();

                for (Certificate cert1 : certificateList) {
                    boolean exists = false;
                    for (Certificate cert2 : oldCertificateList) {
                        if (cert1.equals(cert2)) {
                            exists = true;
                        }
                    }

                    if (!exists) {
                        newCertificateList.add(cert1);
                        countNewCertificates++;
                    }
                }

                newCertificates.put(universityName, newCertificateList);
            }
        }

        if (countNewCertificates > 0) {
            showNewCertificatesNotification(countNewCertificates);
        }
    }

    /**
     * Methode zum Anzeigen der Notification f&uuml;r neue Zeugnisse. Bei
     * anklicken der Notification wird man automatisch in die
     * CertificatesActivity weitergeleitet.
     * 
     * @param newCertificates
     *            Anzahl der neuen Zeugnisse.
     */
    private void showNewCertificatesNotification(final int newCertificates) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context)
                .setContentTitle(
                        context.getResources().getString(
                                R.string.activity_services_certificatenotifier))
                .setContentText(
                        context.getResources()
                                .getString(
                                        R.string.daemon_certificatenotifier_newcertificates))
                .setSmallIcon(R.drawable.ic_launcher);

        Intent intent = new Intent(context, StartActivity.class);
        intent.putExtra(StartActivity.START_ACTIVITY,
                CertificatesActivity.class);

        // TODO K: StackBuilder verstehen und richtig einsetzen
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(StartActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFIERID, mBuilder.build());

    }
}