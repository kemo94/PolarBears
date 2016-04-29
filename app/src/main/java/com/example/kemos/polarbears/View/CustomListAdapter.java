package com.example.kemos.polarbears.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kemos.polarbears.R;

import java.util.ArrayList;


public class CustomListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater;
    private final ArrayList<Post> postItemArray;

    public CustomListAdapter(Context c, ArrayList<Post> postItemArray) {

        this.context = c;
        this.postItemArray = postItemArray;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String imageDataBytes = postItemArray.get(position).getPostImage();

        if ( imageDataBytes != null  && convertView == null)
                convertView = inflater.inflate(R.layout.image_list_cell, null);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.post_list_cell, null);

            ImageView userPhoto = (ImageView) convertView.findViewById(R.id.user_photo);
             userPhoto.setImageResource(R.drawable.polar_bears);

           ImageView postImage = (ImageView) convertView.findViewById(R.id.image_post);
           if  ( postImage != null  && imageDataBytes != null && imageDataBytes.length() > 0) {

                byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Log.d("dddddddddddddd" ,  position + "");
                postImage.setImageBitmap(decodedByte);
            }

            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            userName.setText(postItemArray.get(position).getAuthor());

            TextView postDate = (TextView) convertView.findViewById(R.id.post_date);
            postDate.setText(postItemArray.get(position).getDate());

            TextView postContent = (TextView) convertView.findViewById(R.id.post_content);
            postContent.setText(postItemArray.get(position).getContent());

            return convertView;
    }

    @Override
    public int getCount() {
        return postItemArray.size();
    }

    @Override
    public Object getItem(int position) {
        return postItemArray.get(position).getContent();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}