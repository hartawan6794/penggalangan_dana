package com.example.tryapp.Helper;

import java.text.NumberFormat;
import java.util.Locale;

public class Pengaturan {

    public static final String GALANGAN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/galangan.php";
    public static final String SELECT_GALANGAN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_galangan.php";
    public static final String INSERT_DONASI = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/insert_donasi.php";
    public static final String UPLOAD_IMG_DONASI = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/upload_donasi.php";

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
