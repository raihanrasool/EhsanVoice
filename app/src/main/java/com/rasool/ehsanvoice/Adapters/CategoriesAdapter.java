package com.rasool.ehsanvoice.Adapters;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rasool.ehsanvoice.Interfaces.OnDatabaseChangeListener;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.R;
import com.rasool.ehsanvoice.Utils.SettingUtils;
import com.rasool.ehsanvoice.database.DbHelper;
import java.util.ArrayList;


public class CategoriesAdapter extends ArrayAdapter implements OnDatabaseChangeListener {
    Context context;
    int layoutResourceId;
    DbHelper dbHelper;

    ArrayList<Category> categoryList ;


    public CategoriesAdapter(Context context, int textViewResourceId, ArrayList<Category> objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
        categoryList = objects;
        dbHelper.setOnDatabaseChangeListener(this);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public void onNewDatabaseEntryAdded(Category category) {
        categoryList.add(category);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        textView.setText(categoryList.get(position).getCategoriesName());
        textView.setTextSize(SettingUtils.getSize((Activity) context));

        String categoryImage = categoryList.get(position).getCategoriesImage();
        if (isDrawable(categoryImage)){
            imageView.setImageResource(Integer.parseInt(categoryImage));
        }
        else{
            // bitmap path code goes here...
            imageView.setImageBitmap(BitmapFactory.decodeFile(categoryList.get(position).getCategoriesImage()));
        }
//          Log.i("HSK", " Category Image ID: "+ tokensPath.length+" Last: "+tokensPath[tokensPath.length-1]);


        return v;
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