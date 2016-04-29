package com.example.kemos.polarbears.Model;

import android.content.Context;

import com.example.kemos.polarbears.View.Post;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kemos on 4/23/2016.
 */
public class ParseOperation {
    Context context ;
    Firebase firebasePosts ;
    Firebase firebaseUsers ;
    boolean c = false ;
    public ParseOperation(Context context){
        this.context =context ;
        Firebase.setAndroidContext(context);
        firebasePosts = new Firebase("https://polarbears.firebaseio.com/posts");
        firebaseUsers = new Firebase("https://polarbears.firebaseio.com/users");

    }
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public void addPost(Post post )  {

        Map<String, String> postDetail = new HashMap<String, String>();
        postDetail.put("author", post.getAuthor());
        if ( post.getPostImage() != null )
        postDetail.put("postImage", post.getPostImage());
        postDetail.put("content", post.getContent());
        if ( post.getAudio() != null )
        postDetail.put("audio", post.getAudio());
        postDetail.put("date", getCurrentTimeStamp());
        firebasePosts.push().setValue(postDetail);

    }

    public void addUser(String userName , String password)  {

        Map<String, String> user = new HashMap<String, String>();
        user.put("name", userName );
        user.put("password", password );
        firebaseUsers.push().setValue(user);
    }


}
