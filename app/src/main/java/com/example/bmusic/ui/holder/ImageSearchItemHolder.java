package com.example.bmusic.ui.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.bmusic.model.ImageSearchModel;
import com.example.bmusic.service.MediaPlaybackService;
import com.example.bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.bmusic.ui.recycler.RecyclerActionListener;
import com.example.bmusic.ui.recycler.RecyclerData;
import com.example.hanh_music_31_10.R;

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
