package ballidaku.wlkullu.myUtilities;

/**
 * Created by sharanpalsingh on 16/07/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

import ballidaku.wlkullu.dataModels.ChildModel;
import ballidaku.wlkullu.dataModels.DetailModel;
import ballidaku.wlkullu.dataModels.TitleModel;

/**
 * Created by brst-pc93 o0n 1/6/17.
 */

public class MyDatabase extends SQLiteOpenHelper
{

    String TAG = MyDatabase.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database";

    // Contacts table name
    private static final String TABLE_TITLES = "table_titles";
    private static final String TABLE_CHILD = "table_child";
    private static final String TABLE_DETAIL = "table_detail";

    // Contacts Table Columns names

    private static final String KEY_TITLE_ID = "title_id";
    private static final String KEY_CHILD_ID = "child_id";
    private static final String KEY_DETAIL_ID = "detail_id";

    private static final String KEY_TITLE_TEXT = "title_text";
    private static final String KEY_CHILD_TEXT = "child_text";


    private static final String KEY_IMAGE = "image";
    private static final String KEY_VIDEO= "video";
    private static final String KEY_TEXT = "text";
    private static final String KEY_HEADING = "heading";


    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_TITLE_TABLE = "CREATE TABLE " + TABLE_TITLES + "("+ KEY_TITLE_ID + " INTEGER PRIMARY KEY," + KEY_TITLE_TEXT + " TEXT" + ")";
        db.execSQL(CREATE_TITLE_TABLE);



        String CREATE_CHILD_TABLE = "CREATE TABLE " + TABLE_CHILD + "("+ KEY_CHILD_ID + " INTEGER PRIMARY KEY," + KEY_TITLE_ID + " TEXT," + KEY_CHILD_TEXT + " TEXT" + ")";
        db.execSQL(CREATE_CHILD_TABLE);



        String CREATE_DETAIL_DATA_TABLE = "CREATE TABLE " + TABLE_DETAIL + "("
                + KEY_DETAIL_ID + " INTEGER PRIMARY KEY," + KEY_TITLE_ID + " TEXT," + KEY_CHILD_ID + " TEXT," + KEY_IMAGE + " TEXT," + KEY_VIDEO + " TEXT," + KEY_TEXT + " TEXT," + KEY_HEADING + " TEXT" + ")";

        db.execSQL(CREATE_DETAIL_DATA_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void addTitle(String name)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_TEXT, name);

