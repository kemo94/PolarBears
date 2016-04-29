package com.example.kemos.polarbears.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kemos.polarbears.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class SingleItemView extends Activity {
	private static final int UPDATE_FREQUENCY = 500;
	private final Handler handler = new Handler();
	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};

	private SeekBar seekbar = null;
	private boolean isMoveingSeekBar = false;
	MediaPlayer player = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		String audio = i.getStringExtra("audio");

		if (audio != null && audio != "")
			setContentView(R.layout.singleitemview_sound);
		else
			setContentView(R.layout.singleitemview);

		TextView textViewAuthor = (TextView) findViewById(R.id.userName);
		//ImageView userPhoto = (ImageView) findViewById(R.id.user_photo) ;
		TextView textViewDate = (TextView) findViewById(R.id.post_date);
		TextView textViewContent = (TextView) findViewById(R.id.post_content);
		ImageView imageViewPost = (ImageView) findViewById(R.id.image_post);


		String author = i.getStringExtra("author");
		String content = i.getStringExtra("contnet");
		String imageDataBytes = i.getStringExtra("postImage");
		String date = i.getStringExtra("date");

		textViewContent.setText(content);
		textViewAuthor.setText(author);
		textViewDate.setText(date);

		if (imageDataBytes != null) {
			byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			imageViewPost.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
			imageViewPost.setImageBitmap(decodedByte);
		}

		if (audio != null && audio != "") {
      //      try {
                 player =MediaPlayer.create(getApplicationContext(),R.raw.m3_allah);
//                 player.prepare();

         //       player.setDataSource(write("rr.mp3" , audio));
//            } catch (IOException e) {
  //              e.printStackTrace();
    //        }
            player.start();
            seekbar = (SeekBar) findViewById(R.id.seekbar);
            seekbar.setMax(player.getDuration());
            seekbar.setOnSeekBarChangeListener(seekBarChanged);
            updatePosition();

		}


	}

	private void updatePosition() {
		handler.removeCallbacks(updatePositionRunnable);

		seekbar.setProgress(player.getCurrentPosition());

		handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	}


	private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = false;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = true;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (isMoveingSeekBar) {
				player.seekTo(progress);

				Log.i("OnSeekBarChangeListener", "onProgressChmmmmanged");
			}
		}
	};

    public String write(String fileName, String data) {
        byte[] decoded = Base64.decode(data,
                Base64.DEFAULT);

        String path = Environment.getExternalStorageDirectory().getPath() +"/"+ fileName;
        File outDir = new File(path);
        if (!outDir.isDirectory()) {
            outDir.mkdir();
        }
        try {

            FileOutputStream stream = new FileOutputStream(outDir);
            try {
                stream.write(decoded);
            } finally {
                stream.close();
            }
            return outDir.getPath() ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	if ( player!= null)
		player.stop();
	}

}