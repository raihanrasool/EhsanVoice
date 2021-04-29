package com.rasool.ehsanvoice.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.melnykov.fab.FloatingActionButton;
import com.rasool.ehsanvoice.Adapters.CategoriesAdapter;
import com.rasool.ehsanvoice.AddNewCategory;
import com.rasool.ehsanvoice.CategoriesUpdate;
import com.rasool.ehsanvoice.CategoryDetails;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.R;
import com.rasool.ehsanvoice.database.DbHelper;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<Category> categoryList;
    GridView categories;
    FloatingActionButton addButton;
    DbHelper dbHelper;
    CategoriesAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment,container,false);
        categories = view.findViewById(R.id.simpleGridView);
        addButton = view.findViewById(R.id.categoryAdd);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DbHelper(getContext());
        categoryList = dbHelper.getAllCategories();
        if (categoryList == null) {
            Toast.makeText(getActivity(), "No category found ", Toast.LENGTH_LONG).show();
        } else {
            myAdapter = new CategoriesAdapter(getActivity(), R.layout.grid_view_items, categoryList);
            categories.setAdapter(myAdapter);
        }

        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CategoryDetails.class);
                Category category = categoryList.get(i);
                intent.putExtra("CategoryPosition", category);
                startActivity(intent);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Addintent = new Intent(getActivity(), AddNewCategory.class);
                startActivity(Addintent);
            }
        });


        categories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Category modification ");

                // add the buttons
                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Category category = categoryList.get(position);
                        Intent Updateintent = new Intent(getActivity(), CategoriesUpdate.class);
                        Updateintent.putExtra("CategoryPosition", category);
                        startActivity(Updateintent);
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure you want to delete the Category ");

                        // add the buttons
                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Category category = categoryList.get(position);
                                int categoryId = category.getId();
                                int result = dbHelper.deleteCategories(categoryId);
                                if (result > 0) {
                                    Toast.makeText(getActivity(), "Category Deleted", Toast.LENGTH_LONG).show();
                                    categoryList.remove(category);
                                    myAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getActivity(), "SomeThing went wrong", Toast.LENGTH_LONG).show();
                                }



                            }
                        });

                        // create and show the alert dialog
                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


    }
}


