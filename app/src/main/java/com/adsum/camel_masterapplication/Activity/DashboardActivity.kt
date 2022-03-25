package com.adsum.camel_masterapplication.Activity

import android.app.Activity
import android.content.Intent
import android.icu.text.CaseMap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.LogoutResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityDashboardBinding
import com.adsum.camel_masterapplication.fragment.*

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class DashboardActivity : AppCompatActivity() {
    private var timer: Timer? = null
    private var content: FrameLayout? = null
    private var logintime: String = ""
    lateinit var tvBtnPrint: TextView
    private var user_id by Delegates.notNull<String>()
    private lateinit var dashboardBinding: ActivityDashboardBinding
    var Role by Delegates.notNull<String>()
//    private val mOnNavigationItemSelectedListener =
//        object : BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                when (item.itemId) {
//                    R.id.navigation_home -> {
//
//                        val fragment = FragmentHome()
//                        addFragment(fragment)
//                        val textview = findViewById(R.id.title_page) as TextView
//                        textview.setText(R.string.title_home)
//                        val imageView = findViewById(R.id.title_image) as ImageView
//                        imageView.setImageResource(R.drawable.ic_home_black_24dp)
//                        return true
//                    }
//                    R.id.navigation_camel -> {
//                        val fragment = FragmentCamel()
//                       // addFragment(fragment)
//                        val textview = findViewById(R.id.title_page) as TextView
//                        textview.setText(R.string.title_camel)
//                        val imageView = findViewById(R.id.title_image) as ImageView
//                        imageView.setImageResource(R.drawable.ic_camel_black_24dp)
//                        return true
//                    }
//                    R.id.navigation_race -> {
//                        val fragment = FragmentRace()
//                        addFragment(fragment)
//                        val textview = findViewById(R.id.title_page) as TextView
//                        textview.setText(R.string.title_race)
//                        val imageView = findViewById(R.id.title_image) as ImageView
//                        imageView.setImageResource(R.drawable.ic_race_black_24dp)
//                        return true
//                    }
//                    R.id.navigation_history -> {
//                        val fragment = FragmentHistory()
//                        addFragment(fragment)
//                        val textview = findViewById(R.id.title_page) as TextView
//                        textview.setText(R.string.title_history)
//                        val imageView = findViewById(R.id.title_image) as ImageView
//                        imageView.setImageResource(R.drawable.ic_history_black_24dp)
//                        return true
//                    }
//                    R.id.profile -> {
//                        val fragment = FragmentProfile()
//                       // addFragment(fragment)
//                        val textview = findViewById(R.id.title_page) as TextView
//                        textview.setText(R.string.title_profile)
//                        val imageView = findViewById(R.id.title_image) as ImageView
//                        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
//                        return true
//                    }
//                }
//                return false
//            }
//        }
    /**
     * add/replace fragment in container [framelayout]
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)
        supportActionBar?.hide()
        Role = CommonFunctions.getPreference(this, Constants.role, "").toString()

        if (CommonFunctions.getPreference(this, Constants.isLogin, false)) {
            logintime = CommonFunctions.getPreference(this, Constants.logintime, "").toString()
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logintime)
            var currenttime = System.currentTimeMillis() / 10000
            Log.e("San", currenttime.toString() + ",,," + date.time / 10000);
            var usetime = currenttime - date.time
//            var mins= usetime / (1000 * 60) % 60
//            if(mins>=1){
//                logout()
//            }
        }
//        timerstart()
        content = findViewById(R.id.framee) as FrameLayout
//        val navigation = findViewById(R.id.bottomnavigation) as BottomNavigationView
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        navigation.selectedItemId = R.id.navigation_home
        user_id = CommonFunctions.getPreference(this, Constants.ID, "").toString()
        val imageButton = findViewById(R.id.logout) as ImageButton
        imageButton.setOnClickListener(View.OnClickListener {
            logout()
        })
        dashboardBinding.backTextView2.setOnClickListener {
            onBackPressed()
        }
        dashboardBinding.backImageView2.setOnClickListener {
            onBackPressed()
        }
        tvBtnPrint = findViewById(R.id.printText)

        val fragment = FragmentHome()
        if (Role == "normal_user") {
            addFragment(FragmentCategory())
            supportFragmentManager.addOnBackStackChangedListener {
                val fr = supportFragmentManager.findFragmentById(R.id.framee)
                when (fr) {
                    is FragmentCategory -> {
                        dashboardBinding.titlePage.text = getString(R.string.participate_in_race_round)
                        dashboardBinding.titleImage.visibility = View.VISIBLE
                        dashboardBinding.logout.visibility = View.VISIBLE
                        dashboardBinding.titleImage.setImageResource(R.drawable.ic_baseline_home_24)
                        dashboardBinding.logout.setImageResource(R.drawable.ic_baseline_power_settings_new_24)
                        dashboardBinding.backImageView2.visibility = View.GONE
                        dashboardBinding.backTextView2.visibility = View.GONE
                    }
                    is FragmentcategoryDetail -> {
                        dashboardBinding.backTextView2.text =
                            getString(R.string.participate_in_race_round)
                        dashboardBinding.titlePage.visibility = View.VISIBLE
                        dashboardBinding.titleImage.visibility = View.GONE
                        dashboardBinding.logout.visibility = View.GONE
                        dashboardBinding.backImageView2.visibility = View.VISIBLE
                        dashboardBinding.backTextView2.visibility = View.VISIBLE
                        dashboardBinding.printText.visibility = View.GONE
                        dashboardBinding.titlePage.text = getString((R.string.participate_category))
                    }
                    is FragmentSubcategoryRace -> {
                        dashboardBinding.titleImage.visibility = View.GONE
                        dashboardBinding.logout.visibility = View.GONE
                        dashboardBinding.printText.visibility = View.VISIBLE
                        dashboardBinding.backImageView2.visibility = View.VISIBLE
                        dashboardBinding.backTextView2.visibility = View.VISIBLE
                        dashboardBinding.backTextView2.text = getString(R.string.participate_category)
                        dashboardBinding.titlePage.visibility = View.GONE
                    }
                }
            }
        } else {
            addFragment(fragment)
            supportFragmentManager.addOnBackStackChangedListener {
                val fr = supportFragmentManager.findFragmentById(R.id.framee)
                when (fr) {
                    is FragmentHome -> {
                        dashboardBinding.titlePage.text = getString(R.string.title_home)
                        dashboardBinding.titleImage.visibility = View.VISIBLE
                        dashboardBinding.logout.visibility = View.VISIBLE
                        dashboardBinding.titleImage.setImageResource(R.drawable.ic_baseline_home_24)
                        dashboardBinding.logout.setImageResource(R.drawable.ic_baseline_power_settings_new_24)
                        dashboardBinding.backImageView2.visibility = View.GONE
                        dashboardBinding.backTextView2.visibility = View.GONE
                    }
                    is FragmentProfile -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.text = getString(R.string.title_profile)
                    }
                    is FragmentCamel -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.text = getString(R.string.title_camel)
                    }
                    is NotificationFragment -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.text = getString(R.string.notification)
                    }
                    is FragmentCategory -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.visibility = View.VISIBLE
                        dashboardBinding.titlePage.text = getString(R.string.participate_in_race_round)
                    }
                    is FragmentRaceSchedule -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.text = getString(R.string.title_schedule)
                    }
                    is FragmentArchive -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.visibility = View.VISIBLE
                        dashboardBinding.titlePage.text = getString(R.string.title_archive)
                    }
                    is FragmentTermAndCondition -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_home)
                        dashboardBinding.titlePage.text = getString(R.string.terms_condition)
                    }
                    is FragmentFromArchive -> {
                        dashboardBinding.backTextView2.text = getString(R.string.title_archive)
                        dashboardBinding.titlePage.visibility = View.GONE
                    }
                    is FragmentcategoryDetail -> {
                        dashboardBinding.backTextView2.text =
                            getString(R.string.participate_in_race_round)
                        dashboardBinding.titlePage.visibility = View.VISIBLE
                        dashboardBinding.titlePage.text = getString((R.string.participate_category))
                    }
                    is FragmentSubcategoryRace -> {
                        dashboardBinding.backTextView2.text = getString(R.string.participate_category)
                        dashboardBinding.titlePage.visibility = View.GONE
                    }
                    is FragmentNoOfParticipate -> {
                        dashboardBinding.titlePage.visibility = View.GONE
                    }
                    else -> {
                        dashboardBinding.titlePage.visibility = View.VISIBLE
                        dashboardBinding.titlePage.text = getString(R.string.title_home)
                        dashboardBinding.titleImage.setImageResource(R.drawable.ic_baseline_home_24)
                        dashboardBinding.logout.setImageResource(R.drawable.ic_baseline_power_settings_new_24)
                    }
                }
            }
        }

    }

    fun FragmentProfile() {
        openFragment(FragmentProfile.newInstance("", ""), "FragmentProfile")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun FragmentCamel() {
        openFragment(FragmentCamel.newInstance("", ""), "FragmentCamel")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_camel)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun FragmentNotification() {
        openFragment(NotificationFragment.newInstance("", ""), "FragmentNotification")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun FragmentParticipant() {
        openFragment(FragmentCategory.newInstance("", ""), " FragmentCategory")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun fragmentRaceSchedule() {
        openFragment(FragmentRaceSchedule.newInstance("", ""), "FragmentRaceSchedule")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_schedule)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun fragmentRaceArchive() {
        openFragment(FragmentArchive.newInstance("", ""), "FragmentHistory")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_archive)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun fragmentTermsCondition() {
        openFragment(FragmentTermAndCondition.newInstance("", ""), "FragmentTermAndCondition")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_archive)
        dashboardBinding.titleImage.visibility = View.GONE
        dashboardBinding.logout.visibility = View.GONE
        dashboardBinding.backImageView2.visibility = View.VISIBLE
        dashboardBinding.backTextView2.visibility = View.VISIBLE
        return
    }

    fun openFragment(fragment: Fragment?, name: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framee, fragment!!)
        transaction.addToBackStack(name)
        transaction.commit()
    }

    private fun addFragment(fragment: Fragment) {
//        supportFragmentManager
//            .beginTransaction()
//            .setCustomAnimations(
//                R.anim.design_bottom_sheet_slide_in,
//                R.anim.design_bottom_sheet_slide_out
//            )
//            .replace(R.id.framee, fragment, fragment.javaClass.getSimpleName())
//            .addToBackStack(fragment.javaClass.getSimpleName())
//            .commit()

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framee, fragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        try {
            if (CommonFunctions.checkConnection(this)) {
                var url: String = CamelConfig.WEBURL + CamelConfig.logout
                val mParams: HashMap<String, String> = HashMap()
                CommonFunctions.createProgressBar(this, getString(R.string.please_wait))
                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter(Constants.user_id, user_id)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val (msg, status) = gson.fromJson(
                                response.toString(),
                                LogoutResponse::class.java
                            )
                            if (status == "1") {
                                // CommonFunctions.showToast(this@DashboardActivity, msg)
                                CommonFunctions.setPreference(
                                    this@DashboardActivity,
                                    Constants.isLogin,
                                    false
                                )
                                LoginActivity.startActivity(this@DashboardActivity)

                            } else {
                                CommonFunctions.showToast(this@DashboardActivity, msg)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(this@DashboardActivity, anError.toString())
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(
                activity,
                DashboardActivity::class.java
            )
            activity.startActivity(intent)
        }
    }

    /* override fun onPause() {
         super.onPause()
         timer = Timer()
         Log.i("Main", "Invoking logout timer")
         val logoutTimeTask = LogOutTimerTask()
         timer!!.schedule(logoutTimeTask, 2000) //auto logout in 5 minutes
     }
     override fun onResume() {
         super.onResume()
         if (timer != null) {
             timer!!.cancel()
             Log.i("Main", "cancel timer")
             timer = null
         }
     }
     inner class LogOutTimerTask : TimerTask() {
         override fun run() {

             //redirect user to login screen
             logout()
             CommonFunctions
                     .changeactivity(this@DashboardActivity, LoginActivity::class.java)
             finish()
         }
     }*/
//    fun timerstart() {
//        try {
//            val timer = object : CountDownTimer(30 * 60 * 1000, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//                }
//                override fun onFinish() {
//                    logout()
//                }
//            }
//            timer.start()
//        } catch (e: Exception) {
//        }
//    }
}