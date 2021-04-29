package com.rasool.ehsanvoice;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.rasool.ehsanvoice.Models.Message;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayBackActivity extends Activity {
    private Message items;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    public boolean isplay = false;
    long mintues=0;
    long seconds=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        items = (Message)intent.getSerializableExtra("item");
       // items = (Message).getSerializable("item");
        int categoryId= items.getCatId();
        if (Constants.LOG){
            Log.i("HSK","category id In playBack dailog: "+categoryId);
        }

        try {
            onPlay(isplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();

    }

    private void onPlay(boolean isplay) throws IOException {
        if (!isplay){
            if (mediaPlayer==null){
                startPlaying();
            }else {
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
        if (items.getPath()!=null) {
            mediaPlayer.setDataSource(items.getPath());
            mediaPlayer.prepare();
            // seekBarofRecording.setMax(mediaPlayer.getDuration());
        }else{
            Toast.makeText(PlayBackActivity.this, "No Audio found", Toast.LENGTH_SHORT).show();
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer!=null){
                int mcurrentPosotion = mediaPlayer.getCurrentPosition();

                long mintues = TimeUnit.MILLISECONDS.toMinutes(mcurrentPosotion);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mcurrentPosotion)-
                        TimeUnit.MINUTES.toSeconds(mintues);
                //  currentProgress.setText(String.format("%02d:%02d",mintues,seconds));
                updateSeekBar();

            }

        }
    };

    private void updateSeekBar() {
        handler.postDelayed(mRunnable,1000);

    }

}
