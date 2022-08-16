package com.example.tryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.Helper.SettingSession;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button bt_regis,bt_login;
    EditText ed_name;
    EditText ed_password;
    Pengaturan p;
    ProgressDialog progressDialog;
    SettingSession ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bt_regis = findViewById(R.id.registrasi);
        bt_login = findViewById(R.id.login);
        ed_name = findViewById(R.id.username);
        ed_password = findViewById(R.id.password);

        ed_name.requestFocus();

        p = new Pengaturan();
        ss = new SettingSession(this);
        progressDialog = new ProgressDialog(LoginActivity.this);

        if(ss.readSetting("id_user") != ""){
            Intent i = new Intent(this,Dashboard_Member_Activity.class);
            finish();
            startActivity(i);
        }

        bt_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_name.getText().toString().equals("")){
                    ed_name.setError("Username harus disii");
                    ed_name.requestFocus();
                }else{
                    if(ed_password.getText().toString().equals("")){
                        ed_password.setError("Password tidak boleh kosong");
                        ed_password.requestFocus();
                    }else{
                        if(ed_password.length() < 4){
                            ed_password.setError("Password tidak boleh kurang dari 5 karakter");
                            ed_password.requestFocus();
                        }else{
                            progressDialog.setMessage("Menyiapkan Data Anda.....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            String user = ed_name.getText().toString();
                            String pass = ed_password.getText().toString();
                            Log.d("user", "user: "+user+" pass : "+pass);
                            login(user,pass);
                        }
                    }
                }

            }
        });
    }

    private void login(String user,String pass) {
        AndroidNetworking.post(p.LOGIN_URL)
                .addBodyParameter("user",""+user)
                .addBodyParameter("pass", pass)
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
                                Log.d("json object", "onResponse: "+jo);
                                ss.addUpdateSettings("name",jo.getString("username"));
                                ss.addUpdateSettings("id_user",jo.getString("id_user"));
                                ss.addUpdateSettings("level",jo.getString("level"));
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this,Dashboard_Member_Activity.class);
                                finish();
                                startActivity(i);

                            }else{
                                Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("responEdit","gagal "+anError);
                    }
                });


    }
}