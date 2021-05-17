package com.example.bmusic.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.hanh_music_31_10.R;
import com.google.android.material.tabs.TabLayout;

public class LibraryOverViewFragment extends Fragment {

    private LibraryViewModel mLibraryViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library_overview, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPagerAdapterLibrary adapterLibrary = new ViewPagerAdapterLibrary(getChildFragmentManager(), 0);

        ViewPager viewPager = view.findViewById(R.id.pager_library);
        viewPager.setAdapter(adapterLibrary);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout_library);
        tabLayout.setupWithViewPager(viewPager);

        mLibraryViewModel =
                new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

    }

//    @Override
//    public void onClick(View v) {
//        // khi click vào một view trong các fragment
//        System.out.println("HanhNTHe: Click view viewpager "+v.toString());
////        if(v == R.layout.playlist_library_fragment)
//    }
}
