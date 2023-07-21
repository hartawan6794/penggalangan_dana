package com.example.tryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Adapter.RecyclerViewAdapter;
import com.example.tryapp.Adapter.SliderAdapter;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.Helper.SettingSession;
import com.example.tryapp.model.Slider;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView btn_login,iv_logo,iv_logout;
    TextView tv_viewAll;
//    SwipeRefreshLayout srl_main;
    RecyclerView rv_main;
    ArrayList<String> arr_id,arr_nm_penggalang,arr_judul,arr_dana,arr_menderita,arr_img,arr_terkumpul;
    ProgressDialog progressDialog;

    RecyclerViewAdapter recycleViewAdapter;
    Pengaturan p = new Pengaturan();
    boolean doubleBackToExitPressedOnce = false;
    SettingSession ss;

    String url1 = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/slider-01.jpg";
    String url2 = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/slider-02.jpg";
    String url3 = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/slider-03.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ss = new SettingSession(this);

//        srl_main    = findViewById(R.id.srl_main);
        rv_main     = findViewById(R.id.rv_main);
        SliderView sliderView = findViewById(R.id.slider);
        btn_login = findViewById(R.id.btn_user);
        iv_logo = findViewById(R.id.tv_header_title);
        iv_logout = findViewById(R.id.iv_logout);
        tv_viewAll = findViewById(R.id.vieAll);
        iv_logo.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.VISIBLE);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
//                finish();
                onRestart();
                startActivity(i);
            }
        });

        progressDialog = new ProgressDialog(this);

        rv_main.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_main.setLayoutManager(layoutManager);
        rv_main.setNestedScrollingEnabled(true);
        rv_main.setHasFixedSize(false);

        ArrayList<Slider> sliderDataArrayList = new ArrayList<>();

        // adding the urls inside array list
        sliderDataArrayList.add(new Slider(url1));
        sliderDataArrayList.add(new Slider(url2));
        sliderDataArrayList.add(new Slider(url3));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        scrollRefresh();

    }
    public void scrollRefresh(){
        progressDialog.setMessage("Mengambil Data.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },1200);
    }
    void initializeArray(){
        arr_nm_penggalang = new ArrayList<>();
        arr_judul = new ArrayList<>();
        arr_dana = new ArrayList<>();
        arr_menderita = new ArrayList<>();
        arr_img = new ArrayList<>();
        arr_terkumpul = new ArrayList<>();
        arr_id = new ArrayList<>();

        arr_id.clear();
        arr_nm_penggalang.clear();
        arr_judul.clear();
        arr_dana.clear();
        arr_menderita.clear();
        arr_img.clear();
        arr_terkumpul.clear();
    }

    public void getData(){
        initializeArray();
        AndroidNetworking.get(p.URL_API+"/getDataGalangan")
                .setTag("Get Data")
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
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);

                                    arr_id.add(jo.getString("id_galang"));
                                    arr_nm_penggalang.add(jo.getString("nama_lengkap"));
                                    arr_judul.add(jo.getString("judul"));
                                    arr_dana.add(jo.getString("dana"));
                                    arr_menderita.add(jo.getString("menderita"));
                                    arr_img.add(jo.getString("gambar"));
                                    arr_terkumpul.add(jo.getString("terkumpul"));
                                }
                                recycleViewAdapter = new RecyclerViewAdapter(getApplicationContext(),arr_id,arr_nm_penggalang,arr_judul,arr_dana,arr_menderita,arr_img,arr_terkumpul);
                                rv_main.setAdapter(recycleViewAdapter);
                                int batas = ja.length();
//                                Log.d("BATAS", "BATAS : "+batas);
//                                if(batas >= 3){
//                                    tv_viewAll.setVisibility(View.VISIBLE);
//                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Tidak ada data yang ditemukan", Toast.LENGTH_SHORT).show();
                                recycleViewAdapter = new RecyclerViewAdapter(getApplicationContext(),arr_id,arr_nm_penggalang,arr_judul,arr_dana,arr_menderita,arr_img,arr_terkumpul);
                                rv_main.setAdapter(recycleViewAdapter);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d("TAG", "onError: "+anError.getErrorBody());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan 2x untuk keluar aplikasi", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }
}