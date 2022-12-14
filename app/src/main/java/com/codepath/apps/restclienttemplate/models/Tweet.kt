package com.codepath.apps.restclienttemplate.models

import TimeFormatter
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import org.json.JSONArray
import org.json.JSONObject

@Parcelize
class Tweet(var body: String = "", var createdAt: String = "", var user:User? = null, var id:
Long = 0) : Parcelable {

//    var body: String = ""
//    var createdAt: String = ""
//    var user: User? = null
//    var id: Long = 0

    companion object{

        //mm Creates a Tweet object from a JSON object
        fun fromJson(jsonObject: JSONObject):Tweet {
            val tweet =  Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = getFormattedTimeStamp(jsonObject.getString("created_at"))
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.id = jsonObject.getString("id").toLong()
            return tweet
        }

        // mm Create an Arraylist of Tweets, and use a for loop to populate it from each JSON
        // object
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet>{
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()){
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

        fun getFormattedTimeStamp(timestamp: String):String{
            return TimeFormatter.getTimeDifference(timestamp);
        }
    }
}