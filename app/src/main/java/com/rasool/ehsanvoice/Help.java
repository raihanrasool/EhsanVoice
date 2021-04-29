package com.rasool.ehsanvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.rasool.ehsanvoice.Utils.SettingUtils;

public class Help extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView,feebacktext ,aboutText,recordingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.htoolbar);
        textView = findViewById(R.id.guide);
        feebacktext = findViewById(R.id.feeback);
        aboutText = findViewById(R.id.abouttext);
        recordingText = findViewById(R.id.linkofrecording);
        toolbar.setTitle(R.string.help);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.home);
        setSupportActionBar(toolbar);

        Spanned html = Html.fromHtml(" Online guide: " +
                "<a href='https://sites.google.com/view/ehsanvoice'>https://sites.google.com/view/ehsanvoice)</a>");
                 textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(html);
                textView.setLinkTextColor(getResources().getColor(R.color.cardview_dark_background));
                textView.setTextSize(SettingUtils.getSize(Help.this));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this, MainActivity.class);
                startActivity(intent);
            }
        });


        feebacktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = "rasool.raihan@gmail.com";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + to));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");



                try {
                    // startActivity(emailIntent);
                    startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                } catch (ActivityNotFoundException e) {

                }
            }
        });

        feebacktext.setTextSize(SettingUtils.getSize(Help.this));
        aboutText.setTextSize(SettingUtils.getSize(Help.this));
        recordingText.setTextSize(SettingUtils.getSize(Help.this));




    }
}
