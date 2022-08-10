package com.example.tryapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.model.Galang;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailDonasiActivity extends AppCompatActivity {

    String value;
    TextView tv_nm_penggalang,tv_nm_pasien,tv_penyakit,tv_alamat,tv_tentang,tv_dana,tv_judul;
    ImageView iv_pasien;
    Galang g;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_donasi);

        tv_nm_penggalang = findViewById(R.id.nama_penggalang);
        tv_nm_pasien = findViewById(R.id.nama_pasien);
        tv_penyakit = findViewById(R.id.penyakit_pasien);
        tv_alamat = findViewById(R.id.alamat_pasien);
        tv_tentang = findViewById(R.id.deskripsi);
        tv_dana = findViewById(R.id.dana);
        tv_judul = findViewById(R.id.tv_judul);
        iv_pasien = findViewById(R.id.img_pasien);
        progressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("id");
            //The key argument here must match that used in the other activity
        }
        progressDialog.setMessage("Mengambil Data.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getGalangSelected(value);
    }

    void getGalangSelected(String id){
        AndroidNetworking.post("https://penggalangandanakanker.ptmutiaraferindo.my.id/json/select_gelengan.php")
                .addBodyParameter("id",""+id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){

                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                JSONObject jo = ja.getJSONObject(0);
                                tv_judul.setText("Detail Galangan Dana : "+jo.getString("judul"));
                                g = new Galang(jo.getString("id_galang"),jo.getString("id_member"),
                                        jo.getString("judul"),jo.getString("nama_pasien"),jo.getString("menderita"),
                                        jo.getString("deskripsi"),jo.getString("tgl_mulai"),jo.getString("alamat"),jo.getString("tgl_selesai"),
                                        jo.getString("dana"),jo.getString("terkumpul"),jo.getString("gambar"),jo.getString("bukti_surat"),jo.getString("status"));
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("responEdit","gagal");
                    }
                });
        }
}