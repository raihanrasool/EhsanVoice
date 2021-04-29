package com.rasool.ehsanvoice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rasool.ehsanvoice.Models.SentencesDetailsItems;
import com.rasool.ehsanvoice.R;
import com.rasool.ehsanvoice.UpdateSentencesDailog;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;
import java.util.ArrayList;

public class SentencesAdapter extends RecyclerView.Adapter<SentencesAdapter.SentencesViewHolder> {
    Context context;
    ArrayList<SentencesDetailsItems> arrayList;
    LinearLayoutManager llm;



    public SentencesAdapter(Context context, ArrayList<SentencesDetailsItems> arrayList, LinearLayoutManager llm) {
        this.context = context;
        this.arrayList = arrayList;
        this.llm = llm;
    }

    @NonNull
    @Override
    public SentencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sentences_details_items,parent,false);
        return new SentencesViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull SentencesViewHolder holder, final int position) {
        SentencesDetailsItems sentencesDetailsItems=arrayList.get(position);

        holder.sentenceItems.setText(sentencesDetailsItems.getSentenceText());
        holder.sentenceItems.setTextSize(SettingUtils.getSize((Activity) context));
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateSentencesDailog updateSentencesDailog = new UpdateSentencesDailog();
                Bundle b = new Bundle();
                b.putSerializable("SentenceId", arrayList.get(position));
                updateSentencesDailog.setArguments(b);
                FragmentTransaction fragmentTransaction = ((FragmentActivity) context)
                        .getSupportFragmentManager().beginTransaction();

                updateSentencesDailog.show(fragmentTransaction,"updateDailog");



            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Are you sure you want to delete the Sentence ");

                // add the buttons
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbHelper dbHelper = new DbHelper(context);
                        SentencesDetailsItems sentencesDetailsItems = arrayList.get(position);
                        int SentenceId = sentencesDetailsItems.getSentenceId();
                        int result = dbHelper.deleteSentences(SentenceId);
                        if (result>0){
                            Toast.makeText(context, "Sentence Deleted", Toast.LENGTH_LONG).show();
                            arrayList.remove(sentencesDetailsItems);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "SomeThing went Wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                // create and show the alert dialog
                android.app.AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SentencesViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView delete,edit;
        TextView sentenceItems;

        public SentencesViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.deletesentence);
            edit = itemView.findViewById(R.id.editSentences);
            sentenceItems = itemView.findViewById(R.id.sentences);

        }
    }

    public void openDialog(){
        UpdateSentencesDailog updateSentenceDialog = new UpdateSentencesDailog();

    }
}
