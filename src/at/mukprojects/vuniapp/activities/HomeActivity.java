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

package at.mukprojects.vuniapp.activities;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import at.mukprojects.vuniapp.R;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.storages.SharedInternetConnection;

/**
 * HomeActivity der VUniApp.
 * 
 * @author Mathias
 * @author kerrim
 */
public class HomeActivity extends VUniAppActivity {
    private static final String TAG              = HomeActivity.class
                                                         .getSimpleName();

    /** Preferences. */
    private static final String PREF_FILE        = "PrefFileStart";
    private SharedPreferences   prefFile         = null;

    private static final String WEBSITES_GERMAN  = "de-start";
    private static final String WEBSITES_ENGLISH = "e-start";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        setContentView(R.layout.activity_home);
        super.setActiveNavigationItem(getResources().getString(
                R.string.activity_home));
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);
        super.onCreate(savedInstanceState);
        prefFile = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        super.setActionbarBackground(R.drawable.actionbar_background_blue);

        WebView webview = (WebView) findViewById(R.id.activity_home_layout_webView);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view,
                    final String url) {
                if (Uri.parse(url).getHost().equals("www.mukprojects.at")) {
                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });

        List<Cookie> cookies = SharedInternetConnection
                .getSharedInternetConnection().getCookies();

        if (!cookies.isEmpty()) {
            CookieSyncManager.createInstance(this);
            CookieManager cm = CookieManager.getInstance();
            for (Cookie c : cookies) {
                String tempCookie = c.getName() + "=" + c.getValue()
                        + "; domain=" + c.getDomain();
                cm.setCookie(c.getDomain(), tempCookie);
                CookieSyncManager.getInstance().sync();
            }
        }

        Log.d(TAG, "UserSeite wird angezeigt.");
        if (language.equals(ENGLISH)) {
            Log.d(TAG, "Englische Seite wird geladen.");
            webview.loadUrl("http://www.mukprojects.at/vuniapp/appsite/"
                    + WEBSITES_ENGLISH + ".html");
        } else if (language.equals(GERMAN)) {
            Log.d(TAG, "Deutsche Seite wird geladen.");
            webview.loadUrl("http://www.mukprojects.at/vuniapp/appsite/"
                    + WEBSITES_GERMAN + ".html");
        }

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }
}
