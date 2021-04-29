package com.rasool.ehsanvoice;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.Models.Message;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;

import java.util.ArrayList;

public class ChangeCategory extends AppCompatActivity {
    TextInputEditText currentCategory, newCategory;
    Spinner spinner;
    Message message;
    Toolbar toolbar;
    TextView selectCategory;
    Button changeCategory;
    int id;
    String categoryName;
    String newName;
    ArrayList<Category> categoryList = new ArrayList<>();
    DbHelper dbHelper = new DbHelper(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_category);
        currentCategory = findViewById(R.id.current_category1);
        spinner = findViewById(R.id.spinner1);
        newCategory = findViewById(R.id.newCategory1);
        toolbar = findViewById(R.id.ctoolbar);
        changeCategory = findViewById(R.id.btn_changeCategory);
        selectCategory = findViewById(R.id.SelectCategory);
        toolbar.setTitle(R.string.changeCategory);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        currentCategory.setEnabled(false);
        newCategory.setEnabled(false);
        newCategory.setFocusable(false);
        currentCategory.setFocusable(false);


        //Change size Dynamically
        currentCategory.setTextSize(SettingUtils.getSize(this));
        newCategory.setTextSize(SettingUtils.getSize(this));
        selectCategory.setTextSize(SettingUtils.getSize(this));
        changeCategory.setTextSize(SettingUtils.getSize(this));




        message = (Message) getIntent().getExtras().getSerializable("MessageId");
        id = message.getMessageID();
        categoryName = (String) getIntent().getExtras().getSerializable("CategoryName");
        currentCategory.setText(categoryName);

        changeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result=dbHelper.updateMessageCategory(id, newName);

                if (result>0){
                    Toast.makeText(ChangeCategory.this, "Category Changed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeCategory.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    Toast.makeText(ChangeCategory.this, "SomeThing went wrong", Toast.LENGTH_SHORT).show();

                }

            }
        });


    // GET ALL CATEGORIES AND MAKE  CATEGORY SUGGESTION HERE IN DROPDOWN FOR CHANGE CATEGORY
        categoryList = dbHelper.getAllCategories();

        String[] categorySuggestion = new String[]{};
        categorySuggestion = new String[categoryList.size()];
        for (int j = 0; j < categoryList.size(); j++) {
            categorySuggestion[j] = String.valueOf(categoryList.get(j).getCategoriesName());
        }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, categorySuggestion);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    newName= (String) parent.getItemAtPosition(position);
                    newCategory.setText(newName);
                } // to close the onItemSelected
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });





    }



}
