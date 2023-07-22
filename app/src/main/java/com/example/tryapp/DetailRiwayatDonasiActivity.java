package com.example.tryapp;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Helper.Pengaturan;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailRiwayatDonasiActivity extends AppCompatActivity {

    private final static String TAG = "DetailRiwayatActivity";
    ImageView img_gambar,img_bukti;
    TextView tv_nm_penggalang,tv_nm_pasien,tv_penyakit,tv_alamat,tv_tentang,tv_dana,tv_judul,tv_terkumpul;
    ProgressDialog progressDialog;
    Pengaturan p = new Pengaturan();
    String id;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_donasi);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            id = extra.getString("id");
        }
        initToolbar();
        initVariable();
        progress();
        getData(id);
    }

    private void progress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void initVariable(){
        img_gambar = findViewById(R.id.img_gambar);
        img_bukti = findViewById(R.id.img_bukti);
        tv_nm_penggalang = findViewById(R.id.nama_penggalang);
        tv_nm_pasien = findViewById(R.id.nama_pasien);
        tv_penyakit = findViewById(R.id.penyakit_pasien);
        tv_alamat = findViewById(R.id.alamat_pasien);
        tv_tentang = findViewById(R.id.deskripsi);
        tv_dana = findViewById(R.id.dana);
        tv_judul = findViewById(R.id.tv_judul);
        tv_terkumpul = findViewById(R.id.tv_terkumpul);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Riwayat Detail Galangan Dana");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getData(String id){
        AndroidNetworking.get(p.URL_API+"/galangan")
                .addQueryParameter("id",id)
                .addQueryParameter("type","satuan")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
//                                JSONArray ja = response.getJSONArray("status");
                                JSONObject jo = response.getJSONObject("result");
                                String judul = jo.getString("judul");
                                tv_nm_penggalang.setText(jo.getString("nama_lengkap"));
                                tv_judul.setText("Detail Galangan Dana : "+judul);
                                Picasso.get().load(p.URL_API+"/img/"+jo.getString("gambar")).into(img_gambar);
                                Picasso.get().load(p.URL_API+"/img/"+jo.getString("bukti")).into(img_bukti);
                                tv_nm_pasien.setText(jo.getString("nama_pasien"));
                                tv_penyakit.setText(jo.getString("menderita"));
                                tv_alamat.setText(jo.getString("alamat"));
                                tv_tentang.setText(jo.getString("deskripsi"));
                                tv_dana.setText(p.formatRupiah(Double.valueOf(jo.getString("dana"))));
                                tv_terkumpul.setText(p.formatRupiah(Double.valueOf(jo.getString("terkumpul"))));
                            }else{
                                Toast.makeText(DetailRiwayatDonasiActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG,"ERROR: "+anError.getErrorBody());
                    }
                });
    }


}