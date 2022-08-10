package com.example.tryapp.Helper;

import java.text.NumberFormat;
import java.util.Locale;

public class Pengaturan {

    public static String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

}
