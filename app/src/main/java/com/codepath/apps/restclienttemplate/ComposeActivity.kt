package com.codepath.apps.restclienttemplate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    val TAG = "ComposeActivityTwit"
    lateinit var etCompose : EditText
    lateinit var btnTweet : Button

    lateinit var  client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        // mm Hide App Bar
        if (supportActionBar != null) {
            supportActionBar?.hide();
        }

        // Get views
        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        btnTweet.setOnClickListener {
            // Grab the content of the edittext
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet isn't empty

            if (tweetContent.isEmpty()){
                Toast.makeText(this, "Empty tweets are no go", Toast.LENGTH_SHORT).show()
            }else
            // 2. Make sure the tweet is under character count
                if (tweetContent.length > 140){
                    Toast.makeText(this, "Tweet too big. 140 characters max.", Toast.LENGTH_SHORT)
                        .show()

                } else{
                    // make the API call to post
                    client.publishTweet(tweetContent, object: JsonHttpResponseHandler(){
                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish tweet",throwable)
                        }

                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON?) {
                            Log.i(TAG, "Tweet published!")

                        // mm send the tweet back to TimeLineActivity
                            val tweet = Tweet.fromJson(json!!.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet",tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                    })

                    Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                }
        }



    }

}