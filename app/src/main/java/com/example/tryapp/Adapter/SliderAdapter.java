package com.example.tryapp.Adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.tryapp.R;
import com.example.tryapp.model.Slider;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private final List<Slider> mSliderItems;

    // Constructor
    public SliderAdapter(Context context, ArrayList<Slider> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
    }


    @Override
    public SliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.SliderAdapterViewHolder viewHolder, int position) {

        final Slider sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Picasso.get().load(sliderItem.getImgUrl()).fit().centerCrop().into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }


    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}
