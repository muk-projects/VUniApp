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

package at.mukprojects.vuniapp.helper;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.storages.UniversitySettings;

//CHECKSTYLE OFF

/**
 * Diese Helper Klasse bietet Methoden, um Eingeben zu verschl&uuml;sseln.
 * 
 * @author Mathias
 */
public abstract class SecurityHelper {
    private static final String TAG            = SecurityHelper.class
                                                       .getSimpleName();

    /** UNICODE_FORMAT. */
    private static final String UNICODE_FORMAT = "UTF8";
    /** String, um den Key zu erweitern. */
    private static final String KEY_PREF = "KeyExtender";
    private static String KEY_EXTENDER;

    /**
     * Inzialisiert die Helper Klasse. Dies sollte immer beim Starten der
     * Applikation geschehen.
     * 
     * @param preferences
     *            Sharedpreference mit aktiven Universit&auml;ten.
     */
    public static void initialize(SharedPreferences preferences) {
        if(KEY_EXTENDER == null) {
            String prefKey = preferences.getString(KEY_PREF, null);
            if(prefKey != null) {
                KEY_EXTENDER = prefKey;
            } else {
                prefKey = generateRandomKey();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_PREF, prefKey);
                editor.commit();
                KEY_EXTENDER = prefKey;
            }
        }
    }

    /**
     * Liefert den MD5 Hash zu einem String.
     * 
     * @param plainText
     *            Der zu verschl&uuml;sselnde String.
     * @return Einen HashString.
     */
    public static String getMD5Hash(final String plainText) {
        Log.i(TAG, "Methode: getMD5Hash wird gestartet.");
        try {
            final MessageDigest messageDigest = MessageDigest
                    .getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(plainText.getBytes(Charset
                    .forName(UNICODE_FORMAT)));
            Log.i(TAG, "Methode: getMD5Hash wird verlassen.");
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.e(TAG, noSuchAlgorithmException.getMessage(),
                    noSuchAlgorithmException);
        }
        Log.i(TAG, "Methode: getMD5Hash wird verlassen.");
        return null;
    }

    /**
     * Vergleicht einen Text mit einem HashText, ob Sie gleich sind.
     * 
     * @param plainText
     *            Der unverschl&uuml;sselte Text.
     * @param hashText
     *            Der bereits verschl&uul;sselte Text.
     * @return Einen Wahrheitswert.
     */
    public static boolean equalsStringWithHashString(final String plainText,
            final String hashText) {
        Log.i(TAG, "Methode: equalsStringWithHashString wird gestartet.");
        String chiper = getMD5Hash(plainText);
        if (chiper == null) {
            Log.i(TAG, "Methode: equalsStringWithHashString wird verlassen.");
            return false;
        } else {
            Log.i(TAG, "Methode: equalsStringWithHashString wird verlassen.");
            return chiper.equals(hashText);
        }
    }

    /**
     * Verschl&uuml;sselt einen String mittels Schl&uuml;ssel.
     * 
     * @param plainText
     *            Der zu verschl&uuml;sselnde String.
     * @param key
     *            Der Schl&uuml;ssel.
     * @return Den verschl&uuml;sselten Text.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    public static String encryptString(final String plainText, final String key)
            throws NotInitializedException {
        Log.i(TAG, "Methode: encryptString wird gestartet.");

        /**
         * Input Key Validierung. Es werden die ersten 24 Zeichen des Keys
         * genutzt. Ist der Key k&uuml;rzer wird der String auf 24 Zeichen
         * verl&auml;ngert.
         */
        String usedKey;
        if (key == null) {
            usedKey = KEY_EXTENDER;
        } else if (key.length() >= 24) {
            usedKey = key.substring(0, 24);
        } else {
            int difTo24 = 24 - key.length();
            usedKey = key + KEY_EXTENDER.substring(0, difTo24);
        }

        String cipherText = null;
        try {

            byte[] arrayBytes = usedKey.getBytes(UNICODE_FORMAT);
            KeySpec keySpec = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory
                    .getInstance("DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            /** Encryption. */
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] plainByte = plainText.getBytes(UNICODE_FORMAT);
            byte[] encryptedByte = cipher.doFinal(plainByte);
            cipherText = new String(
                    Base64.encode(encryptedByte, Base64.DEFAULT),
                    UNICODE_FORMAT);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        Log.i(TAG, "Methode: encryptString wird verlassen.");
        return cipherText;
    }

    /**
     * Entschl&uuml;sselt einen String mittels Schl&uuml;ssel.
     * 
     * @param chiper
     *            Der verschl&uuml;sselte String.
     * @param key
     *            Der Schl&uuml;ssel.
     * @return Den entschl&uuml;sselten Text.
     * @throws NotInitializedException
     *             Diese Exception wird geworfen, wenn die Helper Klasse nicht
     *             inizialisiert wurde und daher kein KeyExtender vorhanden ist.
     */
    public static String decryptString(final String chiper, final String key)
            throws NotInitializedException {
        Log.i(TAG, "Methode: decryptString wird gestartet.");

        /**
         * Input Key Validierung. Es werden die ersten 24 Zeichen des Keys
         * genutzt. Ist der Key k&uuml;rzer wird der String auf 24 Zeichen
         * verl&auml;ngert.
         */
        String usedKey;
        if (key == null) {
            usedKey = KEY_EXTENDER;
        } else if (key.length() >= 24) {
            usedKey = key.substring(0, 24);
        } else {
            int difTo24 = 24 - key.length();
            usedKey = key + KEY_EXTENDER.substring(0, difTo24);
        }

        String plainText = null;
        try {

            byte[] arrayBytes = usedKey.getBytes(UNICODE_FORMAT);
            KeySpec keySpec = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory
                    .getInstance("DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            /** Decryption. */
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedByte = Base64.decode(chiper, Base64.DEFAULT);
            byte[] plainByte = cipher.doFinal(encryptedByte);
            plainText = bytes2String(plainByte);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        Log.i(TAG, "Methode: decryptString wird verlassen.");
        return plainText;
    }

    /**
     *      * Returns String From An Array Of Bytes      
     */
    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * Liefert einen random String der L&auml;ge 24 zur&uuml;ck.
     * 
     * @return Einen ranodm String.
     */
    private static String generateRandomKey() {
        final String chars = "abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890"
                + "!§$%&()[]{}?@~+-*#";

        StringBuffer buffer = new StringBuffer();
        int charactersLength = chars.length();
        for (int i = 0; i < 24; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(chars.charAt((int) index));
        }
        return buffer.toString();
    }
}
