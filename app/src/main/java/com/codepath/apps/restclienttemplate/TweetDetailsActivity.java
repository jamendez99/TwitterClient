package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TweetDetailsActivity extends AppCompatActivity {


    private TextView tvUserName;
    private TextView tvBody;
    private TextView tvTimeStamp;
    private ImageView ivProfilePicture;
    private ImageView ivRetweet;
    private TwitterClient client;


    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        client = TwitterApp.getRestClient(this);


        tvUserName = findViewById(R.id.tvUserName);
        tvBody = findViewById(R.id.tvBody);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        ivProfilePicture = findViewById(R.id.ivProfileImage);
        ivRetweet = findViewById(R.id.ivRetweet);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvUserName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText(TweetAdapter.getRelativeTimeAgo(tweet.createdAt));

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfilePicture);

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
                client.retweet(tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                            Intent intent = new Intent();
                            intent.putExtra("retweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



    }
}
