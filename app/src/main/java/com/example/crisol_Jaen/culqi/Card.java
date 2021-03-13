package com.example.crisol_Jaen.culqi;

public class Card {

    private String card_number, cvv, email;

    private int expiration_month, expiration_year;

    public Card(){ }

    public Card(String card_number, String cvv, String email, int expiration_month, int expiration_year) {
        this.card_number = card_number;
        this.cvv = cvv;
        this.email = email;
        this.expiration_month = expiration_month;
        this.expiration_year = expiration_year;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getExpiration_month() {
        return expiration_month;
    }

    public void setExpiration_month(int expiration_month) {
        this.expiration_month = expiration_month;
    }

    public int getExpiration_year() {
        return expiration_year;
    }

    public void setExpiration_year(int expiration_year) {
        this.expiration_year = expiration_year;
    }
}
