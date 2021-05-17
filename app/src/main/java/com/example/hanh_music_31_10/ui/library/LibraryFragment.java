package com.example.hanh_music_31_10.ui.library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.hanh_music_31_10.activity.MainActivity;
import com.example.hanh_music_31_10.activity.SettingsActivity;
import com.example.hanh_music_31_10.model.Playlist;
import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.ui.recycler.BaseRecyclerViewHolder;
import com.example.hanh_music_31_10.ui.recycler.RecyclerActionListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private LibraryViewModel mLibraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("HaiKH", "onCreateView: ");
        setHasOptionsMenu(true);
        mLibraryViewModel =
                new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        mLibraryViewModel.openDetailPlaylist().observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist playlist) {
                if (playlist != null){
                    LibraryFragment.this.openDetailFragment();
                    mLibraryViewModel.setDetailPlaylist(playlist);
                    mLibraryViewModel.setPlaylistFirstClick(null);
                }
            }
        });

        openOverviewFragment();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("HanhNTHe: lib onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("HanhNTHe: lib onPause ");
    }

    private void openOverviewFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_library, new LibraryOverViewFragment(),
                        LibraryOverViewFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openDetailFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_library, new DetailPlayListFragment(), DetailPlayListFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);

        MenuItem settingItem = menu.findItem(R.id.action_setting);

        settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}