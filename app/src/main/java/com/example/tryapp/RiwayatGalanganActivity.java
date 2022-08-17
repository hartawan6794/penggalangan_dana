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
import com.example.tryapp.Adapter.RecyclerViewAdapter;
import com.example.tryapp.Adapter.RiwayatAdapter;
import com.example.tryapp.Helper.Pengaturan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RiwayatGalanganActivity extends AppCompatActivity {

    RecyclerView rv_riwayat;
    RiwayatAdapter riwayatAdapter;
    Pengaturan p;
    private ArrayList<String> arr_id,arr_judul,arr_img,arr_dana,arr_terkumpul;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_galangan);
        initToolbar();

        rv_riwayat = findViewById(R.id.rv_riwayat);
        initLayoutAdapter();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengambil Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getData();

    }

    private void initArray(){
        arr_id = new ArrayList<>();
        arr_judul = new ArrayList<>();
        arr_dana = new ArrayList<>();
        arr_terkumpul = new ArrayList<>();
        arr_img = new ArrayList<>();
        p = new Pengaturan();

        arr_id.clear();
        arr_dana.clear();
        arr_judul.clear();
        arr_terkumpul.clear();
        arr_img.clear();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Riwayat Penggalangan Dana");
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
        rv_riwayat.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_riwayat.setLayoutManager(linearLayoutManager);
        rv_riwayat.setNestedScrollingEnabled(true);
        rv_riwayat.setHasFixedSize(false);
    }

    private void getData(){
        initArray();
        AndroidNetworking.get(p.RIWAYAT_GALANGAN_URL)
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


                                    arr_id.add(jo.getString("id_galang"));
                                    arr_judul.add(jo.getString("judul"));
                                    arr_dana.add(jo.getString("dana"));
                                    arr_img.add(jo.getString("gambar"));
                                    arr_terkumpul.add(jo.getString("terkumpul"));
                                }
                                riwayatAdapter = new RiwayatAdapter(getApplicationContext(),arr_id,arr_judul,arr_img,arr_dana,arr_terkumpul);
                                rv_riwayat.setAdapter(riwayatAdapter);
                            }else{
                                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                                riwayatAdapter = new RiwayatAdapter(getApplicationContext(),arr_id,arr_judul,arr_img,arr_dana,arr_terkumpul);
                                rv_riwayat.setAdapter(riwayatAdapter);
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
}