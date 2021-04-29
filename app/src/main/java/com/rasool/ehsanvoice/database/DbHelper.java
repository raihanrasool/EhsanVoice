package com.rasool.ehsanvoice.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.rasool.ehsanvoice.Constants;
import com.rasool.ehsanvoice.Interfaces.OnDatabaseChangeListenerDetailsItems;
import com.rasool.ehsanvoice.Models.Category;
import com.rasool.ehsanvoice.Models.Message;
import com.rasool.ehsanvoice.Interfaces.OnDatabaseChangeListener;
import com.rasool.ehsanvoice.Models.SentencesDetailsItems;
import com.rasool.ehsanvoice.R;
import java.util.ArrayList;


public class DbHelper extends SQLiteOpenHelper {
    private Context mcontext;
    public static final String DATABASE_NAME = "EhsanVoice.db";
    public static final int DATABASE_VERSION = 1;
    // TABLE1 NAME
    public static final String TABLE_OF_CATEGORIES = "saved_categories_table";
    // CATEGORIES TABBLE COLUNMNS
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_NAME = "name";

    // TABLE OF MESSAGES
    public static final String TABLE_OF_NEW_MESSAGES = "saved_messages_table";
    //MESSAGES TABLE COLUMNS
    public static final String CAT_ID = "categoryid";
    public static final String COLUMN_NAME_ORDER_MESSAGE = "oder";
    public static final String COLUMN_NAME_OF_SPEAK_TEXT = "speaktext";
    public static final String COLUMN_NAME_OF_RECORDING = "name";
    public static final String COLUMN_PATH_RECORDING = "path";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_TIME_ADDED = "time_added";

    // Manage Sentences table
    public static final String TABLE_OF_SENTENCES = "saved_sentences_table";
    public static final String COLUMN_NAME_OF_SENTENCES_DICTIONARY = "sentences";


    public static final String COMA_SEP = ",";

    private static OnDatabaseChangeListener mOnDatabaseChangeListener;
    private static OnDatabaseChangeListenerDetailsItems mOnDatabaseChangeListenerofDetelisItem;


    public static final String SQLITE_CREATE_TABLE = "CREATE TABLE " + TABLE_OF_CATEGORIES + " (" + "id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT" + COMA_SEP +
            COLUMN_NAME + " TEXT" + COMA_SEP +
            COLUMN_PATH + " TEXT" + ")";

    public static final String SQLITE_CREATE_TABLE_OF_MESSAGES = "CREATE TABLE " + TABLE_OF_NEW_MESSAGES + " (" + "id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT" + COMA_SEP +
            CAT_ID + " INTEGER" + COMA_SEP +
            COLUMN_NAME_ORDER_MESSAGE + " INTEGER" + COMA_SEP +
            COLUMN_NAME_OF_SPEAK_TEXT + " TEXT" + COMA_SEP +
            COLUMN_NAME_OF_RECORDING + " TEXT" + COMA_SEP +
            COLUMN_PATH_RECORDING + " TEXT" + "NULL" + COMA_SEP +
            COLUMN_LENGTH + " INTEGER" + COMA_SEP +
            COLUMN_TIME_ADDED + " INTEGER " + ")";

