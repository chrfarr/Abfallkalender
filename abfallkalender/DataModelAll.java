package de.farr_net.abfallkalender;

/**
 * Created by cfarr on 27.12.16.
 */

class DataModelAll {

    private String wochentag;
    private String fdatum;
    private String rest;
    private String papier;
    private String gruen;
    private String bio;
    private String sonstiges;

    DataModelAll(String wochentag, String fdatum, String rest, String papier, String gruen, String bio, String sonstiges) {
        this.wochentag=wochentag;
        this.fdatum=fdatum;
        this.rest=rest;
        this.papier=papier;
        this.gruen=gruen;
        this.bio=bio;
        this.sonstiges=sonstiges;

    }

    String getWochentag() { return wochentag; }

    String getDatum() {
        return fdatum;
    }

    String getRest() {
        return rest;
    }

    String getPapier() {
        return papier;
    }

    String getGruen() {
        return gruen;
    }

    String getBio() {
        return bio;
    }

    String getSonstiges() {
        if (sonstiges.contentEquals("-")) {
            sonstiges = "";
        }
        return sonstiges;
    }

}