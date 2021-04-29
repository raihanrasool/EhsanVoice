package com.rasool.ehsanvoice.Interfaces;
import com.rasool.ehsanvoice.Models.Category;

public interface OnDatabaseChangeListener {

    void onNewDatabaseEntryAdded(Category category);


}
