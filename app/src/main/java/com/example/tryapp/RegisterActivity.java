package com.example.tryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Helper.Pengaturan;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Spinner s_jk;
    EditText ed_nik,ed_nama,ed_user,ed_pass,ed_pekerjaan,ed_alamat,ed_telp;
    Button bt_simpan,bt_cancel;

    Pengaturan p;

    String[] jk = {"--Pilih Jenis Kelamin--","Laki-laki","Perempuan"};
    String nik,nama,user,pass,jk_s,pekerjaan,alamat,telp;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        p = new Pengaturan();
        progressDialog = new ProgressDialog(this);

        s_jk = findViewById(R.id.s_jenis_kelamin);
        ed_nik = findViewById(R.id.ed_nik);
        ed_nama = findViewById(R.id.ed_nama);
        ed_user = findViewById(R.id.ed_user);
        ed_pass = findViewById(R.id.ed_pass);
        ed_pekerjaan = findViewById(R.id.ed_pekerjaan);
        ed_alamat = findViewById(R.id.ed_alamat);
        ed_telp = findViewById(R.id.ed_telp);
        bt_simpan = findViewById(R.id.bt_simpan);
        bt_cancel = findViewById(R.id.bt_cancel);

        ed_nik.requestFocus();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Halaman Daftar Akun");
        actionBar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayAdapter<String> s_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jk);

        s_jk.setAdapter(s_adapter);

        s_jk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("Jenis Kelamin", "onItemClick: "+s_adapter.getItemId(i));
                jk_s = s_adapter.getItem(i);
//                Log.d("String", "onItemSelected: "+name);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Mengambil Data.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                nik = ed_nik.getText().toString();
                nama = ed_nama.getText().toString();
                user = ed_user.getText().toString();
                pass = ed_pass.getText().toString();
                pekerjaan = ed_pekerjaan.getText().toString();
                alamat = ed_alamat.getText().toString();
                telp = ed_telp.getText().toString();
                validation();
            }
        });

    }

    private void validation(){
        if(ed_nik.getText().toString().equals("")){
            ed_nik.requestFocus();
            ed_nik.setError("Masukan nik anda");
        }else{
            if(ed_nik.length() < 16){
                ed_nik.requestFocus();
                ed_nik.setError("Panjang NIK kurang dari 16");
            }else{
                if(ed_user.getText().toString().equals("")){
                    ed_user.requestFocus();
                    ed_user.setError("Masukan user name");
                }else{
                    if(ed_user.getText().toString().contains(" ")){
                        ed_user.requestFocus();
                        ed_user.setError("Username harap tanpa spasi");
                    }else{
                        if(ed_nama.getText().toString().equals("")){
                            ed_nama.requestFocus();
                            ed_nama.setError("Masukan nama anda");
                        }else{
                            if(ed_pass.getText().toString().equals("")){
                                ed_pass.requestFocus();
                                ed_pass.setError("Password tidak boleh kosong");
                            }else{
                                if(ed_pass.length() < 4){
                                    ed_pass.requestFocus();
                                    ed_pass.setError("Password harus lebih dari 4 karakter");
                                }else{
                                    if(jk_s == "--Pilih Jenis Kelamin--" || jk_s == ""){
                                        ((TextView)s_jk.getSelectedView()).setError("Harap memilih jenis kelamin");
                                    }else{
                                        if(ed_pekerjaan.getText().toString().equals("")){
                                            ed_pekerjaan.requestFocus();
                                            ed_pekerjaan.setError("Harap masukan pekerjaan anda");
                                        }else{
                                            if(ed_alamat.getText().toString().equals("")){
                                                ed_alamat.requestFocus();
                                                ed_alamat.setError("Masukan alamat anda");
                                            }else{
                                                if(ed_telp.getText().toString().equals("")){
                                                    ed_telp.requestFocus();
                                                    ed_telp.setError("Masukan nomor telpon anda");
                                                }else{
    //                                                progressDialog.dismiss();
                                                    Toast.makeText(this, "Menyimpan Data", Toast.LENGTH_SHORT).show();
                                                    insert_data(nik,nama,user,pass,jk_s,pekerjaan,alamat,telp);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void insert_data(String nik, String nama, String user,String pass,String jk, String pekerjaan, String alamat, String telp ){
        AndroidNetworking.post(p.INSERT_MEMBER)
                .addBodyParameter("nik",""+nik)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("user",""+user)
                .addBodyParameter("pass",""+pass)
                .addBodyParameter("jk",""+jk)
                .addBodyParameter("pekerjaan",""+pekerjaan)
                .addBodyParameter("alamat",""+alamat)
                .addBodyParameter("telp",""+telp)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            //                            Log.d("respon",""+response.getString("result"));
                            if(status){
                                Toast.makeText(RegisterActivity.this, "Data berhasil di kirim, Silahkan login kembali", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Data gagal di kirim", Toast.LENGTH_SHORT).show();
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