    public static final String SQLITE_CREATE_TABLE_OF_SENTENCES = "CREATE TABLE " + TABLE_OF_SENTENCES + " (" + "id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT" + COMA_SEP +
            COLUMN_NAME_OF_SENTENCES_DICTIONARY + " TEXT " + ")";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLITE_CREATE_TABLE);
        db.execSQL(SQLITE_CREATE_TABLE_OF_MESSAGES);
        db.execSQL(SQLITE_CREATE_TABLE_OF_SENTENCES);

        String[] categories = mcontext.getResources().getStringArray(R.array.SuggestionSentences);
        int length = categories.length;
        for (int i = 0; i < length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_OF_SENTENCES_DICTIONARY, categories[i]);
            db.insert(TABLE_OF_SENTENCES, null, cv);
        }

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, "Home");
        cv.put(COLUMN_PATH, String.valueOf(R.drawable.ic_home_));
        db.insert(TABLE_OF_CATEGORIES, null, cv);
        cv.clear();
        cv.put(COLUMN_NAME, "Work");
        cv.put(COLUMN_PATH, String.valueOf(R.drawable.ic_work));
        db.insert(TABLE_OF_CATEGORIES, null, cv);
        cv.clear();
        cv.put(COLUMN_NAME, "Personal");
        cv.put(COLUMN_PATH, String.valueOf(R.drawable.person));
        db.insert(TABLE_OF_CATEGORIES, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OF_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OF_NEW_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OF_SENTENCES);
    }

    public boolean addNewSentences(SentencesDetailsItems sentencesDetailsItems) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_OF_SENTENCES_DICTIONARY, sentencesDetailsItems.getSentenceText());
            db.insert(TABLE_OF_SENTENCES, null, values);

