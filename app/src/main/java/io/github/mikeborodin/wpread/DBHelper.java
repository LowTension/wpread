package io.github.mikeborodin.wpread;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mikeborodin on 1/9/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION =5;
    private static final String DATABASE_NAME="wpread.db";

    public DBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Post.TABLE  + "("
                + Post.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Post.KEY_title + " TEXT, "
                + Post.KEY_excerpt + " TEXT, "
                + Post.KEY_content + " TEXT, "
                + Post.KEY_image + " BLOB )";

        db.execSQL(CREATE_TABLE_STUDENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
    db.execSQL("DROP TABLE IF EXIST "+Post.TABLE);
    onCreate(db);
    }

}
