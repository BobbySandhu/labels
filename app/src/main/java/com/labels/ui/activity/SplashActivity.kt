package com.labels.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.labels.MainActivity
import com.labels.R
import com.labels.utils.Constants.FIRST_START

class SplashActivity : AppCompatActivity() {

    private lateinit var introIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val preference = PreferenceManager.getDefaultSharedPreferences(baseContext)
            val isFirstStart = preference.getBoolean(FIRST_START, true)

            introIntent = if (isFirstStart)
                Intent(this, IntroSliderActivity::class.java)
            else Intent(this, MainActivity::class.java)

            startActivity(introIntent)
            finish()
        }, 3000)
    }
}
