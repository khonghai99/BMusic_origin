package com.example.hanh_music_31_10.model;

import com.example.hanh_music_31_10.ui.recycler.RecyclerData;
import com.example.hanh_music_31_10.ui.recycler.RecyclerViewType;

public class ImageSearchModel implements RecyclerData {
    private String mCategory;
    private int mImageSearchUrl;

    public ImageSearchModel(String mCategory, int mImageSearchUrl) {
        this.mCategory = mCategory;
        this.mImageSearchUrl = mImageSearchUrl;
    }

    public String getNameCategory() {
        return mCategory;
    }

    public void setId(String mId) {
        this.mCategory = mId;
    }

    public int getImageSearchUrl() {
        return mImageSearchUrl;
    }

    public void setImageSearchUrl(int mImageSearchUrl) {
        this.mImageSearchUrl = mImageSearchUrl;
    }


    @Override
    public int getViewType() {
        return RecyclerViewType.TYPE_ADD_SONG_IN_PLAYLIST;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        return false;
    }
}
