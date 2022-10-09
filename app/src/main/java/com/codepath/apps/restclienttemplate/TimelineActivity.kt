package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

private const val TAG = "TimelineActivityTwit"

class TimelineActivity : AppCompatActivity() {

    // mm Defining the client variable
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        // mm Initialize client variable
        client=TwitterApplication.getRestClient(this)

        // mm Calling the method to load data
        populateHomeTimeline()

    }

    fun populateHomeTimeline(){
        client.getHomeTimeline(object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Timeline populating error: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.i(TAG, "Timeline populated successfully: $json")
            }

        })
    }
}