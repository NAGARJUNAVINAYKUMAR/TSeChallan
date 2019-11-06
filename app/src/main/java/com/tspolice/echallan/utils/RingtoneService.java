package com.tspolice.echallan.utils;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tspolice.echallan.R;

public class RingtoneService extends Service {

    private static Ringtone mRingtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // mAudioManager.setStreamVolume(AudioManager.STREAM_RING,mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 8, AudioManager.FLAG_SHOW_UI);
        mRingtone = RingtoneManager.getRingtone(getBaseContext(),
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alertsound));
        mRingtone.play();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mRingtone.stop();
    }
}
