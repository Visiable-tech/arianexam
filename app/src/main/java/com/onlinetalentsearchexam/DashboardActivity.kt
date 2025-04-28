package com.onlinetalentsearchexam

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityDashboardBinding
import com.onlinetalentsearchexam.commons.Constants
import com.onlinetalentsearchexam.fragments.HomeFragment
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.view.CommonDialogs
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDashboardBinding
    private lateinit var actionBarToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        actionBarToggle = ActionBarDrawerToggle(this, mBinding.drawerLayout,mBinding.toolbar, 0, 0)//added toolbar to show the hamburger menu
        actionBarToggle.isDrawerIndicatorEnabled = true
        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()
        mBinding.drawerLayout.addDrawerListener(actionBarToggle)
        mBinding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(mBinding.toolbar)
        displayView(0)

        mBinding.navigationView.setNavigationItemSelectedListener { menuItem ->

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
                R.id.policy ->{
                    val browse =Intent(Intent.ACTION_VIEW, Uri.parse(Constants.POLICY_URL));
                    startActivity(browse)
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
        mBinding.drawerLayout.openDrawer(mBinding.navigationView)
        return true
    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (this.mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }


    }
    fun closeDrawer() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}