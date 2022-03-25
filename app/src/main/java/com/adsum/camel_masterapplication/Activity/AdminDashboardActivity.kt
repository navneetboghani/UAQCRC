package com.adsum.camel_masterapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityAdminDashboardBinding
import com.adsum.camel_masterapplication.fragment.*
import kotlin.properties.Delegates


class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var fragment: Fragment
    private lateinit var binding: ActivityAdminDashboardBinding
    var Role by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_admin_dashboard)
        val binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Role= CommonFunctions.getPreference(this, Constants.role,"").toString()

        supportActionBar?.hide()

        if (Role == "normal_user"){
            binding.clFirst.visibility=View.GONE
            binding.clSecond.visibility=View.GONE
            binding.clFourth.visibility=View.GONE
        }else {
            binding.clFirst.visibility=View.VISIBLE
            binding.clSecond.visibility=View.VISIBLE
            binding.clFourth.visibility=View.VISIBLE
        }


        binding.titleImage.setOnClickListener{
            onBackPressed()
        }
        binding.tvBack.setOnClickListener {
           onBackPressed()
        }
        supportFragmentManager.addOnBackStackChangedListener {
            val fr = supportFragmentManager.findFragmentById(R.id.fram)
            when (fr) {
                is FragmentUserDetails -> {
                    binding.tvBack.text = getString(R.string.title_control)
                    binding.tvTitlePage.text = getString(R.string.user_detail)
                    binding.imageAdduser.visibility = View.VISIBLE
                    binding.imageAdd.visibility = View.GONE
                    binding.addImageSchedule.visibility = View.GONE
                }
                is AddUserFragment -> {
                    binding.tvBack.text = getString(R.string.user_detail)
                    binding.tvTitlePage.text = getString(R.string.add_user)
                    binding.imageAdduser.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                    binding.addImageSchedule.visibility = View.GONE
                }
                is FragmentCamel -> {
                    binding.tvBack.text = getString(R.string.title_control)
                    binding.tvTitlePage.text = getString(R.string.camel_list)
                    binding.imageAdduser.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                    binding.addImageSchedule.visibility = View.GONE
                }
                is RaceDetailFragment -> {
                    binding.tvBack.text = getString(R.string.title_control)
                    binding.tvTitlePage.visibility = View.VISIBLE
                    binding.tvTitlePage.text = getString(R.string.race_detail)
                    if (Role == "normal_user") {
                        binding.imageAdd.visibility = View.GONE
                    } else {
                        binding.imageAdd.visibility = View.VISIBLE
                    }
                    binding.addImageSchedule.visibility = View.GONE
                    binding.imageAdduser.visibility = View.GONE
                }
                is FragmentAdminRaceSchedule -> {
                    binding.tvBack.text = getString(R.string.title_control)
                    binding.tvTitlePage.visibility = View.VISIBLE
                    binding.tvTitlePage.text = getString(R.string.race_schedule)
                    binding.addImageSchedule.visibility = View.VISIBLE
                    binding.imageAdduser.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                }
                is FragmentAdminImageAdd -> {
                    binding.tvBack.text = getString(R.string.race_schedule)
                    binding.tvTitlePage.visibility = View.VISIBLE
                    binding.tvTitlePage.text = getString(R.string.add_new_race_schedule)
                    binding.imageAdduser.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                    binding.addImageSchedule.visibility = View.GONE
                }
                is FragmentImageUpdate -> {
                    binding.tvBack.text = getString(R.string.race_schedule)
                    binding.tvTitlePage.text = getString(R.string.add_new_race_schedule)
                    binding.addImageSchedule.visibility = View.GONE
                    binding.tvTitlePage.visibility = View.VISIBLE
                }
                is FragmentAddRaceDetail -> {
                    binding.tvBack.text = getString(R.string.add_race)
                    binding.tvTitlePage.text = getString(R.string.add_new_race)
                    binding.imageAdduser.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                    binding.addImageSchedule.visibility = View.GONE
                }
                is SubRaceDetailFragment -> {
                    binding.tvBack.text = getString(R.string.race_detail)
                    binding.imageAdd.visibility = View.GONE
                    binding.tvTitlePage.visibility = View.GONE
                }

                is AddUserListFragment -> {
                    binding.tvBack.text = getString(R.string.race_detail)
                    binding.tvTitlePage.text = getString(R.string.add_user)
                    binding.imageAdduser.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                    binding.addImageSchedule.visibility = View.GONE
                }
                else -> {
                    binding.tvTitlePage.text = getString(R.string.title_control)
                    binding.tvBack.text=getString(R.string.title_home)
                    binding.addImageSchedule.visibility = View.GONE
                    binding.imageAdd.visibility = View.GONE
                    binding.imageAdduser.visibility = View.GONE
                }
            }
        }

        binding.tvUserDetails.setOnClickListener {
            binding.imageAdduser.visibility = View.VISIBLE
            binding.tvTitlePage.setText(getString(R.string.user_detail))
            fragment= FragmentUserDetails()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fram, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.imageAdduser.setOnClickListener{
            binding.imageAdduser.visibility = View.GONE
            binding.imageAdd.visibility = View.GONE
            binding.addImageSchedule.visibility = View.GONE
            binding.tvTitlePage.setText(getString(R.string.user_detail))
            fragment = AddUserFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fram, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        binding.tvCamelDetails.setOnClickListener {
            binding.tvTitlePage.setText(getString(R.string.camel_detail))
            binding.addImageSchedule.visibility = View.GONE
            fragment = FragmentCamel()
//            val textview = findViewById(R.id.tv_titlePage) as TextView
//            textview.setText("Camel profile")
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fram, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.tvRaceDetails.setOnClickListener {
            if (Role == "normal_user") {
                binding.imageAdd.visibility = View.GONE
            } else {
                binding.imageAdd.visibility = View.VISIBLE
            }
            binding.addImageSchedule.visibility = View.GONE
            binding.tvTitlePage.setText(getString(R.string.race_detail))
            fragment= RaceDetailFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fram, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.imageAdd.setOnClickListener {
            binding.imageAdd.visibility = View.GONE
            fragment = FragmentAddRaceDetail()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fram, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.tvRaceSchedule.setOnClickListener {
            binding.tvTitlePage.setText(getString(R.string.race_schedule))
            binding.addImageSchedule.visibility = View.VISIBLE
            fragment = FragmentAdminRaceSchedule()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.replace(R.id.fram, fragment)
            ft.commit()
        }
        binding.addImageSchedule.setOnClickListener {
            binding.tvTitlePage.visibility = View.GONE
            binding.addImageSchedule.visibility = View.GONE
            fragment = FragmentAdminImageAdd()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.replace(R.id.fram, fragment)
            ft.commit()
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        //return super.onOptionsItemSelected(item)
//        return when(item.itemId){
//            R.id.home -> {
//                this.finish()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}