//            if (mOnDatabaseChangeListenerofDetelisItem!=null){
//                mOnDatabaseChangeListenerofDetelisItem.onNewDatabaseEntryAdded(categoriesDetailsItems);
//            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // DELETE SENTENCES METHOD
    public int deleteSentences(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OF_SENTENCES, "id=?", new String[]{String.valueOf(id)});
    }

    public Boolean isSentenceAvailable(String sentence) {
        SQLiteDatabase db = this.getReadableDatabase();

        //query to check if sentence exists?
        String[] columns = {COLUMN_NAME_OF_SENTENCES_DICTIONARY};
        String selection = COLUMN_NAME_OF_SENTENCES_DICTIONARY + " =?";
        String[] selectionArgs = {sentence};
        String limit = "1";
        Cursor cursor = db.query(TABLE_OF_SENTENCES, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // UPDATE SENTENCES METHOD
    public int updateSentences(SentencesDetailsItems sentencesDetailsItems) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_OF_SENTENCES_DICTIONARY, sentencesDetailsItems.getSentenceText());
        return db.update(TABLE_OF_SENTENCES, values, "id=?", new String[]{String.valueOf(sentencesDetailsItems.getSentenceId())});
    }

    // GET ALL SENTENCES METHOD
    public ArrayList<SentencesDetailsItems> getAllSentences() {
        ArrayList<SentencesDetailsItems> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_OF_SENTENCES, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String Sentences = cursor.getString(1);
                SentencesDetailsItems sentencesDetailsItems = new SentencesDetailsItems(id, Sentences);
                arrayList.add(sentencesDetailsItems);

            }
            cursor.close();
            return arrayList;
        } else {

            return null;
        }
    }


    // New Messages Add Method
    public boolean addNewMessage(Message message) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CAT_ID, message.getCatId());
            values.put(COLUMN_NAME_ORDER_MESSAGE, message.getOderOfMessage());
            values.put(COLUMN_NAME_OF_SPEAK_TEXT, message.getSpeakText());
            values.put(COLUMN_NAME_OF_RECORDING, message.getName());
            values.put(COLUMN_PATH_RECORDING, message.getPath());
            values.put(COLUMN_LENGTH, message.getLength());
            values.put(COLUMN_TIME_ADDED, message.getTime_added());
            db.insert(TABLE_OF_NEW_MESSAGES, null, values);

            if (mOnDatabaseChangeListenerofDetelisItem != null) {
                mOnDatabaseChangeListenerofDetelisItem.onNewDatabaseEntryAdded(message);
            }

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // GET ALL MESSAGES
    public ArrayList<Message> getAllMessages(int Categoryid) {
        ArrayList<Message> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OF_NEW_MESSAGES + " WHERE categoryid = ? ORDER BY oder DESC ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(Categoryid)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int messageId = cursor.getInt(0);
                int categoryId = cursor.getInt(1);
                int oderOfMessage = cursor.getInt(2);
                String speak = cursor.getString(3);
                String Nameofrecording = cursor.getString(4);
                String PathOfrecording = cursor.getString(5);
                int LengthOfrecording = cursor.getInt(6);
                int Timeadded = cursor.getInt(7);
                Message message = new Message(messageId, categoryId, oderOfMessage, speak, Nameofrecording, PathOfrecording, LengthOfrecording, Timeadded);
                arrayList.add(message);
                Log.i("Hsk", "values retrived from arraylist " + arrayList.size());

            }
            cursor.close();
            return arrayList;
        } else {

            return null;
        }

    }

    // UPFDATE MESSAGE
    public int updateMessages(Message message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ORDER_MESSAGE, message.getOderOfMessage());
        values.put(COLUMN_NAME_OF_SPEAK_TEXT, message.getSpeakText());
        values.put(COLUMN_NAME_OF_RECORDING, message.getName());
        values.put(COLUMN_PATH_RECORDING, message.getPath());
        values.put(COLUMN_LENGTH, message.getLength());
        values.put(COLUMN_TIME_ADDED, message.getTime_added());
        return db.update(TABLE_OF_NEW_MESSAGES, values, "id=?", new String[]{String.valueOf(message.getMessageID())});
    }


    // UPDATE MESSAGE CATEGORY
    public int updateMessageCategory(int messageID, String newCat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor mCursor = db.rawQuery(
                "SELECT id  FROM saved_categories_table  WHERE name= '" + newCat + "'", null);

        if (Constants.LOG) {
            Log.i("HSK", "Data in Method => Message ID: " + messageID + ", Cat Name: " + newCat);
        }
        int catId = -1;
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                catId = mCursor.getInt(0);
                Log.i("HSK", "New catID from DB:" + mCursor.getInt(0));
            }
        }
        if (Constants.LOG) {
            Log.i("HSK", "New catID from DB:" + catId);
        }
        values.put(CAT_ID, catId);
        return db.update(TABLE_OF_NEW_MESSAGES, values, "id=?", new String[]{String.valueOf(messageID)});
    }

    // DELETE MESSAGE
    public int deleteMessages(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OF_NEW_MESSAGES, "id=?", new String[]{String.valueOf(id)});
    }


    //  ADD CATEGORY
    public boolean addCategory(Category category) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PATH, category.getCategoriesImage());
            values.put(COLUMN_NAME, category.getCategoriesName());
            db.insert(TABLE_OF_CATEGORIES, null, values);
            Log.i("HSK", "getpath: " + category.getCategoriesImage());
            Log.i("HSK", "getName: " + category.getCategoriesName());

            if (mOnDatabaseChangeListener != null) {
                mOnDatabaseChangeListener.onNewDatabaseEntryAdded(category);
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    // GET ALL CATEGORY
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_OF_CATEGORIES, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String Name = cursor.getString(1);
                String Path = cursor.getString(2);
                Category category = new Category(id, Path, Name);
                arrayList.add(category);
            }
            cursor.close();
            return arrayList;
        } else {

            return null;
        }
    }

    public static void setOnDatabaseChangeListener(OnDatabaseChangeListener listener) {
        mOnDatabaseChangeListener = listener;
    }

    public static void setOnDatabaseChangeListenerofDetelisItem(OnDatabaseChangeListenerDetailsItems listener) {
        mOnDatabaseChangeListenerofDetelisItem = listener;
    }

    // DELETE CATEGORY
    public int deleteCategories(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OF_CATEGORIES, "id=?", new String[]{String.valueOf(id)});
    }

    // UPDATE CATEGORY
    public int updateCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, category.getCategoriesName());
        values.put(COLUMN_PATH, category.getCategoriesImage());
        return db.update(TABLE_OF_CATEGORIES, values, "id=?", new String[]{String.valueOf(category.getId())});
    }


    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}
