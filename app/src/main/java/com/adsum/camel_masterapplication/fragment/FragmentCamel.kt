package com.adsum.camel_masterapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.adsum.camel_masterapplication.Adapter.MyPagerAdapter
import com.adsum.camel_masterapplication.R

import com.google.android.material.tabs.TabLayout

class FragmentCamel: Fragment(){


    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    companion object{
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentCamel {
            val fragment: FragmentCamel=
                FragmentCamel()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_camel, container, false)
        viewPager = view.findViewById(R.id.viewpager_main)
        tabs = view.findViewById(R.id.tabs_main)
        val fragmentAdapter = MyPagerAdapter(childFragmentManager,requireActivity(), )
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)
        return view
    }

}
