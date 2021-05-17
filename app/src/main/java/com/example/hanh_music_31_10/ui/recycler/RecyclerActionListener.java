package com.example.hanh_music_31_10.ui.recycler;

import android.view.View;

import com.example.hanh_music_31_10.model.Playlist;
import com.example.hanh_music_31_10.model.Song;

public class RecyclerActionListener {

    public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {

    }

    public void onViewLongClick(int position, View view, BaseRecyclerViewHolder viewHolder) {

    }

    public void clickSong(Song song) {

    }

    //xử lý sự kiện của popupmenu

    //xoa bài hat
    public void updateSongFromMenuButton(Song song, CONTROL_UPDATE state) {
    }
    public void updatePlaylistFromButton(Playlist playlist, CONTROL_UPDATE state){}

    public enum CONTROL_UPDATE {
        DELETE_SONG, DELETE_FAVORITE_SONG, ADD_PLAYLIST, ADD_FAVORITE, DELETE_PLAYLIST, UPDATE_NAME_PLAYLIST, ADD_SONG_TO_PLAYLIST
    }
}
