package com.rasool.ehsanvoice.Fragments;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rasool.ehsanvoice.Models.SentencesDetailsItems;
import com.rasool.ehsanvoice.R;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;

import java.util.ArrayList;
import java.util.Locale;

public class SpeakFragment extends Fragment {
    AutoCompleteTextView speakText;
    Button speak,clear;
    ArrayList<SentencesDetailsItems> arrayList = new ArrayList<>();
    TextToSpeech textToSpeech;
    String textSpeak;
    ArrayAdapter<String> adapter;
    String[] suggestion = new String[]{};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speak_fragment,container,false);
        speakText = view.findViewById(R.id.textspeak);
        speak = view.findViewById(R.id.btn_speak);
        clear = view.findViewById(R.id.btn_clear);

        speakText.setTextSize(SettingUtils.getSize(getActivity()));
        speak.setTextSize(SettingUtils.getSize(getActivity()));
        clear.setTextSize(SettingUtils.getSize(getActivity()));
        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DbHelper dbHelper = new DbHelper(getActivity());
        // Auto cOMPLETE TEXT
        arrayList = dbHelper.getAllSentences();
        suggestion = new String[arrayList.size()];
        for (int j = 0; j < arrayList.size(); j++) {
            suggestion[j] = String.valueOf(arrayList.get(j).getSentenceText());
        }
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item, suggestion);
        speakText.setThreshold(1);
        speakText.setAdapter(adapter);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(getActivity());
                String data = speakText.getText().toString();
               // Log.i("TTS", "button clicked: " + data);
                int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);

                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }
                SentencesDetailsItems sentencesDetailsItems =  new SentencesDetailsItems(data);

                if (!data.isEmpty()) {
                    if (!dbHelper.isSentenceAvailable(data)){
                        dbHelper.addNewSentences(sentencesDetailsItems);
                    }
                    dbHelper.close();
                }else {
                    Toast.makeText(getActivity(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakText.getText().clear();
            }
        });



        textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.UK);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getActivity(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        speakText.setTextSize(SettingUtils.getSize(getActivity()));
        speak.setTextSize(SettingUtils.getSize(getActivity()));
        clear.setTextSize(SettingUtils.getSize(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
    }
}
