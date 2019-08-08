package com.labels.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.labels.MainActivity
import com.labels.ui.fragments.intro.IntroFirstFragment
import com.labels.ui.fragments.intro.IntroSecondFragment
import com.labels.ui.fragments.intro.IntroThirdFragment
import com.labels.utils.Constants.FIRST_START

class IntroSliderActivity : AppIntro2() {

    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference= PreferenceManager.getDefaultSharedPreferences(baseContext)


        //val firstFragment: Fragment = IntroFirstFragment.newInstance()
        val secondFragment: Fragment = IntroSecondFragment.newInstance()
        val thirdFragment: Fragment = IntroThirdFragment.newInstance()

        addSlide(IntroFirstFragment())
        addSlide(secondFragment)
        addSlide(thirdFragment)

        showSkipButton(false)
        isProgressButtonEnabled = true
    }

    override fun onBackPressed() {
        finish();
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        Toast.makeText(this, "skip pressed", Toast.LENGTH_SHORT).show()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        val editor = preference.edit()
        editor.putBoolean(FIRST_START, false).apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSlideChanged(@Nullable oldFragment: Fragment?, @Nullable newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        Toast.makeText(this, "Slide changed", Toast.LENGTH_SHORT).show()
    }
}
