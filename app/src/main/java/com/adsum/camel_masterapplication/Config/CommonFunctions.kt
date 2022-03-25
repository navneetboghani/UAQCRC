package com.adsum.camel_masterapplication.Config

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.preference.PreferenceManager
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.receiver.ConnectivityReceiver

import java.util.regex.Pattern

object CommonFunctions {
    var errMessage = ""
    var tag = "CommonFunctions :"
    var toast: Toast? = null

    /**
     * Create Progress bar
     */
    var pd: ProgressDialog? = null
    var slide_act_flag = true

    /**
     * Check Internet connection available or not
     *
     * @return
     */

    var emailvalid: String? = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    fun checkConnection(activity: Activity): Boolean {
        val isConnected: Boolean = ConnectivityReceiver.isConnected()
        if (!isConnected) showDialog(
            activity,
            activity.resources.getString(R.string.msg_NO_INTERNET_RESPOND)
        )
        return isConnected
    }

    fun showDialog(c: Activity, msg: String?) {
        val alertDialog = AlertDialog.Builder(c).create()
        alertDialog.setTitle(c.getString(R.string.app_name))
        alertDialog.setMessage(msg)
        alertDialog.setCancelable(false)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, c.getString(R.string.dialog_ok)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }
    fun validateEmailAddress(email: String?): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
    fun createProgressBar(
        context: Context?,
        strMsg: String?
    ): ProgressDialog? {
        if (pd == null) {
            pd = ProgressDialog(context, R.style.ProThemeOrange)
            pd!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pd!!.setMessage(strMsg)
            pd!!.setCancelable(false)
            pd!!.show()
        } else {
            pd!!.dismiss()
            pd = null
            pd = ProgressDialog(context, R.style.ProThemeOrange)
            pd!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pd!!.setMessage(strMsg)
            pd!!.setCancelable(false)
            pd!!.show()
        }
        return pd
    }
    fun destroyProgressBar() {
        if (pd != null) pd!!.dismiss()
    }
    fun showToast(context: Context?, msg: String?) {
        try {
            if (toast == null || toast!!.view!!.windowVisibility != View.VISIBLE) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getPreference(
        context: Context?, pref: String?,
        def: Boolean
    ): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(pref, def)
    }


    fun setPreference(c: Context?, pref: String?, `val`: Boolean) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putBoolean(pref, `val`)
        e.commit()
    }
//    fun getPreference(context: Context?, pref: String?, def: Boolean): Boolean {
//        return PreferenceManager.getDefaultSharedPreferences(context)
//            .getBoolean(pref, def)
//    }

    fun setPreference(c: Context?, pref: String?, `val`: Int) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putInt(pref, `val`)
        e.commit()
    }

    fun getPreference(context: Context?, pref: String?, def: Int): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
            pref, def
        )
    }

    fun setPreference(c: Context?, pref: String?, `val`: Float) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putFloat(pref, `val`)
        e.commit()
    }

    fun getPreference(context: Context?, pref: String?, def: Float): Float {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
            pref, def
        )
    }

    fun setPreference(c: Context?, pref: String?, `val`: Long) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putLong(pref, `val`)
        e.commit()
    }

    fun getPreference(context: Context?, pref: String?, def: Long): Long {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
            pref, def
        )
    }

    fun setPreference(c: Context?, pref: String?, `val`: Double) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putLong(pref, java.lang.Double.doubleToLongBits(`val`))
        e.commit()
    }

    fun getPreference(context: Context?, pref: String?, def: Double): Double {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
            pref, java.lang.Double.doubleToLongBits(def)
        ).toDouble()
    }

    fun setPreference(c: Context?, pref: String?, `val`: String?) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putString(pref, `val`)
        e.commit()
    }

    fun getPreference(context: Context?, pref: String?, def: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(pref, def)
    }

    fun title(string: String?): String? {
        var ret = ""
        val sb = StringBuffer()
        val match = Pattern.compile(
            "([a-z])([a-z]*)",
            Pattern.CASE_INSENSITIVE
        ).matcher(string)
        while (match.find()) {
            match.appendReplacement(
                sb, match.group(1).toUpperCase()
                        + match.group(2).toLowerCase()
            )
        }
        ret = match.appendTail(sb).toString()
        return ret
    }

}