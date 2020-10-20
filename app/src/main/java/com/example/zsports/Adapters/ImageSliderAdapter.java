package com.example.zsports.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zsports.Models.ImagesSliderModel;
import com.example.zsports.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<SliderViewHolder> {

    private Context context;
    private List<ImagesSliderModel> imagesSliderModelList;

    public ImageSliderAdapter(Context context, List<ImagesSliderModel> imagesSliderModelList) {
        this.context = context;
        this.imagesSliderModelList = imagesSliderModelList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        viewHolder.sliderImageview.setImageResource(imagesSliderModelList.get(position).getImage());
    }

    @Override
    public int getCount() {
        return imagesSliderModelList.size();
    }
}


class SliderViewHolder extends SliderViewAdapter.ViewHolder
{
    ImageView sliderImageview;
    public SliderViewHolder(View itemView) {
        super(itemView);
        sliderImageview = itemView.findViewById(R.id.sliderImageview);
    }
}