package com.example.tryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Adapter.RecyclerViewAdapter;
import com.example.tryapp.Adapter.RekeningAdapter;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.model.Galang;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailDonasiActivity extends AppCompatActivity {

    String value;
    TextView tv_nm_penggalang,tv_nm_pasien,tv_penyakit,tv_alamat,tv_tentang,tv_dana,tv_judul,tv_proses,tv_kode,tv_ket_kode;
    Button btn_konfirm;
    ArrayList<String> arr_nama,arr_norek;
    ArrayList<Integer> arr_logo;
    ImageView iv_pasien;
    ProgressBar pb;
    ProgressDialog progressDialog;
    Pengaturan p = new Pengaturan();
    RecyclerView rv_rek;
    RekeningAdapter ra;
    ImageView tv_header_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_donasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Pasien");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setLogo(R.drawable.galang);
        actionBar.setHomeButtonEnabled(true);
        tv_header_title = findViewById(R.id.tv_header_title);
        tv_header_title.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_nm_penggalang = findViewById(R.id.nama_penggalang);
        tv_nm_pasien = findViewById(R.id.nama_pasien);
        tv_penyakit = findViewById(R.id.penyakit_pasien);
        tv_alamat = findViewById(R.id.alamat_pasien);
        tv_tentang = findViewById(R.id.deskripsi);
        tv_dana = findViewById(R.id.dana);
        tv_judul = findViewById(R.id.tv_judul);
        iv_pasien = findViewById(R.id.img_pasien);
        pb = findViewById(R.id.progress_b);
        tv_proses = findViewById(R.id.tv_prosses);
        rv_rek = findViewById(R.id.rv_rek);
        tv_kode = findViewById(R.id.tv_kode);
        tv_ket_kode = findViewById(R.id.tv_ket_kode);
        btn_konfirm = findViewById(R.id.btn_konfirm);

//        rv_rek.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_rek.setLayoutManager(layoutManager);
        rv_rek.setNestedScrollingEnabled(true);
        rv_rek.setHasFixedSize(true);

        progressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("id");
            //The key argument here must match that used in the other activity
        }

        tv_kode.setText("Kode unik anda : "+value);
        tv_ket_kode.setText("Silahkan transfer kerekening diatas dengan memberikan kode unik tersebut diakhir nominal transfer. contoh Rp.10000"+value+" Setelah itu lakukan konfrimasi pembayaran dibawah ini...");
        progressDialog.setMessage("Mengambil Data.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("TAG", "onCreate: "+value);
        getGalangSelected(value);
        getDataRekening();
        btn_konfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),KonfrimActivity.class);
                i.putExtra("id",value);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    void initializeArray(){
        arr_logo = new ArrayList<>();
        arr_nama = new ArrayList<>();
        arr_norek = new ArrayList<>();

        arr_logo.clear();
        arr_nama.clear();
        arr_norek.clear();
    }

    void getGalangSelected(String id){
        AndroidNetworking.get(p.URL_API+"/detailDonasi")
                .addQueryParameter("id",id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){

                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                JSONObject jo = ja.getJSONObject(0);
                                tv_judul.setText("Detail Galangan Dana : "+jo.getString("judul"));
                                Picasso.get().load(p.URL_API+"/img/"+jo.getString("gambar")).into(iv_pasien);
                                tv_nm_penggalang.setText(jo.getString("nama_lengkap"));
                                tv_nm_pasien.setText(jo.getString("nama_pasien"));
                                tv_penyakit.setText(jo.getString("menderita"));
                                tv_alamat.setText(jo.getString("alamat"));
                                tv_tentang.setText(jo.getString("deskripsi"));
                                tv_dana.setText(p.formatRupiah(Double.valueOf(jo.getString("dana"))));
                                float progress = p.presentase(jo.getString("dana"),jo.getString("terkumpul"));
                                tv_proses.setText((int) progress+"%");
                                pb.setProgress((int) progress);
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d("error",anError.getResponse().toString());
                    }
                });
        }

        void getDataRekening(){
            initializeArray();
            arr_logo.add(R.drawable.brilogo);
            arr_logo.add(R.drawable.danalogo);
            arr_logo.add(R.drawable.ovo_logo);
            arr_nama.add("Adjie Surya Anandar Siregar");
            arr_nama.add("Adjie Surya Anandar Siregar");
            arr_nama.add("Adjie Surya Anandar Siregar");
            arr_norek.add("5296-01-027971-53-1");
            arr_norek.add("0895329786655");
            arr_norek.add("085296865619");
            ra = new RekeningAdapter(getApplicationContext(),arr_logo,arr_nama,arr_norek);
            rv_rek.setAdapter(ra);
        }

}