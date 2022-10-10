package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "TimelineActivityTwit"

class TimelineActivity : AppCompatActivity() {

    // mm Defining the client variable
    lateinit var client: TwitterClient

    lateinit var rvTweets : RecyclerView
    lateinit var adapter : TweetsAdapter

    lateinit var swipeContainer : SwipeRefreshLayout

    val tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        // mm Initialize client variable
        client=TwitterApplication.getRestClient(this)

        rvTweets = findViewById(R.id.rvTweets)

        swipeContainer = findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timelein")
            populateHomeTimeline()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);


        // mm send Tweet list to adapter
        adapter = TweetsAdapter(tweets)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

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

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
//                Log.i(TAG, "Timeline populated successfully: $json")
                Log.i(TAG, "OnSuccess!")

                // Get Json and turn it into a list of tweets

                val jsonArray = json.jsonArray
                try{
                    // Clear out currently fetched tweets
                    adapter.clear()
                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    adapter.notifyDataSetChanged()
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false)

                } catch (e: JSONException){
                    Log.e(TAG, "JSON Exception: $statusCode")
                }


            }

        })
    }
}