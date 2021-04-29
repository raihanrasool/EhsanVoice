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

public class SentencesDialog extends AppCompatDialogFragment {
    private EditText addsentence;
    Button AddSentenceButton;
    AppCompatTextView textView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dailog,null);

         builder.setView(view);




         addsentence = view.findViewById(R.id.Sentences);
         textView = view.findViewById(R.id.titleOfSentence);
         textView.setText(R.string.addnewsentenc);
         textView.setTextSize(SettingUtils.getSize(getActivity()));
        AddSentenceButton = view.findViewById(R.id.addsentences);
        AddSentenceButton.setTextSize(SettingUtils.getSize(getActivity()));
        AddSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(getActivity());
                String NewSentence =addsentence.getText().toString();
                SentencesDetailsItems sentencesDetailsItems = new SentencesDetailsItems(NewSentence);
                if (!NewSentence.isEmpty()) {
                    if (!dbHelper.isSentenceAvailable(NewSentence)){
                        dbHelper.addNewSentences(sentencesDetailsItems);
                    }
                    Toast.makeText(getActivity(), "New Sentence added", Toast.LENGTH_LONG).show();
                    dbHelper.close();
                    Intent intent = new Intent(getActivity(),SentencesDictionary.class);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(), "Please Enter Sentence", Toast.LENGTH_LONG).show();
                }
            }
        });

        return builder.create();
    }
}
