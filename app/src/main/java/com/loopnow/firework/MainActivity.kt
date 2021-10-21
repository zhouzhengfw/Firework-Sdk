package com.loopnow.firework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.toFlow
import com.google.android.material.snackbar.Snackbar
import com.loopnow.firework.fwsdk.FWSDk
import com.loopnow.firework.fwsdk.FWViewModel
import com.loopnow.firework.utils.FwConsts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var vm =FWViewModel(application)
        findViewById<View>(R.id.btn_test1).setOnClickListener {
            kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
                vm.getFeed()
            }

        }
        findViewById<View>(R.id.btn_test2).setOnClickListener {
            kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
                vm.authorize(FwConsts.HOST_PRODUCTION + FwConsts.OAUTH_URL,  FWSDk.sdkClientId!! ,  FWSDk.sdkGuestId!!)

            }
        }
        lifecycleScope.launch {
//            apolloClient(this@MainActivity).subscribe(TripsBookedSubscription()).toFlow()
//                .retryWhen { _, attempt ->
//                    delay(attempt * 1000)
//                    true
//                }
//                .collect {
//                    val text = when (val trips = it.data?.tripsBooked) {
//                        null -> getString(R.string.subscriptionError)
//                        -1 -> getString(R.string.tripCancelled)
//                        else -> getString(R.string.tripBooked, trips)
//                    }
//                    Snackbar.make(
//                        findViewById(R.id.main_frame_layout),
//                        text,
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
        }
    }
}
