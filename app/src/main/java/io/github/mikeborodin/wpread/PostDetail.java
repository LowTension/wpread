package io.github.mikeborodin.wpread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mikeborodin on 1/12/2016.
 */
public class PostDetail extends AppCompatActivity {

    private int post_id= 0;
    public final static String EXTRA="POST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);
        TextView post_title = (TextView)findViewById(R.id.post_title);
        ImageView post_image = (ImageView)findViewById(R.id.post_image);
        WebView post_content = (WebView)findViewById(R.id.post_web_content);

        Intent intent = getIntent();
        PostRepo postRepo = new PostRepo(this);

        post_id = intent.getIntExtra(EXTRA, 0);
        Post post = new Post("","","",null);
        post = postRepo.getPostById(post_id);



        post_title.setText(post.getTitle());
        post_image.setImageBitmap(post.getImage());
        post_content.loadDataWithBaseURL("",post.getContent(),"text/html","UTF-8","");
    }



}
