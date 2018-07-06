package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    // Pass in tweets array
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into ViewHolder class

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.layout_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data according to position
        Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUseranme.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));


        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public /*static*/ class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUseranme;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public ImageButton ibReply;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform viewById lookups
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUseranme = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ibReply = itemView.findViewById(R.id.ibReply);
            ibReply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Tweet reply = mTweets.get(pos);
                    Intent intent = new Intent(v.getContext(), ComposeActivity.class);
                    intent.putExtra("isReply", true);
                    intent.putExtra("uid", reply.uid);
                    Activity act = (Activity) v.getContext();
                    act.startActivityForResult(intent, 2);
                }
            });



        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
