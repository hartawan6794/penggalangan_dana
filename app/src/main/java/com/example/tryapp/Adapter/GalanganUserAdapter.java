package com.example.tryapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalanganUserAdapter extends RecyclerView.Adapter<GalanganUserAdapter.ViewHolder> {

    Context ctx;
    ArrayList<String> arr_judul,arr_nama,arr_penyakit,arr_alamat,arr_tgl_mulai,arr_tgl_selesai,arr_dana,arr_terkumpul,arr_img,arr_status;
    Pengaturan p = new Pengaturan();

    public GalanganUserAdapter(Context ctx, ArrayList<String> arr_judul,ArrayList<String> arr_nama,
                               ArrayList<String> arr_penyakit,ArrayList<String> arr_alamat,ArrayList<String> arr_tgl_mulai,
                               ArrayList<String> arr_tgl_selesai, ArrayList<String> arr_dana, ArrayList<String> arr_terkumpul,
                               ArrayList<String> arr_img, ArrayList<String> arr_status){
        this.ctx = ctx;
        this.arr_judul = arr_judul;
        this.arr_nama = arr_nama;
        this.arr_penyakit = arr_penyakit;
        this.arr_alamat = arr_alamat;
        this.arr_tgl_mulai = arr_tgl_mulai;
        this.arr_tgl_selesai = arr_tgl_selesai;
        this.arr_dana = arr_dana;
        this.arr_terkumpul = arr_terkumpul;
        this.arr_img = arr_img;
        this.arr_status = arr_status;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_img;
        TextView tv_judul,tv_nama,tv_penyakit,tv_alamat,tv_tgl_mulai,tv_tgl_selesai,tv_dana,tv_terkumpul,tv_status;
        public ViewHolder(View itemView) {
            super(itemView);

            iv_img = itemView.findViewById(R.id.iv_gambar);
            tv_judul = itemView.findViewById(R.id.judul);
            tv_nama = itemView.findViewById(R.id.nama);
            tv_penyakit = itemView.findViewById(R.id.penyakit);
            tv_alamat = itemView.findViewById(R.id.alamat);
            tv_tgl_mulai = itemView.findViewById(R.id.tgl_mulai);
            tv_tgl_selesai = itemView.findViewById(R.id.tgl_selesai);
            tv_dana = itemView.findViewById(R.id.dana);
            tv_terkumpul = itemView.findViewById(R.id.terkumpul);
            tv_status = itemView.findViewById(R.id.status);
        }

    }

    @Override
    public GalanganUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.rvgalanganuser,parent,false);
        return new GalanganUserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalanganUserAdapter.ViewHolder holder, int position) {
        holder.tv_judul.setText(arr_judul.get(position));
        holder.tv_nama.setText(arr_nama.get(position));
        holder.tv_penyakit.setText(arr_penyakit.get(position));
        holder.tv_alamat.setText(arr_alamat.get(position));
        holder.tv_tgl_mulai.setText(arr_tgl_mulai.get(position));
        holder.tv_tgl_selesai.setText(arr_tgl_selesai.get(position));
        holder.tv_terkumpul.setText(arr_terkumpul.get(position));
        holder.tv_dana.setText(arr_dana.get(position));
        holder.tv_status.setText(arr_status.get(position));
        Picasso.get().load(p.PATH_IMAGE + arr_img.get(position)).into(holder.iv_img);

    }

    @Override
    public int getItemCount() {
        return arr_judul.size();
    }
}
