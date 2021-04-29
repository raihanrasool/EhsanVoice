package com.rasool.ehsanvoice.Interfaces;

import com.rasool.ehsanvoice.Models.Message;

public interface OnDatabaseChangeListenerDetailsItems {
    void onNewDatabaseEntryAdded(Message message);
}
