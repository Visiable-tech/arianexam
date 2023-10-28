package com.onlinetalentsearchexam

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityDashboardBinding
import com.onlinetalentsearchexam.fragments.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper


//ACTIVITY NOT IN USE
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        actionBarToggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.toolbar, 0, 0)//added toolbar to show the hamburger menu
        actionBarToggle.isDrawerIndicatorEnabled = true
        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()
        binding.drawerLayout.addDrawerListener(actionBarToggle)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(binding.toolbar)
        displayView(0)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.dashboard -> {
                    displayView(0)
                    //this.binding.drawerLayout.closeDrawer(GravityCompat.START)
                    closeDrawer()
                    true
                }
                R.id.logout -> {
                    displayView(1)
                    //this.binding.drawerLayout.closeDrawer(GravityCompat.START)
                    closeDrawer()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    private fun displayView(position: Int) {
        var fragment: Fragment? = null
        val title = getString(R.string.app_name)
        when (position) {
            0 -> fragment = HomeFragment()
            1 -> {
                val builder1 = AlertDialog.Builder(this@DashboardActivity)
                builder1.setMessage("Do you want to logout?")
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface, id: Int ->
                    dialog.cancel()
                    Paper.book().destroy()
                    startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                    finish()
                }
                builder1.setNegativeButton(
                    "No"
                ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
                val alert = builder1.create()
                alert.show()
            }
            else -> {}
        }
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.content_frame, fragment)
            fragmentTransaction.commit()

            // set the toolbar title
            supportActionBar!!.setTitle(title)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        binding.drawerLayout.openDrawer(binding.navigationView)
        return true
    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
    override fun onBackPressed() {

        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }



    }
    fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}