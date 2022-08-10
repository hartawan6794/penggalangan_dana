package com.example.tryapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.tryapp.Adapter.RecyclerViewAdapter;
import com.example.tryapp.Adapter.SliderAdapter;
import com.example.tryapp.model.Slider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
//import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    RecyclerView rv_main;
    ArrayList<String> arr_id,arr_nm_penggalang,arr_judul,arr_dana,arr_menderita,arr_img,arr_terkumpul;
    ProgressDialog progressDialog;

    RecyclerViewAdapter recycleViewAdapter;

    String url1 = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/slider-01.jpg";
    String url2 = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/slider-02.jpg";
    String url3 = "https://penggalangandanakanker.ptmutiaraferindo.my.id/images/slider-03.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        srl_main    = findViewById(R.id.srl_main);
        rv_main     = findViewById(R.id.rv_main);
        SliderView sliderView = findViewById(R.id.slider);

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

        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                srl_main.setRefreshing(false);
            }
        });

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
        AndroidNetworking.get("https://penggalangandanakanker.ptmutiaraferindo.my.id/json/galangan.php")
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
                                Log.d("respon",""+ja);
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
                            }else{
                                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
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

                    }
                });
    }

}