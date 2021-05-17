package com.example.hanh_music_31_10.ui.holder;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanh_music_31_10.R;
import com.example.hanh_music_31_10.model.ImageSearchModel;
import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.service.MediaPlaybackService;
import com.example.hanh_music_31_10.ui.recycler.BaseRecyclerViewHolder;
import com.example.hanh_music_31_10.ui.recycler.RecyclerActionListener;
import com.example.hanh_music_31_10.ui.recycler.RecyclerData;

public class ImageSearchItemHolder extends BaseRecyclerViewHolder {

    private ImageView mImageSearchButton;

    public ImageSearchItemHolder(@NonNull View itemView) {
        super(itemView);
        mImageSearchButton = itemView.findViewById(R.id.image_search);
//        mImageSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("");
//            }
//        });
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof ImageSearchModel){
            ImageSearchModel image = (ImageSearchModel) data;
//            Glide.with(mImageSearchButton)
//                    .load(image.getImageSearchUrl())
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(mImageSearchButton);
            mImageSearchButton.setImageResource(image.getImageSearchUrl());
        }
//            mImageSearchButton.setImageResource(((ImageSearchModel) data).getImageSearchUrl());
        //load anh
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClick(getAdapterPosition(), v, ImageSearchItemHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {

    }

}
