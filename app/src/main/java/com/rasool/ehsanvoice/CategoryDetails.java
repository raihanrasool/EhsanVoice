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
import android.widget.Toast;

import com.rasool.ehsanvoice.Adapters.MessageAdapter;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.Models.Message;
import com.rasool.ehsanvoice.database.DbHelper;

import java.util.ArrayList;

public class CategoryDetails extends AppCompatActivity {
    RecyclerView categoriesList;
    Toolbar toolbar;
    ArrayList<Message> arrayList = new ArrayList<>();
    int id;
    String name;
    DbHelper dbHelper = new DbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        categoriesList = findViewById(R.id.categoryDetails);
        toolbar = findViewById(R.id.Dtoolbar);
        categoriesList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        categoriesList.setLayoutManager(llm);

        Category category = (Category) getIntent().getExtras().getSerializable("CategoryPosition");
        id = category.getId();
        if (Constants.LOG){
            Log.i("HSK","ID receive in CategoryDetails Activity::"+id);
        }
        name = category.getCategoriesName();
        toolbar.setTitle(name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.home);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });


        arrayList=dbHelper.getAllMessages(id);
        MessageAdapter messageAdapter = new MessageAdapter(this,arrayList,llm,name);
        categoriesList.setAdapter(messageAdapter);

        if (messageAdapter.getItemCount()!=0){

        }else {
            Toast.makeText(this, "Hit + sign on the right top corner and record your first message.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(CategoryDetails.this, AddNewMessage.class);
                intent.putExtra("Categoryid",id);
                intent.putExtra("CategoryName", name);
                startActivity(intent);
                return true;
            case R.id.dictionary:
                Intent Sentenceintent = new Intent(CategoryDetails.this,SentencesDictionary.class);
                startActivity(Sentenceintent);
                return true;
            case R.id.help:
                Intent helpIntent = new Intent(CategoryDetails.this, Help.class);
                startActivity(helpIntent);
                return true;
            case R.id.About:
                Intent Aboutintent = new Intent(CategoryDetails.this,About.class);
                startActivity(Aboutintent);
                return true;
            case R.id.textsize:
                openDialog();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void openDialog(){
        SeekBarDialog seekBarDialog = new SeekBarDialog();
        seekBarDialog.show(getSupportFragmentManager(),"SeekBarDailog");
    }
}
