package com.example.tryapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RekeningAdapter extends RecyclerView.Adapter<RekeningAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<String> arr_nm,arr_norek;
    private ArrayList<Integer> arr_logo;

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_nm,tv_norek;
        public ImageView iv_logo;

        ViewHolder(View v){
            super(v);
            iv_logo = v.findViewById(R.id.iv_logo);
            tv_nm = v.findViewById(R.id.tv_nm);
            tv_norek = v.findViewById(R.id.tv_no_rek);
        }
    }

    public RekeningAdapter(Context ctx,ArrayList<Integer> arr_logo, ArrayList<String> arr_nm, ArrayList<String> arr_norek){
        super();
        this.ctx = ctx;
        this.arr_logo = arr_logo;
        this.arr_nm = arr_nm;
        this.arr_norek = arr_norek;
    }


    @Override
    public RekeningAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.rvrek,parent,false);
        return new RekeningAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RekeningAdapter.ViewHolder holder, int position) {

        Picasso.get().load(arr_logo.get(position)).placeholder(R.drawable.wtf).into(holder.iv_logo);
        holder.tv_nm.setText(arr_nm.get(position));
        holder.tv_norek.setText(arr_norek.get(position));

    }

    @Override
    public int getItemCount() {
        return arr_norek.size();
    }
}
