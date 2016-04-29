package com.example.kemos.polarbears.Controller;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class SoundRecording  {
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    public void play()  {

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            ;
        }
        catch (IOException e){}

    }
    public void record()  {
        try {
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);

            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }
        catch (IOException e){}

    }
    public String stopRecord() {
        byte[] videoBytes = null ;
        try {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(outputFile));

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes  = baos.toByteArray();
        }
        catch (IOException e){}
        return Base64.encodeToString(videoBytes, Base64.DEFAULT);
    }

}