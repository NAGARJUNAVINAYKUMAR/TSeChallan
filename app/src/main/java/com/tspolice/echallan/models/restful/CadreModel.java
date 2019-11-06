package com.tspolice.echallan.models.restful;

import java.io.Serializable;

public class CadreModel implements Serializable {
    private int cadre_cd;
    private String cadre_sf, cadre_name;

    public CadreModel() {
        // default constructor
    }

    public int getCadreCode() {
        return cadre_cd;
    }

    public void setCadreCode(int cadreCode) {
        this.cadre_cd = cadreCode;
    }

    public String getCadreSF() {
        return cadre_sf;
    }

    public void setCadreSF(String cadreSF) {
        this.cadre_sf = cadreSF;
    }

    public String getCadreName() {
        return cadre_name;
    }

    public void setCadreName(String cadreName) {
        this.cadre_name = cadreName;
    }
}
