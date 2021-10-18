package com.loopnow.firework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.toFlow
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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
