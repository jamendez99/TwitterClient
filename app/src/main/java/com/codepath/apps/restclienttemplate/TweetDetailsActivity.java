package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {


    private TextView tvUserName;
    private TextView tvBody;
    private TextView tvTimeStamp;
    private ImageView ivProfilePicture;


    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);


        tvUserName = findViewById(R.id.tvUserName);
        tvBody = findViewById(R.id.tvBody);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        ivProfilePicture = findViewById(R.id.ivProfileImage);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvUserName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText(TweetAdapter.getRelativeTimeAgo(tweet.createdAt));

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfilePicture);

    }
}
