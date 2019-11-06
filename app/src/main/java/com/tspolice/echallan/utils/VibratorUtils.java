package com.tspolice.echallan.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class VibratorUtils {

    private Context mContext;
    private Vibrator vibrator;
    private Uri notification;
    private MediaPlayer mediaPlayer;
    private Ringtone ringtone;
    private long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
    private TextToSpeech textToSpeech;

    public VibratorUtils(Context context) {
        this.mContext = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    }

    public void vibratePhone(long vibrateMilliSeconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateMilliSeconds, VibrationEffect.DEFAULT_AMPLITUDE));
            //ttsMethod();
            Intent i = new Intent(mContext, RingtoneService.class);
            mContext.startService(i);
        } else {
            vibrator.vibrate(pattern, 0);
            // ttsMethod();
            Intent i = new Intent(mContext, RingtoneService.class);
            mContext.startService(i);
        }
    }

    public void vibrateStopPhone() {
        try {
            vibrator.cancel();
            Intent i = new Intent(mContext, RingtoneService.class);
            mContext.stopService(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ttsMethod() {
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                    String ttsData = "Dear Officer This Vehicle has a remarks Please try to Detain the vehicle ";
                    textToSpeech.speak(ttsData, TextToSpeech.QUEUE_FLUSH, null);
                    textToSpeech.setSpeechRate(1000);
                } else {
                    Toast.makeText(mContext, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
