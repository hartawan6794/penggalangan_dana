package com.example.tryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tryapp.Helper.SettingSession;

import java.util.Locale;

public class Dashboard_Member_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_user;
    String username;
    SettingSession ss;
    ImageView iv_logout;
    CardView cv_profil,cv_riwayat,cv_tambah_galangan,cv_daftar_galangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_member);
        initToolbar();

        tv_user = findViewById(R.id.selemat_datang);
        iv_logout = findViewById(R.id.iv_logout);
        cv_profil = findViewById(R.id.profile);
        cv_riwayat = findViewById(R.id.riwayat);
        cv_daftar_galangan = findViewById(R.id.daftar_golongan);
        cv_tambah_galangan = findViewById(R.id.tambah_galangan);
        cv_daftar_galangan = findViewById(R.id.daftar_golongan);

        ss = new SettingSession(this);
        username = ss.readSetting("name");
        tv_user.setText("Selamat Datang "+username);
        iv_logout.setVisibility(View.VISIBLE);


        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        cv_profil.setOnClickListener(this);
        cv_riwayat.setOnClickListener(this);
        cv_tambah_galangan.setOnClickListener(this);
        cv_daftar_galangan.setOnClickListener(this);

    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard Member");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard_Member_Activity.this);
        builder.setCancelable(true);
        builder.setTitle("Keluar Aplikasi");
        builder.setMessage("Anda yakin ingin keluar ?");
        builder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ss.deleteAllSettings();
                        Intent i = new Intent(Dashboard_Member_Activity.this,MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        Intent i;
        if(view == cv_profil){
            i = new Intent(this,ProfileUserActivity.class);
            startActivity(i);
        }else if(view == cv_riwayat){
            i = new Intent(this,RiwayatGalanganActivity.class);
            startActivity(i);
        }else if(view == cv_tambah_galangan){
            i = new Intent(this,FormTambahGalanganActivity.class);
            startActivity(i);
        }else if(view == cv_daftar_galangan){
            i = new Intent(this,GalanganDanaUserActivity.class);
            startActivity(i);
        }
    }
}