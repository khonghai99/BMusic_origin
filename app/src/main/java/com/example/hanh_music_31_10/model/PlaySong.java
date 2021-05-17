package com.example.hanh_music_31_10.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaySong implements Serializable {

    private Song mPlaySong;
    private ArrayList<Song> mPlayListSong;

    public PlaySong(Song mPlaySong, ArrayList<Song> mPlayListSong) {
        this.mPlaySong = mPlaySong;
        this.mPlayListSong = mPlayListSong;
    }

    public PlaySong(int position, ArrayList<Song> mPlayListSong){
        this.mPlayListSong = mPlayListSong;
        this.mPlaySong = mPlayListSong.get(position);
    }

    public Song getPlaySong() {
        return mPlaySong;
    }

    public void setPlaySong(Song mPlaySong) {
        this.mPlaySong = mPlaySong;
    }

    public ArrayList<Song> getPlayListSong() {
        return mPlayListSong;
    }

    public void setPlayListSong(ArrayList<Song> mPlayListSong) {
        this.mPlayListSong = mPlayListSong;
    }
}
