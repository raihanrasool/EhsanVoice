package com.rasool.ehsanvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rasool.ehsanvoice.Adapters.SentencesAdapter;
import com.rasool.ehsanvoice.Models.SentencesDetailsItems;
import com.rasool.ehsanvoice.database.DbHelper;

import java.util.ArrayList;

public class SentencesDictionary extends AppCompatActivity {
    RecyclerView sentencesList;
    Toolbar toolbar;
    ArrayList<SentencesDetailsItems> arrayList = new ArrayList<>();
    DbHelper dbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences_dictionary);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sentencedictionary);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.home);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentencesDictionary.this, MainActivity.class);
                startActivity(intent);
            }
        });


        sentencesList = findViewById(R.id.dictionarySentences);
        sentencesList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        sentencesList.setLayoutManager(llm);
         arrayList = dbHelper.getAllSentences();
         if(Constants.LOG){
             Log.i("HSK","sentences Array size"+arrayList.size());
         }


       SentencesAdapter sentencesAdapter = new SentencesAdapter(this,arrayList,llm);
        sentencesList.setAdapter(sentencesAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                openDialogForSentences();
                return true;
            case R.id.dictionary:
                Intent SentencesDictionary = new Intent(SentencesDictionary.this, SentencesDictionary.class);
                startActivity(SentencesDictionary);
                return true;
            case R.id.help:
                Intent helpintent = new Intent(SentencesDictionary.this, Help.class);
                startActivity(helpintent);
                return true;
            case R.id.About:
                Intent Aboutintent = new Intent(SentencesDictionary.this, About.class);
                startActivity(Aboutintent);
                return true;


            case R.id.textsize:
                openDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDialogForSentences(){
        SentencesDialog sentencesDialog = new SentencesDialog();
        sentencesDialog.show(getSupportFragmentManager(),"Sentences Dialog");
    }

    public void openDialog(){
        SeekBarDialog seekBarDialog = new SeekBarDialog();
        seekBarDialog.show(getSupportFragmentManager(),"SeekBarDailog");
    }
}
