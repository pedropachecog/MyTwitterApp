package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    var maxid : Long = 0

    val tweets = ArrayList<Tweet>()

    // Store a member variable for the listener
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

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

        var linearLayoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager = linearLayoutManager
        rvTweets.adapter = adapter

        // mm Creates endless scroll listener
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData();
            }
        }
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)

        // mm Calling the method to load data
        populateHomeTimeline()

    }

    private fun loadMoreData() {
        maxid -= 1
        client.getNextPageOfTweets(object: JsonHttpResponseHandler(){
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(TAG, "Loading more tweets error: $statusCode")
                }

                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                    Log.i(TAG, "More tweets loaded successfully: $json")
                    Log.i(TAG, "OnSuccess!")

                    // Get Json and turn it into a list of tweets

                    val jsonArray = json.jsonArray
                    try{
                        val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                        tweets.addAll(listOfNewTweetsRetrieved)
                        adapter.notifyDataSetChanged()
                        maxid = findMinId(tweets)
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false)

                    } catch (e: JSONException){
                        Log.e(TAG, "JSON Exception: $statusCode")
                    }


                }

            },  maxid)
    }

    // TODO
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handles click on menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Compose){
//            Toast.makeText(this, "Ready to compose tweete¡ ", Toast.LENGTH_SHORT).show()
            // Navigate to compose screen
            val intent = Intent(this, ComposeActivity:: class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
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
                Log.i(TAG, "Timeline populated successfully: $json")
                Log.i(TAG, "OnSuccess!")

                // Get Json and turn it into a list of tweets

                val jsonArray = json.jsonArray
                try{
                    // Clear out currently fetched tweets
                    adapter.clear()
                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    maxid = findMinId(tweets)
                    adapter.notifyDataSetChanged()
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false)

                } catch (e: JSONException){
                    Log.e(TAG, "JSON Exception: $statusCode")
                }


            }

        })
    }

    private fun findMinId(tweets: ArrayList<Tweet>): Long {
        if (tweets.size == 0) return 0
        var minid = tweets[0].id
        for (tweet in tweets){
            if (tweet.id < minid)
                minid = tweet.id
        }
        return minid
    }
}