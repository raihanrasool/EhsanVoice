package com.rasool.ehsanvoice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.rasool.ehsanvoice.Utils.SettingUtils;


public class SeekBarDialog extends AppCompatDialogFragment {
    TextView textView,textsmall,textmediam,textLarge,textExtraLarge,textXXXLarge;
    SeekBar sb;
    int seekValues;
    int saveProgress;
    int textSize = 16;



    public SeekBarDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.seek_bar_dailog,null);
        builder.setView(view);
        textView = view.findViewById(R.id.textTitle);
        textsmall = view.findViewById(R.id.size);
        textmediam = view.findViewById(R.id.size1);
        textLarge = view.findViewById(R.id.size2);
        textExtraLarge = view.findViewById(R.id.size4);
        textXXXLarge = view.findViewById(R.id.size3);

        textsmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog(16,textsmall);

            }
        });

        textmediam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog(18,textmediam);
               // changeBackground(textmediam);
            }
        });

        textLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog(20,textLarge);
                //changeBackground(textLarge);
            }
        });

        textExtraLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog(22,textExtraLarge);
               onResume();
            }
        });

        textXXXLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog(24,textXXXLarge);
                //changeBackground(textXXXLarge);
            }
        });





        return builder.create();
    }


    public void openDailog(final int size, final TextView textView){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to set This Size ");

        // add the buttons
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingUtils.setSize(getActivity(),size);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "Text Size Changed", Toast.LENGTH_LONG).show();
                changeBackground(textView);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // create and show the alert dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void changeBackground(TextView textView){

        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SettingUtils.getSize(getActivity())==16) {
            changeBackground(textsmall);
        }
        if (SettingUtils.getSize(getActivity())==18) {
            changeBackground(textmediam);
        }
        if (SettingUtils.getSize(getActivity())==20) {
            changeBackground(textLarge);
        }
        if (SettingUtils.getSize(getActivity())==22) {
            changeBackground(textExtraLarge);
        }
        if (SettingUtils.getSize(getActivity())==24) {
            changeBackground(textXXXLarge);
        }
    }
}
