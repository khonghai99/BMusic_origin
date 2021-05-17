package com.example.hanh_music_31_10.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanh_music_31_10.R;
import com.example.hanh_music_31_10.activity.ActivityViewModel;
import com.example.hanh_music_31_10.activity.MainActivity;
import com.example.hanh_music_31_10.model.Constants;
import com.example.hanh_music_31_10.model.ImageSearchModel;
import com.example.hanh_music_31_10.model.PlaySong;
import com.example.hanh_music_31_10.model.Playlist;
import com.example.hanh_music_31_10.model.PlaylistSearch;
import com.example.hanh_music_31_10.model.Song;
import com.example.hanh_music_31_10.ui.recycler.BaseRecyclerAdapter;
import com.example.hanh_music_31_10.ui.recycler.BaseRecyclerViewHolder;
import com.example.hanh_music_31_10.ui.recycler.RecyclerActionListener;
import com.example.hanh_music_31_10.ui.recycler.RecyclerViewType;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailSearchFragment extends Fragment {

    private TextView mTitleSearch;
    private RecyclerView mRecyclerView;
    private SearchViewModel searchViewModel;
    private ActivityViewModel mActivityViewModel;

    BaseRecyclerAdapter<Song> mAdapter;

    RecyclerActionListener mRecyclerViewAction = new RecyclerActionListener(){
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
           //check lại playSong vì đây là bài hát online nên không thể thực hiện theo cách này
            searchViewModel.setItemSearchFirstClick(mAdapter.getData().get(position));
            mAdapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail_search, container, false);

        mTitleSearch = root.findViewById(R.id.title_search);
        mRecyclerView = root.findViewById(R.id.recycler_song_in_search);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new BaseRecyclerAdapter<Song>( mRecyclerViewAction, ((MainActivity) getActivity()).getService()){
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_DETAIL_SEARCH;
            }
        };

        mRecyclerView.setAdapter(mAdapter);
        mActivityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        searchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        searchViewModel.getDetailImageSearch().observe(getViewLifecycleOwner(), new Observer<ImageSearchModel>() {
            @Override
            public void onChanged(ImageSearchModel s) {
                if(s != null ){
                    mTitleSearch.setText("Kết quả tìm kiếm thể loại: "+s.getNameCategory());
                    getData(s.getNameCategory());
                }
            }
        });
        return root;
    }

    private void getData(String namePlaylist) {
        //load song online từ server
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_SEARCH_CATEGORY_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();

                Object object = dataSnapshot.getValue(Object.class);
                String json = gson.toJson(object);

                Type listType = new TypeToken<ArrayList<PlaylistSearch>>() {}.getType();
                ArrayList<PlaylistSearch> data = gson.fromJson(json, listType);

//                for (String key : map.keySet()) {
//                    data.add(map.get(key));
//                }

//                if (homeViewModel.getPlaylist().getValue().size() == 0)
//                homeViewModel.setPlaylist(data);
                for (int i =0; i < data.size() ; i++){
                    PlaylistSearch playlistData = data.get(i);
                    if(namePlaylist.equals(playlistData.getNameCategory())){
                        mAdapter.update(playlistData.getListSong());
                    }
                }
//                    mData.add(data.get(i));
//
//                List<Song> dataSong3 = new ArrayList<Song>();
//                mAdapter.update(dataSong3);

//                Playlist playlist = map.get(map.keySet().toArray()[0]);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


//        List<Song> dataSong3 = new ArrayList<Song>();
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Muon Mang la Tu Luc", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Tung yeu", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        dataSong3.add(new Song(2, "Tung yeu", "", "Phan Duy Anh", "", "5:13", 0, ""));
//        mAdapter.update(dataSong3);
    }
}
