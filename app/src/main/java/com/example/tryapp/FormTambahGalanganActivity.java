package com.example.tryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.Helper.SettingSession;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class FormTambahGalanganActivity extends AppCompatActivity implements View.OnClickListener {

    final Calendar myCalendar= Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateMulai, dateSelesai;
    private static final int STORAGE_PERMISSION_CODE = 123;
    EditText ed_judul,ed_nama_pasien,ed_penyakit,ed_alamat,ed_tentang,ed_tgl_mulai,ed_tgl_selesai,ed_dana;
    ImageView iv_poto,iv_bukti;
    Button bt_simpan,bt_batal,bt_post_poto,bt_post_bukti;
    ProgressDialog progressDialog;

    String poto_pasien ="post",poto_bukti="post";
    Pengaturan p;
    SettingSession ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_tambah_galangan);
        initToolbar();
        initVariable();
        requestStoragePermission();

        dateMulai=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateTglMulai();
            }
        };

         dateSelesai=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateTglSelesai();
            }
        };

        bt_simpan.setOnClickListener(this);
        bt_batal.setOnClickListener(this);
        ed_tgl_mulai.setOnClickListener(this);
        ed_tgl_selesai.setOnClickListener(this);
        bt_post_poto.setOnClickListener(this);
        bt_post_bukti.setOnClickListener(this);
    }

    private void requestStoragePermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                return;

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }
            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

    }

    private void updateTglMulai(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        ed_tgl_mulai.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateTglSelesai(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        ed_tgl_selesai.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Form Tambah Galangan");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initVariable(){
        ed_judul = findViewById(R.id.post_judul);
        ed_nama_pasien = findViewById(R.id.post_nama);
        ed_penyakit = findViewById(R.id.post_penyakit);
        ed_alamat = findViewById(R.id.post_alamat);
        ed_tentang = findViewById(R.id.post_tentang);
        ed_tgl_mulai = findViewById(R.id.post_tgl_mulai);
        ed_tgl_selesai = findViewById(R.id.post_tgl_selesai);
        ed_dana = findViewById(R.id.post_dana);

        bt_post_poto = findViewById(R.id.post_poto_pasien);
        bt_post_bukti = findViewById(R.id.post_poto_bukti);
        bt_simpan = findViewById(R.id.btn_simpan);
        bt_batal = findViewById(R.id.btn_batal);

        iv_poto = findViewById(R.id.viewImage);
        iv_bukti = findViewById(R.id.viewBukti);

        p = new Pengaturan();
        progressDialog = new ProgressDialog(this);
        ss = new SettingSession(this);
    }

    private void selectImage() {
        final CharSequence[] options = { "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FormTambahGalanganActivity.this);
        builder.setTitle("Upload Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Pilih Dari Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectImageBukti() {
        final CharSequence[] options = { "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FormTambahGalanganActivity.this);
        builder.setTitle("Upload Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Pilih Dari Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {

        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                final Uri selectedImageUri = data.getData();
                String imagepath = getRealPathFromURI(selectedImageUri,this);
                File imageFile = new File(imagepath);
                poto_pasien =getFileName(selectedImageUri);
//                Log.d("name image", "name " +nama);
                Picasso.get().load(selectedImageUri).fit().into(iv_poto);
                bt_post_poto.setVisibility(View.GONE);
                uploadToServer(imageFile);

            }if (requestCode == 2) {
                final Uri selectedImageUri = data.getData();
                String imagepath = getRealPathFromURI(selectedImageUri,this);
                File imageFile = new File(imagepath);
                poto_bukti =getFileName(selectedImageUri);
//                Log.d("name image", "name " +nama);
                Picasso.get().load(selectedImageUri).fit().into(iv_bukti);
                bt_post_bukti.setVisibility(View.GONE);
                uploadToServer(imageFile);

            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        if(view == bt_post_poto){
            selectImage();
        }else if(view == bt_post_bukti){
            selectImageBukti();
        }else if(view == ed_tgl_mulai){
            new DatePickerDialog(FormTambahGalanganActivity.this,dateMulai,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }else if(view == ed_tgl_selesai){
            new DatePickerDialog(FormTambahGalanganActivity.this,dateSelesai,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }else if(view == bt_simpan){
            AlertDialog.Builder builder = new AlertDialog.Builder(FormTambahGalanganActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Simpan");
            builder.setMessage("Anda yakin data telah sesuai ?");
            builder.setPositiveButton("Ya",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage("Mengirim Data.....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            validasi();
                        }
                    });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }else if(view == bt_batal){
            onBackPressed();
        }
    }

    private void uploadToServer(File file){
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .build();

        AndroidNetworking.initialize(FormTambahGalanganActivity.this,client);


        AndroidNetworking.upload(p.UPLOAD_IMG_DONASI)
                .addMultipartFile("file",file)
                //.addMultipartParameter("key","value")
                //.setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        int peroses = (int) ((bytesUploaded/totalBytes) *100);
                        progressDialog.setMessage("Sedang Mengupload \n"+ peroses + " %");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Log.d("progress", "onProgress: "+(bytesUploaded / totalBytes)*100 + " %");
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("response", "onResponse: "+response);
                        try{
                            Boolean status = response.getBoolean("success");
                            if(status){
                                Toast.makeText(FormTambahGalanganActivity.this, "Berhasil Mengupload", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("response", "onResponse: "+anError);

                    }
                });

    }

    private void validasi(){
        if(ed_judul.getText().toString().equals("")){
            ed_judul.setError("Judul belum di isi");
            ed_judul.requestFocus();
        }else{
            if(ed_nama_pasien.getText().toString().equals("")){
                ed_nama_pasien.setError("Nama pasien belum di isi");
                ed_nama_pasien.requestFocus();
            }else{
                if(ed_penyakit.getText().toString().equals("")){
                    ed_penyakit.setError("Nama penyakit belum di isi");
                    ed_penyakit.requestFocus();
                }else {
                    if(ed_alamat.getText().toString().equals("")){
                        ed_alamat.setError("Alamat belum di isi");
                        ed_alamat.requestFocus();
                    }else{
                        if(ed_tentang.getText().toString().equals("")){
                            ed_tentang.setError("Tentang pasien belum di isi");
                            ed_tentang.requestFocus();
                        }else{
                            if(ed_tgl_mulai.getText().toString().equals("")){
                                ed_tgl_mulai.setError("Tanggal mulai belum di isi");
                                ed_tgl_mulai.requestFocus();
                            }else {
                                if(ed_tgl_selesai.getText().toString().equals("")){
                                    ed_tgl_selesai.setError("Tanggal selesai belum di isi");
                                    ed_tgl_selesai.requestFocus();
                                }else {
                                    if(ed_dana.getText().toString().equals("")){
                                        ed_dana.setError("Dana Belum di isi");
                                        ed_dana.requestFocus();
                                    }else{
                                        if(poto_pasien == "post"){
                                            bt_post_poto.setError("Poto pasien belum di upload");
                                        }else{
                                            if(poto_bukti == "post"){
                                                bt_post_bukti.setError("Poto bukti belum di upload");
                                            }else{
//                                                Toast.makeText(this, "Mengirim Data", Toast.LENGTH_SHORT).show();
                                                simpanData();
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

    private void simpanData(){
        String judul = ed_judul.getText().toString();
        String nama = ed_nama_pasien.getText().toString();
        String penyakit = ed_penyakit.getText().toString();
        String alamat = ed_alamat.getText().toString();
        String tentang = ed_tentang.getText().toString();
        String tgl_mulai = ed_tgl_mulai.getText().toString();
        String tgl_selesai = ed_tgl_selesai.getText().toString();
        String dana = ed_dana.getText().toString();
        String id = ss.readSetting("id_user");
        AndroidNetworking.post(p.INSERT_GALANGAN)
                .addBodyParameter("id_member",""+id)
                .addBodyParameter("judul",""+judul)
                .addBodyParameter("nama_pasien",""+nama)
                .addBodyParameter("menderita",""+penyakit)
                .addBodyParameter("deskripsi",""+tentang)
                .addBodyParameter("alamat",""+alamat)
                .addBodyParameter("tgl_mulai",""+tgl_mulai)
                .addBodyParameter("tgl_selesai",""+tgl_selesai)
                .addBodyParameter("dana",""+dana)
                .addBodyParameter("gambar",""+poto_pasien)
                .addBodyParameter("bukti_surat",""+poto_bukti)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("success");
                            //                            Log.d("respon",""+response.getString("result"));
                            if(status){
                                Toast.makeText(FormTambahGalanganActivity.this, "Data berhasil di simpan", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(FormTambahGalanganActivity.this, "Data gagal di kirim", Toast.LENGTH_SHORT).show();
                            }
                            Intent i = new Intent(FormTambahGalanganActivity.this,Dashboard_Member_Activity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(i);
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