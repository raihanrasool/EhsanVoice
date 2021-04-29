package com.rasool.ehsanvoice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;

import com.rasool.ehsanvoice.Models.SentencesDetailsItems;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;

public class UpdateSentencesDailog extends AppCompatDialogFragment {
    private EditText UpdatesentenceeditText;
    Button UpdateSentenceButton;
    AppCompatTextView textView;
    int id;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_update_sentences,null);

        builder.setView(view);



        SentencesDetailsItems sentencesDetailsItems = (SentencesDetailsItems) getArguments().getSerializable("SentenceId");
        id = sentencesDetailsItems.getSentenceId();
        UpdatesentenceeditText = view.findViewById(R.id.updateSentences);
        UpdateSentenceButton = view.findViewById(R.id.btn_updateSentences);
        UpdatesentenceeditText.setText(sentencesDetailsItems.getSentenceText());
        textView = view.findViewById(R.id.titleOfUpdate);
        textView.setText(R.string.updatesentences);
        // Change Size Dynamically
        textView.setTextSize(SettingUtils.getSize(getActivity()));
        UpdateSentenceButton.setTextSize(SettingUtils.getSize(getActivity()));


        UpdateSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(getActivity());
                String NewSentence =UpdatesentenceeditText.getText().toString();
                SentencesDetailsItems sentencesDetailsItems = new SentencesDetailsItems(id,NewSentence);
               int result = dbHelper.updateSentences(sentencesDetailsItems);
                dbHelper.close();
                if (result >0){
                    Toast.makeText(getActivity(), "Sentence updated ", Toast.LENGTH_LONG).show();
                    Intent intent =  new Intent(getActivity(),SentencesDictionary.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "SomeThing went wrong", Toast.LENGTH_SHORT).show();
                }

                getActivity().finish();
            }
        });

        return builder.create();
    }
}


