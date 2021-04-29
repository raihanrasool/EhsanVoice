package com.rasool.ehsanvoice;

import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.melnykov.fab.FloatingActionButton;
import com.rasool.ehsanvoice.Models.Message;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class PlayBack extends DialogFragment {
    TextView fileName, currentProgress, LengthOfFile;
    SeekBar seekBarofRecording;
    FloatingActionButton PlayAydio;


    private Message items;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    public boolean isplay = false;
    long mintues = 0;
    long seconds = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = (Message) getArguments().getSerializable("item");
        int categoryId = items.getCatId();
        if (Constants.LOG) {
            Log.i("HSK", "category id In playBack dailog: " + categoryId);
        }
        mintues = TimeUnit.MILLISECONDS.toMinutes(items.getLength());
        seconds = TimeUnit.MILLISECONDS.toSeconds(items.getLength()) - TimeUnit.MINUTES.toSeconds(mintues);

        try {
            onPlay(isplay);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onPlay(boolean isplay) throws IOException {
        if (!isplay) {
            if (mediaPlayer == null) {
                startPlaying();
            } else {
                pausePlaying();
            }

        }
    }

    private void pausePlaying() {
        //  PlayAydio.setImageResource(R.drawable.ic_play_arrow);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.pause();

    }

    private void startPlaying() throws IOException {
        //  PlayAydio.setImageResource(R.drawable.ic_pause);
        mediaPlayer = new MediaPlayer();
        if (items.getPath() != null) {
            mediaPlayer.setDataSource(items.getPath());
            mediaPlayer.prepare();
            // seekBarofRecording.setMax(mediaPlayer.getDuration());
        } else {
            Toast.makeText(getActivity(), "No Audio found", Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });

        updateSeekBar();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void setSeekbarvalues() {
        ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary));
        //  seekBarofRecording.getProgressDrawable().setColorFilter(colorFilter);
        // seekBarofRecording.getThumb().setColorFilter(colorFilter);

        seekBarofRecording.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(progress);
                    handler.removeCallbacks(mRunnable);
                    long mintues = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()) -
                            TimeUnit.MINUTES.toSeconds(mintues);
                    currentProgress.setText(String.format("%02d:%02d", mintues, seconds));

                    updateSeekBar();

                } else if (mediaPlayer == null && b) {
                    try {
                        prepareMediaPlayerFromPoint(progress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateSeekBar();

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

    private void prepareMediaPlayerFromPoint(int progress) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(items.getPath());
        mediaPlayer.prepare();
        // seekBarofRecording.setMax(mediaPlayer.getDuration());
        mediaPlayer.seekTo(progress);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();

            }
        });

    }

    private void stopPlaying() {
        // PlayAydio.setImageResource(R.drawable.ic_play_arrow);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        // seekBarofRecording.setProgress(seekBarofRecording.getMax());
        isplay = !isplay;

//        currentProgress.setText(LengthOfFile.getText());
//        seekBarofRecording.setProgress(seekBarofRecording.getMax());
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int mcurrentPosotion = mediaPlayer.getCurrentPosition();
                // seekBarofRecording.setProgress(mcurrentPosotion);

                long mintues = TimeUnit.MILLISECONDS.toMinutes(mcurrentPosotion);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mcurrentPosotion) -
                        TimeUnit.MINUTES.toSeconds(mintues);
                //  currentProgress.setText(String.format("%02d:%02d",mintues,seconds));
                updateSeekBar();

            }

        }
    };

    private void updateSeekBar() {
        handler.postDelayed(mRunnable, 1000);

    }


}
