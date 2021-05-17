package com.example.bmusic.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bmusic.model.Song;
import com.example.bmusic.service.MediaPlaybackService;
import com.example.bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.bmusic.ui.recycler.RecyclerActionListener;
import com.example.bmusic.ui.recycler.RecyclerData;
import com.example.hanh_music_31_10.R;

public class SongSearchItemHolder extends BaseRecyclerViewHolder {

    private ImageView mImageSearchButton;
    private TextView mTextView;

    public SongSearchItemHolder(@NonNull View itemView) {
        super(itemView);
        mImageSearchButton = itemView.findViewById(R.id.image_song_item_search);
        mTextView = itemView.findViewById(R.id.song_name_item_search);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Song) {
            Song song = (Song) data;
            mTextView.setText(song.getNameSong());
            Glide.with(mImageSearchButton)
                    .load(song.getImageUrl())
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .apply(RequestOptions.
                            placeholderOf(R.drawable.placeholder_music))
                    .into(mImageSearchButton);
        }
        //load anh
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewLongClick(getAdapterPosition(), v, SongSearchItemHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {

    }
}
