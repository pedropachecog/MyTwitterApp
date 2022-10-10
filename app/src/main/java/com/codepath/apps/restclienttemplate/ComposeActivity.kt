package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose : EditText
    lateinit var btnTweet : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        // Get views
        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

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
                    // TODO Make an api call
                    Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                }
        }
    }
}