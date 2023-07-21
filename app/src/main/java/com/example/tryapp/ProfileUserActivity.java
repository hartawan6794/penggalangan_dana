package com.example.tryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.Helper.SettingSession;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileUserActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_nik,tv_nama,tv_user,tv_jk,tv_pekerjaan,tv_alamat,tv_telp;
    Button bt_ubah;
    String id;
    SettingSession ss;
    Pengaturan p;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        tv_nik = findViewById(R.id.tv_nik);
        tv_nama = findViewById(R.id.tv_nama);
        tv_user = findViewById(R.id.tv_user);
        tv_jk = findViewById(R.id.tv_jk);
        tv_pekerjaan = findViewById(R.id.tv_pekerjaan);
        tv_telp = findViewById(R.id.tv_telp);
        tv_alamat = findViewById(R.id.tv_alamat);
        bt_ubah = findViewById(R.id.bt_edit);

        ss = new SettingSession(this);
        id = ss.readSetting("id_user");
        p = new Pengaturan();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang mengamnil data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profil Member");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getData(id);

        bt_ubah.setOnClickListener(this);

    }


    private void getData(String id){
        AndroidNetworking.post(p.URL_API)
                .addBodyParameter("id",""+id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
//                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
//                                Log.d("respon",""+ja);
                                JSONObject jo = ja.getJSONObject(0);
                                tv_nik.setText(jo.getString("nik"));;
                                tv_nama.setText(jo.getString("nama_lengkap"));;
                                tv_user.setText(jo.getString("username"));
                                tv_jk.setText(jo.getString("jk"));
                                tv_pekerjaan.setText(jo.getString("pekerjaan"));
                                tv_alamat.setText(jo.getString("alamat"));
                                tv_telp.setText(jo.getString("telp"));
                            }else{
                                tv_nik.setText("admin");;
                                tv_nama.setText("admin");;
                                tv_user.setText("admin");
                                tv_jk.setText("admin");
                                tv_pekerjaan.setText("admin");
                                tv_alamat.setText("admin");
                                tv_telp.setText("admin");
                                bt_ubah.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("responEdit","gagal"+anError);
                    }
                });
    }

    @Override
    public void onClick(View view) {

        if(view == bt_ubah){
            Intent i = new Intent(this,RegisterActivity.class);
            i.putExtra("id",id);
            finish();
            startActivity(i);
        }
    }
}