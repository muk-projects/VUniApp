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
 * Diese Klasse bietet einen mit allen von den Readern genutzten Cookies
 * ausgestatteten Browser zur Verf&uuml;gung. Dieser kann als Activity mit
 * folgenden Extras gestartet werden: <br />
 * <ul>
 * <li><strong>data</strong> HTML Daten die geladen werden sollen. Werden keine
 * angegeben wird in URL f&uuml;r eine URL zum Laden geschaut.</li>
 * <li><strong>url</strong> f&uuml;r die URL der aufzurufenden Seite.</li>
 * <li><strong>limitedHost</strong> f&uuml;r den Host auf dem dieser Browser
 * limitiert werden soll.</li>
 * <li><strong>navigationItem</strong> Item welches in der Navigation markiert
 * werden soll.</li>
 * </ul>
 * Das limitedHost Extra ist optional. Wird keiner angegeben so kann mit dem
 * internen Browser alles aufgerufen werden. Wird einer angegeben und man
 * verl&auml;sst den Host, so wird der vom Benutzer eingestellte Standardbrowser
 * genutzt.
 * 
 * @author kerrim
 */

public class BrowserActivity extends VUniAppActivity {
    private static final String TAG = BrowserActivity.class.getSimpleName();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "Methode: onCreate wird gestartet.");
        setContentView(R.layout.activity_browser);

        Intent intent = getIntent();

        final String data = intent.getStringExtra("data");
        final String url = intent.getStringExtra("url");
        final String limitedHost = intent.getStringExtra("limitedHost");
        final String navigationItem = intent.getStringExtra("navigationItem");

        super.setActiveNavigationItem(navigationItem);
        super.setCurrentActivity(this);
        super.language = getString(R.string.title);
        super.onCreate(savedInstanceState);

        WebView webview = (WebView) findViewById(R.id.activity_browser_webview);

        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setJavaScriptEnabled(true);

        if (data != null) {
            webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        } else if (url != null) {
            Log.d(TAG, url + " wird geladen.");

            webview.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(final WebView view,
                        final String url) {
                    if (limitedHost == null
                            || (Uri.parse(url).getHost() != null && Uri
                                    .parse(url).getHost().equals(limitedHost))) {
                        return false;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
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

            webview.loadUrl(url);
        }

        Log.i(TAG, "Methode: onCreate wird verlassen.");
    }
}