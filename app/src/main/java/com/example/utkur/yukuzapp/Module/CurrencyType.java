package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 10/31/2017.
 */

public class CurrencyType {
    private int id;
    private String sign;
    private String title;

    private CurrencyType() {
    }

    public CurrencyType(int id, String sign, String title) {
        this.id = id;
        this.sign = sign;
        this.title = title;
    }

    public static CurrencyType getInstance() {
        return new CurrencyType();
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }
}
