package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.fragment.FemaleFragment
import com.adsum.camel_masterapplication.fragment.MaleFragment


class MyPagerAdapter(fm: FragmentManager , context: Context) : FragmentPagerAdapter(fm) {

    private var context = context

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                return MaleFragment()
                //return FemaleFragment()
            }
            else -> {
                return FemaleFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.abkar)
            else -> {
                return context.getString(R.string.crumple)
            }
        }
    }
}