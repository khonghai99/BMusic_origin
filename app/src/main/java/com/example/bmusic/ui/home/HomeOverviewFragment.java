package com.example.bmusic.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmusic.activity.MainActivity;
import com.example.bmusic.model.Playlist;
import com.example.bmusic.model.Song;
import com.example.bmusic.ui.recycler.BaseRecyclerAdapter;
import com.example.bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.bmusic.ui.recycler.RecyclerActionListener;
import com.example.bmusic.ui.recycler.RecyclerViewType;
import com.example.hanh_music_31_10.R;

import java.util.List;

public class HomeOverviewFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    private BaseRecyclerAdapter<Playlist> adapter;

    RecyclerActionListener mRecyclerActionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
        }

        @Override
        public void onViewLongClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
        }

        @Override
        public void clickSong(Song song) {
            homeViewModel.setSongFirstClick(song);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_overview, container, false);
        mRecyclerView = root.findViewById(R.id.list_block_playlist_home);
        mRecyclerView.setHasFixedSize(true);

        mLinearLayout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayout);

        adapter = new BaseRecyclerAdapter<Playlist>(mRecyclerActionListener, ((MainActivity) getActivity()).getService()) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_BLOCK_HOME_CATEGORY;
            }
        };
        mRecyclerView.setAdapter(adapter);

        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getPlaylist().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                adapter.update(playlists);
            }
        });

        return root;
    }
}