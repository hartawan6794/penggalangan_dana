package com.example.tryapp.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

public class Pengaturan {

    public static final String URL_API = "https://penggalangandanakanker.grosirsalsabila.my.id/api";

    public static String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public static float presentase(String d, String t){
        float dana = Integer.parseInt(d);
        float terkumpul = Integer.parseInt(t);
        float progress = (terkumpul/dana)*100;
        return progress;
    }


}
