package com.labels

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.labels.ui.fragments.DashboardFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        setDrawer()
        setNavigationItemClick()
        setDrawerFragment()
    }

    private fun setDrawer() {
        val toggle = object : ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            //todo add drawer open and closed methods
        }

        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setNavigationItemClick() {
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_settings -> Toast.makeText(applicationContext, "working", Toast.LENGTH_SHORT).show()
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setDrawerFragment() {
        replaceFragment(DashboardFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()
    }
}
