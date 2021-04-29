package com.rasool.ehsanvoice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.melnykov.fab.FloatingActionButton;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.Models.Message;
import com.rasool.ehsanvoice.Models.SentencesDetailsItems;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UpdateMessages extends AppCompatActivity {
    FloatingActionButton record, playAudio;
    Button Pause_button, updateMessage;
    TextView recordText, fileName, lengthOfFile, currentProgress;
    EditText priority;
    Chronometer chronometer;
    AutoCompleteTextView TextToSpeak;
    SeekBar seekBar;
    Toolbar toolbar;
    Spinner spinner;
    int id;
    MediaRecorder mediaRecorder;
    long mStartingTimesMillis = 0;
    long mElaspedMillis = 0;
    File file;
    String FileName;
    long timeWhenPause = 0;
    Message message;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    public boolean isplay = false;
    long mintues = 0;
    long seconds = 0;
    String categoryName;
    ArrayList<SentencesDetailsItems> arrayList = new ArrayList<>();
    Integer sentenceOrder;
    public static final int RequestPermissionCode = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_messages);
        record = findViewById(R.id.UPbtn_record);
        playAudio = findViewById(R.id.Upplay);
        Pause_button = findViewById(R.id.btn_pause);
        recordText = findViewById(R.id.UPrecording_status_txt);
        chronometer = findViewById(R.id.UPchronometer);
        TextToSpeak = findViewById(R.id.UPspeaktext);
        updateMessage = findViewById(R.id.UPdatemessage);
        fileName = findViewById(R.id.Upfile_name_textView);
        lengthOfFile = findViewById(R.id.UPfile_length_textview);
        seekBar = findViewById(R.id.upseekbar);
        currentProgress = findViewById(R.id.Upcurrent_progress_txt);
        toolbar = findViewById(R.id.toolbar);
        priority = findViewById(R.id.oderOfMessage);
        priority.setEnabled(false);
        spinner = findViewById(R.id.upspinner1);
        toolbar.setTitle(R.string.updatemessage);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        isRecordingPermissionGranted();

        setSeekbarvalues();
        message = (Message) getIntent().getExtras().getSerializable("MessageId");
        id = message.getCatId();
        if (Constants.LOG) {
            Log.i("HSK", "id receive in updateMessage Activity: " + id);
        }
        mintues = TimeUnit.MILLISECONDS.toMinutes(message.getLength());
        seconds = TimeUnit.MILLISECONDS.toSeconds(message.getLength()) - TimeUnit.MINUTES.toSeconds(mintues);
        TextToSpeak.setText(message.getSpeakText());

        final Message updatedMessage = new Message(message.getMessageID(),message.getCatId(), message.getOderOfMessage(), message.getSpeakText(), message.getName(), message.getPath(), message.getLength(), message.getTime_added());
        //Auto Complete Text
        DbHelper dbHelper = new DbHelper(this);
        arrayList = dbHelper.getAllSentences();
        String[] sentencesSuggestion = new String[]{};
        sentencesSuggestion = new String[arrayList.size()];
        for (int j = 0; j < arrayList.size(); j++) {
            sentencesSuggestion[j] = String.valueOf(arrayList.get(j).getSentenceText());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, sentencesSuggestion);
        TextToSpeak.setThreshold(1);
        TextToSpeak.setAdapter(adapter);

        // Change size dynamically
        updateMessage.setTextSize(SettingUtils.getSize(this));
        TextToSpeak.setTextSize(SettingUtils.getSize(this));
        recordText.setTextSize(SettingUtils.getSize(this));
        fileName.setTextSize(SettingUtils.getSize(this));

        Integer[] messageOrderSuggestion = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, messageOrderSuggestion){


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                convertView = super.getDropDownView(position, convertView,
                        parent);

                convertView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams p = convertView.getLayoutParams();
                p.height = 70; // set the height of spinner dropdown
                convertView.setLayoutParams(p);
                return convertView;
            }

        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int oderOfMessage = message.getOderOfMessage();
        spinner.setSelection(dataAdapter.getPosition(oderOfMessage));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sentenceOrder = (Integer) parent.getItemAtPosition(position);
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onPlay(isplay);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isplay = !isplay;
            }
        });

        fileName.setText(R.string.recordPreivew);
        // lengthOfFile.setText(String.format("%02d:%02d", mintues, seconds));
        categoryName = String.valueOf(getIntent().getExtras().get("CategoryName"));
