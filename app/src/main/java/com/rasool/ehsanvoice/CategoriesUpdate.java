package com.rasool.ehsanvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CategoriesUpdate extends AppCompatActivity {
    ImageView UpdateCategoryIcon;
    EditText name;
    Button updateCategory,editImage;
    Toolbar toolbar;
    int id;
    private int RESULT_LOAD_IMG = 1;
    String picturePath = null;
    DbHelper dbHelper = new DbHelper(this);
     Category category;
    Category updatedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_update);
        UpdateCategoryIcon = findViewById(R.id.updatePhoto);
        name = findViewById(R.id.updateName);
        editImage = findViewById(R.id.editimage);
        updateCategory = findViewById(R.id.UpdateCategory);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.updatecategory);

        showSoftKeyboard(name);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        // Change size dynamically
        name.setTextSize(SettingUtils.getSize(this));
        updateCategory.setTextSize(SettingUtils.getSize(this));


        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        category = (Category) getIntent().getExtras().getSerializable("CategoryPosition");
        id = category.getId();
        Bitmap bmp = BitmapFactory.decodeFile(category.getCategoriesImage());
        UpdateCategoryIcon.setImageBitmap(bmp);
        name.setText(category.getCategoriesName());

         updatedCategory = new Category(category.getId(),category.getCategoriesImage(),category.getCategoriesName());
        picturePath = updatedCategory.getCategoriesImage();
        if (Constants.LOG){
            Log.i(Constants.TAG,"picture path in update category on create: "+updatedCategory.getCategoriesImage()+"category name: "+updatedCategory.getCategoriesName());
        }

        UpdateCategoryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        updateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = name.getText().toString();
                updatedCategory.setCategoriesImage(picturePath);
                updatedCategory.setCategoriesName(categoryName);
               int result = dbHelper.updateCategory(updatedCategory);
                if (Constants.LOG){
                    Log.i(Constants.TAG,"picture path in update method : "+updatedCategory.getCategoriesImage());
                }

               if (result>0){
                  Intent intent = new Intent(CategoriesUpdate.this,MainActivity.class);
                  // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);

               }else {
                   Toast.makeText(CategoriesUpdate.this, "SomeThing went wrong", Toast.LENGTH_SHORT).show();

               }

            }

        });

        if (isDrawable(updatedCategory.getCategoriesImage())){
            UpdateCategoryIcon.setImageResource(Integer.parseInt(updatedCategory.getCategoriesImage()));
        }else {
            Bitmap bmp1 = BitmapFactory.decodeFile(updatedCategory.getCategoriesImage());
            UpdateCategoryIcon.setImageBitmap(bmp1);
            if (Constants.LOG){
                Log.i(Constants.TAG,"picture path in update category: "+updatedCategory.getCategoriesImage());
            }
        }

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
                UpdateCategoryIcon.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(CategoriesUpdate.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
           // Toast.makeText(CategoriesUpdate.this, "You haven't picked Image",Toast.LENGTH_LONG).show();

        }
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
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

    Boolean isDrawable (String name){
        try{
            int x = Integer.parseInt(name);
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


}
