package com.example.bmusic.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bmusic.activity.ActivityViewModel;
import com.example.bmusic.activity.MainActivity;
import com.example.bmusic.model.Constants;
import com.example.bmusic.model.PlaySong;
import com.example.bmusic.model.Playlist;
import com.example.bmusic.model.Song;
import com.example.bmusic.service.MediaPlaybackService;
import com.example.hanh_music_31_10.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel mHomeViewMode;
    private MediaPlaybackService mService;
    private ActivityViewModel mActivityViewMode;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivityViewMode = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        mService = ((MainActivity) getActivity()).getService();
        mHomeViewMode =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mHomeViewMode.openDetailSong().observe(getViewLifecycleOwner(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                if(song != null) {
                    openDetailFragment(song);
                    mHomeViewMode.setDetailSong(song);
                    mHomeViewMode.setSongFirstClick(null);
                }
            }
        });

        openOverviewFragment();

        getData();

        return root;
    }

    private void openOverviewFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, new HomeOverviewFragment(), HomeOverviewFragment.class.getName())
                .commit();
    }


    private void openDetailFragment(Song song) {
        // HaiKH M??? view detail c???a 1 b??i h??t khi click v??o item ??? view home
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.home_fragment_container, new DetailSongFragment(), DetailSongFragment.class.getName())
//                .addToBackStack(null)
//                .commit();

        //play music
        if (mService != null) {
            // HaiKH ch??? comt n??y l?? check c?? b??i n??o ??ang ph??t ko
//            if (mService.isMusicPlay() && mService.isPlaying()) {
//                mService.pause();
//            } else {
            // HaiHK play b??i v???a click
                mActivityViewMode.setPlaylist(new PlaySong(song, new ArrayList<>(Collections.singletonList(song))));
//            }
        }
    }

    //Create data
    private void getData() {
        ArrayList<Playlist> mData = new ArrayList<>();
        // HaiKH Get list MPH sap xep theo ngay ra mat
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_SONG_PATH).orderByChild("releaseDate")
                .limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();

                Object object = dataSnapshot.getValue(Object.class);
                String json = gson.toJson(object);

                try {
                    Type listType = new TypeToken<HashMap<String, Song>>() {
                    }.getType();
                    HashMap<String, Song> data = gson.fromJson(json, listType);
                    if (data != null) {
                        Playlist playlist = new Playlist(1,"M???i ph??t h??nh", new ArrayList<>(data.values()));
                        mData.add(playlist);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_HOME_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();

                Object object = dataSnapshot.getValue(Object.class);
                String json = gson.toJson(object);

                Type listType = new TypeToken<ArrayList<Playlist>>() {}.getType();
                ArrayList<Playlist> data = gson.fromJson(json, listType);

                for (int i =0; i < data.size() ; i++)
                    mData.add(data.get(i));
                mHomeViewMode.setPlaylist(mData);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }
}