package com.s300373.studentliste;

import java.util.Date;

/**
 * Created by RudiAndre on 22.10.2017.
 */

public class Melding {
    long _ID;
    String tidspunkt;
    String melding;
    String status;

    public Melding() {
    }

    public Melding(String tidspunkt, String melding) {
        this.tidspunkt = tidspunkt;
        this.melding = melding;
    }

    public Melding(long _ID, String tidspunkt, String melding, String status) {
        this._ID = _ID;
        this.tidspunkt = tidspunkt;
        this.melding = melding;
        this.status = status;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(String tidspunkt) {
        this.tidspunkt = tidspunkt;
    }

    public String getMelding() {
        return melding;
    }

    public void setMelding(String melding) {
        this.melding = melding;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
