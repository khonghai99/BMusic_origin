package com.example.bmusic.ui.media_playback;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bmusic.activity.MainActivity;
import com.example.bmusic.provider.FavoriteSongProvider;
import com.example.bmusic.provider.FavoriteSongsTable;
import com.example.bmusic.service.MediaPlaybackService;
import com.example.hanh_music_31_10.R;
import com.example.bmusic.ui.receiver.TimerReceiver;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;

public class MainBottomSheetFragment extends BottomSheetDialogFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView mImageSongTop;
    private TextView mNamSongTop;
    private TextView mArtistSongTop;
    private ImageView mButtonMenu;
    private ImageView mRepeatSong;
    private ImageView mShuffleSong;
    private SeekBar mSeekBar;
    private TextView mPlayTime;
    private TextView mTotalTime;
    private ImageView mButtonLike;
    private ImageView mButtonPrevious;
    private ImageView mButtonPlaySong;
    private ImageView mButtonNext;
    private ImageView mButtonDisLike;

    private SeekBar seekBarTimer;
    private TextView textViewContentTimer;
    private TextView textViewCurrentTimeTimer;
    private TextView textViewTotalTimeTimer;

    private MediaPlaybackService mMediaPlaybackService;
    private boolean mCheckService = false;

    MediaPlaybackModel mediaPlaybackModel;

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ccnews_fragment_articles, container, false);
        mediaPlaybackModel =
                new ViewModelProvider(requireActivity()).get(MediaPlaybackModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PagerAdapterBottom pagerAdapter = new PagerAdapterBottom(getChildFragmentManager(), 0);
        // HaiKH giao dien bai hat dang phat
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_playback);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout_playback);
        mTabLayout.setupWithViewPager(mViewPager);
        initView(view);

        if (mCheckService) {
            updatePlaySongUI();
        }

        mButtonPlaySong.setOnClickListener(view17 -> {
            if (mMediaPlaybackService.isMusicPlay()) {
                if (mMediaPlaybackService.isPlaying()) {
                    mMediaPlaybackService.pause();
                } else if (!mMediaPlaybackService.isPlaying()) {
                    mMediaPlaybackService.play();
                }
            } else {
                mMediaPlaybackService.preparePlay();
            }
        });

        mButtonNext.setOnClickListener(view16 -> {
            if (mMediaPlaybackService.isMusicPlay()) {
                mMediaPlaybackService.nextSong();
            }
        });

        mButtonPrevious.setOnClickListener(view15 -> {
            if (mMediaPlaybackService.isMusicPlay()) {
                mMediaPlaybackService.previousSong();
            }
        });

        mRepeatSong.setOnClickListener(view14 -> mMediaPlaybackService.loopSong());

        mShuffleSong.setOnClickListener(view13 -> mMediaPlaybackService.shuffleSong());

        mButtonLike.setOnClickListener(view12 -> {
            if (mMediaPlaybackService.isPlayOnline()) {
                Toast.makeText(getContext(), "Cần download bài hát để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
            } else {
                if (mMediaPlaybackService.loadFavoriteStatus(mMediaPlaybackService.getId()) == 2) {
                    setDefaultFavoriteStatus(mMediaPlaybackService.getId());
                    mButtonLike.setImageResource(R.drawable.ic_like);
                    mButtonDisLike.setImageResource(R.drawable.ic_dislike);
                    Toast.makeText(getActivity(), "Đã bỏ thích bài hát", Toast.LENGTH_SHORT).show();
                } else {
                    likeSong(mMediaPlaybackService.getId());
                    mButtonLike.setImageResource(R.drawable.ic_liked_black_24dp);
                    mButtonDisLike.setImageResource(R.drawable.ic_dislike);
                }
            }
        });

        mButtonDisLike.setOnClickListener(view1 -> {
            if (mMediaPlaybackService.isPlayOnline()) {
                Toast.makeText(getContext(), "Cần download bài hát để sử dụng tính năng này !", Toast.LENGTH_SHORT).show();
            } else {
                if (mMediaPlaybackService.loadFavoriteStatus(mMediaPlaybackService.getId()) == 1) {
                    setDefaultFavoriteStatus(mMediaPlaybackService.getId());
                    mButtonLike.setImageResource(R.drawable.ic_like);
                    mButtonDisLike.setImageResource(R.drawable.ic_dislike);
                    Toast.makeText(getActivity(), "Đã bỏ thích bài hát", Toast.LENGTH_SHORT).show();
                } else {
                    dislikeSong(mMediaPlaybackService.getId());
                    mButtonLike.setImageResource(R.drawable.ic_like);
                    mButtonDisLike.setImageResource(R.drawable.ic_disliked_black_24dp);
                }
            }
        });

        mButtonMenu.setOnClickListener(v -> showDialog());

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlaybackService.setSeekTo(seekBar.getProgress());
            }
        });

