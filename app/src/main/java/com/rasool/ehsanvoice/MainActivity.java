package com.rasool.ehsanvoice;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.rasool.ehsanvoice.Adapters.MyTabAdapter;

public class MainActivity extends AppCompatActivity {

    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;
    Toolbar toolbar;
    private Boolean exit = false;

    public static final int RequestPermissionCode = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isWriteStoragePermissionGranted();
        toolbar = findViewById(R.id.toolbar);
        tabStrip = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new MyTabAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        menu.findItem(R.id.add).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dictionary:
                Intent SentencesDictionary = new Intent(MainActivity.this, SentencesDictionary.class);
                startActivity(SentencesDictionary);
                return true;
            case R.id.help:
                Intent helpintent = new Intent(MainActivity.this, Help.class);
                startActivity(helpintent);
                return true;
            case R.id.About:
                Intent Aboutintent = new Intent(MainActivity.this, About.class);
                startActivity(Aboutintent);
                return true;
            case R.id.textsize:
                openDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isWriteStoragePermissionGranted() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {

            case RequestPermissionCode: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }

    public void openDialog() {
        SeekBarDialog seekBarDialog = new SeekBarDialog();
        seekBarDialog.show(getSupportFragmentManager(), "SeekBarDailog");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        IsFinish("Do you want to exit application?");

    }

    public void IsFinish(String alertmessage) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // android.os.Process.killProcess(android.os.Process.myPid());
                        // This above line close correctly
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        // finish();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alertmessage)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}
