package com.example.kemos.polarbears.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kemos.polarbears.Model.ParseOperation;
import com.example.kemos.polarbears.R;
import com.example.kemos.polarbears.View.CustomListAdapter;
import com.example.kemos.polarbears.View.Post;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocialActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences prefs;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ArrayList<Post> arrayPosts = new ArrayList<Post>();
    ParseOperation postOperation ;
    Post post = new Post();
    Bitmap imageBitmap;
    ListView listview;
    Firebase firebasePosts;
    boolean isRecording = false ,  checkFinishRecord = false ;
    String audioFile ;
    Button record , takePhoto , postBtn;
    EditText postContent ;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_activity);

        Firebase.setAndroidContext(getApplicationContext());
        firebasePosts = new Firebase("https://polarbears.firebaseio.com/posts");

        listview = (ListView) findViewById(R.id.listview);
        postOperation = new ParseOperation(this);
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        getPosts();

        postContent = (EditText) findViewById(R.id.post_content);
        postBtn = (Button) findViewById(R.id.btn_post);
        record = (Button) findViewById(R.id.record_audio);
        takePhoto = (Button) findViewById(R.id.take_photo);

        postBtn.setOnClickListener(onButtonClick);
        record.setOnClickListener(onButtonClick);
        takePhoto.setOnClickListener(onButtonClick);
	}
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             imageBitmap = (Bitmap) extras.get("data");

        }
    }
    public void getPosts()  {

        firebasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, Object> td = (HashMap<String,Object>) snapshot.getValue();
                if ( td != null )
                    for ( final Map.Entry<String, Object> mapEntry : td.entrySet() ) {
                        Firebase firebase = new Firebase("https://polarbears.firebaseio.com/posts/" + mapEntry.getKey());
                        firebase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Post post = dataSnapshot.getValue(Post.class);
                                post.setId( mapEntry.getKey());
                                if ( !check(arrayPosts , mapEntry.getKey() ) )
                                arrayPosts.add(post);

                                listview.setAdapter(new CustomListAdapter(getApplicationContext(), arrayPosts));
                                listview.setSelection(listview.getCount() - 1);
                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        Intent intent = new Intent(SocialActivity.this , SingleItemView.class);
                                                intent.putExtra("author" ,arrayPosts.get(i).getAuthor() );
                                                intent.putExtra("contnet" , arrayPosts.get(i).getContent());
                                                intent.putExtra("postImage" , arrayPosts.get(i).getPostImage());
                                                intent.putExtra("date" , arrayPosts.get(i).getDate());
                                                intent.putExtra("audio" , arrayPosts.get(i).getAudio());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

   boolean check(ArrayList<Post>posts , String id){
       for ( int i = 0 ;i < posts.size() ;i ++ )
           if ( posts.get(i).getId().equals(id))
               return true ;
           return false ;
   }

    private View.OnClickListener onButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_post: {

                    if ( imageBitmap != null ) {
                        post.setPostImage(encodeImages());
                    }

                    try {
                        Uri url = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.m3_allah);
                    //  File file = new File(url.toString());
                        InputStream rawInputStream = getResources().openRawResource(R.raw.m3_allah);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
           //             FileInputStream fis = new FileInputStream(new File(url.toString()));
                    //    DataInputStream dataInputStream = new DataInputStream(rawInputStream);
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = rawInputStream.read(buf)))
                            baos.write(buf, 0, n);

                        byte[] videoBytes  = baos.toByteArray();
                        audioFile = Base64.encodeToString(videoBytes, Base64.DEFAULT);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                if ( audioFile != null && audioFile.length() > 0 ) {
        //                post.setAudio(audioFile);
                    }
                    post.setAuthor(prefs.getString("UserName", null));
                    post.setContent(postContent.getText().toString());
                    postContent.setText("");

                    if ( post.getContent().length() > 0 || imageBitmap != null){// || audioFile != null  ) {
                        postOperation.addPost(post);

                    }
                    else{

                        Toast.makeText(SocialActivity.this, R.string.fill_post, Toast.LENGTH_LONG).show();
                    }
                    break;
                }

                case R.id.record_audio: {/*
                    SoundRecording soundRecording = new SoundRecording() ;
                    if ( !isRecording && !checkFinishRecord) {
                        soundRecording.record();
                        record.setText("Stop Record");
                        isRecording = true;
                    }
                    else if ( isRecording && !checkFinishRecord) {
                        audioFile = soundRecording.stopRecord();
                        isRecording = false;
                        checkFinishRecord = true ;
                        record.setText("Play");
                    }
                    else if ( checkFinishRecord) {
                        soundRecording.play();
                        record.setText("Record Audio");
                        checkFinishRecord = false ;
                    }*/
                   // startActivity(new Intent(SocialActivity.this , AudioRecordTest.class));
                    break;
                }

                case R.id.take_photo: {

                    dispatchTakePictureIntent();
                    break;
                }
            }
        }
    };
    String encodeImages(){

        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        imageBitmap.recycle();
        byte[] byteArray = bYtE.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}