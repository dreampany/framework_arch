package com.dreampany.framework.data.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by TheFinestArtist on 2/12/15.
 */
public class AudioUtil {

    private static final Object mSingletonLock = new Object();
    private static AudioManager audioManager;
    private static MediaPlayer player;
    private static SoundPool sp;

    private static AudioManager getInstance(Context context) {
        synchronized (mSingletonLock) {
            if (audioManager != null)
                return audioManager;
            if (context != null)
                audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            return audioManager;
        }
    }

    public static void adjustMusicVolume(Context context, boolean up, boolean showInterface) {
        int direction = up ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
        int flag = AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE | (showInterface ? AudioManager.FLAG_SHOW_UI : 0);
        getInstance(context).adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, flag);
    }

    public static void playKeyClickSound(Context context, int volume) {
        if (volume == 0)
            return;
        getInstance(context).playSoundEffect(AudioManager.FX_KEY_CLICK, (float) volume / 100.0f);
    }

    public static void stop() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public static void play(Context context, int rawId) {
        stop();

        player = MediaPlayer.create(context, rawId);
        player.setOnCompletionListener(mediaPlayer -> stop());

        player.start();
    }

    public static void playSound(Activity activity, int soundId) {
        if (sp == null) {
            if (AndroidUtil.hasLollipop()) {
                AudioAttributes attrs = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                sp = new SoundPool.Builder()
                        .setMaxStreams(10)
                        .setAudioAttributes(attrs)
                        .build();
            } else {
                sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            }
        }

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        int soundIds[] = new int[10];
        soundIds[0] = sp.load(activity, soundId, 1);
        sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
    }

    public static void releaseSound() {
        if (sp != null) {
            sp.release();
        }
    }
}
