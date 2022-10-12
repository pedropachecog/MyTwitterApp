package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color.red
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    val TAG = "ComposeActivityTwit"
    lateinit var etCompose : EditText
    lateinit var btnTweet : Button
    lateinit var tvCount : TextView

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
        tvCount = findViewById(R.id.tvCount)

        client = TwitterApplication.getRestClient(this)

        btnTweet.setOnClickListener {
            // Grab the content of the edittext
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet isn't empty

            if (tweetContent.isEmpty()){
                Toast.makeText(this, "The tweet is empty", Toast.LENGTH_SHORT).show()
            }else
            // 2. Make sure the tweet is under character count
                if (tweetContent.length > 280){
                    Toast.makeText(this, "Tweet is too long. 280 characters maximum", Toast
                        .LENGTH_SHORT)
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

//                    Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                }
        }

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val length: Int = etCompose.length()
                val convert = length.toString()
                tvCount.text = convert
                if (length > 280) {
                    tvCount.setTextColor(getResources().getColor(R.color.error_red))
                    btnTweet.isEnabled = false
                }
                    else {
                        tvCount.setTextColor(getResources().getColor(R.color.secondaryColor))

                    btnTweet.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

    }

}