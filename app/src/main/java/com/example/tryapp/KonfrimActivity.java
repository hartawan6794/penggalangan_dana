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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
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
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.tryapp.Helper.Pengaturan;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class KonfrimActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    ImageView iv_view;
    String value,tanggal,nama,email,gambar = "post";
    String nominal;
    EditText ed_date,ed_nm,ed_email,ed_nominal;
    Button bt_kirim,btn_upload,bt_batal;
    ProgressDialog progressDialog;
    private String Document_img1="";
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    Pengaturan p = new Pengaturan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfrim);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("id");
            requestStoragePermission();
        }

        initToolbar();;


        progressDialog = new ProgressDialog(this);
        ed_date = findViewById(R.id.post_tgl);
        ed_nm = findViewById(R.id.post_nama);
        ed_email = findViewById(R.id.email);
        ed_nominal = findViewById(R.id.post_nominal);
        bt_kirim = findViewById(R.id.btn_kirim);
        iv_view = findViewById(R.id.viewImage);
        btn_upload = findViewById(R.id.post_gambar);
        bt_batal = findViewById(R.id.bt_batal);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        ed_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(KonfrimActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Log.d("click", String.valueOf(ed_date.getText()));
            }
        });

        bt_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed_date.getText().toString().equals("")){
                    ed_date.setError("Tanggal belum di pilih");
                    ed_date.requestFocus();
                }else{
                    if(ed_email.getText().toString().contains(" ")){
                        ed_email.setError("Email tidak valid");
                        ed_email.requestFocus();
                    }else{
                        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString().trim()).matches()){
                            ed_email.setError("Email tidak valid");
                            ed_email.requestFocus();
                        }else{
                            if(ed_nominal.getText().toString().equals("")){
                                ed_nominal.setError("Nominal belum di isi");
                                ed_nominal.requestFocus();
                            }else{
                                if(gambar == "post"){
                                    btn_upload.setError("Belum upload bukti transfer");
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(KonfrimActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Konfirmasi Data");
                                    builder.setMessage("Anda yakin data sudah sesuai ?");
                                    builder.setPositiveButton("Ya",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    progressDialog.setMessage("Mengirim Data.....");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();
                                                    tanggal = ed_date.getText().toString();
                                                    nama = ed_nm.getText().toString();
                                                    email = ed_email.getText().toString();
                                                    nominal = ed_nominal.getText().toString().trim();

                                                    kirimDonasi();
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
                            }
                        }
                    }
                }



            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        bt_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Form Donasi");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        ed_date.setText(dateFormat.format(myCalendar.getTime()));
    }

    void kirimDonasi(){
        AndroidNetworking.post(p.URL_API+"/postDonasi")
                .addBodyParameter("tgl_transfer",""+tanggal)
                .addBodyParameter("nominal",""+nominal)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("email",""+email)
                .addBodyParameter("image",""+gambar)
                .addBodyParameter("id",""+value)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("error");
    //                            Log.d("respon",""+response.getString("result"));
                            if(!status){
                                Toast.makeText(KonfrimActivity.this, "Data berhasil di kirim", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(KonfrimActivity.this, "Data gagal di kirim", Toast.LENGTH_SHORT).show();
                            }
                            Intent i = new Intent(KonfrimActivity.this,DetailDonasiActivity.class);
                            i.putExtra("id",value);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void selectImage() {
        final CharSequence[] options = { "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(KonfrimActivity.this);
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
                gambar =getFileName(selectedImageUri);
                Log.d("name image", "name " +nama);
                Picasso.get().load(selectedImageUri).fit().into(iv_view);
                btn_upload.setVisibility(View.GONE);
                OkHttpClient client = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(false)
                        .build();

                AndroidNetworking.initialize(KonfrimActivity.this,client);


                AndroidNetworking.upload(p.URL_API+"/uploadFile")
                        .addMultipartFile("file",imageFile)
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
                                Log.d("response", "onResponse: "+response);

                                progressDialog.dismiss();
                                try{
                                    Boolean status = response.getBoolean("success");
                                    if(status){
                                        Toast.makeText(KonfrimActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("response", "onResponse: "+anError.getErrorBody());

                            }
                        });

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
}