        // Inserting Row
        db.insert(TABLE_TITLES, null, values);
        db.close(); // Closing database connection

    }


    public ArrayList<TitleModel> getAllTitlesData()
    {
        ArrayList<TitleModel> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TITLES, new String[]{KEY_TITLE_ID, KEY_TITLE_TEXT}, null, null, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do
            {
                TitleModel titleModel = new TitleModel();

                titleModel.setId(cursor.getString(0));
                titleModel.setTitle(cursor.getString(1));

                list.add(titleModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;

    }

    public String getTitle(String id)
    {

        String title = "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TITLES, new String[]{KEY_TITLE_ID, KEY_TITLE_TEXT}, KEY_TITLE_ID+ "=?", new String[]{id}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        if (cursor != null && cursor.getCount() > 0)
        {
            title =cursor.getString(1);
            cursor.close();
        }

        db.close();

        return title;
    }


    public void updateTitleRecord(TitleModel titleModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_ID, titleModel.getId());
        values.put(KEY_TITLE_TEXT, titleModel.getTitle());

        // updating row
        db.update(TABLE_TITLES, values, KEY_TITLE_ID + " = ?", new String[]{titleModel.getId()});

        db.close();
    }

    public void deleteTitleRecord(String titleID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DETAIL, KEY_TITLE_ID+ " = ?", new String[]{titleID});

       // db.execSQL("Select count(*) from "+TABLE_DETAIL+" where "+KEY_TITLE_ID+" = "+titleID);

        db.delete(TABLE_CHILD, KEY_TITLE_ID + " = ?", new String[]{String.valueOf(titleID)});

      //  db.execSQL("Select count(*) from "+TABLE_CHILD+" where "+KEY_TITLE_ID+" = "+titleID);


       // db.execSQL("Select count(*) from "+KEY_TITLE_ID+" where "+KEY_TITLE_ID+" = "+titleID);

        db.delete(TABLE_TITLES, KEY_TITLE_ID + " = ?", new String[]{String.valueOf(titleID)});
        db.close();


    }

    //***********************************************************
    //CHILD
    //***********************************************************


    public void addChild(String titleID, String childName)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_ID, titleID);
        values.put(KEY_CHILD_TEXT, childName);

        // Inserting Row
        db.insert(TABLE_CHILD, null, values);
        db.close(); // Closing database connection

    }


    public ArrayList<ChildModel> getAllChildData(String titleId)
    {
        ArrayList<ChildModel> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CHILD, new String[]{KEY_CHILD_ID,KEY_TITLE_ID, KEY_CHILD_TEXT},KEY_TITLE_ID+ "=?", new String[]{titleId}, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do
            {
                ChildModel childModel = new ChildModel();

                childModel.setChildId(cursor.getString(0));
                childModel.setTitleId(cursor.getString(1));
                childModel.setChildText(cursor.getString(2));

                list.add(childModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;

    }

    public String getChildName(String childId)
    {

        String title = "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHILD, new String[]{KEY_CHILD_ID, KEY_TITLE_ID,KEY_CHILD_TEXT}, KEY_CHILD_ID+ "=?", new String[]{childId}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        if (cursor != null && cursor.getCount() > 0)
        {
            title =cursor.getString(2);
            cursor.close();
        }

        db.close();

        return title;
    }


    public void updateChildRecord(ChildModel childModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHILD_ID, childModel.getChildId());
        values.put(KEY_TITLE_ID, childModel.getTitleId());
        values.put(KEY_CHILD_TEXT, childModel.getChildText());

        // updating row
        db.update(TABLE_CHILD, values, KEY_CHILD_ID + " = ?", new String[]{childModel.getChildId()});

        db.close();
    }

    public void deleteChildRecord(String childId)
    {
        /*SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHILD, KEY_CHILD_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();*/


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DETAIL, KEY_CHILD_ID+ " = ?", new String[]{childId});
        //  db.close();


        // SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHILD, KEY_CHILD_ID + " = ?", new String[]{String.valueOf(childId)});
        // db.close();
    }








    //***********************************************************
    //DETAIL
    //***********************************************************
    public void addDetailData(DetailModel detailModel)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_ID, detailModel.getTitleId());
        values.put(KEY_CHILD_ID, detailModel.getChildId());
        values.put(KEY_IMAGE, detailModel.getImagePathList() == null ? "" : detailModel.getImagePathList().toString());
        values.put(KEY_VIDEO, detailModel.getVideoPath());
        values.put(KEY_TEXT, detailModel.getText());
        values.put(KEY_HEADING, detailModel.getHeading());

        // Inserting Row
        db.insert(TABLE_DETAIL, null, values);
        db.close(); // Closing database connection

    }

    public ArrayList<DetailModel> getAllDetailData(String childId)
    {
        ArrayList<DetailModel> list = new ArrayList<>();


       // Log.e("ABB1111",""+childId);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_DETAIL, new String[]{KEY_DETAIL_ID,KEY_TITLE_ID,KEY_CHILD_ID,KEY_IMAGE,KEY_VIDEO,KEY_TEXT,KEY_HEADING}, KEY_CHILD_ID+ "=?", new String[]{childId}, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do
            {
                DetailModel detailModel = new DetailModel();

                String imagePaths=cursor.getString(3).replace("[","").replace("]","");

               // Log.e(TAG,"imagePaths1 "+imagePaths);
               // Log.e(TAG,"imagePaths2 "+Arrays.asList(imagePaths.split(",")));

                detailModel.setDetailId(cursor.getString(0));

               // Log.e("ABB1111","getting detail Id "+cursor.getString(0));
                detailModel.setTitleId(cursor.getString(1));
                detailModel.setChildId(cursor.getString(2));

                if(!imagePaths.isEmpty())
                {
                    detailModel.setImagePathList(Arrays.asList(imagePaths.split(",")));
                }
                detailModel.setVideoPath(cursor.getString(4));
                detailModel.setText(cursor.getString(5));
                detailModel.setHeading(cursor.getString(6));

               // Log.e("ABB12",""+cursor.getString(6));

                list.add(detailModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;

    }


    public void updateDetailRecord(DetailModel detailModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DETAIL_ID, detailModel.getDetailId());
        values.put(KEY_TITLE_ID, detailModel.getTitleId());
        values.put(KEY_CHILD_ID, detailModel.getChildId());
        values.put(KEY_IMAGE, detailModel.getImagePathList() == null ? "":detailModel.getImagePathList().toString());
        values.put(KEY_VIDEO, detailModel.getVideoPath());
        values.put(KEY_TEXT, detailModel.getText());
        values.put(KEY_HEADING, detailModel.getHeading());

        //Log.e("ABB",""+detailModel.getHeading());

        //Log.e("ABB1111","update detail Id "+detailModel.getDetailId());

        // updating row
        db.update(TABLE_DETAIL, values, KEY_DETAIL_ID + " = ?", new String[]{detailModel.getDetailId()});

        db.close();
    }


    public void deleteDetailRecord(String detailId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DETAIL, KEY_DETAIL_ID+ " = ?", new String[]{detailId});
        db.close();


    }

}