//        Update button action.
        updateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(UpdateMessages.this);
                if (Constants.LOG) {
                    Log.i("HSK", "Updated message : " +updatedMessage.toString()+" New Order: "+ sentenceOrder);
                }
                updatedMessage.setSpeakText(TextToSpeak.getText().toString());
                updatedMessage.setOderOfMessage(sentenceOrder);
                int result = dbHelper.updateMessages(updatedMessage);
                Intent intent = new Intent(UpdateMessages.this, CategoryDetails.class);
                Category category = new Category(id, categoryName);
                intent.putExtra("CategoryPosition", category);
                if (Constants.LOG) {
                    Log.i("HSK", "category value in update method : " + id);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                if (result > 0) {
                } else {
                    Toast.makeText(UpdateMessages.this, "SomeThing Went wrong", Toast.LENGTH_SHORT).show();
                }
            }

        });


        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onRecord();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Stoprecording();

                        //set new values in updatedMessage variable.
                        updatedMessage.setName(FileName);
                        updatedMessage.setPath(file.getAbsolutePath());
                        updatedMessage.setLength(mElaspedMillis);
                        updatedMessage.setTime_added(System.currentTimeMillis());

                        break;
                }
                return false;
            }
        });
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
        playAudio.setImageResource(R.drawable.ic_play_arrow);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.pause();
    }

    private void startPlaying() throws IOException {
        playAudio.setImageResource(R.drawable.ic_pause);
        mediaPlayer = new MediaPlayer();
        if (file != null) {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
        } else {
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

    private void setSeekbarvalues() {
        ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary));
        seekBar.getProgressDrawable().setColorFilter(colorFilter);
        seekBar.getThumb().setColorFilter(colorFilter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        // mediaPlayer.setDataSource(message.getPath());
        if (file != null) {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.seekTo(progress);
        } else {
            // Toast.makeText(this, "Please record the Audio First", Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();

            }
        });

    }

    private void stopPlaying() {
        playAudio.setImageResource(R.drawable.ic_play_arrow);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        seekBar.setProgress(seekBar.getMax());
        isplay = !isplay;

        currentProgress.setText(lengthOfFile.getText());
        seekBar.setProgress(seekBar.getMax());
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int mcurrentPosotion = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mcurrentPosotion);
                long mintues = TimeUnit.MILLISECONDS.toMinutes(mcurrentPosotion);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mcurrentPosotion) -
                        TimeUnit.MINUTES.toSeconds(mintues);
                currentProgress.setText(String.format("%02d:%02d", mintues, seconds));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(mRunnable, 1000);

    }


    private void onRecord() {


        record.setImageResource(R.drawable.ic_stop);
        File folder = new File(Environment.getExternalStorageDirectory() + "/EhsanVoice");
        if (!folder.exists()) {
            folder.mkdir();
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startRecording();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        recordText.setText(R.string.recording);

    }


    public void Stoprecording() {

        record.setImageResource(R.drawable.ic_mic_black_24dp);
        chronometer.stop();
        timeWhenPause = 0;
        recordText.setText(R.string.record);
        stopRecording();

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

//        if (textToSpeech != null) {
//            textToSpeech.stop();
//        }   textToSpeech.shutdown();
    }

    private void startRecording() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        FileName = "audio_" + ts;
        file = new File(Environment.getExternalStorageDirectory() + "/EhsanVoice/" + FileName + ".mp3");

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
        try {
            mediaRecorder.stop();
        } catch (RuntimeException stopException) {
            //handle cleanup here
            Toast.makeText(this, R.string.record, Toast.LENGTH_LONG).show();
        }
        mElaspedMillis = (System.currentTimeMillis() - mStartingTimesMillis);
        mediaRecorder.release();
        mintues = TimeUnit.MILLISECONDS.toMinutes(mElaspedMillis);
        seconds = TimeUnit.MILLISECONDS.toSeconds(mElaspedMillis) - TimeUnit.MINUTES.toSeconds(mintues);
        lengthOfFile.setText(String.format("%02d:%02d", mintues, seconds));

    }

    public boolean isRecordingPermissionGranted() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateMessages.this,
                Manifest.permission.RECORD_AUDIO)) {

        } else {
            ActivityCompat.requestPermissions(UpdateMessages.this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, RequestPermissionCode);
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {

            case RequestPermissionCode: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }
}