//        btImgListSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().popBackStack();
//            }
//        });
    }

    private void likeSong(int id) {
        ContentValues values = new ContentValues();
        values.put(FavoriteSongsTable.ID_PROVIDER, id);
        values.put(FavoriteSongsTable.IS_FAVORITE, 2);
        Cursor cursor = findSongById(id);
        if (cursor != null && cursor.moveToFirst()) {
            getActivity().getContentResolver().update(FavoriteSongProvider.CONTENT_URI, values,
                    "id_provider = \"" + id + "\"", null);
        } else {
            getActivity().getContentResolver().insert(FavoriteSongProvider.CONTENT_URI, values);
        }
        Toast.makeText(getActivity().getBaseContext(),
                "Đã thêm bài hát vào yêu thích", Toast.LENGTH_LONG).show();
    }

    private void dislikeSong(int id) {
        ContentValues values = new ContentValues();
        values.put(FavoriteSongsTable.ID_PROVIDER, id);
        values.put(FavoriteSongsTable.IS_FAVORITE, 1);
        Cursor cursor = findSongById(id);
        if (cursor != null && cursor.moveToFirst()) {
            getActivity().getContentResolver().update(FavoriteSongProvider.CONTENT_URI, values,
                    "id_provider = \"" + id + "\"", null);
        } else {
            getActivity().getContentResolver().insert(FavoriteSongProvider.CONTENT_URI, values);
        }
        Toast.makeText(getActivity().getBaseContext(),
                "Đã xoá bài hát khỏi yêu thích", Toast.LENGTH_LONG).show();
    }

    // tim kiem theo id cua bai hat
    public Cursor findSongById(int id) {
        return getActivity().getContentResolver().query(FavoriteSongProvider.CONTENT_URI, new String[]{FavoriteSongsTable.IS_FAVORITE},
                FavoriteSongsTable.ID_PROVIDER + "=?",
                new String[]{String.valueOf(id)}, null);
    }

    public void setDefaultFavoriteStatus(int id) {
//        ContentValues values = new ContentValues();
//        values.put(FavoriteSongsProvider.IS_FAVORITE, 0);
//        getActivity().getContentResolver().update(FavoriteSongsProvider.CONTENT_URI, values, "ID_PROVIDER = " + id, null);

        ContentValues values = new ContentValues();
        values.put(FavoriteSongsTable.ID_PROVIDER, id);
        values.put(FavoriteSongsTable.IS_FAVORITE, 0);
        Cursor cursor = findSongById(id);
        if (cursor != null && cursor.moveToFirst()) {
            getActivity().getContentResolver().update(FavoriteSongProvider.CONTENT_URI, values,
                    "id_provider = \"" + id + "\"", null);
        } else {
            getActivity().getContentResolver().insert(FavoriteSongProvider.CONTENT_URI, values);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) return;
        View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = (BottomSheetBehavior<FrameLayout>) params.getBehavior();
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setSkipCollapsed(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getMusicActivity().mMediaPlaybackService != null) {
            mMediaPlaybackService = getMusicActivity().mMediaPlaybackService;
            mCheckService = true;
        }

//        getMusicActivity().setServiceConnectListenner2(new MainActivityMusic.IServiceConnectListenner2() {
//            @Override
//            public void onConnect() {
//                mMediaPlaybackService = getMusicActivity().mMediaPlaybackService;
//                mCheckService = true;
//
//                if (!mMediaPlaybackService.isMusicPlay()) {
//                    if (mMediaPlaybackService.getSharedPreferences().contains("SONG_LIST")) {
//                        mMediaPlaybackService.loadData();
//                        updateSaveSong();
//                    }
//                }
//            }
//        });

        if (mCheckService) {
            updatePlaySongUI();
//            if (!mMediaPlaybackService.isMusicPlay()) {
//                if (mMediaPlaybackService.getSharedPreferences().contains("SONG_LIST")) {
//                    mMediaPlaybackService.loadData();
//                    updateSaveSong();
//                }
//            }
        }
    }

    protected MainActivity getMusicActivity() {
        if (getActivity() instanceof MainActivity) {
            return (MainActivity) getActivity();
        }
        return null;
    }

    private void initView(View view) {
        mImageSongTop = view.findViewById(R.id.image_song_small);
        mNamSongTop = view.findViewById(R.id.name_song_playback);
        mArtistSongTop = view.findViewById(R.id.artist_song_playback);
        mButtonMenu = view.findViewById(R.id.btn_menu_playback);
        mRepeatSong = view.findViewById(R.id.btn_repeat_playback);
        mShuffleSong = view.findViewById(R.id.btn_shuffle_playback);
        mSeekBar = view.findViewById(R.id.seek_bar_song);
        mPlayTime = view.findViewById(R.id.time_song_playback);
        mTotalTime = view.findViewById(R.id.total_time_song_playback);
        mButtonLike = view.findViewById(R.id.btn_like_playback);
        mButtonPrevious = view.findViewById(R.id.btn_previous_playback);
        mButtonPlaySong = view.findViewById(R.id.btn_pause_playback);
        mButtonNext = view.findViewById(R.id.btn_next_playback);
        mButtonDisLike = view.findViewById(R.id.btn_dislike_playback);
    }

    public void updatePlaySongUI() {
        if (mMediaPlaybackService.isMusicPlay()) {
            mTotalTime.setText(mMediaPlaybackService.getTotalTime());
            mSeekBar.setMax(mMediaPlaybackService.getDuration());
            updateTimeSong();
            if (mMediaPlaybackService.isPlaying()) {
                mButtonPlaySong.setImageResource(R.drawable.ic_pause_circle_filled_orange_24dp);
            } else {
                mButtonPlaySong.setImageResource(R.drawable.ic_play_circle_filled_orange_24dp);

            }
        }

        mNamSongTop.setText(mMediaPlaybackService.getNameSong());
        mArtistSongTop.setText(mMediaPlaybackService.getArtist());
        if (mMediaPlaybackService.getPlayingSong().isOffline()) {
            //update ImageFragment
            Glide.with(mImageSongTop)
                    .load(mMediaPlaybackService.getPlayingSong().loadImageFromPath(mMediaPlaybackService.getPathSong()))
                    .apply(RequestOptions.
                            placeholderOf(R.drawable.icon_default_song))
                    .into(mImageSongTop);
            mediaPlaybackModel.setPathImage(mMediaPlaybackService.getPathSong());
//            mImageSongTop.setImageBitmap(mMediaPlaybackService.getPlayingSong().loadImageFromPath(mMediaPlaybackService.getPathSong()));
//                        imgSongSmall.setImageBitmap(loadImageFromPath(mMediaPlaybackService.getPathSong()));
        } else {
//                mImageSongTop.setImageResource(R.drawable.icon_default_song);
//                     set fragment image   image.setImageResource(R.drawable.icon_default_song);
            Glide.with(mImageSongTop)
                    .load(mMediaPlaybackService.getPlayingSong().getImageUrl())
                    .apply(RequestOptions.
                            placeholderOf(R.drawable.icon_default_song))
                    .into(mImageSongTop);
            mediaPlaybackModel.setPathImage(mMediaPlaybackService.getPlayingSong().getImageUrl());
        }

        if (mMediaPlaybackService.loadFavoriteStatus(mMediaPlaybackService.getId()) == 2) {
            mButtonLike.setImageResource(R.drawable.ic_liked_black_24dp);
            mButtonDisLike.setImageResource(R.drawable.ic_dislike);
        } else if (mMediaPlaybackService.loadFavoriteStatus(mMediaPlaybackService.getId()) == 1) {
            mButtonLike.setImageResource(R.drawable.ic_like);
            mButtonDisLike.setImageResource(R.drawable.ic_disliked_black_24dp);
        } else {
            mButtonLike.setImageResource(R.drawable.ic_like);
            mButtonDisLike.setImageResource(R.drawable.ic_dislike);
        }

        //HanhNTHe: Tint
        int color;
        int loop = mMediaPlaybackService.getmLoopStatus();
        int shuffle = mMediaPlaybackService.getmShuffle();
        if (loop == 0) {
            mRepeatSong.setImageResource(R.drawable.ic_repeat_white_24dp);
            color = R.color.icon_color;
        } else if (loop == 1) {
            mRepeatSong.setImageResource(R.drawable.ic_repeat_orange_24dp);
            color = R.color.icon_click_color;
        } else {
            mRepeatSong.setImageResource(R.drawable.ic_repeat_one_orange_24dp);
            color = R.color.icon_click_color;
        }
        mRepeatSong.setColorFilter(ContextCompat.getColor(getContext(), color), android.graphics.PorterDuff.Mode.SRC_IN);

        if (shuffle == 0) {
            mShuffleSong.setImageResource(R.drawable.ic_shuffle_white_24dp);
            color = R.color.icon_color;
        } else {
            mShuffleSong.setImageResource
                    (R.drawable.ic_shuffle_orange_24dp);
            color = R.color.icon_click_color;
        }
        mShuffleSong.setColorFilter(ContextCompat.getColor(getContext(), color), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");
                mPlayTime.setText(formatTimeSong.format(mMediaPlaybackService.getCurrentDuration()));
                mSeekBar.setProgress(mMediaPlaybackService.getCurrentDuration());
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    public void showDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.switch_off_timer, null);
        seekBarTimer = view.findViewById(R.id.seekbar_timer);
        textViewContentTimer = view.findViewById(R.id.content_timer);
        textViewCurrentTimeTimer = view.findViewById(R.id.current_time_timer);
        textViewTotalTimeTimer = view.findViewById(R.id.total_time_timer);

        seekBarTimer.setMax(120 * 60 * 1000);
        if (mMediaPlaybackService.getCurrentTimeTimer() == 0) {
            textViewContentTimer.setText("");
        } else {
            textViewContentTimer.setText("Ứng dụng sẽ tự động tắt nhạc sau " + mMediaPlaybackService.getCurrentTimeTimer() / 1000 / 60 + " phút");
        }

        final AlertDialog dialogTimer = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Bắt đầu hẹn giờ", null)
                .show();

        final Button positiveButton = dialogTimer.getButton(AlertDialog.BUTTON_POSITIVE);
        if (mMediaPlaybackService.getCurrentTimeTimer() == 0) {
            positiveButton.setEnabled(false);
        } else {
            positiveButton.setText("Kết thúc hẹn giờ");
            positiveButton.setEnabled(true);
        }

        positiveButton.setOnClickListener(v -> {
            if (positiveButton.getText().equals("Bắt đầu hẹn giờ")) {
                mMediaPlaybackService.setCurrentTimeTimer(seekBarTimer.getProgress());
                dialogTimer.dismiss();
                startAlarm();
            } else {
                mMediaPlaybackService.setCurrentTimeTimer(0);
                dialogTimer.dismiss();
            }
        });

        seekBarTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewCurrentTimeTimer.setText(getCurrentTime(progress));
                if (progress == 0) {
                    dialogTimer.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialogTimer.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mMediaPlaybackService.getCurrentTimeTimer(), pendingIntent);
        }
    }

    String getCurrentTime(int miliSecond) {
        int minute = miliSecond / 1000 / 60;
        int second = (miliSecond / 1000) % 60;
        if (second == 0) {
            return minute + " phút";
        } else {
            return minute + " phút " + second + " giây";
        }
    }
}
