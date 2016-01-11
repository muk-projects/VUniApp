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

package at.mukprojects.vuniapp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import at.mukprojects.vuniapp.baseclasses.activities.VUniAppActivity;
import at.mukprojects.vuniapp.dao.UniversityUserDAO;
import at.mukprojects.vuniapp.exceptions.ConnectionException;
import at.mukprojects.vuniapp.exceptions.CreateException;
import at.mukprojects.vuniapp.exceptions.DeleteException;
import at.mukprojects.vuniapp.exceptions.NotInitializedException;
import at.mukprojects.vuniapp.exceptions.ReadException;
import at.mukprojects.vuniapp.exceptions.UpdateException;
import at.mukprojects.vuniapp.helper.SecurityHelper;
import at.mukprojects.vuniapp.models.UniversityUser;
import at.mukprojects.vuniapp.models.base.University;
import at.mukprojects.vuniapp.storages.Cache;

/**
 * Standard- UserService Implementierung.
 * 
 * @author Mathias
 */
public class UniversityUserServiceStd implements UniversityUserService {
    private static final String TAG = UniversityUserServiceStd.class
                                            .getSimpleName();

    /** DAOs. */
    private UniversityUserDAO   userDAO;

    /**
     * Erstellt eine neue UserServiceStd.
     * 
     * @param userDAO
     *            Die UserDAO, welche verwendet werden soll. Einstellung
     *            verwendet.
     * @throws ConnectionException
     *             Diese Exception wird geworfen, wenn es w&auml;hrend dem
     *             Verbindungsaufbau mit der DAO zu einem Fehler kommt.
     */
    public UniversityUserServiceStd(final UniversityUserDAO userDAO)
            throws ConnectionException {
        this.userDAO = userDAO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.UserService#getUsers()
     */
    @Override
    public final List<UniversityUser> getUsers() throws ReadException,
            NotInitializedException {
        Log.i(TAG, "Methode: getUsers wird gestartet.");
        List<UniversityUser> retList = decryptUserList(userDAO.getAllUsers());
        Log.i(TAG, "Methode: getUsers wird verlassen.");
        return retList;
    }

    /**
     * Liefert alle Benutzer unverschl&uuml;sselt zur&uuml;ck.
     * 
     * @return Die Benutzer der Datenbank.
     * @throws ReadException
     *             Die Exception wird geworfen, wenn es w&auml;hrend des
     *             Lesevorgangs zu einem Fehler kommt.
     */
    private List<UniversityUser> getUsersEncrypted() throws ReadException {
        Log.i(TAG, "Die Methode getUsersEncrypted lädt alle Benutzer.");
        List<UniversityUser> retList = userDAO.getAllUsers();
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#getUsers(at.mukprojects.vuniapp
     * .models.User)
     */
    @Override
    public final List<UniversityUser> getUsers(final UniversityUser user)
            throws ReadException, NotInitializedException {
        Log.i(TAG, "Methode: getUsers wird gestartet.");
        Log.d(TAG, "Encrypt User.");
        UniversityUser encryptedUser = encryptUser(user);
        List<UniversityUser> retList = decryptUserList(userDAO
                .getUsers(encryptedUser));
        Log.i(TAG, "Methode: getUsers wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see at.mukprojects.vuniapp.services.UserService#getUserByID(long)
     */
    @Override
    public final UniversityUser getUserByID(final long id)
            throws ReadException, NotInitializedException {
        Log.i(TAG, "Methode: getUserByID wird gestartet.");
        UniversityUser retUser = decryptUser(userDAO.getUserByID(id));
        Log.i(TAG, "Methode: getUserByID wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#createUser(at.mukprojects
     * .vuniapp.models.User)
     */
    @Override
    public final UniversityUser createUser(final UniversityUser user)
            throws CreateException, NotInitializedException {
        Log.i(TAG, "Methode: createUser wird gestartet.");
        Log.d(TAG, "Encrypt User.");
        UniversityUser encryptedUser = encryptUser(user);
        UniversityUser retUser = decryptUser(userDAO.createUser(encryptedUser));
        Log.i(TAG, "Methode: createUser wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#createOrUpdateUser(at.mukprojects
     * .vuniapp.models.User)
     */
    @Override
    public final UniversityUser createOrUpdateUser(final UniversityUser user)
            throws CreateException, ReadException, UpdateException,
            NotInitializedException {
        if (!validateInputParameter(user)) {
            throw new CreateException("Es wurde ein ungülter User übergeben.");
        }
        UniversityUser searchUser = new UniversityUser(null, null,
                user.getUniversity(), user.getUserKey());
        if (searchUser.getUserKey() == null) {
            searchUser.setUserKey(UniversityUser.USERKEY_ONLY_USER);
        }

        List<UniversityUser> retUsers = getUsers(searchUser);
        UniversityUser retUser = null;
        if (retUsers.size() == 1) {
            Log.d(TAG,
                    "Der User hat bereits existiert und wird daher aktulisiert.");
            updateUser(user, retUsers.get(0));
            retUser = getUsers(searchUser).get(0);
        } else if (retUsers.size() > 1) {
            Log.d(TAG,
                    "Ein Inkonsitenter Zustand ist eingetreten. Es existieren bereits zu viel User."
                            + " Alle überzähligen User werden gelöscht.");
            try {
                deleteUser(searchUser);
            } catch (DeleteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            retUser = createUser(user);
        } else {
            Log.d(TAG,
                    "Der User hat noch nicht existiert und wird daher erstellt.");
            retUser = createUser(user);
        }
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#updateUser(at.mukprojects
     * .vuniapp.models.User, at.mukprojects.vuniapp.models.User)
     */
    @Override
    public final int updateUser(final UniversityUser setUser,
            final UniversityUser whereUser) throws UpdateException,
            ReadException, NotInitializedException {
        Log.i(TAG, "Methode: updateUser wird gestartet.");
        Log.d(TAG, "Encrypt setUser.");
        UniversityUser encryptedSetUser = encryptUser(setUser);
        Log.d(TAG, "Encrypt whereUser.");
        UniversityUser encryptedWhereUser = encryptUser(whereUser);
        int entriesChanged = userDAO.updateUser(encryptedSetUser,
                encryptedWhereUser);
        Log.i(TAG, "Methode: updateUser wird verlassen.");
        return entriesChanged;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#deleteUser(at.mukprojects
     * .vuniapp.models.User)
     */
    @Override
    public final int deleteUser(final UniversityUser user)
            throws DeleteException, ReadException, NotInitializedException {
        Log.i(TAG, "Methode: deleteUser wird gestartet.");
        Log.d(TAG, "Encrypt User.");
        UniversityUser encryptedUser = encryptUser(user);
        int entriesChanged = userDAO.deleteUser(encryptedUser);
        Log.d(TAG, "encrypted User: " + encryptedUser);
        Log.i(TAG, "Methode: deleteUser wird verlassen.");
        return entriesChanged;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#encryptionUsers(java.lang
     * .String, java.lang.String, boolean)
     */
    @Override
    public final void encryptionUsers(final String newPin, final String oldPin)
            throws ReadException, UpdateException, NotInitializedException {
        Log.i(TAG, "Methode: encryptionUsers wird gestartet.");
        List<UniversityUser> dBUser = getUsersEncrypted();
        List<UniversityUser> dBUserDecrypted = decryptUserList(dBUser, oldPin);
        List<UniversityUser> newEncryptedUsers = encryptUserList(
                dBUserDecrypted, newPin);

        for (int i = 0; i < dBUserDecrypted.size(); i++) {
            UniversityUser whereUser = new UniversityUser(null, null, null,
                    null);
            whereUser.setId(dBUserDecrypted.get(i).getId());
            userDAO.updateUser(newEncryptedUsers.get(i), whereUser);
        }
        Log.i(TAG, "Methode: encryptionUsers wird verlassen.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#encryptUser(at.mukprojects
     * .vuniapp.models.User)
     */
    @Override
    public final UniversityUser encryptUser(final UniversityUser decryptedUser)
            throws NotInitializedException {
        Log.i(TAG, "Methode: encryptUser wird gestartet.");
        String key = null;
        if (Cache.getCache().containsData(VUniAppActivity.CACHE_PIN)) {
            key = (String) Cache.getCache().getCachedData(
                    VUniAppActivity.CACHE_PIN);
        }
        UniversityUser retUser = encryptUser(decryptedUser, key);
        Log.i(TAG, "Methode: encryptUser wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#decryptUser(at.mukprojects
     * .vuniapp.models.User)
     */
    @Override
    public final UniversityUser decryptUser(final UniversityUser encryptedUser)
            throws NotInitializedException {
        Log.i(TAG, "Methode: decryptUser wird gestartet.");
        String key = null;
        if (Cache.getCache().containsData(VUniAppActivity.CACHE_PIN)) {
            key = (String) Cache.getCache().getCachedData(
                    VUniAppActivity.CACHE_PIN);
        }
        UniversityUser retUser = decryptUser(encryptedUser, key);
        Log.i(TAG, "Methode: decryptUser wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#encryptUserList(java.util
     * .List)
     */
    @Override
    public final List<UniversityUser> encryptUserList(
            final List<UniversityUser> userList) throws NotInitializedException {
        Log.i(TAG, "Methode: encryptUserList wird gestartet.");
        ArrayList<UniversityUser> retList = new ArrayList<UniversityUser>();
        for (UniversityUser userListItem : userList) {
            retList.add(encryptUser(userListItem));
        }
        Log.i(TAG, "Methode: encryptUserList wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#decryptUserList(java.util
     * .List)
     */
    @Override
    public final List<UniversityUser> decryptUserList(
            final List<UniversityUser> userList) throws NotInitializedException {
        Log.i(TAG, "Methode: decryptUserList wird gestartet.");
        ArrayList<UniversityUser> retList = new ArrayList<UniversityUser>();
        for (UniversityUser userListItem : userList) {
            retList.add(decryptUser(userListItem));
        }
        Log.i(TAG, "Methode: decryptUserList wird verlassen.");
        return retList;
    }

    /**
     * Validiert einen User, ob er in die Datenbank geschrieben werden kann.
     * 
     * @param user
     *            Der zu pr&uuml;fende User.
     * @return Liefert True falls der User valide ist oder false falls er nicht
     *         g&uuml;ltig ist.
     */
    private boolean validateInputParameter(final UniversityUser user) {
        if (user == null) {
            return false;
        }
        if (user.getUsername() == null || user.getPassword() == null
                || user.getUniversity() == null) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#encryptUser(at.mukprojects
     * .vuniapp.models.User, java.lang.String)
     */
    @Override
    public final UniversityUser encryptUser(final UniversityUser decryptedUser,
            final String pin) throws NotInitializedException {
        Log.i(TAG, "Methode: encryptUser wird gestartet.");
        UniversityUser retUser = null;
        if (decryptedUser != null) {
            UniversityUser user = decryptedUser.copy();
            if (user == null) {
                retUser = null;
            } else {
                String userName = user.getUsername();
                if (userName != null) {
                    user.setUsername(SecurityHelper
                            .encryptString(userName, pin));
                }
                String userPassword = user.getPassword();
                if (userPassword != null) {
                    user.setPassword(SecurityHelper.encryptString(userPassword,
                            pin));
                }
                retUser = user;
            }
        }
        Log.i(TAG, "Methode: encryptUser wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#decryptUser(at.mukprojects
     * .vuniapp.models.User, java.lang.String)
     */
    @Override
    public final UniversityUser decryptUser(final UniversityUser encryptedUser,
            final String pin) throws NotInitializedException {
        Log.i(TAG, "Methode: decryptUser wird gestartet.");
        UniversityUser retUser = null;
        if (encryptedUser != null) {
            UniversityUser user = encryptedUser.copy();
            if (user == null) {
                retUser = null;
            } else {
                String userName = user.getUsername();
                if (userName != null) {
                    user.setUsername(SecurityHelper
                            .decryptString(userName, pin));
                }
                String userPassword = user.getPassword();
                if (userPassword != null) {
                    user.setPassword(SecurityHelper.decryptString(userPassword,
                            pin));
                }
                retUser = user;
            }
        }
        Log.i(TAG, "Methode: decryptUser wird verlassen.");
        return retUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#encryptUserList(java.util
     * .List, java.lang.String)
     */
    @Override
    public final List<UniversityUser> encryptUserList(
            final List<UniversityUser> userList, final String pin)
            throws NotInitializedException {
        Log.i(TAG, "Methode: encryptUserList wird gestartet.");
        ArrayList<UniversityUser> retList = new ArrayList<UniversityUser>();
        for (UniversityUser userListItem : userList) {
            retList.add(encryptUser(userListItem, pin));
        }
        Log.i(TAG, "Methode: encryptUserList wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UserService#decryptUserList(java.util
     * .List, java.lang.String)
     */
    @Override
    public final List<UniversityUser> decryptUserList(
            final List<UniversityUser> userList, final String pin)
            throws NotInitializedException {
        Log.i(TAG, "Methode: decryptUserList wird gestartet.");
        ArrayList<UniversityUser> retList = new ArrayList<UniversityUser>();
        for (UniversityUser userListItem : userList) {
            retList.add(decryptUser(userListItem, pin));
        }
        Log.i(TAG, "Methode: decryptUserList wird verlassen.");
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UniversityUserService#getUsers(at.mukprojects
     * .vuniapp.models.base.University)
     */
    @Override
    public final HashMap<String, UniversityUser> getUsersFromUniversity(
            final University university) throws ReadException,
            NotInitializedException {
        Log.i(TAG, "Methode: getUsers wird gestartet.");
        HashMap<String, UniversityUser> retUsers = new HashMap<String, UniversityUser>();
        List<UniversityUser> users = getUsers(new UniversityUser(null, null,
                university, null));
        for (UniversityUser user : users) {
            retUsers.put(user.getUserKey(), user);
        }
        Log.i(TAG, "Methode: getUsers wird verlassen.");
        return retUsers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * at.mukprojects.vuniapp.services.UniversityUserService#getUsersFromUniversity
     * (at.mukprojects.vuniapp.models.base.University, java.util.List)
     */
    @Override
    public final HashMap<String, UniversityUser> getUsersFromUniversity(
            final University university, final List<String> userKeys)
            throws ReadException, NotInitializedException {
        Log.i(TAG, "Methode: getUsersFromUniversity wird gestartet.");
        HashMap<String, UniversityUser> retUsers = new HashMap<String, UniversityUser>();
        for (String key : userKeys) {
            UniversityUser serachUser = new UniversityUser(null, null,
                    university, key);
            List<UniversityUser> serachedUsers = getUsers(serachUser);
            if (!serachedUsers.isEmpty()) {
                retUsers.put(serachedUsers.get(0).getUserKey(),
                        serachedUsers.get(0));
            }
        }
        Log.i(TAG, "Methode: getUsersFromUniversity wird verlassen.");
        return retUsers;
    }
}
