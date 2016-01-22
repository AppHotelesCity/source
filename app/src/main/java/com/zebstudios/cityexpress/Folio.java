package com.zebstudios.cityexpress;

/**
 * Created by DanyCarreto on 21/01/16.
 */
public class Folio {
    String folio;
    String balance;

    public Folio(String folio, String balance) {
        this.folio = folio;
        this.balance = balance;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
