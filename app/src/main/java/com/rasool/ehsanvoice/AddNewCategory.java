package com.rasool.ehsanvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddNewCategory extends AppCompatActivity {
    ImageView addCategoryIcon;
    EditText Name;
    Button addCategory;
    String addNameCategory;
    Toolbar toolbar;
    private int RESULT_LOAD_IMG = 1;
    String picturePath;
    DbHelper dbHelper = new DbHelper(this);
    Category category;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        addCategoryIcon = findViewById(R.id.addPhoto);
        Name = findViewById(R.id.addName);
        addCategory = findViewById(R.id.AddCategory);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.newcategory);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        addCategory.setTextSize(SettingUtils.getSize(this));
        Name.setTextSize(SettingUtils.getSize(this));


        addCategoryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNameCategory = Name.getText().toString();
                if (!addNameCategory.isEmpty()) {
                    if(picturePath !=null){
                        category = new Category(picturePath, addNameCategory);
                    }
                    else{
                        category = new Category(addNameCategory);
                    }

                    dbHelper.addCategory(category);
                    dbHelper.close();
                    Intent intent = new Intent(AddNewCategory.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(AddNewCategory.this, "Please Enter Category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                picturePath = getRealPathFromURI(imageUri, this);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                addCategoryIcon.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddNewCategory.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }


    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCategory.setTextSize(SettingUtils.getSize(this));
        Name.setTextSize(SettingUtils.getSize(this));

    }


    Boolean isDrawable (String name){
        try{
            int x = Integer.parseInt(name);
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
