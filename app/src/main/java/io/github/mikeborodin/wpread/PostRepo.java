package io.github.mikeborodin.wpread;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mikeborodin on 1/9/2016.
 */
public class PostRepo {
    private DBHelper dbHelper;
    public PostRepo(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    public int insert(Post post)
    {



        SQLiteDatabase db = dbHelper.getReadableDatabase();
       ContentValues values = new ContentValues();
        values.put(Post.KEY_title,post.getTitle());
        values.put(Post.KEY_excerpt,post.getExcerpt());
        values.put(Post.KEY_content,post.getContent());

        //prepare img
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.getImage().compress(Bitmap.CompressFormat.PNG, 0, stream);

        values.put(Post.KEY_image, stream.toByteArray());

        long post_id = db.insert(Post.TABLE,null,values);
        db.close();
        return (int) post_id;
    }

    public void delete(int post_id)
    {
        SQLiteDatabase db= dbHelper.getReadableDatabase();
        db.delete(Post.TABLE,Post.KEY_ID+" = ?",new String[]{String.valueOf(post_id)});
        db.close();
    }

    public void deleteAll()
    {
     SQLiteDatabase  db=dbHelper.getReadableDatabase();
     db.delete(Post.TABLE,null,null);
     db.close();
    }

    public void update(Post post) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Post.KEY_title, post.getTitle());
        values.put(Post.KEY_excerpt,post.getExcerpt());
        values.put(Post.KEY_content, post.getContent());

        //prepare img
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.getImage().compress(Bitmap.CompressFormat.PNG, 0, stream);
        values.put(Post.KEY_image, stream.toByteArray());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Post.TABLE, values, Post.KEY_ID + "= ?", new String[]{String.valueOf(post.getId())});
        db.close(); // Closing database connection
    }


    public ArrayList<Post>  getPostList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  " SELECT  " +
                Post.KEY_ID + " , "      +
                Post.KEY_title + " , "   +
                Post.KEY_excerpt + " , " +
                Post.KEY_content +" , "  +
                Post.KEY_image +
                " FROM " + Post.TABLE;

        ArrayList<Post> postList = new ArrayList<Post>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(
                        cursor.getString(cursor.getColumnIndex(Post.KEY_title)),
                        cursor.getString(cursor.getColumnIndex(Post.KEY_excerpt)),
                        cursor.getString(cursor.getColumnIndex(Post.KEY_content)),
                        BitmapFactory.decodeByteArray(cursor.getBlob(
                                cursor.getColumnIndex(Post.KEY_image)),
                                0,
                                cursor.getBlob(cursor.getColumnIndex(Post.KEY_image)).length)
                );
                post.setId(cursor.getInt(cursor.getColumnIndex(Post.KEY_ID)));
                postList.add(post);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return postList;
    }

    public Post getPostById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Post post = new Post("","","",null);
        Cursor cursor = db.rawQuery("select * from "+Post.TABLE+" where "+Post.KEY_ID+"='"+String.valueOf(Id)+"'", null);

        if (cursor.moveToFirst()) {
            do {
                post.setId(cursor.getInt(cursor.getColumnIndex(Post.KEY_ID)));
                post.setTitle(cursor.getString(cursor.getColumnIndex(Post.KEY_title)));
                post.setExcerpt(cursor.getString(cursor.getColumnIndex(Post.KEY_excerpt)));
                post.setContent(cursor.getString(cursor.getColumnIndex(Post.KEY_content)));
                post.setImage(
                        BitmapFactory.decodeByteArray(cursor.getBlob(
                                        cursor.getColumnIndex(Post.KEY_image)),
                                0,
                                cursor.getBlob(cursor.getColumnIndex(Post.KEY_image)).length)
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return post;
    }

    public boolean post_exists(String title) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select "+Post.KEY_ID+" from "+Post.TABLE+" where "+Post.KEY_title+"='"+title+"'",
                null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


}
