package com.rasool.ehsanvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rasool.ehsanvoice.Utils.SettingUtils;

public class About extends AppCompatActivity {
    TextView contactEmail, developerText,appName ,versionText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = findViewById(R.id.toolbar);
        contactEmail = findViewById(R.id.Contact);
        developerText = findViewById(R.id.developer);
        appName = findViewById(R.id.descriptionaboutus);
        versionText = findViewById(R.id.version);
        toolbar.setTitle(R.string.about);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.home);
        setSupportActionBar(toolbar);

        contactEmail.setOnClickListener(new View.OnClickListener() {
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
        contactEmail.setTextSize(SettingUtils.getSize(About.this));
        developerText.setTextSize(SettingUtils.getSize(About.this));
        appName.setTextSize(SettingUtils.getSize(About.this));
        versionText.setTextSize(SettingUtils.getSize(About.this));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(About.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
