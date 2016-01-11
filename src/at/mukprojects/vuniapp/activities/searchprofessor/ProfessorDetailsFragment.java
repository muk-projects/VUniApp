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

package at.mukprojects.vuniapp.activities.searchprofessor;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.fragments.VUniAppFragment;
import at.mukprojects.vuniapp.baseclasses.tasks.BaseTask;
import at.mukprojects.vuniapp.models.Professor;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.universities.interfaces.ProfessorInterface;

/**
 * Fragment zum Anzeigen der Professor Daten. Diese k&ouml;nnen dem Fragment als
 * Professor Objekt &uuml;bergeben werden. Das Fragment bef&uuml;llt die Details
 * des Professors automatisch aus der beiliegenden URL.
 * 
 * @author kerrim
 */
public class ProfessorDetailsFragment extends VUniAppFragment {
    private static final long   serialVersionUID = 1L;

    private static final String TAG              = ProfessorDetailsFragment.class
                                                         .getSimpleName();

    private Professor           professor;

    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_searchprofessor_professordetails, container,
                false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public final void onStart() {
        Log.i(TAG, "Methode: onStart wird gestartet.");
        super.onStart();
        Bundle bundle = getArguments();
        professor = (Professor) bundle.getSerializable("professor");

        (new GetProfessorDetailTask()).execute();

        Log.i(TAG, "Methode: onStart wird verlassen.");
    }

    /**
     * Professoren Details anfordern.
     * 
     * @author kerrim
     * @author Mathias
     */
    class GetProfessorDetailTask extends BaseTask<Void, Professor> {
        /**
         * Erstellt einen neuen Task.
         */
        public GetProfessorDetailTask() {
            super(getResources().getString(
                    R.string.activity_searchprofessor_gettingdetails),
                    getResources().getString(
                            R.string.activity_searchprofessor_searching),
                    false, getActivity());
        }

        @Override
        protected Professor doInBackground(final Void... params) {
            Professor output = null;
            try {
                University university = professor.getUniversity();
                if (university instanceof ProfessorInterface) {
                    output = ((ProfessorInterface) university)
                            .getProfessorDetails(professor);
                }
            } catch (IOException e) {
                super.ioException = e;
            }
            return output;
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onPostExecute(final Professor result) {
            super.onPostExecute(result);
            if (super.noSuperError && result != null) {
                WebView webview = (WebView) getView().findViewById(
                        R.id.fragment_searchprofessor_profdetails_webview);

                webview.getSettings().setBuiltInZoomControls(true);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(
                            final WebView webView, final String url) {

                        if (url.startsWith("mailto:")) {
                            String address = url.substring(
                                    url.indexOf(':') + 1, url.length());

                            Intent emailIntent = new Intent(
                                    android.content.Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(
                                    android.content.Intent.EXTRA_EMAIL,
                                    new String[] { address });
                            startActivity(emailIntent);

                            return true;
                        }

                        webView.loadUrl(url);
                        return true;
                    }
                });
                webview.loadDataWithBaseURL(result.getUrlToDetails(),
                        result.getDetails(), "text/html", "utf-8", null);
            }
            Log.i(TAG, "ASyncTask: GetProfessorDetailTask wird verlassen.");
        }
    }
}
