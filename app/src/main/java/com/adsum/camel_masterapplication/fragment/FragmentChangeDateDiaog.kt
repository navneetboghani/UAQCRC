package com.adsum.camel_masterapplication.fragment


import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentChangeDateDiaogBinding
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import java.util.*
import com.github.florent37.singledateandtimepicker.dialog.BottomSheetHelper

import com.github.florent37.singledateandtimepicker.DateHelper








class FragmentChangeDateDiaog : Fragment() {

    private lateinit var fragmentChangeDateDiaogBinding: FragmentChangeDateDiaogBinding





    companion object {
        fun newInstance(): FragmentChangeDateDiaog {
            val fragment: FragmentChangeDateDiaog =
                FragmentChangeDateDiaog()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView: View =  inflater.inflate(R.layout.fragment_change_date_diaog, container, false)








        return rootView
    }







}