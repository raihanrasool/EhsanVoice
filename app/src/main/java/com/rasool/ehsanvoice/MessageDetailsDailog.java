package com.rasool.ehsanvoice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rasool.ehsanvoice.Models.Message;
import com.rasool.ehsanvoice.database.DbHelper;

public class MessageDetailsDailog extends DialogFragment {
    Button edit, changeCategoryButton, deleteMessage;
    TextView textView;
    private Message items;
    String categoryName;
    int messageId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = (Message) getArguments().getSerializable("item");
        messageId = items.getMessageID();
        categoryName = (String) getArguments().getSerializable("categoryName");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.messagedetailsdailog, null);

        builder.setView(view);
        textView = view.findViewById(R.id.title);
        textView.setText(R.string.option);
        edit = view.findViewById(R.id.editMessage);
        changeCategoryButton = view.findViewById(R.id.changeCategory);
        deleteMessage = view.findViewById(R.id.deleteM);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateMessages.class);
                intent.putExtra("CategoryName", categoryName);
                intent.putExtra("MessageId", items);
                startActivity(intent);
                getActivity().finish();

            }
        });
        changeCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ChangeCategory.class);
                intent.putExtra("CategoryName", categoryName);
                intent.putExtra("MessageId", items);
                startActivity(intent);
                getActivity().finish();


            }
        });
        deleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure you want to delete the Message ");

                // add the buttons
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbHelper dbHelper1 = new DbHelper(getActivity());
                        int MessageId = items.getMessageID();
                        int result = dbHelper1.deleteMessages(MessageId);
                        if (result > 0) {
                            Toast.makeText(getActivity(), "Message Deleted", Toast.LENGTH_LONG).show();

                            // arrayList.remove(items);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "SomeThing went wrong", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return builder.create();
    }


}
