package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;

    private EditText etCompose;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        tvCount = findViewById(R.id.tvCount);
        etCompose.addTextChangedListener(mTextEditorWatcher);
    }

    public void onComposeButton(View view) {
        // EditText etCompose = findViewById(R.id.etCompose);
        String tweetText = etCompose.getText().toString();

        if (getIntent().getBooleanExtra("isReply", false)) {
            client.sendTweet(tweetText, getIntent().getLongExtra("uid", 0), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response);
                        Intent intent = new Intent();
                        intent.putExtra("my_tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            client.sendTweet(tweetText, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response);
                        Intent intent = new Intent();
                        intent.putExtra("my_tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("ComposeActivity", "Failed to handle response");
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
