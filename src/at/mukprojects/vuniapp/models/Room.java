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

package at.mukprojects.vuniapp.models;

import java.io.Serializable;

import at.mukprojects.vuniapp.models.base.University;

/**
 * Raum Objekt.
 * 
 * @author Mathias
 */
public class Room implements Serializable {
    private static final long  serialVersionUID = 1L;
    public static final String ROOM_EXTRA       = "room";

    private String             roomName;
    private University         university;
    private String             urlToDetails;
    private String             address;
    private String             detailInfo;

    /**
     * Erstellt ein neues Raum Objekt.
     * 
     * @param roomName
     *            Name des Raums.
     * @param university
     *            Universit&auml;t des Raums.
     */
    public Room(final String roomName, final University university) {
        super();
        this.roomName = roomName;
        this.university = university;
    }

    /**
     * Erstellt ein neues Raum Objekt.
     * 
     * @param roomName
     *            Name des Raums.
     * @param university
     *            Universit&auml;t des Raums.
     * @param urlToDetails
     *            Url zu detailierter Information, zum Beispiel Adresse oder
     *            Karte.
     */
    public Room(final String roomName, final University university,
            final String urlToDetails) {
        this(roomName, university);
        this.urlToDetails = urlToDetails;
    }

    /**
     * Erstellt ein neues Raum Objekt.
     * 
     * @param roomName
     *            Name des Raums.
     * @param university
     *            Universit&auml;t des Raums.
     * @param urlToDetails
     *            Url zu detailierter Information, zum Beispiel Adresse oder
     *            Karte.
     * @param address
     *            Adresse des Raums.
     * @param detailInfo
     *            Info des Raums (Stock, T&uuml;r etc.).
     */
    public Room(final String roomName, final University university,
            final String urlToDetails, final String address,
            final String detailInfo) {
        this(roomName, university, urlToDetails);
        this.address = address;
        this.detailInfo = detailInfo;
    }

    /**
     * Liefert den Name des Raums.
     * 
     * @return Den Name des Raums.
     */
    public final String getRoomName() {
        return roomName;
    }

    /**
     * Liefert die Universit&auml;t des Raums.
     * 
     * @return Key die Universit&auml;t des Raums.
     */
    public final University getUniversity() {
        return university;
    }

    /**
     * Liefert die Url des Raums.
     * 
     * @return Die Url des Raums.
     */
    public final String getUrlToDetails() {
        return urlToDetails;
    }

    /**
     * Setzt die Url des Raums.
     * 
     * @param urlToDetails
     *            Die Url des Raums.
     */
    public final void setUrlToDetails(final String urlToDetails) {
        this.urlToDetails = urlToDetails;
    }

    /**
     * Liefert die Adresse des Raums.
     * 
     * @return Die Adresse des Raums.
     */
    public final String getAddress() {
        return address;
    }

    /**
     * Setzt die Adresse des Raums.
     * 
     * @param address
     *            Die Adresse des Raums.
     */
    public final void setAddress(final String address) {
        this.address = address;
    }

    /**
     * Liefert die Info des Raums.
     * 
     * @return Die info des Raums.
     */
    public final String getDetailInfo() {
        return detailInfo;
    }

    /**
     * Setzt die Info des Raums.
     * 
     * @param detailInfo
     *            Die Info des Raums.
     */
    public final void setDetailInfo(final String detailInfo) {
        this.detailInfo = detailInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result
                + ((detailInfo == null) ? 0 : detailInfo.hashCode());
        result = prime * result
                + ((roomName == null) ? 0 : roomName.hashCode());
        result = prime * result
                + ((university == null) ? 0 : university.hashCode());
        result = prime * result
                + ((urlToDetails == null) ? 0 : urlToDetails.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Room other = (Room) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (detailInfo == null) {
            if (other.detailInfo != null) {
                return false;
            }
        } else if (!detailInfo.equals(other.detailInfo)) {
            return false;
        }
        if (roomName == null) {
            if (other.roomName != null) {
                return false;
            }
        } else if (!roomName.equals(other.roomName)) {
            return false;
        }
        if (university == null) {
            if (other.university != null) {
                return false;
            }
        } else if (!university.equals(other.university)) {
            return false;
        }
        if (urlToDetails == null) {
            if (other.urlToDetails != null) {
                return false;
            }
        } else if (!urlToDetails.equals(other.urlToDetails)) {
            return false;
        }
        return true;
    }
}
