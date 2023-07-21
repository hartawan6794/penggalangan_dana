package com.example.tryapp;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Adapter.GalanganUserAdapter;
import com.example.tryapp.Adapter.RiwayatAdapter;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.Helper.SettingSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalanganDanaUserActivity extends AppCompatActivity {

    RecyclerView rv_main;
    GalanganUserAdapter adapter;
    private ArrayList<String> arr_judul,arr_nama,arr_penyakit,arr_alamat,arr_tgl_mulai,arr_tgl_selesai,arr_dana,arr_terkumpul,arr_img,arr_status;
    ProgressDialog progressDialog;
    SettingSession ss;
    Pengaturan p;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galangan_dana_user);
        rv_main = findViewById(R.id.rv_daftar_galangan);

        initLayoutAdapter();
        initToolbar();

        ss = new SettingSession(this);
        p = new Pengaturan();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String id = ss.readSetting("id_user");

//        if(id == "1"){
//            url = p.SELECT_GALANGAN_ADMIN_URL;
//        }else{
//            url = p.SELECT_GALANGAN_USER_URL;
//        }

        getData(id,url);
//        getData(id);


    }

    private void getData(String id,String url){
        initArray();
        AndroidNetworking.post(url)
                .addBodyParameter("id_member",""+id)
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("respone", "onResponse: "+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);

                                    arr_nama.add(jo.getString("nama_pasien"));
                                    arr_judul.add(jo.getString("judul"));
                                    arr_penyakit.add(jo.getString("menderita"));
                                    arr_alamat.add(jo.getString("alamat"));
                                    arr_tgl_mulai.add(jo.getString("tgl_mulai"));
                                    arr_tgl_selesai.add(jo.getString("tgl_selesai"));
                                    arr_dana.add(jo.getString("dana"));
                                    arr_terkumpul.add(jo.getString("terkumpul"));
                                    arr_img.add(jo.getString("gambar"));
                                    arr_status.add(jo.getString("status"));
                                }
                                adapter = new GalanganUserAdapter(getApplicationContext(),arr_judul,arr_nama,arr_penyakit,arr_alamat,arr_tgl_mulai,arr_tgl_selesai,arr_dana,arr_terkumpul,arr_img,arr_status);
                                rv_main.setAdapter(adapter);
                            }else{
                                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Galangan Dana Anda");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initLayoutAdapter(){
        rv_main.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_main.setLayoutManager(linearLayoutManager);
        rv_main.setNestedScrollingEnabled(true);
        rv_main.setHasFixedSize(false);
    }

    private void initArray(){
        arr_judul = new ArrayList<>();
        arr_nama = new ArrayList<>();
        arr_dana = new ArrayList<>();
        arr_img = new ArrayList<>();
        arr_terkumpul = new ArrayList<>();
        arr_alamat = new ArrayList<>();
        arr_penyakit = new ArrayList<>();
        arr_status = new ArrayList<>();
        arr_tgl_mulai = new ArrayList<>();
        arr_tgl_selesai = new ArrayList<>();

        arr_judul.clear();
        arr_nama.clear();
        arr_dana.clear();
        arr_img.clear();
        arr_terkumpul.clear();
        arr_alamat.clear();
        arr_penyakit.clear();
        arr_status.clear();
        arr_tgl_mulai.clear();
        arr_tgl_selesai.clear();
    }
}