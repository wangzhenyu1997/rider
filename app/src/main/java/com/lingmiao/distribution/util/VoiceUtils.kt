package com.lingmiao.distribution.util

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import com.lingmiao.distribution.R
import com.lingmiao.distribution.app.MyApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object VoiceUtils {

    private var soundPool: SoundPool? = null
    private var soundIdFourModel = 0
    private var soundIdTwoModel = 0

    fun playVoiceOfFourModel() {
        soundPool?.apply {
            MainScope().launch {
                play(soundIdFourModel, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun playVoiceOfTwoModel() {
        soundPool?.apply {
            MainScope().launch {
                play(soundIdTwoModel, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun init() {
        if (soundPool == null) {
            soundPool = getDefaultSoundPool();
            soundIdFourModel = soundPool!!.load(MyApp.getInstance(), R.raw.siji_audio, 1)
            soundIdTwoModel = soundPool!!.load(MyApp.getInstance(), R.raw.qishou_audio, 1)
        }
    }

    private fun getDefaultSoundPool() : SoundPool {
        var soundPool : SoundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(10)
                .build()
        } else {
            soundPool = SoundPool(10, AudioManager.STREAM_ALARM, 1)
        }
        return soundPool;
    }

    fun release() {
        if (soundPool != null) {
            soundPool!!.release()
            soundPool = null
        }
    }
}