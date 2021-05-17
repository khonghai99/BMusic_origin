package com.example.bmusic.ui.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.example.bmusic.activity.AddSongToPlaylist;
import com.example.bmusic.model.Song;
import com.example.bmusic.service.MediaPlaybackService;
import com.example.bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.bmusic.ui.recycler.RecyclerActionListener;
import com.example.bmusic.ui.recycler.RecyclerData;
import com.example.hanh_music_31_10.R;

public class AddSongInPlayListItemHolder extends BaseRecyclerViewHolder {

    private CheckBox mSelectSong;

    public AddSongInPlayListItemHolder(@NonNull View itemView) {
        super(itemView);
        mSelectSong = itemView.findViewById(R.id.item_song);
        attachListener();
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Song) {
            Song song = (Song) data;
            mSelectSong.setText(song.getNameSong());
            mSelectSong.setOnCheckedChangeListener(null);
        } else if (data instanceof AddSongToPlaylist.CheckboxSong) {
            AddSongToPlaylist.CheckboxSong cbSong = (AddSongToPlaylist.CheckboxSong) data;
            mSelectSong.setText(cbSong.mSong.getNameSong());
            mSelectSong.setChecked(cbSong.mChecked);
            mSelectSong.setOnCheckedChangeListener((buttonView, isChecked) -> cbSong.mChecked = isChecked);
        }

    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(v -> actionListener.onViewClick(getAdapterPosition(), v, AddSongInPlayListItemHolder.this));
    }

    @Override
    public void setService(MediaPlaybackService service) {
    }

    //Listener nhận sự kiện khi các Checkbox thay đổi trạng thái
    CompoundButton.OnCheckedChangeListener m_listener
            = (compoundButton, b) -> System.out.println("HanhNTHe: compoundButton " + compoundButton + "  text " + compoundButton.getText());

    //Gán Listener vào CheckBox
    void attachListener() {
        mSelectSong.setOnCheckedChangeListener(m_listener);
    }
}
