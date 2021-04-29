package com.rasool.ehsanvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import android.speech.tts.TextToSpeech;
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

public class AddNewMessage extends AppCompatActivity {
    FloatingActionButton record, playAudio;
    Button  AddNewMessageButton;
    TextView NameOfFile, RecordText, currentProgress, LengthOfFile;
    Chronometer chronometer;
    SeekBar seekBar;
    Toolbar toolbar;
    Spinner spinner;
    EditText orderOfMessage;
    AutoCompleteTextView TextToSpeak;
    TextToSpeech textToSpeech;
    MediaRecorder mediaRecorder;
    long mStartingTimesMillis = 0;
    long mElaspedMillis = 0;
    private Handler handler = new Handler();
    public boolean isplay = false;
    File file;
    String FileName;
    long timeWhenPause = 0;
    int id;
    String categoryName;
    Integer sentenceOrder;
    MediaPlayer mediaPlayer;
    long mintues = 0;
    long seconds = 0;
    DbHelper dbHelper = new DbHelper(this);
    public static final int RequestPermissionCode = 5;
    ArrayList<SentencesDetailsItems> arrayList = new ArrayList<>();



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_message);
        toolbar = findViewById(R.id.toolbar);
        orderOfMessage = findViewById(R.id.oder);
        orderOfMessage.setEnabled(false);
        toolbar.setTitle(R.string.newmessage);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        isRecordingPermissionGranted();

        mintues = TimeUnit.MILLISECONDS.toMinutes(mElaspedMillis);
        seconds = TimeUnit.MILLISECONDS.toSeconds(mElaspedMillis) - TimeUnit.MINUTES.toSeconds(mintues);

        record = findViewById(R.id.btn_record);
        playAudio = findViewById(R.id.play);
        RecordText = findViewById(R.id.recording_status_txt);
        chronometer = findViewById(R.id.chronometer);
        TextToSpeak = findViewById(R.id.speaktext);
        AddNewMessageButton = findViewById(R.id.Addmessage);
        NameOfFile = findViewById(R.id.file_name_textView);
        currentProgress = findViewById(R.id.current_progress_txt);
        LengthOfFile = findViewById(R.id.file_length_textview);
        seekBar = findViewById(R.id.seekbar);
        spinner = findViewById(R.id.spinner1);
        setSeekbarvalues();

        // Change size dynamically
        AddNewMessageButton.setTextSize(SettingUtils.getSize(this));
        TextToSpeak.setTextSize(SettingUtils.getSize(this));
        RecordText.setTextSize(SettingUtils.getSize(this));
        NameOfFile.setTextSize(SettingUtils.getSize(this));

        // Auto cOMPLETE TEXT
        arrayList = dbHelper.getAllSentences();
        String[] suggestion = new String[]{};
        suggestion = new String[arrayList.size()];
        for (int j = 0; j < arrayList.size(); j++) {
            suggestion[j] = String.valueOf(arrayList.get(j).getSentenceText());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, suggestion);
        TextToSpeak.setThreshold(1);
        TextToSpeak.setAdapter(adapter);
        playAudio.setColorPressed(R.color.colorPressed);


        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null) {
                    try {
                        onPlay(isplay);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isplay = !isplay;
                } else {
                    Toast.makeText(AddNewMessage.this, "Please first Record the Audio", Toast.LENGTH_SHORT).show();
                }
            }
        });
        NameOfFile.setText(R.string.recordPreivew);
        LengthOfFile.setText(String.format("%02d:%02d", mintues, seconds));

        id = (int) getIntent().getExtras().get("Categoryid");
        if (Constants.LOG) {
            Log.i("HSK", "category id received in AddNewMessage Screen" + id);
        }
        categoryName = String.valueOf(getIntent().getExtras().get("CategoryName"));
        AddNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String speak = TextToSpeak.getText().toString();
                if (file!=null && !speak.isEmpty()) {
                    Message message = new Message(id, sentenceOrder, speak, FileName, file.getAbsolutePath(), mElaspedMillis, System.currentTimeMillis());
                    dbHelper.addNewMessage(message);
                    Intent intent = new Intent(AddNewMessage.this, CategoryDetails.class);
                    Category category = new Category(id, categoryName);
                    intent.putExtra("CategoryPosition", category);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    if (Constants.LOG){
                        Log.i(Constants.TAG,"condition 1 is executed ");
                    }
                    if (Constants.LOG){
                        Log.i(Constants.TAG,"path value in condition 1"+file.getAbsolutePath());
                    }
                }

                else if (!speak.isEmpty()){
                    Message message = new Message(id, sentenceOrder, speak, FileName, null, mElaspedMillis, System.currentTimeMillis());
                    dbHelper.addNewMessage(message);
                    dbHelper.close();
                    Intent intent = new Intent(AddNewMessage.this, CategoryDetails.class);
                    Category category = new Category(id, categoryName);
                    intent.putExtra("CategoryPosition", category);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    if (Constants.LOG){
                        Log.i(Constants.TAG,"condition 2 is executed ");
                    }
                }
              else {
                    Toast.makeText(AddNewMessage.this, "Please enter text ", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Integer[] sentenceOrder = new Integer[]{1,2,3,4,5,6,7,8,9,10};

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item,sentenceOrder ){

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                AddNewMessage.this.sentenceOrder = (Integer) parent.getItemAtPosition(position);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });




      //  Pause_button.setVisibility(View.GONE);
        record.setColorPressed(getResources().getColor(R.color.colorPrimary));

        // TOUCH TO RECORD LISTNER

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onRecord();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Stoprecording();
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
        mediaPlayer.setDataSource(file.getAbsolutePath());
        mediaPlayer.prepare();
        seekBar.setMax(mediaPlayer.getDuration());

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
        if (file != null) {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.seekTo(progress);
        } else {
            Toast.makeText(this, "Please record the Audio First", Toast.LENGTH_SHORT).show();
        }
//        mediaPlayer.prepare();
//        seekBar.setMax(mediaPlayer.getDuration());
//        mediaPlayer.seekTo(progress);
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

        currentProgress.setText(LengthOfFile.getText());
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

       // Intent intent = new Intent(AddNewMessage.this, RecordingServices.class);
        // if (start) {

        record.setImageResource(R.drawable.ic_stop);
        File folder = new File(Environment.getExternalStorageDirectory() + "/EhsanVoice");

        if (!folder.exists()) {
            folder.mkdir();
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        // startService(intent);
        startRecording();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        RecordText.setText("Recording.....");

    }


    public void Stoprecording() {
        record.setImageResource(R.drawable.ic_mic_black_24dp);
        chronometer.stop();
        timeWhenPause = 0;
        RecordText.setText(R.string.record);
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
            Toast.makeText(this, "Press and Hold to start the recording", Toast.LENGTH_LONG).show();
        }
        mElaspedMillis = (System.currentTimeMillis() - mStartingTimesMillis);
        mediaRecorder.release();
       // Toast.makeText(getApplicationContext(), "Recording saved" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        if (Constants.LOG) {
            Log.i("HSK", "absolute path" + file.getAbsolutePath());
        }
        mintues = TimeUnit.MILLISECONDS.toMinutes(mElaspedMillis);
        seconds = TimeUnit.MILLISECONDS.toSeconds(mElaspedMillis) - TimeUnit.MINUTES.toSeconds(mintues);
        LengthOfFile.setText(String.format("%02d:%02d", mintues, seconds));

    }

    public boolean isRecordingPermissionGranted() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewMessage.this,
                Manifest.permission.RECORD_AUDIO)) {

        } else {
            ActivityCompat.requestPermissions(AddNewMessage.this, new String[]{
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
