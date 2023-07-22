package com.example.tryapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tryapp.DetailRiwayatDonasiActivity;
import com.example.tryapp.Helper.Pengaturan;
import com.example.tryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<String> arr_id,arr_judul,arr_img,arr_dana,arr_terkumpul;
    Pengaturan p = new Pengaturan();

    public RiwayatAdapter(Context ctx, ArrayList<String> arr_id,ArrayList<String> arr_judul,ArrayList<String> arr_img,ArrayList<String> arr_dana, ArrayList<String> arr_terkumpul){
        this.ctx = ctx;
        this.arr_id = arr_id;
        this.arr_judul = arr_judul;
        this.arr_img = arr_img;
        this.arr_dana = arr_dana;
        this.arr_terkumpul = arr_terkumpul;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_g;
        TextView tv_judul,tv_dana,tv_terkumpul;
        Button bt_bio;

        public ViewHolder(View itemView) {
            super(itemView);
            img_g = itemView.findViewById(R.id.img_g);
            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_dana = itemView.findViewById(R.id.tv_dana);
            tv_terkumpul = itemView.findViewById(R.id.tv_terkumpul);
            bt_bio = itemView.findViewById(R.id.bt_biodata);
        }
    }


    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.rv_riwayat,parent,false);
        return new RiwayatAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RiwayatAdapter.ViewHolder holder, int position) {
        holder.tv_judul.setText(arr_judul.get(position));
        holder.tv_dana.setText("Jumlah Penggalangan "+p.formatRupiah(Double.valueOf(arr_dana.get(position))));
        holder.tv_terkumpul.setText("Terkumpul " +p.formatRupiah(Double.valueOf(arr_terkumpul.get(position))));
        Picasso.get()
                .load(p.URL_API+"/img/"+arr_img.get(position))
                .into(holder.img_g);

        holder.bt_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx.getApplicationContext(), DetailRiwayatDonasiActivity.class);
                i.putExtra("id",arr_id.get(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr_id.size();
    }
}
