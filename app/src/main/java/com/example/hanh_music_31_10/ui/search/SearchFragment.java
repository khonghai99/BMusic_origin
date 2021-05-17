package com.example.hanh_music_31_10.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hanh_music_31_10.R;
import com.example.hanh_music_31_10.activity.SettingsActivity;
import com.example.hanh_music_31_10.model.ImageSearchModel;
import com.example.hanh_music_31_10.model.Playlist;
import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.ui.home.DetailSongFragment;
import com.example.hanh_music_31_10.ui.library.DetailPlayListFragment;
import com.example.hanh_music_31_10.ui.library.LibraryFragment;
import com.example.hanh_music_31_10.ui.library.LibraryOverViewFragment;
import com.example.hanh_music_31_10.ui.library.LibraryViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel mSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mSearchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchViewModel.openDetailSearch().observe(getViewLifecycleOwner(), new Observer<ImageSearchModel>() {
            @Override
            public void onChanged(ImageSearchModel image) {
                if (image != null) {
                    SearchFragment.this.openDetailFragment();
                    mSearchViewModel.setDetailImageSearch(image);
                    mSearchViewModel.setImageSearchFirstClick(null);
                }
            }
        });

        mSearchViewModel.openDetailHomeFragment().observe(getViewLifecycleOwner(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                if(song != null) {
                    openDetailHomeFragment();
                    mSearchViewModel.setClickSong(song);
                    mSearchViewModel.setItemSearchFirstClick(null);
                }
            }
        });

        openOverviewFragment();
        return root;
    }


    private void openOverviewFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_search, new SearchOverViewFragment(),
                        SearchOverViewFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openDetailFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_search, new DetailSearchFragment(), DetailSearchFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openDetailHomeFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_search, new DetailSongFragment(), DetailSongFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

}