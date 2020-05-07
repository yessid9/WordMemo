package com.example.android.wordmemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.android.wordmemo.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        @Suppress("UNUSED_VARIABLE")
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        PhraseManager.load(filesDir)
    }


    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }

}
