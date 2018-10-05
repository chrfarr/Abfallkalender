package de.farr_net.abfallkalender;

/**
 * Created by cfarr on 27.12.16.
 */

class DataModelNext {

    private String wochentag;
    private String fdatum;
    private String typ;

    DataModelNext(String wochentag, String fdatum, String typ) {
        this.wochentag=wochentag;
        this.fdatum=fdatum;
        this.typ=typ;
    }

    String getWochentag() {
        return wochentag;
    }

    String getDatum() {
        return fdatum;
    }

    String getTyp() { return typ; }

}