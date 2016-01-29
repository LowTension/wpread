package io.github.mikeborodin.wpread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    public static String LOG_TAG = "my_log";
    private TextView id_view;
    PostRepo postRepo = new PostRepo(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //My Code
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ParseTask(context).execute();
            }
        });

        ListView mListView= (ListView)findViewById(R.id.listView);
        ArrayList<Post> posts = postRepo.getPostList();
        CustomAdapter customAdapter = new CustomAdapter(context,posts);
        mListView.setAdapter(customAdapter);


        if(posts.size()!=0){

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    id_view = (TextView) view.findViewById(R.id.post_id);
                    String post_id = id_view.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), PostDetail.class);
                    intent.putExtra(PostDetail.EXTRA, Integer.parseInt(post_id));
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(context, "0 posts", Toast.LENGTH_LONG).show();
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            new ParseTask(context).execute();

        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            new ParseTask(this).execute();
            return true;
        }
        if(id==R.id.action_delete){
            postRepo.deleteAll();
            updatePostList(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void updatePostList(Context context){

        ListView mListView= (ListView)findViewById(R.id.listView);
        ArrayList<Post> posts = postRepo.getPostList();
        CustomAdapter customAdapter = new CustomAdapter(this,posts);
        mListView.setAdapter(customAdapter);

        if(posts.size()!=0){

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    id_view = (TextView) view.findViewById(R.id.post_id);
                    String post_id = id_view.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), PostDetail.class);
                    intent.putExtra(PostDetail.EXTRA, Integer.parseInt(post_id));
                    startActivity(intent);
                }
            });
        }else{
            Toast.makeText(context, "Refresh failed", Toast.LENGTH_SHORT).show();
        }

    }




    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        private Context context;

        public ParseTask(Context context)
        {
            this.context=context;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://reality.realitymagazines.com/wp-json/wp/v2/posts");


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");


                urlConnection.connect();
                Log.d("PARSE TASK", "URL CONNECT");
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject dataJsonObj = null;
            Bitmap mIcon = null;
            try {
                JSONArray posts = new JSONArray(resultJson);
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);

                    if (!postRepo.post_exists(Html.fromHtml(post.getJSONObject("title").getString("rendered")).toString())) {
                        Log.d("ADDING", Html.fromHtml(post.getJSONObject("title").getString("rendered")).toString());


                        try {
                            try {
                                InputStream in = new java.net.URL(post.getJSONObject("better_featured_image").getString("source_url")).openStream();
                                mIcon = BitmapFactory.decodeStream(in);
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        postRepo.insert(
                                new Post(
                                        Html.fromHtml(post.getJSONObject("title").getString("rendered")).toString(),
                                        Html.fromHtml(post.getJSONObject("excerpt").getString("rendered")).toString(),
                                        (post.getJSONObject("content").getString("rendered")),
                                        mIcon
                                ));
                    }else{
                        Log.d("PARSE TASK","NOT ADDING");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return resultJson;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            updatePostList(context);
            swipeRefreshLayout.setRefreshing(false);

        }
    }



}
