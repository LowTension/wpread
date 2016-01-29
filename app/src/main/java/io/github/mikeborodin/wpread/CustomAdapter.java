package io.github.mikeborodin.wpread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mikeborodin on 1/1/2016.
 */
public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Post> objects;

    private class ViewHolder{
        TextView id;
        TextView title;
        TextView body;
        ImageView image;
    }

    public CustomAdapter(Context context,ArrayList<Post> objects)
    {
        inflater=LayoutInflater.from(context);
        this.objects = objects;
    }
    public int getCount()
    {
        return objects.size();
    }

    public Post getItem(int position)
    {
        return objects.get(position);
    }
    public long getItemId(int position){
        return position;
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        ViewHolder holder =null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.post_item,null);

            holder.id=(TextView)convertView.findViewById(R.id.post_id);
            holder.image=(ImageView)convertView.findViewById(R.id.post_image);
            holder.title=(TextView)convertView.findViewById(R.id.post_title);
            holder.body=(TextView)convertView.findViewById(R.id.post_excerpt);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        if(objects.get(position).getId()!=0){
            holder.id.setText(String.valueOf(objects.get(position).getId()));
        }


        holder.image.setImageBitmap(objects.get(position).getImage());
        holder.title.setText(objects.get(position).getTitle());
        holder.body.setText(objects.get(position).getExcerpt());
        return convertView;
    }



}
