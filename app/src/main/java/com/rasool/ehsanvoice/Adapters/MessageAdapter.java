package com.rasool.ehsanvoice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rasool.ehsanvoice.Constants;
import com.rasool.ehsanvoice.Interfaces.OnDatabaseChangeListenerDetailsItems;
import com.rasool.ehsanvoice.MessageDetailsDailog;
import com.rasool.ehsanvoice.Models.Message;
import com.rasool.ehsanvoice.PlayBack;
import com.rasool.ehsanvoice.PlayBackActivity;
import com.rasool.ehsanvoice.R;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;
import java.util.ArrayList;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CategoriesDetailsViewHolder> implements OnDatabaseChangeListenerDetailsItems {
    Context context;
    ArrayList<Message> arrayList;
    LinearLayoutManager llm;
    DbHelper dbHelper;
    TextToSpeech textToSpeech;
    String categoryName;
    Message message;

    public MessageAdapter(Context context, ArrayList<Message> arrayList, LinearLayoutManager llm, String categoryName) {
        this.context = context;
        this.arrayList = arrayList;
        this.llm = llm;
        this.categoryName=categoryName;
        dbHelper.setOnDatabaseChangeListenerofDetelisItem(this);
    }

    @NonNull
    @Override
    public CategoriesDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_details_items, parent, false);
        return new CategoriesDetailsViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesDetailsViewHolder holder, final int position) {
         message = arrayList.get(position);
        holder.speak.setText(message.getSpeakText());
        holder.speak.setTextSize(SettingUtils.getSize((Activity) context));

        holder.textSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message Items = arrayList.get(position);
                String data = Items.getSpeakText();
                Log.i("TTS", "button clicked: " + data);
                int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);
                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onNewDatabaseEntryAdded(Message message) {
        arrayList.add(message);
        notifyDataSetChanged();

    }

    public class CategoriesDetailsViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView play, textSpeech;
        TextView speak;
        CardView cardView;


        public CategoriesDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            play = itemView.findViewById(R.id.playrecord);
            textSpeech = itemView.findViewById(R.id.hearing);
            speak = itemView.findViewById(R.id.speaktest);
            cardView = itemView.findViewById(R.id.card_view);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    PlayBack playBack = new PlayBack();
//                    Bundle b = new Bundle();
//                    b.putSerializable("item", arrayList.get(getAdapterPosition()));
//                    playBack.setArguments(b);
//                    FragmentTransaction fragmentTransaction = ((FragmentActivity) context)
//                            .getSupportFragmentManager().beginTransaction();

                    if (message.getPath()!=null) {
                        if (Constants.LOG){
                            Log.i(Constants.TAG,"path value in adapter"+message.getPath());
                        }
                       // playBack.show(fragmentTransaction, "dailog PlayBack");
                        Intent myIntent = new Intent(context, PlayBackActivity.class);
                        myIntent.putExtra("item", arrayList.get(getAdapterPosition())); //Optional parameters
                        context.startActivity(myIntent);
                    }else {
                        Toast.makeText(context, R.string.showdailog, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int ttsLang = textToSpeech.setLanguage(Locale.US);

                        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "The Language is not supported!");
                        } else {
                            Log.i("TTS", "Language Supported.");
                        }
                        Log.i("TTS", "Initialization success.");
                    } else {
                        Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    PlayBack playBack = new PlayBack();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("item", arrayList.get(getAdapterPosition()));
//                    playBack.setArguments(bundle);
//                    FragmentTransaction fragmentTransaction = ((FragmentActivity) context)
//                            .getSupportFragmentManager().beginTransaction();
                    if (message.getPath()!=null) {
                        if (Constants.LOG){
                            Log.i(Constants.TAG,"path value in adapter"+message.getPath());
                        }
                       // playBack.show(fragmentTransaction, "dailog PlayBack");
                        Intent myIntent = new Intent(context, PlayBackActivity.class);
                        myIntent.putExtra("item", arrayList.get(getAdapterPosition())); //Optional parameters
                        context.startActivity(myIntent);
                    }else {
                        Toast.makeText(context, R.string.showdailog, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    MessageDetailsDailog messageDetailsDailog = new MessageDetailsDailog();
                    Bundle b = new Bundle();
                    b.putSerializable("item", arrayList.get(getAdapterPosition()));
                    b.putSerializable("categoryName", categoryName);
                    messageDetailsDailog.setArguments(b);
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) context)
                            .getSupportFragmentManager().beginTransaction();
                    messageDetailsDailog.show(fragmentTransaction, "MessageDteailsDialog");
                    return true;
                }
            });

        }
    }


}
