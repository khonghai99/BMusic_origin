package com.example.hanh_music_31_10.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanh_music_31_10.R;
import com.example.hanh_music_31_10.model.Playlist;
import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.service.MediaPlaybackService;
import com.example.hanh_music_31_10.ui.recycler.BaseRecyclerAdapter;
import com.example.hanh_music_31_10.ui.recycler.BaseRecyclerViewHolder;
import com.example.hanh_music_31_10.ui.recycler.RecyclerActionListener;
import com.example.hanh_music_31_10.ui.recycler.RecyclerData;
import com.example.hanh_music_31_10.ui.recycler.RecyclerViewType;

public class BlockHomeCategoryHolder extends BaseRecyclerViewHolder {

    private TextView mTitleCategory;
    private RecyclerView mListSongHorizontal;
    private RecyclerActionListener recyclerActionListener;
    private BaseRecyclerAdapter<Song> adapter;

    public BlockHomeCategoryHolder(@NonNull View itemView) {
        super(itemView);
        mTitleCategory = itemView.findViewById(R.id.title_category);
        mListSongHorizontal = itemView.findViewById(R.id.list_song_same_category);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Playlist) {
            Playlist playlist = (Playlist) data;
            mTitleCategory.setText(playlist.getNamePlaylist());

            mListSongHorizontal.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));

            adapter = new BaseRecyclerAdapter<Song>(playlist.getSongList(), recyclerActionListener) {
                @Override
                public int getItemViewType(int position) {
                    return RecyclerViewType.TYPE_ITEM_SONG_IN_HOME;
                }
            };
            mListSongHorizontal.setAdapter(adapter);
        }
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        recyclerActionListener = new RecyclerActionListener() {
            @Override
            public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
                actionListener.clickSong(adapter.getData().get(position));
            }

            @Override
            public void onViewLongClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
                actionListener.onViewLongClick(position, view, viewHolder);
            }
        };
    }

    @Override
    public void setService(MediaPlaybackService service) {

    }

}
