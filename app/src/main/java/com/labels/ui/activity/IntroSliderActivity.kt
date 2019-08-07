package com.labels.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.labels.ui.fragments.intro.IntroFirstFragment
import com.labels.ui.fragments.intro.IntroSecondFragment
import com.labels.ui.fragments.intro.IntroThirdFragment

class IntroSliderActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val firstFragment: Fragment = IntroFirstFragment.newInstance()
        val secondFragment: Fragment = IntroSecondFragment.newInstance()
        val thirdFragment: Fragment = IntroThirdFragment.newInstance()

        addSlide(IntroFirstFragment())
        addSlide(secondFragment)
        addSlide(thirdFragment)

        showSkipButton(false)
        isProgressButtonEnabled = true
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        Toast.makeText(this, "skip pressed", Toast.LENGTH_SHORT).show()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Do something when users tap on Done button.
        Toast.makeText(this, "Done pressed", Toast.LENGTH_SHORT).show()
    }

    override fun onSlideChanged(@Nullable oldFragment: Fragment?, @Nullable newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
        Toast.makeText(this, "Slide changed", Toast.LENGTH_SHORT).show()
    }
}
