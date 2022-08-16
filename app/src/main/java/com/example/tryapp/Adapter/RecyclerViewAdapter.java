package com.example.tryapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.tryapp.DetailDonasiActivity;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context ctx;
    private ArrayList<String> arr_id,arr_nm_penggalang,arr_img,arr_judul,arr_dana,arr_menderita,arr_terkumpul;
    ProgressDialog progressDialog;
    Pengaturan p = new Pengaturan();
    private int BATAS = 4;

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_nm_p,tv_judul,tv_dana,tv_menderita,tv_proses;
        public ImageView iv_img;
        public CardView cv_main;
        public ProgressBar pb_dana;
        public Button btn_donasi;

        MyViewHolder(View v){
            super(v);
            cv_main = itemView.findViewById(R.id.cv_main);
            tv_nm_p = itemView.findViewById(R.id.nama_penggalang);
            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_dana = itemView.findViewById(R.id.tv_dana);
            tv_menderita = itemView.findViewById(R.id.tv_menderita);
            iv_img = (ImageView) itemView.findViewById(R.id.img_g);
            tv_proses = itemView.findViewById(R.id.tv_prosses);
            pb_dana = itemView.findViewById(R.id.progress_b);
            btn_donasi = itemView.findViewById(R.id.btn_donasi);
            progressDialog = new ProgressDialog(ctx);
        }
    }

    public RecyclerViewAdapter(Context mContext,ArrayList<String> arr_id,ArrayList<String> arr_nm_penggalang,
                               ArrayList<String> arr_judul, ArrayList<String> arr_dana,
                               ArrayList<String> arr_menderita, ArrayList<String> arr_img, ArrayList<String> arr_terkumpul) {
        super();
        this.ctx = mContext;
        this.arr_id = arr_id;
        this.arr_nm_penggalang = arr_nm_penggalang;
        this.arr_judul = arr_judul;
        this.arr_dana = arr_dana;
        this.arr_menderita = arr_menderita;
        this.arr_img = arr_img;
        this.arr_terkumpul = arr_terkumpul;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.rvmain,parent,false);
        return new RecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {

        float progress = p.presentase(arr_dana.get(position), arr_terkumpul.get(position));
        DecimalFormat df = new DecimalFormat(".##");
        holder.tv_nm_p.setText(arr_nm_penggalang.get(position));
        holder.tv_judul.setText(arr_judul.get(position));
        holder.tv_dana.setText(p.formatRupiah(Double.valueOf(arr_dana.get(position))));
        holder.tv_menderita.setText(arr_menderita.get(position));
        holder.tv_proses.setText(String.valueOf(df.format(progress))+"%");
        holder.pb_dana.setProgress((int) progress);
        Picasso.get().load("https://penggalangandanakanker.ptmutiaraferindo.my.id/images/"+arr_img.get(position))
                .placeholder(R.drawable.wtf).into(holder.iv_img);
        holder.btn_donasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, DetailDonasiActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id", arr_id.get(position));
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arr_id.size() < BATAS){
            return arr_id.size();
        }else {

        return BATAS;
        }
    }
}
