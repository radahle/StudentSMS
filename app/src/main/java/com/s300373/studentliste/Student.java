package com.s300373.studentliste;

/**
 * Created by RudiAndre on 11.10.2017.
 */

public class Student {
    long _ID;
    String fornavn;
    String etternavn;
    String telefon;

    public Student() {

    }

    public Student(long _ID) {
        this._ID = _ID;
    }

    public Student(String fornavn, String etternavn) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
    }

    public Student(String fornavn, String etternavn, String telefon) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.telefon = telefon;
    }

    public Student(String fornavn, String etternavn, String telefon, long _ID) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.telefon = telefon;
        this._ID = _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public long get_ID() {
        return _ID;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getTelefon() {
        return telefon;
    }
}
