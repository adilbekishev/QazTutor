package com.example.qaztutor.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.qaztutor.MainActivity
import com.example.qaztutor.R
import com.example.qaztutor.databinding.ActivityLessonViewBinding
import com.example.qaztutor.models.Chapter
import com.example.qaztutor.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LessonViewActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLessonViewBinding
    private lateinit var mActivity: Activity
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLessonViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mActivity = this

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setUserName()

        mBinding.toolBar.navHam.setOnClickListener {
            mBinding.drawerLayout.openDrawer(Gravity.START)
        }

        mBinding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navHome -> {
                    mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    finishAffinity()
                    overridePendingTransition(0,0)
                    var intent = Intent(mActivity, MainActivity::class.java)
                    intent.putExtra("fragment_id", "home")
                    startActivity(intent)
                }

                R.id.navAccount -> {
                    finishAffinity()
                    mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    var intent = Intent(mActivity, AccountActivity::class.java)
                    startActivity(intent)
                }
                R.id.navCourses -> {
                    mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    finishAffinity()
                    overridePendingTransition(0,0)
                    var intent = Intent(mActivity, MainActivity::class.java)
                    intent.putExtra("fragment_id", "courses")
                    startActivity(intent)
                }
                R.id.navSettings -> Toast.makeText(
                    mActivity,
                    "Settings clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        val testUnit = intent.getSerializableExtra("chapter") as Chapter
        mBinding.unitNumberTextView.setText(testUnit.id)
        mBinding.unitTitleTextView.setText(testUnit.title)
        mBinding.contentTextView.setText(testUnit.content)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUserName() {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {
                val user = it.getValue(User::class.java)
                mBinding.toolBar.userNameTextView.setText(user!!.name)
            }
    }

}