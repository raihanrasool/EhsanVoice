package com.rasool.ehsanvoice.Services;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;




import java.io.File;
import java.io.IOException;

public class RecordingServices extends Service {

    MediaRecorder mediaRecorder;
    long mStartingTimesMillis = 0;
    long mElaspedMillis = 0;

    File file;
    String FileName;
    // DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        // dbHelper = new DbHelper(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    private void startRecording() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        FileName = "audio_" + ts;
        file = new File(Environment.getExternalStorageDirectory() + "/SpeakRecording/" + FileName + ".mp3");


        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

            mStartingTimesMillis = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopRecording() {

        mediaRecorder.stop();
        mElaspedMillis = (System.currentTimeMillis() - mStartingTimesMillis);
        mediaRecorder.release();

        Toast.makeText(getApplicationContext(), "Recording saved" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

//        RecordingItems recordingItems = new RecordingItems(FileName,file.getAbsolutePath(),mElaspedMillis,System.currentTimeMillis());
//        dbHelper.addRecording(recordingItems);
//        Log.i("Hsk","column Name: "+recordingItems.getName() +"path:"+recordingItems.getPath() +" Length: "+recordingItems.getLength() +" Time: " +recordingItems.getTime_added());
//
//        ///    Toast.makeText(getApplicationContext(), "Recording saved"+dbHelper.getAllAudio(), Toast.LENGTH_LONG).show();
//        Log.i("Hsk","values retrived "+dbHelper.getAllAudio().toString());

    }

    @Override
    public void onDestroy() {
        if (mediaRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

}


