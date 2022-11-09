package com.example.tryapp.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

public class Pengaturan {

    public static final String GALANGAN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/galangan.php";
    public static final String INSERT_GALANGAN = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/insert_galangan.php";
    public static final String SELECT_GALANGAN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_galangan.php";
    public static final String SELECT_GALANGAN_USER_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_galangan_user.php";
    public static final String SELECT_GALANGAN_ADMIN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_galangan_admin.php";
    public static final String RIWAYAT_GALANGAN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/galangan_selesai.php";
    public static final String SELECT_DETAIL_RIWAYAT_GALANGAN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_galangan_selesai.php";
    public static final String SELECT_DETAIL_RIWAYAT_GALANGAN_ADMIN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_galangan_selesai_admin.php";
    public static final String INSERT_DONASI = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/insert_donasi.php";
    public static final String UPLOAD_IMG_DONASI = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/upload_donasi.php";
    public static final String LOGIN_URL = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/login.php";
    public static final String SELECT_MEMBER = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_member.php";
    public static final String INSERT_MEMBER = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/insert_member.php";
    public static final String UPDATE_MEMBER = "https://penggalangandanakanker.ptmutiaraferindo.my.id/json/update_member.php";
    public static final String PATH_IMAGE = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/";

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
