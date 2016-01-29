package io.github.mikeborodin.wpread;

import android.graphics.Bitmap;
import android.text.Spanned;

import java.net.URL;

/**
 * Created by mikeborodin on 12/31/2015.
 */
public class Post {
    private String title;
    private String excerpt;
    private String content;
    private Bitmap image;
    private int id;

    // DB VALUES
    public  static final String TABLE="Post";
    public  static final String KEY_ID="id";
    public  static final String KEY_title="title";
    public  static final String KEY_excerpt="excerpt";
    public  static final String KEY_content="content";
    public  static final String KEY_image="image";

    public Post(String title,String excerpt,String content, Bitmap image)
    {
        this.title=title;
        this.excerpt=excerpt;
        this.content=content;
        this.image = image;
    }



    public int getId(){return this.id;}
    public void setId(int id){this.id=id;}

    public String getTitle(){ return title; }
    public void setTitle(String title) {this.title=title;}

    public String getExcerpt()
    {
        return excerpt;
    }
    public void setExcerpt(String excerpt)
    {
        this.excerpt=excerpt;
    }

    public String getContent(){return this.content;}
    public void setContent(String content){this.content=content;}

    public Bitmap getImage()
    {
        return this.image;
    }
    public void setImage(Bitmap image)
    {
        this.image=image;
    }
}
