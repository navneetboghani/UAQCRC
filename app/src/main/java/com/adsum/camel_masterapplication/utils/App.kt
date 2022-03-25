package com.adsum.camel_masterapplication.utils

import android.os.StrictMode
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication

import timber.log.Timber

class App : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()

        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


//        initRealm()
//        initTwitter()

       // initOneSignal()

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

//    private fun initRealm() {
//        Realm.init(this)
//        val config = RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()
//                .build()
//        Realm.setDefaultConfiguration(config)
//    }
//
//    private fun initTwitter() {
//        val twitterConsumerKey = getString(R.string.twitter_consumer_key)
//        val twitterConsumerSecret = getString(R.string.twitter_consumer_secret)
//        val config = TwitterConfig.Builder(this)
//                .twitterAuthConfig(TwitterAuthConfig(twitterConsumerKey, twitterConsumerSecret))
//                .build()
//        Twitter.initialize(config)
//    }

//    private fun initOneSignal() {
//        OneSignal.startInit(this)
//            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init();
//    }
//private fun initOneSignal() {
//    OneSignal.startInit(this)
//        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//        .unsubscribeWhenNotificationsAreDisabled(true)
//        .init();
//}
    companion object {
        lateinit var instance: App
    }



}
