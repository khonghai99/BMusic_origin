package com.example.hanh_music_31_10.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hanh_music_31_10.R;
import com.example.hanh_music_31_10.activity.ActivityViewModel;
import com.example.hanh_music_31_10.activity.MainActivity;
import com.example.hanh_music_31_10.model.Constants;
import com.example.hanh_music_31_10.model.PlaySong;
import com.example.hanh_music_31_10.model.Playlist;
import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.service.MediaPlaybackService;
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
        // HaiKH Mở view detail của 1 bài hát khi click vào item ở view home
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.home_fragment_container, new DetailSongFragment(), DetailSongFragment.class.getName())
//                .addToBackStack(null)
//                .commit();

        //play music
        if (mService != null) {
            // HaiKH chỗ comt này là check có bài nào đang phát ko
//            if (mService.isMusicPlay() && mService.isPlaying()) {
//                mService.pause();
//            } else {
            // HaiHK play bài vừa click
                mActivityViewMode.setPlaylist(new PlaySong(song, new ArrayList<>(Collections.singletonList(song))));
//            }
        }
    }

    //Create data
    private void getData() {
        ArrayList<Playlist> mData = new ArrayList<>();
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