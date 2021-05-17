package com.example.hanh_music_31_10.ui.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.service.MediaPlaybackService;

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindViewHolder(RecyclerData data);

    public abstract void setupClickableViews(RecyclerActionListener actionListener);

    //HanhNTHe: service
    public abstract void setService(MediaPlaybackService service);

}
