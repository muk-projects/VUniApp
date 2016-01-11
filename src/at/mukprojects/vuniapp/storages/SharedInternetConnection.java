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

package at.mukprojects.vuniapp.storages;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * Internetverbindung als Singleton, welche w&auml;hrend das Programm
 * ausgef&uuml;hrt wird, Cookies und weitere Daten zwischenspeichert.
 * 
 * @author kerrim
 */
public final class SharedInternetConnection {
    private static final String             TAG          = SharedInternetConnection.class
                                                                 .getSimpleName();

    private static SharedInternetConnection sharedInternetConnection;

    private DefaultHttpClient               httpClient   = null;
    private BasicCookieStore                cookieStore  = null;
    private HttpContext                     localContext = null;

    private String                          lastHeaderLocation;

    /**
     * Erzeugt eine neue SharedInternetConnection.
     */
    private SharedInternetConnection() {
        httpClient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
                CookiePolicy.NETSCAPE);
        httpClient.getParams().setParameter(
                ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        httpClient.setRedirectHandler(new DefaultRedirectHandler() {
            @Override
            public boolean isRedirectRequested(final HttpResponse response,
                    final HttpContext context) {
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    Log.v(TAG, "HTTP Header: " + header);
                }

                if (response.getLastHeader("Location") != null) {
                    lastHeaderLocation = response.getLastHeader("Location")
                            .getValue();
                }

                boolean isRedirect = false;
                isRedirect = super.isRedirectRequested(response, context);
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        Log.d(TAG,
                                "Von der Seite wurde eine Weiterleitung beantragt.");
                        return true;
                    }
                }
                return isRedirect;
            }
        });
    }

    /**
     * Liefert eine SharedInternetConnection zur&uuml;ck. Diese kann zum
     * Beispiel Cookies speichern und somit etwaige Anmeldungen ersparen.
     * 
     * @return Einzige SharedInternetConnection Instanz.
     */
    public static SharedInternetConnection getSharedInternetConnection() {
        if (sharedInternetConnection == null) {
            sharedInternetConnection = new SharedInternetConnection();
        }
        return sharedInternetConnection;
    }

    /**
     * F&uuml;hrt einen Http Request aus und liefert das Ergebnis in einem Http
     * Response zur&uuml;ck.
     * 
     * @param request
     *            Auszuf&uuml;hrende Anfrage.
     * @return Http Response mit der Antwort.
     * @throws IOException
     *             Wird geworfen falls ein Verbindungsfehler auftritt.
     */
    public HttpResponse execute(final HttpUriRequest request)
            throws IOException {
        return httpClient.execute(request, localContext);
    }

    /**
     * Pr&uuml;ft ob ein Cookie existiert und nicht abgelaufen ist.
     * 
     * @param cookieName
     *            Name des Cookies, welches gepr&uuml;ft werden soll.
     * @return True falls das Cookie existiert, sonst false.
     */
    public boolean cookieExists(final String cookieName) {
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName) && !c.isExpired(new Date())) {
                return true;
            }
        }
        return false;

    }

    /**
     * Liefert alle gespeicherten Cookies zur&uuml;ck.
     * 
     * @return Liste aller gespeicherten Cookies.
     */
    public List<Cookie> getCookies() {
        return cookieStore.getCookies();
    }

    /**
     * Entfernt alle gespeicherten Cookies.
     */
    public void clearCookies() {
        cookieStore.clear();
    }

    /**
     * Liefert die letzte Location die in einem HTTP Header vorhanden war.
     * 
     * @return Letzte Location die in einem HTTP Header gefunden wurde.
     */
    public String getLastHeaderLocation() {
        return lastHeaderLocation;
    }
}
